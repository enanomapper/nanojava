/* Copyright (C) 2012  Egon Willighagen <egonw@users.sf.net>
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.bitbucket.nanojava.io;

import java.util.List;
import java.util.Map;

import org.bitbucket.nanojava.data.Material;
import org.bitbucket.nanojava.data.measurement.IEndPoint;
import org.bitbucket.nanojava.data.measurement.IErrorlessMeasurementValue;
import org.bitbucket.nanojava.data.measurement.IMeasurement;
import org.bitbucket.nanojava.data.measurement.IMeasurementValue;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.libio.cml.Convertor;
import org.openscience.cdk.libio.cml.ICMLCustomizer;
import org.xmlcml.cml.element.CMLList;
import org.xmlcml.cml.element.CMLMoleculeList;
import org.xmlcml.cml.element.CMLName;
import org.xmlcml.cml.element.CMLProperty;
import org.xmlcml.cml.element.CMLScalar;

import com.github.jqudt.Unit;
import com.github.jqudt.onto.units.EnergyUnit;
import com.github.jqudt.onto.units.LengthUnit;

import nu.xom.Element;

public class CDKSerializer {

	private static Convertor convertor;
	static {
		CDKSerializer.convertor = new Convertor(true, "cml");
		CDKSerializer.convertor.registerCustomizer(new ICMLCustomizer() {
			@Override
			public void customize(IAtomContainer molecule, Object nodeToAdd) throws Exception {
				Object orderProperty = molecule.getProperty(Material.ORDER);
				if (orderProperty != null) {
					CMLScalar scalar = new CMLScalar();
					scalar.setDictRef("nano:order");
					scalar.setValue("" + orderProperty);
					((Element)nodeToAdd).appendChild(scalar);
				}
			}
			
			@Override
			public void customize(IBond bond, Object nodeToAdd) throws Exception {}
			
			@Override
			public void customize(IAtom atom, Object nodeToAdd) throws Exception {}
		});
	}
	
	public static String toCMLString(Material material) {
		CMLMoleculeList cmlMaterial = toCML(material);
		return cmlMaterial.toXML();
	}

	public static CMLList toCML(List<Material> materials) {
		CMLList list = new CMLList();
		for (Material material : materials) {
			list.appendChild(toCML(material));
		}
		return list;
	}

	public static CMLMoleculeList toCML(Material material) {
		CMLMoleculeList cmlMaterial = convertor.cdkAtomContainerSetToCMLList(material);
		cmlMaterial.setConvention("nano:material");
		cmlMaterial.addNamespaceDeclaration("nano", "http://linkedchemistry.org/nano#");

		// set the labels
		for (String label : material.getLabels()) {
			CMLName cmlLabel = new CMLName();
			cmlLabel.setStringContent(label);
			cmlMaterial.appendChild(cmlLabel);
		}

		// set the type
		CMLScalar scalar = new CMLScalar();
		scalar.setDictRef("nano:type");
		scalar.setValue("" + material.getType());
		cmlMaterial.appendChild(scalar);

		// set the size
		IMeasurement sizeMeasurement = material.getSize();
		if (sizeMeasurement != null && sizeMeasurement instanceof IMeasurementValue) {
			CMLProperty sizeProp = new CMLProperty();
			sizeProp.setDictRef("nano:dimension");
			CMLScalar sizeScalar = new CMLScalar();
			if (sizeMeasurement.getUnit() == LengthUnit.NM) {
				sizeScalar.setUnits("qudt:nm");
			}
			sizeScalar.setValue(((IMeasurementValue)sizeMeasurement).getValue());
			sizeProp.addScalar(sizeScalar);
			cmlMaterial.appendChild(sizeProp);
		}
		// set the zeta potential
		IMeasurement zpMeasurement = material.getZetaPotential();
		if (zpMeasurement != null && zpMeasurement instanceof IMeasurementValue) {
			CMLProperty epProp = new CMLProperty();
			epProp.setDictRef("nano:zetaPotential");
			CMLScalar epScalar = new CMLScalar();
			if (zpMeasurement.getUnit() == EnergyUnit.EV) {
				epScalar.setUnits("qudt:eV");
			}
			epScalar.setValue(((IMeasurementValue)zpMeasurement).getValue());
			epProp.addScalar(epScalar);
			cmlMaterial.appendChild(epProp);
		}

		// set the characterizations
		Map<IEndPoint,IMeasurement> characterizations = material.getCharacterizations();
		for (IEndPoint endPoint : characterizations.keySet()) {
			IMeasurement measurement = characterizations.get(endPoint);
			if (measurement != null) {
				for (String namespace : Namespaces.prefixes.keySet()) {
					String endPointStr = endPoint.getURI().toString();
					if (endPointStr.startsWith(namespace)) {
						String prefix = Namespaces.prefixes.get(namespace);
						String entry = endPointStr.substring(namespace.length());

						CMLProperty prop = new CMLProperty();
						prop.addNamespaceDeclaration(prefix, namespace);
						prop.setDictRef(prefix + ":" + entry);

						scalar = new CMLScalar();
						Unit unit = measurement.getUnit();
						for (String unitNS : Namespaces.prefixes.keySet()) {
							String unitStr = unit.getResource().toString();
							if (unitStr.startsWith(unitNS)) {
								prefix = Namespaces.prefixes.get(unitNS);
								entry = unitStr.substring(unitNS.length());
								scalar.addNamespaceDeclaration(prefix, unitNS);
								scalar.setUnits(prefix + ":" + entry);
							}
						}
						if (measurement instanceof IMeasurementValue) {
							scalar.setValue(((IMeasurementValue)measurement).getValue());
						} else if (measurement instanceof IErrorlessMeasurementValue) {
							scalar.setValue(((IErrorlessMeasurementValue)measurement).getValue());
						}
						prop.addScalar(scalar);
						cmlMaterial.appendChild(prop);
					}
				}
			}
		}

		return cmlMaterial;
	}

	
}
