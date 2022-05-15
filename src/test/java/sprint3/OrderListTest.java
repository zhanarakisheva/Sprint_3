package sprint3;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import sprint3.dto.NewOrderRequest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class OrderListTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void getOrder(){
        given()
                .get("/api/v1/orders")
                .then()
                .assertThat()
                .statusCode(200)
                .body("orders", notNullValue());
    }
}
