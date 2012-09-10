/* Copyright (C) 2012  Egon Willighagen <egonw@users.sf.net>
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
package org.bitbucket.nanojava.io;

import org.bitbucket.nanojava.data.MaterialType;
import org.bitbucket.nanojava.data.Nanomaterial;
import org.junit.Assert;
import org.junit.Test;
import org.xmlcml.cml.element.CMLMolecule;

public class SerializationTest {

	@Test
	public void roundTripType() {
		Nanomaterial material = new Nanomaterial("METALOXIDE");
		CMLMolecule cmlMaterial = Serializer.toCML(material);
		Assert.assertNotNull(cmlMaterial);
		System.out.println(cmlMaterial.toXML());
		Nanomaterial roundTripped = Deserializer.fromCML(cmlMaterial);
		Assert.assertNotNull(roundTripped);
		Assert.assertEquals(MaterialType.METALOXIDE, roundTripped.getType());

		material = new Nanomaterial("GRAPHENE");
		cmlMaterial = Serializer.toCML(material);
		Assert.assertNotNull(cmlMaterial);
		roundTripped = Deserializer.fromCML(cmlMaterial);
		Assert.assertNotNull(roundTripped);
		Assert.assertEquals(MaterialType.GRAPHENE, roundTripped.getType());
	}

}
