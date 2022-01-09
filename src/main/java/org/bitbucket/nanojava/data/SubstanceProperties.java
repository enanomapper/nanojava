/* Copyright (C) 2014  Egon Willighagen <egonw@users.sf.net>
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

import org.openscience.cdk.interfaces.ISubstance;

public enum SubstanceProperties {

	/** {@link ISubstance} property of type MaterialType. */
	CHARACTERIZATIONS,
	/** {@link ISubstance} property of type {@link MaterialType}. */
	TYPE,
	/** {@link ISubstance} property of type List<String>. */
	LABELS,
	/** {@link ISubstance} property of type {@link Morphology}. */
	MORPHOLOGY,
	/** {@link ISubstance} property of type {@link Spacegroup}. */
	SPACEGROUP,
	/** {@link ISubstance} property of type {@link Chirality}. */
	CHIRALITYTYPE

}
