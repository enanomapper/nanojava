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

import org.bitbucket.nanojava.data.Morphology;
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
 * Helper class to generate nano InChIs for the provided ISubstance.
 */
public class NInChIGenerator {

	public static String generator(ISubstance substance) throws CDKException {
		String nanoInChI = "InChI=1A/";
		for (int i=0; i<substance.getAtomContainerCount(); i++) {
			IAtomContainer component = substance.getAtomContainer(i);
			InChIGenerator generator = InChIGeneratorFactory.getInstance().getInChIGenerator(component);
			String inchi = generator.getInchi();
			System.out.println("InChI: " + inchi);
			nanoInChI += inchi.substring(9); // skip InChI=1S/
			Morphology morph = SubstanceManipulator.getMorphology(component);
			if (morph == Morphology.SPHERE) { nanoInChI += "/msp"; } else
			if (morph == Morphology.SHELL) { nanoInChI += "/msh"; }
			IMeasurement diameter = SubstanceManipulator.getMeasurement(component, EndPoints.DIAMETER);
			System.out.println("diam: " + diameter);
			if (diameter != null) {
				if ("nm".equals(diameter.getUnit().getAbbreviation())) {
					if (diameter instanceof IMeasurementValue) {
						nanoInChI += "/s" + (int)((IMeasurementValue)diameter).getValue() + "d-9";
					} else if (diameter instanceof IErrorlessMeasurementValue) {
						nanoInChI += "/s" + (int)((IErrorlessMeasurementValue)diameter).getValue() + "d-9";
					}
				}
			}
			nanoInChI += "/y" + (i+1);
		}
		return nanoInChI;
	}

}
