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
package org.bitbucket.nanojava.descriptor;

import org.bitbucket.nanojava.data.Nanomaterial;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.qsar.DescriptorSpecification;
import org.openscience.cdk.qsar.DescriptorValue;
import org.openscience.cdk.qsar.result.DoubleResult;
import org.openscience.cdk.qsar.result.DoubleResultType;
import org.openscience.cdk.qsar.result.IDescriptorResult;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

/**
 * Values are calculated with Mopac2012 using the PM6 method.
 */
public class IonizationEnthalpyMopac2012Descriptor implements INanomaterialDescriptor {

	public String[] getDescriptorNames() {
        return new String[]{"IonEnthMopac2012"};
	}

	public String[] getParameterNames() {
        return new String[0];
	}

	public Object getParameterType(String arg0) {
		return null;
	}

	public Object[] getParameters() {
		return new Object[0];
	}

	public DescriptorSpecification getSpecification() {
	    return new DescriptorSpecification(
	        "http://egonw.github.com/resource/NM_001001",
	        this.getClass().getName(),
	        "$Id: 9927243df29a118e9bfd0b8624bc8d77d3c6db07 $",
	        "The NanoJava Project"
	    );
	}

	public void setParameters(Object[] arg0) throws CDKException {
		return; // no parameters
	}

	public DescriptorValue calculate(Nanomaterial container) {
        if (container == null) return newNaNDescriptor();

	    IMolecularFormula molFormula = container.getChemicalComposition();
	    if (molFormula == null) return newNaNDescriptor();
	    String mfString = MolecularFormulaManipulator.getString(molFormula);

	    double val = Double.NaN;
	    if ("Al2O3".equals(mfString)) {
	    	val = 1187.82572;
	    } else if ("Bi2O3".equals(mfString)) {
	    	val = 1137.39784; 
	    } else if ("CeO2".equals(mfString)) {
	    	val = 944.70000;
	    } else if ("CoO".equals(mfString)) {
	    	val = 594.58375; 
	    } else if ("Cr2O3".equals(mfString)) {
	    	val = 1266.39724; 
	    } else if ("CuO".equals(mfString)) {
	    	val = 713.73801;
	    } else if ("CuFe2O4Zn".equals(mfString)) {
	    	val = 662.43584; // links via Aldrich to PubChem SID 24883132 telling FeIII, Zn2+, Cu2+
	    	                 // taking the lowest enthalpy
	    } else if ("Fe2O3".equals(mfString)) {
	    	val = 1363.39566; 
	    } else if ("FeO".equals(mfString)) {
	    	val = 726.86041;
	    } else if ("Fe3O4".equals(mfString)) {
	    	val = 726.86041; // take the lowest, this FeII, Fe3O4 = FeO + Fe2O3
	    } else if ("In2O3".equals(mfString)) {
	    	val = 1271.13268; 
	    } else if ("La2O3".equals(mfString)) {
	    	val = 1017.21598; 
	    } else if ("MnO2".equals(mfString)) {
	    	val = 1602.02881; // MnO2
	    } else if ("NiO".equals(mfString)) {
	    	val = 596.79692; 
	    } else if ("Sb2O3".equals(mfString)) {
	    	val = 1233.05572; 
	    } else if ("SiO2".equals(mfString)) {
	    	val = 1686.38205; 
	    } else if ("SnO2".equals(mfString)) {
	    	val = 1717.32461; 
	    } else if ("TiO2".equals(mfString)) {
	    	val = 1575.72837; 
	    } else if ("V2O3".equals(mfString)) {
	    	val = 1097.72684; 
	    } else if ("Y2O3".equals(mfString)) {
	    	val = 837.15457; 
	    } else if ("OZn".equals(mfString)) {
	    	val = 662.43584;
	    } else if ("O2Zr".equals(mfString)) {
	    	val = 1357.66444; 
	    } else {
		    return newNaNDescriptor();
	    }

	    return new DescriptorValue(
	    	getSpecification(),
	    	getParameterNames(),
	    	getParameters(),
	    	new DoubleResult(val),
	    	getDescriptorNames()
	    );
	}

	private DescriptorValue newNaNDescriptor() {
	    return new DescriptorValue(
	        getSpecification(),
	        getParameterNames(),
	        getParameters(),
	        new DoubleResult(Double.NaN),
	        getDescriptorNames()
	    );
    }

    public IDescriptorResult getDescriptorResultType() {
		return new DoubleResultType();
	}

	@Override
	public void initialise(IChemObjectBuilder builder) {
		// nothing to be done
	}

}
