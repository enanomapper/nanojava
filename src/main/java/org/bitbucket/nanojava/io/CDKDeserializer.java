/* Copyright (C) 2012-2013  Egon Willighagen <egonw@users.sf.net>
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.ParsingException;

import org.bitbucket.nanojava.data.Material;
import org.bitbucket.nanojava.data.measurement.EndPoints;
import org.bitbucket.nanojava.data.measurement.ErrorlessMeasurementValue;
import org.bitbucket.nanojava.data.measurement.IEndPoint;
import org.bitbucket.nanojava.data.measurement.IMeasurement;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemFile;
import org.openscience.cdk.io.CMLReader;
import org.openscience.cdk.silent.ChemFile;
import org.openscience.cdk.tools.manipulator.ChemFileManipulator;
import org.xmlcml.cml.base.CMLBuilder;
import org.xmlcml.cml.base.CMLElement;
import org.xmlcml.cml.element.CMLList;
import org.xmlcml.cml.element.CMLMolecule;
import org.xmlcml.cml.element.CMLMoleculeList;
import org.xmlcml.cml.element.CMLName;
import org.xmlcml.cml.element.CMLProperty;
import org.xmlcml.cml.element.CMLScalar;

import com.github.jqudt.Unit;
import com.github.jqudt.onto.UnitFactory;

public class CDKDeserializer {

	public static Material fromCMLString(String cmlMaterialString) throws ParsingException, IOException {
    	Document nmxDoc;
    	nmxDoc = new CMLBuilder().buildEnsureCML(new ByteArrayInputStream(cmlMaterialString.getBytes()));
    	Element rootElem = nmxDoc.getRootElement();
    	if (rootElem instanceof CMLMolecule) { // requirement
    		CMLMolecule cmlMaterial = (CMLMolecule)rootElem;
    		return Deserializer.fromCML(cmlMaterial);
    	}
    	return null;
	}

	public static List<Material> fromCMLListString(String cmlListString) throws ParsingException, IOException {
    	Document nmxDoc;
    	nmxDoc = new CMLBuilder().buildEnsureCML(new ByteArrayInputStream(cmlListString.getBytes()));
    	Element rootElem = nmxDoc.getRootElement();
    	if (rootElem instanceof CMLList) { // requirement
    		CMLList cmlList = (CMLList)rootElem;
    		return Deserializer.fromCML(cmlList);
    	}
    	return null;
	}

	public static List<Material> fromCML(CMLList cmlMaterials) {
		List<Material> materials = new ArrayList<Material>();
		for (CMLElement element : cmlMaterials.getChildCMLElements()) {
			if (element instanceof CMLMoleculeList) {
				Material material = fromCML((CMLMoleculeList)element);
				if (material != null) materials.add(material);
			}
		}
		return materials;
	}

	public static Material fromCML(CMLMoleculeList cmlMaterial) {
		if (!cmlMaterial.getConvention().matches("nano:material")) return null;
		Material material = new Material("METALOXIDE");

		List<String> labels = new ArrayList<String>();
		for (CMLElement element : cmlMaterial.getChildCMLElements()) {
			if (element instanceof CMLProperty) {
				CMLProperty prop = (CMLProperty)element;
				String dictRef = prop.getDictRef();
				String url = resolveDictRef(prop, dictRef);
				if (url != null) {
					for (CMLElement propScalar : prop.getChildCMLElements()) {
						if (propScalar instanceof CMLScalar) {
							CMLScalar scalar = (CMLScalar)propScalar;
							String unitUrl = resolveDictRef(scalar, scalar.getUnits());
							// OK, let's figure out the end point and unit
							IEndPoint endpoint = getEndPoint(url);
							Unit unit = getUnit(unitUrl);
							if (endpoint != null && unit != null) {
								IMeasurement characterization = new ErrorlessMeasurementValue(
									endpoint, scalar.getDouble(), unit
								);
								material.addCharacterization(characterization);
							}
						}
					}
				}
			} else if (element instanceof CMLScalar) {
				CMLScalar scalar = (CMLScalar)element;
				if (scalar.getDictRef().equals("nano:type")) material.setType(scalar.getValue());
			} else if (element instanceof CMLName) {
				labels.add(element.getStringContent());
			} else if (element instanceof CMLMolecule) {
				try {
					CMLReader reader = new CMLReader(
						new ByteArrayInputStream(element.toXML().toString().getBytes())
					);
					IChemFile chemFile = reader.read(new ChemFile());
					for (IAtomContainer container :
						  ChemFileManipulator.getAllAtomContainers(chemFile)) {
						material.addAtomContainer(container);
					}
					reader.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (labels.size() > 0) material.setLabels(labels);
		return material;
	}

	private static Unit getUnit(String unitUrl) {
		if (unitUrl == null) return null;
		return UnitFactory.getInstance().getUnit(unitUrl);
	}

	@SuppressWarnings("serial")
	private static Map<String,IEndPoint> endpoints = new HashMap<String, IEndPoint>() {{
			// can this be done smarter??
		    addEndPoint(EndPoints.DIAMETER_DLS);
		    addEndPoint(EndPoints.DIAMETER_TEM);
		    addEndPoint(EndPoints.SIZE);
		    addEndPoint(EndPoints.PH);
		    addEndPoint(EndPoints.ZETA_POTENTIAL);
		    addEndPoint(EndPoints.PURITY);
	    }
	    public void addEndPoint(IEndPoint endpoint) {
	    	this.put(endpoint.getURI().toString(), endpoint);
	    }
	};

	private static IEndPoint getEndPoint(String url) {
		// support for hacky past code
		if ("nano:dimension".equals(url)) {
			return EndPoints.SIZE;
		} else if ("nano:zetaPotential".equals(url)) {
			return EndPoints.ZETA_POTENTIAL;
		}
		return endpoints.get(url);
	}

	private static String resolveDictRef(CMLElement element, String value) {
		// support for hacky past code
		if ("nano:dimension".equals(value)) {
			return value;
		} else if ("nano:zetaPotential".equals(value)) {
			return value;
		}
		for (int i=0; i<element.getNamespaceDeclarationCount(); i++) {
			String prefix = element.getNamespacePrefix(i);
			if (value.startsWith(prefix + ":")) {
				String localpart = value.substring(prefix.length()+1);
				String namespace = element.getNamespaceURIForPrefix(prefix);
				String fullUrl = namespace+localpart;
				return fullUrl;
			}
		}
		if (value.startsWith("qudt:")) {
			String localpart = value.substring("qudt".length()+1);
			String namespace = "http://qudt.org/vocab/unit#";
			if ("eV".equals(localpart)) { localpart = "ElectronVolt"; } else
			if ("nm".equals(localpart)) { localpart = "Nanometer"; namespace = "http://www.openphacts.org/units/"; }
			String fullUrl =  namespace + localpart;
			return fullUrl;
		}
		return null;
	}

}
