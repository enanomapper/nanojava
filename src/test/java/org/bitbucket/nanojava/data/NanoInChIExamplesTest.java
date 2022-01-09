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

import org.bitbucket.nanojava.data.measurement.EndPoints;
import org.bitbucket.nanojava.data.measurement.ErrorlessMeasurementValue;
import org.bitbucket.nanojava.data.measurement.MeasurementValue;
import org.bitbucket.nanojava.inchi.NInChIGenerator;
import org.junit.Assert;
import org.junit.Test;
import org.openscience.cdk.exception.CDKException;

import com.github.jqudt.onto.units.LengthUnit;

/**
 * These tests are aimed at testing the NInChI Alpha version.
 *
 * @author egonw
 */
public class NanoInChIExamplesTest {

	/**
	 * Tests the left-side example from Figure 8 of <a href="https://doi.org/10.3390/nano10122493">this article</a>.
	 */
	@Test
	public void figureEightLeft() throws Exception {
		Material material = MaterialBuilder.type("METALOXIDE")
			.label("silica nanoparticles with gold coating")
			.componentFromSMILES(1, "O=[Si]=O", "SPHERE", "AMORPHOUS", new ErrorlessMeasurementValue(EndPoints.DIAMETER, 20, LengthUnit.NM))
			.componentFromSMILES(2, "[Au]", "SHELL", new ErrorlessMeasurementValue(EndPoints.THICKNESS, 2, LengthUnit.NM))
			.asMaterial();

		String nanoInChI = NInChIGenerator.generator(material);
		Assert.assertEquals("NInChI=0.00.1A/Au/msh/s2t-9!O2Si/c1-3-2/msp/s20d-9/k000/y2&1", nanoInChI);
	}

	@Test
	public void figureEightLeft_WithErrors() throws Exception {
		Material material = MaterialBuilder.type("METALOXIDE")
			.label("silica nanoparticles with gold coating")
			.componentFromSMILES(1, "O=[Si]=O", "SPHERE", "AMORPHOUS", new MeasurementValue(EndPoints.DIAMETER, 20, 3, LengthUnit.NM))
			.componentFromSMILES(2, "[Au]", "SHELL", new MeasurementValue(EndPoints.THICKNESS, 2, 0.5, LengthUnit.NM))
			.asMaterial();

		String nanoInChI = NInChIGenerator.generator(material);
		Assert.assertEquals("NInChI=0.00.1A/Au/msh/s2t-9!O2Si/c1-3-2/msp/s20d-9/k000/y2&1", nanoInChI);
	}

	/**
	 * Tests the right-side example from Figure 8 of <a href="https://doi.org/10.3390/nano10122493">this article</a>.
	 */
	@Test
	public void figureEightRight() throws Exception {
		Material material = MaterialBuilder.type("METALOXIDE")
			.label("gold nanoparticle with organic coating")
			.componentFromSMILES(1, "[Au]", "SPHERE", new ErrorlessMeasurementValue(EndPoints.DIAMETER, 20, LengthUnit.NM))
			.componentFromSMILES(2, "CCCCCCCCCCCCCCCC[N+](C)(C)C.[Br-]", null)
			.asMaterial();

		String nanoInChI = NInChIGenerator.generator(material);
		Assert.assertEquals("NInChI=0.00.1A/Au/msp/s20d-9!C19H42N.BrH/c1-5-6-7-8-9-10-11-12-13-14-15-16-17-18-19-20(2,3)4;/h5-19H2,1-4H3;1H/q+1;/p-1/y1&2", nanoInChI);
	}

	@Test
	public void simpleGoldParticle() throws Exception {
		Material material = MaterialBuilder.type("METAL")
			.componentFromSMILES(1, "[Au]", "SPHERE", new ErrorlessMeasurementValue(EndPoints.DIAMETER, 3.0, LengthUnit.NM))
			.asMaterial();
		String nanoInChI = NInChIGenerator.generator(material);
		Assert.assertEquals("NInChI=0.00.1A/Au/msp/s3d-9/y1", nanoInChI);
	}

	@Test
	public void swcnt() throws CDKException {
		Material material = MaterialBuilder.type("CARBONNANOTUBE")
			.componentFromSMILES(1, "[C]", "TUBE", null, "THREE_BY_ONE", new ErrorlessMeasurementValue(EndPoints.DIAMETER, 0.4, LengthUnit.NM))
			.asMaterial();
		String nanoInChI = NInChIGenerator.generator(material);
		Assert.assertEquals("NInChI=0.00.1A/C/mtu/s4d-10/w(3,1)/y1", nanoInChI);
	}
}
