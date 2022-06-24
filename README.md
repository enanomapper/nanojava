[![build](https://github.com/egonw/nanojava/actions/workflows/maven.yml/badge.svg)](https://github.com/egonw/nanojava/actions/workflows/maven.yml)
[![codecov](https://codecov.io/gh/egonw/nanojava/branch/master/graph/badge.svg?token=7AGTB9R55I)](https://codecov.io/gh/egonw/nanojava)

# nanojava

Java library for descriptor calculation for (nano)materials.

The library can be used either as a JAR file or through Docker. It can also either print the calculated descriptors to the terminal or write them to a file using TSV format (tab-separated file).

It is also available on DockerHub (https://hub.docker.com/r/aammar/nano-java) for direct usage without compilation or Docker build!!

### Build with Docker

1. Make sure that Docker is installed and ready to go (https://docs.docker.com/)

2. Clone the current repository

3. Enter the repository with "cd" command

   ```bash
   cd nanojava
   ```

3. Run the following command:

   ```bash
   docker build -t nanojava . 
   ```

4. Now, you can use the library through Docker as in the Usage section (use "docker run nanojava" instead of "docker run aammar/nano-java" in this case).

### Build with maven

If you do not have Docker or if it is an overhead for your use case, you can bundle nanojava library as a standalone jar file.

1. Make sure that JDK 8 and Maven 3.x are installed and ready to go

2. Clone the current repository

3. Enter the repository with "cd" command

   ```bash
   cd nanojava
   ```

3. Run the following command:

   ```bash
   mvn package
   ```

4. Now, you will have the standalone jar file in the "target" folder named: 

   nanojava-1.2.0-SNAPSHOT-jar-with-dependencies.jar

5. The JAR file is used in the same way as the Docker image in the "Usage" section with replacing:

   "docker run" with "java -jar nanojava-1.2.0-SNAPSHOT-jar-with-dependencies.jar"

   and make sure you provide the absolute paths for your files in this case.

### Usage

**NOTE:** descriptor names are the class names of the descriptors available under the package io.github.egonw.nanojava.descriptor:

- ParticleSizeDescriptor
- MetalElementMassDescriptor
- ZetaPotentialDescriptor
- MetalAtomCountDescriptor
- MetalGroupDescriptor
- EnergyBandDescriptor
- HeatOfFormationMopac2012Descriptor
- MolecularWeightDescriptor
- MetalPeriodDescriptor
- HeatOfFormationDescriptor
- OxygenAtomCountDescriptor



**Example 1**: Calculate a single descriptor for a nano material formula provided through command line, and output the results to the terminal.

```bash
docker run aammar/nano-java -d OxygenAtomCountDescriptor -m Al2O3
```

Output:

| Nano Material | OxygenAtomCountDescriptor |
| ------------- | ------------------------- |
| Al2O3         | 3                         |

------

**Example 2:** Calculate all available descriptors in nanojava library for a nano material formula provided through command line, and output the results to the terminal.

```bash
docker run aammar/nano-java -d all -m Al2O3
```

------

**Example 3:**  Calculate a single descriptor for all nano materials provided as an XML file (you can find an example of that in this repository "materials.xml") , and output the results to the terminal. Pay attention to the volume mapping between the folder on your PC where the XML file resides and the a path of your choice inside the Docker container!!

```bash
docker run -v XML_PATH_ON_YOUR_PC:/data aammar/nano-java -d OxygenAtomCountDescriptor -f /data/materials.xml
```

Output:

| Nano Material | OxygenAtomCountDescriptor |
| ------------- | ------------------------- |
| Al2O3         | 3                         |
| CeO2          | 2                         |
| CoO           | 1                         |
| Co3O4         | 4                         |
| Cr2O3         | 3                         |
| CuO           | 1                         |
| Fe2O3         | 3                         |
| Fe3O4         | 4                         |
| Gd2O3         | 3                         |
| HfO2          | 2                         |
| In2O3         | 3                         |

------

**Example 4**:  Calculate all available descriptors in nanojava library for all nano materials provided as an XML file (you can find an example of that in this repository "materials.xml") , and output the results to the terminal. Pay attention to the volume mapping between the folder on your PC where the XML file resides and the a path of your choice inside the Docker container!!

```bash
docker run -v XML_PATH_ON_YOUR_PC:/data aammar/nano-java -d all -f /data/materials.xml
```

------

**Example 5**: If you want to write the descriptors calculation results as a TSV file, add the argument "-o OUTPUT_FILE_ABSOLUTE_PATH". For the following examples, the output file path will be used "/data/descriptors.tsv"

That results the following commands for the previous examples:

```bash
docker run aammar/nano-java -d OxygenAtomCountDescriptor -m Al2O3 -o /data/descriptors.tsv

docker run aammar/nano-java -d all -m Al2O3 -o /data/descriptors.tsv

docker run -v XML_PATH_ON_YOUR_PC:/data aammar/nano-java -d OxygenAtomCountDescriptor -f /data/materials.xml -o /data/descriptors.tsv

docker run -v XML_PATH_ON_YOUR_PC:/data aammar/nano-java -d all -f /data/materials.xml -o /data/descriptors.tsv
```

