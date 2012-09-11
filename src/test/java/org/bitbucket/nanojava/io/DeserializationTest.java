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

import java.io.IOException;

import junit.framework.Assert;

import nu.xom.ParsingException;

import org.bitbucket.nanojava.data.MaterialType;
import org.bitbucket.nanojava.data.Nanomaterial;
import org.junit.Test;

public class DeserializationTest {

	@Test
	public void readType() throws ParsingException, IOException {
		String input = "<molecule xmlns=\"http://www.xml-cml.org/schema\" xmlns:nano=\"http://.../\" convention=\"nano:material\"><scalar dictRef=\"nano:type\" dataType=\"xsd:string\">GRAPHENE</scalar></molecule>";
		Nanomaterial material = Deserializer.fromCMLString(input);
		Assert.assertNotNull(material);
		Assert.assertEquals(MaterialType.GRAPHENE, material.getType());
	}

}
