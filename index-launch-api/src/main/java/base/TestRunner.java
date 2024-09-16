package base;

import com.beust.jcommander.internal.Lists;
import org.testng.TestNG;
import utilities.PropertiesReader;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;

public class TestRunner {
	
	public static String absolutePath;

	public static void main(String[] args) throws URISyntaxException, IOException {
		String groups = null;
		getpath();
		TestNG testng = new TestNG();
		List<String> suites = Lists.newArrayList();
		suites.add(absolutePath);
		testng.setTestSuites(suites);
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
		testng.setExcludedGroups(groups);
		testng.run();
	}
	
	public static void getpath() throws URISyntaxException {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		URL res = loader.getResource("testng.xml");
		File file = Paths.get(res.toURI()).toFile();
		absolutePath = file.getAbsolutePath();
	}
	
}
