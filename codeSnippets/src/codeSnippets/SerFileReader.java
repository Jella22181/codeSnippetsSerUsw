package codeSnippets;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Stream;

public class SerFileReader {

	public static void main(String[] args) {
		SerFileReader s = new SerFileReader();
		s.testReadConf();
	}

	public void testReadConf() {

		// C:\SER\PS Dev
		// Quickstart\eclipse-workspace\inheritDescriptor-dx4agent\conf\LBBW_TransferTest.properties

		String src = System.getProperty("user.dir");// C:\Program Files (x86)\SER\DOXiS4AgentServer
		String target = src;
		src += "\\srcLib\\src.txt";
		target += "\\srcLib\\target.txt";

		System.out.println("current dir = " + src);

		// Properties.getProperty(String key)

		Map<String, String> propertyMapSrc = new LinkedHashMap();

		FileReader reader = null;

		try {// nu.studer.java.util.OrderedProperties
			reader = new FileReader(src);
			Properties p = new Properties();
			Properties properties = new Properties();
			properties.load(new FileInputStream(src));

			// p.load(reader);

			Enumeration keys = properties.propertyNames();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				propertyMapSrc.put(key, properties.getProperty(key));
			}
			reader.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		catch (IOException e) {
			e.printStackTrace();
		}

		// TODO wenn preserved order: die mapTarget als StreamTokenizer -> linkedHashmap
		Map<String, String> propertyMapTarget = new LinkedHashMap();

		FileReader reader2 = null;

		FileInputStream fis;
//		try {
//			fis = new FileInputStream(target);
//			Reader r = new BufferedReader(new InputStreamReader(fis));
//			StreamTokenizer st = new StreamTokenizer(r);
			
			try (Stream<String> stream = Files.lines(Paths.get(target))) {
// TODO hier weiter
				// wenn festgelegte Reihenfolge gewünscht, streeam, in linkedhashmap alle reinsetzen 
				stream.forEach(e-> {propertyMapTarget.put("k", e);});//System.out::println);

			} catch (IOException e) {
				e.printStackTrace();
			}
			


		try {

			reader2 = new FileReader(target);

			Properties properties2 = new Properties();
			properties2.load(new FileInputStream(target));

			// p.load(reader2);

			Enumeration keys = properties2.propertyNames();

			while (keys.hasMoreElements()) {

				String key = (String) keys.nextElement();

				propertyMapTarget.put(key, properties2.getProperty(key));

			}

			reader.close();

		} catch (FileNotFoundException e1) {

			e1.printStackTrace();

		}

		catch (IOException e) {

			e.printStackTrace();

		}

		// src + target loaded
		Set setSrc = propertyMapSrc.keySet();
		Iterator it = setSrc.iterator();

		while (it.hasNext()) {
			String keySrc = (String) it.next();
			if (propertyMapTarget.containsKey(keySrc)) {

				String valueSrc = propertyMapSrc.get(keySrc);
				propertyMapTarget.put(keySrc, valueSrc);
				System.out.println(keySrc + " -> " + valueSrc);
			}
		}

	}
}
