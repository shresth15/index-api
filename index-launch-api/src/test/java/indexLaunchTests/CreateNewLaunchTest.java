package indexLaunchTests;

import base.Base;
import io.restassured.http.Method;
import org.apache.hc.core5.reactor.Command;
import org.testng.annotations.Test;

import static common.Common.requestSpecGet;
import static common.Common.validateStatusCode;
import static io.restassured.RestAssured.given;

public class CreateNewLaunchTest extends Base {

    @Test(priority = 0)
    public void createLaunch(){
            System.out.println("Test Started");
            System.out.println("Token is: "+ token);
            response = given().baseUri(baseuri)
                    .header("Content-Type", "application/json").when()
                    .body("{\n" +
                            "    \"firstname\" : \"Jim\",\n" +
                            "    \"lastname\" : \"Brown\",\n" +
                            "    \"totalprice\" : 111,\n" +
                            "    \"depositpaid\" : true,\n" +
                            "    \"bookingdates\" : {\n" +
                            "        \"checkin\" : \"2018-01-01\",\n" +
                            "        \"checkout\" : \"2019-01-01\"\n" +
                            "    },\n" +
                            "    \"additionalneeds\" : \"Breakfast\"\n" +
                            "}")
//                    .post("/booking").then().log().all().extract().response();
// Log All will print the complete response including headers
                    .post("/booking").then().extract().response();
            System.out.println("Response:" + response.asPrettyString());
            validateStatusCode("200");
    }


    @Test(priority = 1)
    public void getLaunch(){
        response = given().spec(requestSpecGet()).baseUri(baseuri)
                .get("/booking/1").then().extract().response();
        System.out.println(response.asPrettyString());
        validateStatusCode("500");
    }

    //@Test(priority = 1)
    public void getLaunch1(){
        System.out.println("Test Started");
        System.out.println("Token is: "+ token);
        response = given().baseUri(baseuri)
                .header("Content-Type", "application/json")
// use when().param("","") for filters
                .get().then().log().all().extract().response();
    }
}
