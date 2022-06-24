/* Copyright (C) 2013  Egon Willighagen <egonw@users.sf.net>
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
package io.github.egonw.nanojava.data.measurement;

import java.net.URI;


public class EndPoints {

	public final static IEndPoint SIZE = new EndPoint("size", URI.create("http://purl.org/obo/owl/PATO#PATO_0000117"), null);
	public final static IEndPoint DIAMETER = new EndPoint("diameter (TEM)", URI.create("http://purl.org/obo/owl/PATO#0001334"), SIZE);
	public final static IEndPoint DIAMETER_TEM = new EndPoint("diameter (TEM)", URI.create("http://purl.org/obo/owl/PATO#PATO_0001334"), DIAMETER);
	public final static IEndPoint DIAMETER_DLS = new EndPoint("diameter (DLS)", URI.create("http://purl.bioontology.org/ontology/npo#NPO_1915"), DIAMETER);
	public final static IEndPoint THICKNESS = new EndPoint("diameter (TEM)", URI.create("http://purl.org/obo/owl/PATO#0000915"), SIZE);

	public final static IEndPoint ZETA_POTENTIAL = new EndPoint("zeta potential", URI.create("http://purl.bioontology.org/ontology/npo#NPO_1302"), null);

	public final static IEndPoint PH = new EndPoint("pH", URI.create("http://purl.org/obo/owl/UO#UO_0000196"), null);

	public final static IEndPoint PURITY = new EndPoint("purity", URI.create("http://purl.bioontology.org/ontology/npo#NPO_1345"), null);
}
