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

import java.util.ArrayList;
import java.util.List;

import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.smiles.SmilesParser;

public class MaterialBuilder {

	private Material material;
	
	private MaterialBuilder(String type) {
		this.material = new Material(type);
	}

	public static MaterialBuilder type(String type) {
		MaterialBuilder builder = new MaterialBuilder(type);
		return builder;
	}

	public MaterialBuilder label(String label) {
		List<String> labels = this.material.getLabels();
		if (labels == null || labels.size() == 0) labels = new ArrayList<String>();
		labels.add(label);
		this.material.setLabels(labels);
		return this;
	}

	public MaterialBuilder labels(String... labels) {
		List<String> mlabels = this.material.getLabels();
		if (mlabels == null) {
			mlabels = new ArrayList<String>();
			this.material.setLabels(mlabels);
		}
		for (String label : labels) mlabels.add(label);
		return this;
	}

	public MaterialBuilder componentFromSMILES(int order, String componentSMILES) throws InvalidSmilesException {
		SmilesParser parser = new SmilesParser(material.getBuilder());
		IAtomContainer core = parser.parseSmiles(componentSMILES);
		return this.component(order, core);
	}

	public MaterialBuilder component(int order, IAtomContainer component) {
		component.setProperty(Material.ORDER, order);
		this.material.addAtomContainer(component);
		return this;
	}

	public Material asMaterial() {
		return this.material;
	}

}
