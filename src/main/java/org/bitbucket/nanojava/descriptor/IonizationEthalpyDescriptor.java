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
import org.openscience.cdk.interfaces.IIsotope;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.qsar.DescriptorSpecification;
import org.openscience.cdk.qsar.DescriptorValue;
import org.openscience.cdk.qsar.result.DoubleResult;
import org.openscience.cdk.qsar.result.DoubleResultType;
import org.openscience.cdk.qsar.result.IDescriptorResult;

public class IonizationEthalpyDescriptor implements INanomaterialDescriptor {

	public String[] getDescriptorNames() {
        return new String[]{"IonEnth"};
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

	    double val = Double.NaN;
	    for (IIsotope isotope : molFormula.isotopes()) {
	        if ("Al".equals(isotope.getSymbol())) {
	            val = 1187.83;
	        } else if ("Bi".equals(isotope.getSymbol())) {
	            val = 1137.40; 
	        } else if ("Ce".equals(isotope.getSymbol())) {
	            val = 944.70; // CeO2 (PM6 SPARKLE)
	        } else if ("Co".equals(isotope.getSymbol())) {
	            val = 601.80; 
	        } else if ("Cr".equals(isotope.getSymbol())) {
	            val = 1268.70; 
	        } else if ("Cu".equals(isotope.getSymbol())) {
	            val = 706.25; 
	        } else if ("Fe".equals(isotope.getSymbol())) {
	            val = 1408.29; 
	        } else if ("In".equals(isotope.getSymbol())) {
	            val = 1271.13; 
	        } else if ("La".equals(isotope.getSymbol())) {
	            val = 1017.22; 
	        } else if ("Mn".equals(isotope.getSymbol())) {
	            val = 1601.91; // MnO2
	        } else if ("Ni".equals(isotope.getSymbol())) {
	            val = 576.90; 
	        } else if ("Sb".equals(isotope.getSymbol())) {
	            val = 1233.06; 
	        } else if ("Si".equals(isotope.getSymbol())) {
	            val = 1686.38; 
	        } else if ("Sn".equals(isotope.getSymbol())) {
	            val = 1717.32; 
	        } else if ("Ti".equals(isotope.getSymbol())) {
	            val = 1575.73; 
	        } else if ("V".equals(isotope.getSymbol())) {
	            val = 1079.73; 
	        } else if ("Y".equals(isotope.getSymbol())) {
	            val = 837.15; 
	        } else if ("Zn".equals(isotope.getSymbol())) {
	            val = 662.44;
	        } else if ("Zr".equals(isotope.getSymbol())) {
	            val = 1357.66; 
	        } else {
	            continue;
	        }

	        return new DescriptorValue(
	                getSpecification(),
	                getParameterNames(),
	                getParameters(),
	                new DoubleResult(val),
	                getDescriptorNames()
	        );
	    }
	    
	    return newNaNDescriptor();
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

}
