/* Copyright (C) 2011  Egon Willighagen <egonw@users.sf.net>
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bitbucket.nanojava.data.measurement.EndPoints;
import org.bitbucket.nanojava.data.measurement.IEndPoint;
import org.bitbucket.nanojava.data.measurement.IMeasurement;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.silent.Substance;

@SuppressWarnings("serial")
public class Material extends Substance {

	/** IChemObject property of type MaterialType. */
	private final String CHARACTERIZATIONS = "nanojava.characterizations";
	/** IChemObject property of type {@link MaterialType}. */
	private final String TYPE = "nanojava.type";
	/** IChemObject property of type List<String>. */
	private final String LABELS = "nanojava.labels";

	private IMolecularFormula chemicalComposition;

	public Material() {}

	public Material(MaterialType type) {
	    this.setType(type);
	}

	public Material(String type) throws IllegalArgumentException {
	    setType(type);
	}
	
	public IMeasurement getSize() {
		Map<IEndPoint,IMeasurement> characterizations = getProperty(CHARACTERIZATIONS);
		if (characterizations == null) return null;
		return characterizations.get(EndPoints.SIZE);
	}

	public void setSize(IMeasurement size) {
		if (size.getEndPoint() == null) size.setEndPoint(EndPoints.SIZE);
		addCharacterization(size);
	}

	public IMeasurement getZetaPotential() {
		Map<IEndPoint,IMeasurement> characterizations = getProperty(CHARACTERIZATIONS);
		if (characterizations == null) return null;
		return characterizations.get(EndPoints.ZETA_POTENTIAL);
	}

	public void setZetaPotential(IMeasurement zetaPotential) {
		if (zetaPotential.getEndPoint() == null) zetaPotential.setEndPoint(EndPoints.ZETA_POTENTIAL);
		addCharacterization(zetaPotential);
	}

	public void setChemicalComposition(IMolecularFormula chemicalComposition) {
		this.chemicalComposition = chemicalComposition;
	}
	public IMolecularFormula getChemicalComposition() {
		return chemicalComposition;
	}

    public void setType(String type) {
    	for (MaterialType nmType : MaterialType.values()) {
	        if (nmType.name().equals(type)) {
	            this.setType(nmType);
	            return;
	        }
	    }
	    throw new IllegalArgumentException(
	        "Unsupported MaterialType"
	    );
    }

    public void setType(MaterialType type) {
        setProperty(TYPE, type);
    }

    public MaterialType getType() {
        return getProperty(TYPE);
    }

    public List<String> getLabels() {
    	List<String> labels = getProperty(LABELS);
    	if (labels == null) return Collections.emptyList();
    	return Collections.unmodifiableList(labels);
    }

    public Map<IEndPoint,IMeasurement> getCharacterizations() {
		Map<IEndPoint,IMeasurement> characterizations = getProperty(CHARACTERIZATIONS);
    	if (characterizations == null) return Collections.emptyMap();
    	return Collections.unmodifiableMap(characterizations);
    }

    public void addCharacterization(IMeasurement characterization) {
    	if (characterization == null || characterization.getEndPoint() == null)
    		throw new NullPointerException("The characterization or its end point is null");
		Map<IEndPoint,IMeasurement> characterizations = getProperty(CHARACTERIZATIONS);
    	if (characterizations == null) {
    		characterizations = new HashMap<IEndPoint,IMeasurement>();
    		setProperty(CHARACTERIZATIONS, characterizations);
    	}
    	characterizations.put(characterization.getEndPoint(), characterization);
    }

    public void setLabels(List<String> newLabels) {
    	if (newLabels == null || newLabels.size() == 0) return;
    	List<String> labels = getProperty(LABELS);
    	if (labels == null) {
    		labels = new ArrayList<String>();
    		setProperty(LABELS, labels);
    	}
    	labels.addAll(newLabels);
    }
}
