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
package io.github.egonw.nanojava.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.ParsingException;

import io.github.egonw.nanojava.data.Material;
import org.xmlcml.cml.base.CMLBuilder;
import org.xmlcml.cml.element.CMLList;

/**
 * Helper class to save CMLNano files in R.
 */
public class Loader {

	private String filename;
	
	public Loader(String filename) {
		this.filename = filename;
	}

	public List<Material> load() throws IOException, ParsingException {
		File file = new File(filename);
		Document nmxDoc;
		nmxDoc = new CMLBuilder().buildEnsureCML(new FileInputStream(file));
		Element rootElem = nmxDoc.getRootElement();
		if (rootElem instanceof CMLList) { // requirement
			CMLList cmlMaterial = (CMLList)rootElem;
			return Deserializer.fromCML(cmlMaterial);
		}
		return Collections.emptyList();
	}

}
