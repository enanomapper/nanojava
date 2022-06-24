package io.github.egonw.nanojava.io;

import java.util.HashMap;
import java.util.Map;

public class Namespaces {

	public static final Map<String,String> prefixes = new HashMap<String, String>() {{
		put("http://purl.org/obo/owl/PATO#", "pato");
		put("http://purl.org/obo/owl/UO#", "uo");
		put("http://purl.bioontology.org/ontology/npo#", "npo");
		put("http://qudt.org/vocab/unit#", "qudt");
		put("http://www.openphacts.org/units/", "ops");
	}};
	
}
