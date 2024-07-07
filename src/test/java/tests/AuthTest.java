package tests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import org.example.api.models.AuthLoginRequest;
import org.example.api.models.AuthLoginResponse;
import org.example.api.services.Auth;
import org.example.api.utils.JSONReader;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Objects;

import static io.restassured.RestAssured.expect;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertTrue;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;
import static org.unitils.reflectionassert.ReflectionComparatorMode.IGNORE_DEFAULTS;

public class AuthTest {

    private Response loginResponse;
    ResponseSpecification loginResSpecs = expect().statusCode(200).contentType(ContentType.JSON);
    AuthLoginResponse loginObj;

    @BeforeMethod
    public void login() {
        loginResponse = Auth.login(new AuthLoginRequest("emilys","emilyspass"));
    }

    @Test(priority = 1)
    public void loginWithValidUserTest() throws IOException {

        loginObj= loginResponse.then().spec(loginResSpecs).body("$.token", not(empty()))
                .extract().as(AuthLoginResponse.class);
        AuthLoginResponse expectedResponse = JSONReader.readJsonFromFile("src/main/resources/expectedResponses/expectedAuthLoginResponse_ValidUser.json", AuthLoginResponse.class);

        assertReflectionEquals(expectedResponse, loginObj,IGNORE_DEFAULTS);

    }

    @Test(priority = 2)
    public void getAuthenticatedUserTest_ValidToken() {

        Auth.getLoggedInUserDetails(loginObj.getToken()).then().spec(loginResSpecs).body("id", equalTo(loginObj.getId()));

    }

    @Test(priority = 3)
    public void refreshTokenTest_ValidToken() {

        Auth.refresh(loginObj.refreshToken).then().log().all().spec(loginResSpecs).body("$.token", not(empty()));
    }

}
