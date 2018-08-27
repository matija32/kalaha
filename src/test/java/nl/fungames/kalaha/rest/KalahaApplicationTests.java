package nl.fungames.kalaha.rest;

import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import io.restassured.RestAssured;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class KalahaApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8000;//Integer.parseInt(environment.getRequiredProperty("server.port"));
    }

    @Test
    public void exampleTest() throws Exception {
        this.mvc.perform(get("/api/status")).andExpect(status().isOk())
                .andExpect(content().string("Hello World"));
    }

	@Test
	public void testContextLoading() {

	}

	@Test
    public void testGettingStatus() {
        when().request("GET", "/api/status").then().statusCode(200).assertThat().body("aaa", equalTo(13));
    }

	@Test
    public void testPlayingMoves() {

    }

    @Test
    public void testFinishingGame() {

    }

    @Test
    public void testRestarting(){

    }


}
