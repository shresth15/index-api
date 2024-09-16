package utilities;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {

	public static Properties prop;
	public static InputStream inputStream;
	
	public static String getproperty(String key) throws IOException {
		Object value = null;
		try {
			prop = new Properties();
			// use the below 3 lines for local execution
			String propFileName = "config.properties";
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			inputStream = loader.getResourceAsStream("config.properties");
			// use the below 2 lines for jar
//			String propFileName = System.getProperty("user.dir") + "/config.properties";
//			inputStream = new FileInputStream(propFileName);
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
			value = prop.get(key);
//			System.out.println(prop.get(key));
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			inputStream.close();
		}
		return value.toString();
	}
}
