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

import junit.framework.Assert;

import org.bitbucket.nanojava.data.Nanomaterial;
import org.junit.Test;
import org.openscience.cdk.qsar.DescriptorValue;
import org.openscience.cdk.qsar.result.IDescriptorResult;

public abstract class NanomaterialDescriptorTest {

    INanomaterialDescriptor descriptor;
    
    public void setDescriptor(Class descriptorClass) throws Exception {
        if (descriptor == null) {
            Object descriptor = descriptorClass.newInstance();
            if (!(descriptor instanceof INanomaterialDescriptor)) {
                throw new Exception(
                    "The passed descriptor class must be a INanomaterialDescriptor"
                );
            }
            this.descriptor = (INanomaterialDescriptor)descriptor;
        }
    }
    
    @Test
    public void testCalculate_Empty() throws Exception {
        Nanomaterial material = new Nanomaterial();
        DescriptorValue value = descriptor.calculate(material);
        Assert.assertNotNull(value);
        IDescriptorResult result = value.getValue();
        Assert.assertNotNull(result);
        Assert.assertNotSame(0, result.length());
    }
    
}
