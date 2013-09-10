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
import java.util.List;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.ParsingException;

import org.bitbucket.nanojava.data.Nanomaterial;
import org.bitbucket.nanojava.data.measurement.EndPoints;
import org.bitbucket.nanojava.data.measurement.IMeasurementValue;
import org.bitbucket.nanojava.data.measurement.MeasurementValue;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;
import org.xmlcml.cml.base.CMLBuilder;
import org.xmlcml.cml.base.CMLElement;
import org.xmlcml.cml.element.CMLFormula;
import org.xmlcml.cml.element.CMLList;
import org.xmlcml.cml.element.CMLMolecule;
import org.xmlcml.cml.element.CMLName;
import org.xmlcml.cml.element.CMLProperty;
import org.xmlcml.cml.element.CMLScalar;

import com.github.jqudt.onto.units.EnergyUnit;
import com.github.jqudt.onto.units.LengthUnit;

public class Deserializer {

	public static Nanomaterial fromCMLString(String cmlMaterialString) throws ParsingException, IOException {
    	Document nmxDoc;
    	nmxDoc = new CMLBuilder().buildEnsureCML(new ByteArrayInputStream(cmlMaterialString.getBytes()));
    	Element rootElem = nmxDoc.getRootElement();
    	if (rootElem instanceof CMLMolecule) { // requirement
    		CMLMolecule cmlMaterial = (CMLMolecule)rootElem;
    		return Deserializer.fromCML(cmlMaterial);
    	}
    	return null;
	}

	public static List<Nanomaterial> fromCML(CMLList cmlMaterials) {
		List<Nanomaterial> materials = new ArrayList<Nanomaterial>();
		for (CMLElement element : cmlMaterials.getChildCMLElements()) {
			if (element instanceof CMLMolecule) {
				Nanomaterial material = fromCML((CMLMolecule)element);
				if (material != null) materials.add(material);
			}
		}
		return materials;
	}

	public static Nanomaterial fromCML(CMLMolecule cmlMaterial) {
		if (!cmlMaterial.getConvention().matches("nano:material")) return null;
		Nanomaterial material = new Nanomaterial("METALOXIDE");

		for (CMLScalar scalar : cmlMaterial.getScalarElements()) {
			if (scalar.getDictRef().equals("nano:type")) material.setType(scalar.getValue());
		}

		List<String> labels = new ArrayList<String>();
		for (CMLElement element : cmlMaterial.getChildCMLElements()) {
			if (element instanceof CMLProperty) {
				CMLProperty prop = (CMLProperty)element;
				if (prop.getDictRef().equals("nano:dimension")) {
					for (CMLElement propScalar : prop.getChildCMLElements()) {
						if (propScalar instanceof CMLScalar && ((CMLScalar) propScalar).getUnits().equals("qudt:nm")) {
							IMeasurementValue sizeValue = new MeasurementValue(
							    EndPoints.SIZE,
								((CMLScalar) propScalar).getDouble(), Double.NaN, LengthUnit.NM
							);
							material.setSize(sizeValue);
						}
					}
				} else if (prop.getDictRef().equals("nano:zetaPotential")) {
					for (CMLElement propScalar : prop.getChildCMLElements()) {
						if (propScalar instanceof CMLScalar && ((CMLScalar) propScalar).getUnits().equals("qudt:eV")) {
							IMeasurementValue zpValue = new MeasurementValue(
								EndPoints.ZETA_POTENTIAL,
								((CMLScalar) propScalar).getDouble(), Double.NaN, EnergyUnit.EV
							);
							material.setZetaPotential(zpValue);
						}
					}
				} else if (prop.getDictRef().equals("npo:NPO_1915")) { // FIXME: do something smarter here
					for (CMLElement propScalar : prop.getChildCMLElements()) {
						if (propScalar instanceof CMLScalar && ((CMLScalar) propScalar).getUnits().equals("ops:Nanometer")) {
							IMeasurementValue zpValue = new MeasurementValue(
								EndPoints.DIAMETER_DLS,
								((CMLScalar) propScalar).getDouble(), Double.NaN, LengthUnit.NM
							);
							material.addCharacterization(zpValue);
						}
					}
				} else if (prop.getDictRef().equals("pato:PATO_0001334")) { // FIXME: do something smarter here
					for (CMLElement propScalar : prop.getChildCMLElements()) {
						if (propScalar instanceof CMLScalar && ((CMLScalar) propScalar).getUnits().equals("ops:Nanometer")) {
							IMeasurementValue zpValue = new MeasurementValue(
								EndPoints.DIAMETER_TEM,
								((CMLScalar) propScalar).getDouble(), Double.NaN, LengthUnit.NM
							);
							material.addCharacterization(zpValue);
						}
					}
				}
			} else if (element instanceof CMLName) {
				labels.add(element.getStringContent());
			} else if (element instanceof CMLFormula) {
				if (material.getChemicalComposition() == null) { // ignore second and later copies
					material.setChemicalComposition(
						MolecularFormulaManipulator.getMolecularFormula(
							element.getAttributeValue("inline"),
							DefaultChemObjectBuilder.getInstance()
						)
					);
				}
			}
		}
		if (labels.size() > 0) material.setLabels(labels);
		return material;
	}

}
