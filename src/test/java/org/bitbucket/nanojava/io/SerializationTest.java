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

import java.util.ArrayList;
import java.util.List;

import org.bitbucket.nanojava.data.MaterialType;
import org.bitbucket.nanojava.data.Nanomaterial;
import org.bitbucket.nanojava.data.measurement.MeasurementValue;
import org.bitbucket.nanojava.data.measurement.Unit;
import org.junit.Assert;
import org.junit.Test;
import org.xmlcml.cml.element.CMLList;
import org.xmlcml.cml.element.CMLMolecule;

public class SerializationTest {

	@Test
	public void roundTripType() {
		Nanomaterial material = new Nanomaterial("METALOXIDE");
		CMLMolecule cmlMaterial = Serializer.toCML(material);
		Assert.assertNotNull(cmlMaterial);
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

	@Test
	public void roundTripSize() {
		Nanomaterial material = new Nanomaterial("METALOXIDE");
		material.setSize(new MeasurementValue(20.0, 7, Unit.NM));
		CMLMolecule cmlMaterial = Serializer.toCML(material);
		Assert.assertNotNull(cmlMaterial);
		System.out.println(cmlMaterial.toXML());
		Nanomaterial roundTripped = Deserializer.fromCML(cmlMaterial);
		Assert.assertNotNull(roundTripped);
		Assert.assertEquals(20.0, ((MeasurementValue)roundTripped.getSize()).getValue(), 0.1);
	}

	@Test
	public void roundTripZetaPotential() {
		Nanomaterial material = new Nanomaterial("METALOXIDE");
		material.setZetaPotential(new MeasurementValue(-45.0, 3, Unit.EV));
		CMLMolecule cmlMaterial = Serializer.toCML(material);
		Assert.assertNotNull(cmlMaterial);
		Nanomaterial roundTripped = Deserializer.fromCML(cmlMaterial);
		Assert.assertNotNull(roundTripped);
		Assert.assertEquals(-45.0, ((MeasurementValue)roundTripped.getZetaPotential()).getValue(), 0.1);
	}

	@Test
	public void roundTripList() {
		List<Nanomaterial> materials = new ArrayList<Nanomaterial>();
		materials.add(new Nanomaterial("GRAPHENE"));
		materials.add(new Nanomaterial("METALOXIDE"));
		CMLList list = Serializer.toCML(materials);
		Assert.assertNotNull(list);
		List<Nanomaterial> roundTripped = Deserializer.fromCML(list);
		Assert.assertNotNull(roundTripped);
		Assert.assertEquals(2, roundTripped.size());
		Assert.assertEquals(MaterialType.GRAPHENE, roundTripped.get(0).getType());
		Assert.assertEquals(MaterialType.METALOXIDE, roundTripped.get(1).getType());
	}
}
