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

import org.bitbucket.nanojava.data.MaterialType;
import org.bitbucket.nanojava.manipulator.SubstanceManipulator;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IIsotope;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.interfaces.ISubstance;
import org.openscience.cdk.qsar.DescriptorSpecification;
import org.openscience.cdk.qsar.DescriptorValue;
import org.openscience.cdk.qsar.result.DoubleResult;
import org.openscience.cdk.qsar.result.DoubleResultType;
import org.openscience.cdk.qsar.result.IDescriptorResult;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

public class MetalElementMassDescriptor implements ISubstanceDescriptor {

	public String[] getDescriptorNames() {
        return new String[]{"MeGrp"};
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
	        "http://egonw.github.com/resource/NM_001008",
	        this.getClass().getName(),
	        "$Id: 9927243df29a118e9bfd0b8624bc8d77d3c6db07 $",
	        "The NanoJava Project"
	    );
	}

	public void setParameters(Object[] arg0) throws CDKException {
		return; // no parameters
	}

	public DescriptorValue calculate(ISubstance container) {
	    if (container == null) return newNaNDescriptor();
	    if (SubstanceManipulator.getType(container) != MaterialType.METALOXIDE)
	        return newNaNDescriptor();
	    IMolecularFormula molFormula = SubstanceManipulator.getChemicalComposition(container);
	    if (molFormula == null) return newNaNDescriptor();

	    int metalCount = 0;
	    double mass = 0;
        for (IIsotope isotope : molFormula.isotopes()) {
            if (!"O".equals(isotope.getSymbol())) {
                metalCount++;
                if (metalCount > 1) return newNaNDescriptor();
                mass = MolecularFormulaManipulator.getNaturalExactMass(
                    MolecularFormulaManipulator.getMolecularFormula(
                        isotope.getSymbol(), isotope.getBuilder()
                    )
                );
            }
        }
        
		return new DescriptorValue(
		    getSpecification(),
		    getParameterNames(),
		    getParameters(),
		    new DoubleResult(mass),
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
