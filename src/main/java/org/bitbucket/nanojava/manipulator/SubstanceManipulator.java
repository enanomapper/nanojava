/* Copyright (C) 2014  Egon Willighagen <egonw@users.sf.net>
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
package org.bitbucket.nanojava.manipulator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bitbucket.nanojava.data.MaterialType;
import org.bitbucket.nanojava.data.SubstanceProperties;
import org.bitbucket.nanojava.data.measurement.EndPoints;
import org.bitbucket.nanojava.data.measurement.IEndPoint;
import org.bitbucket.nanojava.data.measurement.IMeasurement;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.interfaces.ISubstance;
import org.openscience.cdk.silent.MolecularFormula;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

public class SubstanceManipulator {

	public static IMeasurement getSize(ISubstance substance) {
		Map<IEndPoint,IMeasurement> characterizations = substance.getProperty(SubstanceProperties.CHARACTERIZATIONS);
		if (characterizations == null) return null;
		return characterizations.get(EndPoints.SIZE);
	}

	public static void setSize(ISubstance substance, IMeasurement size) {
		if (size.getEndPoint() == null) size.setEndPoint(EndPoints.SIZE);
		addCharacterization(substance, size);
	}

	public static IMeasurement getZetaPotential(ISubstance substance) {
		Map<IEndPoint,IMeasurement> characterizations =
			substance.getProperty(SubstanceProperties.CHARACTERIZATIONS);
		if (characterizations == null) return null;
		return characterizations.get(EndPoints.ZETA_POTENTIAL);
	}

	public static void setZetaPotential(ISubstance substance, IMeasurement zetaPotential) {
		if (zetaPotential.getEndPoint() == null) zetaPotential.setEndPoint(EndPoints.ZETA_POTENTIAL);
		addCharacterization(substance, zetaPotential);
	}

	public static IMolecularFormula getChemicalComposition(ISubstance substance) {
		if (substance.getAtomContainerCount() == 0) return null;
		IMolecularFormula chemicalComposition = new MolecularFormula();
		for (IAtomContainer container : substance.atomContainers()) {
			chemicalComposition.add(
				MolecularFormulaManipulator.getMolecularFormula(container)
			);
		}
		return chemicalComposition;
	}

    public static void setType(ISubstance substance, String type) {
    	for (MaterialType nmType : MaterialType.values()) {
	        if (nmType.name().equals(type)) {
	            setType(substance, nmType);
	            return;
	        }
	    }
	    throw new IllegalArgumentException(
	        "Unsupported MaterialType"
	    );
    }

    public static void setType(ISubstance substance, MaterialType type) {
    	substance.setProperty(SubstanceProperties.TYPE, type);
    }

    public static MaterialType getType(ISubstance substance) {
        return substance.getProperty(SubstanceProperties.TYPE);
    }

    public static List<String> getLabels(ISubstance substance) {
    	List<String> labels = substance.getProperty(SubstanceProperties.LABELS);
    	if (labels == null) return Collections.emptyList();
    	return Collections.unmodifiableList(labels);
    }

    public static Map<IEndPoint,IMeasurement> getCharacterizations(ISubstance substance) {
		Map<IEndPoint,IMeasurement> characterizations =
			substance.getProperty(SubstanceProperties.CHARACTERIZATIONS);
    	if (characterizations == null) return Collections.emptyMap();
    	return Collections.unmodifiableMap(characterizations);
    }

    public static void addCharacterization(ISubstance substance, IMeasurement characterization) {
    	if (characterization == null || characterization.getEndPoint() == null)
    		throw new NullPointerException("The characterization or its end point is null");
		Map<IEndPoint,IMeasurement> characterizations =
			substance.getProperty(SubstanceProperties.CHARACTERIZATIONS);
    	if (characterizations == null) {
    		characterizations = new HashMap<IEndPoint,IMeasurement>();
    		substance.setProperty(SubstanceProperties.CHARACTERIZATIONS, characterizations);
    	}
    	characterizations.put(characterization.getEndPoint(), characterization);
    }

    public static void setLabels(ISubstance substance, List<String> newLabels) {
    	if (newLabels == null || newLabels.size() == 0) return;
    	List<String> labels = substance.getProperty(SubstanceProperties.LABELS);
    	if (labels == null) {
    		labels = new ArrayList<String>();
    		substance.setProperty(SubstanceProperties.LABELS, labels);
    	}
    	labels.addAll(newLabels);
    }

}
