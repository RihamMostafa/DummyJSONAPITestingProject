package org.example.api.services;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.example.api.models.AuthLoginRequest;
import org.json.JSONObject;

import static io.restassured.RestAssured.given;

public class Auth {

    static RequestSpecification loginReqSpecs = given().baseUri("https://dummyjson.com").basePath("auth");
    public static Response login(AuthLoginRequest userCreds)
    {
        return given().spec(loginReqSpecs).body(userCreds)
                .contentType(ContentType.JSON)
                .when().post("login");
    }

    public static Response getLoggedInUserDetails(String token)
    {
        return  given().spec(loginReqSpecs).auth().oauth2(token).when().get("me");

    }

    public static Response refresh(String refreshToken)
    {
        return given().spec(loginReqSpecs).body(new JSONObject().put("refreshToken",refreshToken).toString())
                .contentType(ContentType.JSON).log().all()
                .when().post("refresh");
    }

}
