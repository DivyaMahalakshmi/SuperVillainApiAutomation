import com.fasterxml.jackson.databind.util.JSONPObject;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.User;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import utils.TestUtils;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class SuperVillainApiTest {


    @Test
    public void testGetUsers() {

        given().
                when().
                get("https://supervillain.herokuapp.com/v1/user").
                then().
                assertThat().
                statusCode(200).
                and().
                contentType(ContentType.JSON);
    }

    @Test
    public void testCreateUsers() {
        String name = TestUtils.randomIdentifier();
        JSONObject user = new JSONObject();
        user.put("username", name);
        user.put("score", 0);

        given().
                when().
                header("Content-Type","application/json").
                body(user.toJSONString()).
                post("https://supervillain.herokuapp.com/v1/user").
                then().
                assertThat().
                statusCode(201);
    }


    @Test
    public void testCreateUserWithoutUserName() {
        String name = TestUtils.randomIdentifier();
        JSONObject user = new JSONObject();
        user.put("score", 0);

        given().
                when().
                header("Content-Type","application/json").
                body(user.toJSONString()).
                post("https://supervillain.herokuapp.com/v1/user").
                then().
                assertThat().
                statusCode(400);
    }


    @Test
    public void testCreateUsersWithoutUserObject() {
        given().
                when().
                post("https://supervillain.herokuapp.com/v1/user").
                then().
                assertThat().
                statusCode(415);
    }

    @Test
    public void testUpdateScore() {

        String name = TestUtils.randomIdentifier();
        JSONObject user = new JSONObject();
        user.put("username", name);
        user.put("score", 0);

        given().
                when().
                header("Content-Type","application/json").
                body(user.toJSONString()).
                post("https://supervillain.herokuapp.com/v1/user").
                then().
                assertThat().
                statusCode(201);

        user.put("score",100);
        JsonPath response  = given().
                when().
                header("Content-Type","application/json").
                body(user.toJSONString()).
                put("https://supervillain.herokuapp.com/v1/user").
                then().
                assertThat().
                statusCode(204);
    }


    @Test
    public void testUpdateWithoutUser() {
        given().
                when().
                put("https://supervillain.herokuapp.com/v1/user").
                then().
                assertThat().
                statusCode(201);
    }




    @Test
    public void testUpdateUserWithMaximumScoreAlready() {


        String name = TestUtils.randomIdentifier();
        JSONObject user = new JSONObject();
        user.put("username", name);
        user.put("score", 4000);

        given().
                when().
                header("Content-Type","application/json").
                body(user.toJSONString()).
                post("https://supervillain.herokuapp.com/v1/user").
                then().
                assertThat().
                statusCode(201);

        user.put("score", 5000);
        given().
                when().
                header("Content-Type","application/json").
                body(user.toJSONString()).
                put("https://supervillain.herokuapp.com/v1/user").
                then().
                assertThat().
                statusCode(204);
    }
}
