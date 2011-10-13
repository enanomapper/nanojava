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
import org.bitbucket.nanojava.data.measurement.IMeasurement;
import org.bitbucket.nanojava.data.measurement.IMeasurementRange;
import org.bitbucket.nanojava.data.measurement.IMeasurementValue;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.qsar.DescriptorSpecification;
import org.openscience.cdk.qsar.DescriptorValue;
import org.openscience.cdk.qsar.result.DoubleResult;
import org.openscience.cdk.qsar.result.DoubleResultType;
import org.openscience.cdk.qsar.result.IDescriptorResult;

public class ParticleSizeDescriptor implements INanomaterialDescriptor {

	public String[] getDescriptorNames() {
        return new String[]{"Size"};
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
	        "http://egonw.github.com/resource/NM_001005",
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

	    IMeasurement size = container.getSize();
	    if (size == null) return newNaNDescriptor();

	    double sizeValue = Double.NaN;
	    if (size instanceof IMeasurementRange) {
	        IMeasurementRange range = (IMeasurementRange)size;
	        // take the avg value
	        sizeValue = (range.getMaximumValue() + range.getMinimumValue())/2.0;
	    } else if (size instanceof IMeasurementValue) {
	        IMeasurementValue range = (IMeasurementValue)size;
	        sizeValue = range.getValue();
	    }

		return new DescriptorValue(
		    getSpecification(),
		    getParameterNames(),
		    getParameters(),
		    new DoubleResult(sizeValue),
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

}
