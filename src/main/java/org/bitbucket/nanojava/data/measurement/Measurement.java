/* Copyright (C) 2011-2013  Egon Willighagen <egonw@users.sf.net>
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
package org.bitbucket.nanojava.data.measurement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.jqudt.Unit;

public abstract class Measurement implements IMeasurement {

	protected IEndPoint endPoint;
	protected Unit unit;
	private List<ICondition> conditions;

    public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public Unit getUnit() {
		return this.unit;
	}

	@Override
	public List<ICondition> getConditions() {
		if (this.conditions == null) return Collections.emptyList();
		return this.conditions;
	}

	@Override
	public void setConditions(List<ICondition> conditions) {
		if (conditions == null || conditions.size() == 0) return;
		this.conditions = new ArrayList<ICondition>();
		this.conditions.addAll(conditions);
	}

	@Override
	public void setEndPoint(IEndPoint endPoint) {
		this.endPoint = endPoint;
	}

	@Override
	public IEndPoint getEndPoint() {
		return this.endPoint;
	}

}
