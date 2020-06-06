/* Copyright (C) 2020 Ammar Ammar <ammar257ammar@gmail.com>
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

package org.bitbucket.nanojava;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "nj")
public class CliOptions {

	@Option(names = {"-h", "-?", "--help" }, usageHelp = true, description = "Display a help message")
	boolean help = false;

	@Option(names = {"-d", "--descriptor"}, description = "Select an operation to perform: desc (calculate descriptor value)", required = false)
	String descriptor = "";

	@Option(names = {"-f", "--file"}, description = "Select a file to read the materials from)", required = false)
	String file = null;
	
	@Option(names = {"-o", "--output"}, description = "Select an output file path to write the descriptor value(s)", required = false)
	String output = null;
	
	@Option(names = {"-m", "--material"}, description = "Provide the material formulae (e.g. Fe3O4)", required = false)
	String material = "";
	
	CliOptions(String[] args) {
		try {
			CliOptions cliOptions = CommandLine.populateCommand(this, args);
			
			if (cliOptions.help) {
				new CommandLine(this).usage(System.out);
				System.exit(0);
			}
			
		} catch (CommandLine.ParameterException pe) {
			System.out.println(pe.getMessage());
			new CommandLine(this).usage(System.out);
			System.exit(64);
		}
	}
}
