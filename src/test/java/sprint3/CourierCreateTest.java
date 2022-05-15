package sprint3;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import sprint3.dto.Courier;
import sprint3.dto.CourierLoginRequest;
import sprint3.dto.CourierLoginResponse;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class CourierCreateTest {


    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void createCourier() {
        Courier courier = new Courier("disgusting-pickle7319", "morty", "Rick");
        try {
            given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(courier)
                    .when()
                    .post("/api/v1/courier")
                    .then()
                    .assertThat()
                    .statusCode(201)
                    .body("ok", equalTo(true));
        } finally {
            deleteCourier(getCourierId(courier));
        }

    }

    @Test
    public void duplicateCourier(){
        Courier courier1 = new Courier("disgusting-pickle7319", "morty1", "Pickle-Rick1");
        Courier courier2 = new Courier("disgusting-pickle7319", "morty2", "Pickle-Rick2");
        createCourier(courier1);
        try {
            given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(courier2)
                    .when()
                    .post("/api/v1/courier")
                    .then()
                    .assertThat()
                    .statusCode(409)
                    .body("message", equalTo("Этот логин уже используется"));
        } finally {
            deleteCourier(getCourierId(courier1));
        }
    }

    @Test
    public void incompleteRequest(){
        Courier courier = new Courier();
        courier.setLogin("disgusting-pickle7319");
        courier.setFirstName("Rick");
            given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(courier)
                    .when()
                    .post("/api/v1/courier")
                    .then()
                    .assertThat()
                    .statusCode(400)
                    .body("message", equalTo("Недостаточно данных для создания учетной записи"));

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
