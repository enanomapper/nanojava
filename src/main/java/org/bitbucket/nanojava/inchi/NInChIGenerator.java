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
		String nanoInChI = "InChI=1A/";

		// determine the component parts
		Map<String,Integer> nInChIComponents = new HashMap<>(); 
		for (int i=0; i<substance.getAtomContainerCount(); i++) {
			String nInChIComponent = "";
			IAtomContainer component = substance.getAtomContainer(i);
			InChIGenerator generator = InChIGeneratorFactory.getInstance().getInChIGenerator(component);
			String inchi = generator.getInchi();
			int order = component.getProperty(Material.ORDER);
			nInChIComponent += inchi.substring(9); // skip InChI=1S/

			// add morphology layer
			Morphology morph = SubstanceManipulator.getMorphology(component);
			if (morph == Morphology.SPHERE) { nInChIComponent += "/msp"; } else
			if (morph == Morphology.SHELL) { nInChIComponent += "/msh"; }

			// add size layer
			IMeasurement diameter = SubstanceManipulator.getMeasurement(component, EndPoints.DIAMETER);
			if (diameter != null) {
				if ("nm".equals(diameter.getUnit().getAbbreviation())) {
					if (diameter instanceof IMeasurementValue) {
						nInChIComponent += "/s" + (int)((IMeasurementValue)diameter).getValue() + "d-9";
					} else if (diameter instanceof IErrorlessMeasurementValue) {
						nInChIComponent += "/s" + (int)((IErrorlessMeasurementValue)diameter).getValue() + "d-9";
					}
				}
			}
			IMeasurement thickness = SubstanceManipulator.getMeasurement(component, EndPoints.THICKNESS);
			if (thickness != null) {
				if ("nm".equals(thickness.getUnit().getAbbreviation())) {
					if (thickness instanceof IMeasurementValue) {
						nInChIComponent += "/s" + (int)((IMeasurementValue)thickness).getValue() + "t-9";
					} else if (thickness instanceof IErrorlessMeasurementValue) {
						nInChIComponent += "/s" + (int)((IErrorlessMeasurementValue)thickness).getValue() + "t-9";
					}
				}
			}

			// add spacegroup layer
			Spacegroup group = SubstanceManipulator.getSpacegroup(component);
			if (group == Spacegroup.AMORPHOUS) { nInChIComponent += "/k000"; }

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

}
