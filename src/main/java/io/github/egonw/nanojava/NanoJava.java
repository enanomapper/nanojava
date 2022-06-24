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

package io.github.egonw.nanojava;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.egonw.nanojava.data.Material;
import io.github.egonw.nanojava.descriptor.ISubstanceDescriptor;
import io.github.egonw.nanojava.io.Loader;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

import nu.xom.ParsingException;
import picocli.CommandLine;

import java.util.jar.JarEntry;
import java.util.jar.JarFile;


public class NanoJava {

	public static void main(String[] args) {

		CliOptions cli = new CliOptions(args);

		if (cli.help)
			printUsageAndExit();

		if (cli.descriptor.equals("")) {
			System.out.println("\nYou should provide the descriptor name to calculate!\n");
			printUsageAndExit();
		}

		if (cli.file == null && cli.material.equals("")) {
			System.out.println("\nYou should provide a material (using -m) or a file of materials (using -f) \n");
			printUsageAndExit();
		}

		if (cli.file != null) {

			Loader loader = new Loader(cli.file.trim());

			try {

				List<Material> materialList = loader.load();

				calcDescriptors(materialList, cli.descriptor, cli.output);

			} catch (IOException | ParsingException e) {
				e.printStackTrace();
			}

		} else if (!"".equals(cli.material.trim())) {

			Material material = new Material();
			material.addAtomContainer(MolecularFormulaManipulator.getAtomContainer(cli.material.trim(),
					DefaultChemObjectBuilder.getInstance()));
			
			List<String> labels = new ArrayList<String>(material.getLabels());
			labels.add("formula:"+cli.material.trim());
			
			material.setLabels(labels);

			calcDescriptors(material, cli.descriptor, cli.output);
		}
	}

	private static void calcDescriptors(List<Material> materialList, String descriptorStr, String output) {

		Map<String, ISubstanceDescriptor> descriptorMap = getDescriptorsList(descriptorStr);

		if (output == null) {

			System.out.print("Nano Material");

			for (Map.Entry<String, ISubstanceDescriptor> entry : descriptorMap.entrySet()) {
				System.out.print("\t" + entry.getKey());
			}

			System.out.println();
			
			for (Material material : materialList) {

				String formula = material.getLabels().stream().filter(s -> s.startsWith("formula:")).findFirst()
						.orElse("");

				System.out.print(formula.substring(8));

				for (Map.Entry<String, ISubstanceDescriptor> entry : descriptorMap.entrySet()) {
					System.out.print("\t" + entry.getValue().calculate(material).getValue().toString());
				}
				
				System.out.println();
			}

		} else {

			try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(output)))) {

				bw.write("Nano Material");

				for (Map.Entry<String, ISubstanceDescriptor> entry : descriptorMap.entrySet()) {
					bw.write("\t" + entry.getKey());
				}
				
				bw.newLine();

				for (Material material : materialList) {

					String formula = material.getLabels().stream().filter(s -> s.startsWith("formula:")).findFirst()
							.orElse("");

					bw.write(formula.substring(8));

					for (Map.Entry<String, ISubstanceDescriptor> entry : descriptorMap.entrySet()) {
						bw.write("\t" + entry.getValue().calculate(material).getValue().toString());
					}

					bw.newLine();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void calcDescriptors(Material material, String descriptorStr, String output) {

		List<Material> materialList = new ArrayList<Material>();
		materialList.add(material);

		calcDescriptors(materialList, descriptorStr, output);
	}

	private static Map<String, ISubstanceDescriptor> getDescriptorsList(String desc) {

		try {

			Map<String, ISubstanceDescriptor> map = new HashMap<String, ISubstanceDescriptor>();

			if ("all".equals(desc)) {

				String packageName = "io.github.egonw.nanojava.descriptor";

				String[] jars;
				String classPath = System.getProperty("java.class.path");
				jars = classPath.split(File.pathSeparator);

				for (String jar : jars) {
					
					JarFile jarFile;
					
					try {

						jarFile = new JarFile(jar);
						Enumeration<JarEntry> enumeration = jarFile.entries();

						while (enumeration.hasMoreElements()) {

							JarEntry jarEntry = (JarEntry) enumeration.nextElement();

							if (jarEntry.toString().endsWith(".class")) {
								
								String className = jarEntry.toString().replace('/', '.').replaceAll("\\.class", "");
								
								if (!(className.indexOf(packageName) != -1))
									continue;
								if (className.indexOf("ISubstanceDescriptor") != -1)
									continue;

								if (!map.keySet()
										.contains(className.replace("io.github.egonw.nanojava.descriptor.", ""))) {
									
									Class<?> descriptorClass = Class.forName(className);
									Object descriptorO = descriptorClass.newInstance();
									
									if (!(descriptorO instanceof ISubstanceDescriptor))
										continue;
									
									map.put(className.replace("io.github.egonw.nanojava.descriptor.", ""),
											(ISubstanceDescriptor) descriptorO);
								}
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			} else {

				Class<?> descriptorClass = Class.forName("io.github.egonw.nanojava.descriptor." + desc);

				Object descriptorO = descriptorClass.newInstance();

				if (!(descriptorO instanceof ISubstanceDescriptor)) {
					throw new Exception("The passed descriptor class must be a INanomaterialDescriptor");
				}

				map.put(desc, (ISubstanceDescriptor) descriptorO);

			}

			return map;

		} catch (ClassNotFoundException e) {
			System.out.println(
					"\nYou should provide a valid descriptor class from package io.github.egonw.nanojava.descriptor \n");
			printUsageAndExit();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private static void printUsageAndExit() {
		printUsageAndExit(null);
	}

	private static void printUsageAndExit(Throwable e) {
		String[] args = { "-h" };
		CommandLine.usage(new CliOptions(args), System.out);
		if (e == null)
			System.exit(0);
		e.printStackTrace();
		System.exit(-1);
	}
}
