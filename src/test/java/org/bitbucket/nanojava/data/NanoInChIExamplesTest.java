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

import java.io.ByteArrayOutputStream;

import org.bitbucket.nanojava.data.measurement.EndPoints;
import org.bitbucket.nanojava.data.measurement.ErrorlessMeasurementValue;
import org.bitbucket.nanojava.inchi.NInChIGenerator;
import org.bitbucket.nanojava.io.CDKSerializer;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.xmlcml.cml.element.CMLMoleculeList;

import com.github.jqudt.onto.units.LengthUnit;

import nu.xom.Document;
import nu.xom.Serializer;

public class NanoInChIExamplesTest {

	@Ignore("Known to fail")
	@Test
	public void figureEightLeft() throws Exception {
		Material material = MaterialBuilder.type("METALOXIDE")
			.label("silica nanoparticles with gold coating")
			.componentFromSMILES(1, "[Au]", "SHELL")
			.componentFromSMILES(2, "O=[Si]=O", "SPHERE")
			.asMaterial();
		String nanoInChI = NInChIGenerator.generator(material);
		Assert.assertEquals("InChI=1A/Au/msh/s2t-9!O2Si/c1-3-2/msp/s20d-9/k000/y2&1", nanoInChI);

		CMLMoleculeList cmlMaterial = CDKSerializer.toCML(material);
		Assert.assertNotNull(cmlMaterial);
		System.out.println(asIndentedString(cmlMaterial));
	}

	@Test
	public void simpleGoldParticle() throws Exception {
		Material material = MaterialBuilder.type("METAL")
			.componentFromSMILES(1, "[Au]", "SPHERE", new ErrorlessMeasurementValue(EndPoints.DIAMETER, 3.0, LengthUnit.NM))
			.asMaterial();
		String nanoInChI = NInChIGenerator.generator(material);
		Assert.assertEquals("InChI=1A/Au/msp/s3d-9/y1", nanoInChI);
	}

	private String asIndentedString(CMLMoleculeList cmlMaterial) throws Exception {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		Serializer xomSerializer = new Serializer(output, "UTF-8");
		xomSerializer.setIndent(2);
		xomSerializer.write(new Document(cmlMaterial));
		return output.toString();
	}

}
