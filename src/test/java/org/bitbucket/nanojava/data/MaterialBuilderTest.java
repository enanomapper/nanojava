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
package org.bitbucket.nanojava.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.bitbucket.nanojava.manipulator.SubstanceManipulator;
import org.junit.Test;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;

public class MaterialBuilderTest {

	@Test
	public void type() throws Exception {
		Material material = MaterialBuilder.type("METALOXIDE")
			.asMaterial();
		assertEquals(MaterialType.METALOXIDE, material.getType());
	}

	@Test
	public void label() throws Exception {
		Material material = MaterialBuilder.type("METALOXIDE")
			.label("silica nanoparticles with gold coating")
			.asMaterial();
		List<String> labels = material.getLabels();
		assertEquals(1, labels.size());
		assertEquals("silica nanoparticles with gold coating", labels.get(0));
	}

	@Test
	public void labels() throws Exception {
		Material material = MaterialBuilder.type("METALOXIDE")
			.labels("silica nanoparticles with gold coating", "SiO2-Au")
			.asMaterial();
		List<String> labels = material.getLabels();
		assertEquals(2, labels.size());
		assertTrue(labels.contains("silica nanoparticles with gold coating"));
		assertTrue(labels.contains("SiO2-Au"));
	}

	@Test
	public void componentMorphology() throws Exception {
		Material material = MaterialBuilder.type("METALOXIDE")
			.componentFromSMILES(1, "O=[Si]=O", "SPHERE")
			.asMaterial();
		assertEquals(1, material.getAtomContainerCount());
		IAtomContainer component = material.getAtomContainer(0);
		assertNotNull(component);
		assertEquals(Morphology.SPHERE, SubstanceManipulator.getMorphology(component));
	}

	@Test
	public void componentMorphologyBad() throws Exception {
        Exception exception = assertThrows(
            IllegalArgumentException.class, () ->
            {
            	MaterialBuilder.type("METALOXIDE")
            		.componentFromSMILES(1, "O=[Si]=O", "FOO");
            }
        );
        assertNotNull(exception);
        System.out.println(exception.getMessage());
        assertTrue(exception.getMessage().contains("Unsupported Morphology"));
	}

	@Test
	public void badSMILES() throws Exception {
        assertThrows(
            InvalidSmilesException.class, () ->
            {
            	MaterialBuilder.type("METALOXIDE")
            		.componentFromSMILES(1, "O=[Si]1", "SPHERE");
            }
        );
	}
}
