package sprint3;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import sprint3.dto.Courier;
import sprint3.dto.NewOrderRequest;
import sprint3.dto.NewOrderResponse;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


@RunWith(Parameterized.class)
public class OrderCreateTest {

    private String[] color;

    public OrderCreateTest(String[] color) {
        this.color = color;
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Parameterized.Parameters
    public static Object[] getParameters() {
        return new Object[][]{
                { new String[]{"BLACK"} },
                { new String[]{"BLACK", "GREY"} },
                { new String[]{"GREY","BLACK"} },
                { new String[]{} }
        };
    }

    @Test
    public void orderCreate() {
        NewOrderResponse order = given()
                .header("Content-type", "application/json")
                .and()
                .body(new NewOrderRequest("Naruto", "Uchiha", "Konoha, 142 apt.", "4", "+7 800 355 35 35", "2020-06-06", "Saske, come back to Konoha", 5, color))
                .when()
                .post("/api/v1/orders")
                .then()
                .assertThat()
                .statusCode(201)
                .body("track", notNullValue())
                .and()
                .extract()
                .as(NewOrderResponse.class);

        cancelOrder(order);
    }

    public void cancelOrder (NewOrderResponse order) {
        RestAssured.with()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .put("/api/v1/orders/cancel");
    }
}
