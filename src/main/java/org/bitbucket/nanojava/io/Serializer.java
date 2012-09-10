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

import org.bitbucket.nanojava.data.Nanomaterial;
import org.bitbucket.nanojava.data.measurement.IMeasurement;
import org.bitbucket.nanojava.data.measurement.IMeasurementValue;
import org.bitbucket.nanojava.data.measurement.Unit;
import org.xmlcml.cml.element.CMLMolecule;
import org.xmlcml.cml.element.CMLProperty;
import org.xmlcml.cml.element.CMLScalar;

public class Serializer {

	public static CMLMolecule toCML(Nanomaterial material) {
		CMLMolecule cmlMaterial = new CMLMolecule();
		cmlMaterial.setConvention("nano:material");
		cmlMaterial.addNamespaceDeclaration("nano", "http://.../");

		// set the type
		CMLScalar scalar = new CMLScalar();
		scalar.setDictRef("nano:type");
		scalar.setValue("" + material.getType());
		cmlMaterial.addScalar(scalar);

		// set the size
		IMeasurement sizeMeasurement = material.getSize();
		if (sizeMeasurement != null && sizeMeasurement instanceof IMeasurementValue) {
			CMLProperty sizeProp = new CMLProperty();
			sizeProp.setDictRef("nano:dimension");
			CMLScalar sizeScalar = new CMLScalar();
			if (sizeMeasurement.getUnit() == Unit.NM) {
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
			if (zpMeasurement.getUnit() == Unit.EV) {
				epScalar.setUnits("qudt:eV");
			}
			epScalar.setValue(((IMeasurementValue)zpMeasurement).getValue());
			epProp.addScalar(epScalar);
			cmlMaterial.appendChild(epProp);
		}

		return cmlMaterial;
	}

}
