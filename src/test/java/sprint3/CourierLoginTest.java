package sprint3;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import sprint3.dto.Courier;
import sprint3.dto.CourierLoginRequest;
import sprint3.dto.CourierLoginResponse;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CourierLoginTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void courierLogin(){
        Courier courier = new Courier("disgusting-pickle7319", "morty", "Rick");
        createCourier(courier);
        try {
            given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(new CourierLoginRequest(courier.getLogin(), courier.getPassword()))
                    .when()
                    .post("/api/v1/courier/login")
                    .then()
                    .assertThat()
                    .statusCode(200)
                    .body("id", notNullValue());
        } finally {
            deleteCourier(getCourierId(courier));
        }
    }

    @Test
    public void invalidPassword(){
        Courier courier = new Courier("disgusting-pickle7319", "morty", "Rick");
        createCourier(courier);
        try {
            given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(new CourierLoginRequest(courier.getLogin(), "nujh"))
                    .when()
                    .post("/api/v1/courier/login")
                    .then()
                    .assertThat()
                    .statusCode(404)
                    .body("message", equalTo("Учетная запись не найдена"));
        } finally {
            deleteCourier(getCourierId(courier));
        }
    }

    @Test
    public void invalidLogin(){
        Courier courier = new Courier("disgusting-pickle7319", "morty", "Rick");
        createCourier(courier);
        try {
            given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(new CourierLoginRequest("kjhghj", courier.getPassword()))
                    .when()
                    .post("/api/v1/courier/login")
                    .then()
                    .assertThat()
                    .statusCode(404)
                    .body("message", equalTo("Учетная запись не найдена"));
        } finally {
            deleteCourier(getCourierId(courier));
        }
    }

    @Test
    public void incompleteRequest(){

            given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(new CourierLoginRequest(null, "123"))
                    .when()
                    .post("/api/v1/courier/login")
                    .then()
                    .assertThat()
                    .statusCode(400)
                    .body("message", equalTo("Недостаточно данных для входа"));
    }


    public void createCourier(Courier courier) {
        RestAssured.with()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .post("/api/v1/courier");
    }

    public void deleteCourier(String id) {
        RestAssured.delete("/api/v1/courier/{id}", id);
    }

    public String getCourierId(Courier courier) {
        CourierLoginResponse response = RestAssured.with()
                .header("Content-type", "application/json")
                .and()
                .body(new CourierLoginRequest(courier.getLogin(), courier.getPassword()))
                .post("/api/v1/courier/login")
                .as(CourierLoginResponse.class);
        return String.valueOf(response.getId());
    }
}
