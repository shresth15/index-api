package base;

import org.testng.TestNG;
import utilities.PropertiesReader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TestRunner2 {

	public static String absolutePath;

	public static void main(String[] args) throws IOException {
		String groups = null;
		// Retrieve the suite file from the jar and then write it out to file-system for using with testNG
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream is = loader.getResourceAsStream("testng.xml");
		File mysuite = new File("testng.xml");
		try {
			OutputStream os = new FileOutputStream(mysuite);

			byte buf[] = new byte[4096];
			int len;
			try {
				while ((len = is.read(buf)) > 0) {
					os.write(buf, 0, len);
					os.close();
					is.close();
					System.out.println(mysuite.toPath().toString());
				}
			} catch (IOException e) {
				e.printStackTrace(); // To change body of catch statement use File | Settings | File Templates.
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace(); // To change body of catch statement use File | Settings | File Templates.
		}

		List<String> xmlFileList = new ArrayList<String>();
		xmlFileList.add("testng.xml");
		TestNG testng = new TestNG();
		testng.setTestClasses(new Class[] { TestRunner2.class });
		testng.setTestSuites(xmlFileList);
		if (PropertiesReader.getproperty("banking").equals("false")) 
			groups = "banking";
		if (PropertiesReader.getproperty("consent").equals("false")) 
			groups = groups +" , "+ "consent";
		if (PropertiesReader.getproperty("register").equals("false")) 
			groups = groups +" , "+ "register";
		if (PropertiesReader.getproperty("admin").equals("false")) 
			groups = groups +" , "+ "admin";
		if (PropertiesReader.getproperty("security").equals("false")) 
			groups = groups +" , "+ "security";
		if (PropertiesReader.getproperty("manual").equals("false"))
			groups = groups +" , "+ "manual"+" , "+"consent-manual";
		testng.setExcludedGroups(groups);
		testng.run();
		deleteFiles();
	}
	
	public static void deleteFiles() {
		String filepath = System.getProperty("user.dir") + "/testng.xml";
		File file = new File(filepath);
		file.delete();
	}
}
