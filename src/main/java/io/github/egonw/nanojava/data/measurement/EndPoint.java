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

public class EndPoint implements IEndPoint {

	private String label;
	private IEndPoint parent;
	// for NPO support
	private URI uri;

	public EndPoint(String label, URI uri, IEndPoint parent) {
		this.label = label;
		this.uri = uri;
		this.parent = parent;
	}

	@Override
	public String getLabel() {
		return this.label;
	}

	@Override
	public URI getURI() {
		return this.uri;
	};
	
	@Override
	public IEndPoint getParent() {
		return this.parent;
	};

	@Override
	public int hashCode() {
		return (""+label+uri+parent).hashCode();
	}

}
