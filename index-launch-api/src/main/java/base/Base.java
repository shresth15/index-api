package base;

import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import utilities.PropertiesReader;

import java.io.IOException;

import static io.restassured.RestAssured.given;

/**
 * Base class to initialize any request
 *
 */
public class Base {
	public static String baseuri, token;
	public static Response response;

	@BeforeSuite(alwaysRun = true)
	public void beforesuite() throws IOException {
		baseuri = PropertiesReader.getproperty("baseurl");
	}

	@BeforeMethod(alwaysRun = true)
	public static void generatetoken() throws IOException {
		token = given().baseUri(baseuri)
				.header("Content-Type", "application/json").when()
				.body("{\n" +
						"    \"username\" : \"admin\",\n" +
						"    \"password\" : \"password123\"\n" +
						"}")
				.post("/auth").asString();
	}

//	@AfterSuite(alwaysRun = true)
	public void aftersuite() throws InterruptedException {
		Thread.sleep(5000);
	}

}
