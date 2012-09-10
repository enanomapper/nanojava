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

import org.bitbucket.nanojava.data.Nanomaterial;
import org.xmlcml.cml.element.CMLMolecule;
import org.xmlcml.cml.element.CMLScalar;

public class Deserializer {

	public static Nanomaterial fromCML(CMLMolecule cmlMaterial) {
		if (!cmlMaterial.getConvention().matches("nano:material")) return null;
		Nanomaterial material = new Nanomaterial("METALOXIDE");
		
		for (CMLScalar scalar : cmlMaterial.getScalarElements()) {
			if (scalar.getDictRef().equals("nano:type")) material.setType(scalar.getValue());
		}
		return material;
	}

}