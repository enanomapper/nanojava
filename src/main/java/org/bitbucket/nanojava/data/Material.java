/* Copyright (C) 2011-2022  Egon Willighagen <egonw@users.sf.net>
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
package org.bitbucket.nanojava.data;

import java.util.List;
import java.util.Map;

import org.bitbucket.nanojava.data.measurement.IEndPoint;
import org.bitbucket.nanojava.data.measurement.IMeasurement;
import org.bitbucket.nanojava.manipulator.SubstanceManipulator;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.silent.Substance;

@SuppressWarnings("serial")
public class Material extends Substance {

	/**
	 * Property for IAtomContainers in this Material, from the inside to the outside.
	 * Order 1 is the core, order 2 the first shell, etc.
	 */
	public static String ORDER = "MaterialComponentOrder"; 

	public Material() {}

	public Material(MaterialType type) {
	    this.setType(type);
	}

	public Material(String type) throws IllegalArgumentException {
	    setType(type);
	}
	
	public IMeasurement getSize() {
		return SubstanceManipulator.getSize(this);
	}

	public void setSize(IMeasurement size) {
		SubstanceManipulator.setSize(this, size);
	}

	public IMeasurement getZetaPotential() {
		return SubstanceManipulator.getZetaPotential(this);
	}

	public void setZetaPotential(IMeasurement zetaPotential) {
		SubstanceManipulator.setZetaPotential(this, zetaPotential);
	}

	public IMolecularFormula getChemicalComposition() {
		return SubstanceManipulator.getChemicalComposition(this);
	}

    public void setType(String type) {
    	SubstanceManipulator.setType(this, type);
    }

    public void setType(MaterialType type) {
        SubstanceManipulator.setType(this, type);
    }

    public MaterialType getType() {
        return SubstanceManipulator.getType(this);
    }

    public void setMorphology(String type) {
    	SubstanceManipulator.setType(this, type);
    }

    public List<String> getLabels() {
    	return SubstanceManipulator.getLabels(this);
    }

    public Map<IEndPoint,IMeasurement> getCharacterizations() {
    	return SubstanceManipulator.getCharacterizations(this);
    }

    public void addCharacterization(IMeasurement characterization) {
    	SubstanceManipulator.addCharacterization(this, characterization);
    }

    public void setLabels(List<String> newLabels) {
    	SubstanceManipulator.setLabels(this, newLabels);
    }
}
