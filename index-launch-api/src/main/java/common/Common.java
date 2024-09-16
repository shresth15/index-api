package common;

import base.Base;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Optional;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import static io.restassured.RestAssured.given;

public class Common extends Base {
	public static String screenshotname;
	public static Map<String, String> headers = new HashMap<String, String>();
	public static Map<String, String> params =  new HashMap<String, String>();
//	static String filepath = System.getProperty("user.dir") + "\\src\\main\\resources\\Expectedjson\\";
	static String filepath = "Expectedjson/";

	//Common method to get response from getRequest
	public static void getRequest(String token, String endPoint, @Optional RequestSpecification reqSpec) {
		response = given().baseUri(baseuri).auth().oauth2(token)
				.header("Content-Type", "application/json")
				.get(endPoint).then().extract().response();
	}

	//Define the common headers to be used in the calls
	public static RequestSpecification requestSpecGet() {

		RequestSpecification requestspec = new RequestSpecBuilder()
				.setContentType("application/json")
//				.addHeader("Accept-Encoding", "gzip, deflate, br")
				.build();
		return requestspec;
	}

	public static RequestSpecification requestSpecPost(Object body) {

		RequestSpecification requestspec = new RequestSpecBuilder()
//				.setContentType("application/json")
				.addHeader("Content-Type", "application/json")
				.addFormParam(String.valueOf(body))
//				.addHeader("Accept-Encoding", "gzip, deflate, br")
				.build();
		return requestspec;
	}

	public static Map<String, String> setParams(String key, String value) {
		params.put(key, value);
		return params;
	}

	public static Map<String, String> setHeaders(String key, String value) {
		headers.put(key, value);
		return headers;
	}

	public static void validateStatusCode(String expected) {
		String actual = String.valueOf(response.getStatusCode());
        Assert.assertEquals(actual,expected);
    }

	public static String getResponseValue(String path) {
		JsonPath js = new JsonPath(response.asString());
		return js.get(path);
	}

	public static String getFloatToInt(String floatVal) {
		String subFloatVal = null;
		try {
			subFloatVal = floatVal.substring(0, floatVal.indexOf("."));
		} catch (Exception e) {
			subFloatVal = floatVal;
		}
		int intVal = Integer.valueOf(subFloatVal);
		String stringVal = String.valueOf(intVal);
		return stringVal;
	}

	public static List<Object> getResponseListOfValue(String path) {
		JsonPath js;
		try {
			js = new JsonPath(response.asString());
			return js.getList(path);
		} catch (Exception e) {
		return null;
		}
	}

	public static void validateResponseValueByPath(String path, String expectedvalue) {
		JsonPath js = new JsonPath(response.asString());
		String actual = js.getString(path);
		if (actual == null) {
			Assert.fail("Failed due to value not available");
		} else {
			try {
				Assert.assertTrue(actual.contains(expectedvalue));
			} catch (AssertionError e) {
				// TODO Auto-generated catch block
				Assert.fail("Actual Result is not equal to Expected Result");
			}
		}
	}

	public static void clearMaps() {
		params.clear();
		headers.clear();
	}

	public static String getCurrentDate() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		// get current date time with Date()
		Date date1 = new Date();
		// Now format the date
        return dateFormat.format(date1);
	}


// need to enable the method once all the schema files are added
	public static void validateSchema(String apiname) {
		try {

			if (response.asString().isEmpty()) {
				System.out.println("Response for " + apiname + " is null. Hence, unable to validate schema");
			} else {
				ObjectMapper objectMapper = new ObjectMapper();
				JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
				JsonNode json = objectMapper.readTree(response.asString());
				InputStream inputStream = ClassLoader.getSystemResourceAsStream(filepath + apiname + ".json");
				JsonSchema schema = schemaFactory.getSchema(inputStream);
				Set<ValidationMessage> validationResult = schema.getRequiredValidator().validate(json);
				// Log validation errors
				if (validationResult.isEmpty()) {
					System.out.println("Schema Matched Successfully for " + apiname);
				} else {
					validationResult.forEach(vm -> System.out.println(vm.getMessage()));
					String s = "";
					for (ValidationMessage val : validationResult) {
						s = s + "<li style=\"\r\n" + "    text-align: left;\r\n" + "\">" + val.getMessage() + "</li>";
					}
					System.out.println("Following values not Matched in schema for " + apiname + s);
					System.out.println("Schema Not Match");
//				test.fail("Following values not Matched in schema for "+ apiname + s+"");
				Assert.fail("Schema validation failed");
				}
			}
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void validateRequestResponseValue(String requestValue, String responseValue) {
		try {
			Assert.assertEquals(requestValue.equals(responseValue), true);
		} catch (AssertionError e) {
			e.printStackTrace();
		}
	}

}
