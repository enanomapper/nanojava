/* Copyright (C) 2022  Egon Willighagen <egonw@users.sf.net>
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
package org.bitbucket.nanojava.inchi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bitbucket.nanojava.data.Chirality;
import org.bitbucket.nanojava.data.Material;
import org.bitbucket.nanojava.data.Morphology;
import org.bitbucket.nanojava.data.Spacegroup;
import org.bitbucket.nanojava.data.measurement.EndPoints;
import org.bitbucket.nanojava.data.measurement.IErrorlessMeasurementValue;
import org.bitbucket.nanojava.data.measurement.IMeasurement;
import org.bitbucket.nanojava.data.measurement.IMeasurementValue;
import org.bitbucket.nanojava.manipulator.SubstanceManipulator;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.inchi.InChIGenerator;
import org.openscience.cdk.inchi.InChIGeneratorFactory;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.ISubstance;

/**
 * Helper class to generate nano InChIs for the provided ISubstance. This class is based
 * on the current ideas of what such a nano InChI may look like. There is no specification,
 * just explored ideas. The structure of the "NInChI" can change overnight. This implementation
 * allows us to explore the proposal's characteristics.
 *
 * <p>Specifically, this class implements the "NInChI Alpha" from
 * <a href="https://doi.org/10.3390/nano10122493">this article</a>.
 * And the current implementation is NOT EVEN COMPLETE.
 */
public class NInChIGenerator {

	public static String generator(ISubstance substance) throws CDKException {
		String nanoInChI = "NInChI=0.00.1A/";

		// determine the component parts
		Map<String,Integer> nInChIComponents = new HashMap<>(); 
		for (int i=0; i<substance.getAtomContainerCount(); i++) {
			IAtomContainer component = substance.getAtomContainer(i);
			int order = component.getProperty(Material.ORDER);

			String nInChIComponent = generateStructureLayer(component);
			nInChIComponent += gerateMorphologyLayer(component);
			nInChIComponent += generateSizeLayer(component);
			nInChIComponent += generateChiralityLayer(component);
			nInChIComponent += generateCrystalStructureLayer(component);

			// store for later alphabetical ordering
			nInChIComponents.put(nInChIComponent, order);
		}
		List<String> componentStrings = new ArrayList<String>();
		componentStrings.addAll(nInChIComponents.keySet());
		Collections.sort(componentStrings);
		String yLayer = "/y";
		String componentLayer = "";
		for (String component : componentStrings) {
			componentLayer += component + "!";
			yLayer += "" + nInChIComponents.get(component) + "&";
		}
		componentLayer = componentLayer.substring(0,componentLayer.length()-1); // remove the trailing !
		yLayer = yLayer.substring(0,yLayer.length()-1); // remove the trailing &

		// add the component ordering, from inside out
		nanoInChI += componentLayer + yLayer;
		return nanoInChI;
	}

	private static String generateChiralityLayer(IAtomContainer component) {
		String layer = "";
		Chirality chirality = SubstanceManipulator.getChiralityType(component);
		if (chirality == Chirality.THREE_BY_ONE) layer += "/w(3,1)";
		return layer;
	}

	private static String generateCrystalStructureLayer(IAtomContainer component) {
		String layer = "";
		Spacegroup group = SubstanceManipulator.getSpacegroup(component);
		if (group == Spacegroup.AMORPHOUS) { layer += "/k000"; }
		return layer;
	}

	private static String gerateMorphologyLayer(IAtomContainer component) {
		String layer = "";
		Morphology morph = SubstanceManipulator.getMorphology(component);
		if (morph == Morphology.SPHERE) { layer += "/msp"; } else
		if (morph == Morphology.SHELL) { layer += "/msh"; } else
		if (morph == Morphology.TUBE) { layer += "/mtu"; }
		return layer;
	}

	private static String generateStructureLayer(IAtomContainer component) throws CDKException {
		String layer = "";
		InChIGenerator generator = InChIGeneratorFactory.getInstance().getInChIGenerator(component);
		String inchi = generator.getInchi();
		layer += inchi.substring(9); // skip InChI=1S/
		return layer;
	}

	private static String generateSizeLayer(IAtomContainer component) throws CDKException {
		String layer = "";
		IMeasurement measurement = SubstanceManipulator.getMeasurement(component, EndPoints.DIAMETER);
		if (measurement != null) layer = sizeFromMeasurement(layer, measurement, "d");
		measurement = SubstanceManipulator.getMeasurement(component, EndPoints.THICKNESS);
		if (measurement != null) layer = sizeFromMeasurement(layer, measurement, "t");
		return layer;
	}

	private static String sizeFromMeasurement(String layer, IMeasurement measurement, String measurementType)
			throws CDKException {
		if ("nm".equals(measurement.getUnit().getAbbreviation())) {
			double value = 0.0;
			if (measurement instanceof IMeasurementValue) {
				value = ((IMeasurementValue)measurement).getValue();
			} else if (measurement instanceof IErrorlessMeasurementValue) {
				value = ((IErrorlessMeasurementValue)measurement).getValue();
			} else {
				throw new CDKException("Yet unsupported measurement value: " + measurement.getClass().getName());
			}
			String exponent = "-9";
			if (value < 1.0) {
				value = value*10.0;
				exponent = "-10";
			}
			layer += "/s" + (int)value + measurementType + exponent;
		} else {
			throw new CDKException("Yet unsupported unit type: " + measurement.getUnit().getClass().getName());
		}
		return layer;
	}

}
