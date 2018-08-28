package nl.fungames.kalaha.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class KalahaApplicationTests {

    private MockMvc mvc;

    @Before
    public void setUp(){
        mvc = MockMvcBuilders.standaloneSetup(new GameController()).build();
    }

	@Test
    public void testGettingInitialStatus() throws Exception {
        verifyGameStatusIsInitial();
    }

    @Test
    public void testPlayingMoves() throws Exception {

        this.mvc.perform(post("/api/play")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"player\" : \"ONE\", \"pitId\" : 2}"))
                .andExpect(status().isAccepted());

        this.mvc.perform(get("/api/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusPerPlayer.ONE.stonesInKalahaPit").value(1))
                .andExpect(jsonPath("$.statusPerPlayer.ONE.stonesInNormalPits.*", equalTo(Arrays.asList(6, 6, 0, 7, 7, 7))))
                .andExpect(jsonPath("$.statusPerPlayer.ONE.allowedToSeedFromNormalPit.*", equalTo(Arrays.asList(false, false, false, false, false, false))))
                .andExpect(jsonPath("$.statusPerPlayer.TWO.stonesInKalahaPit").value(0))
                .andExpect(jsonPath("$.statusPerPlayer.TWO.stonesInNormalPits.*", equalTo(Arrays.asList(7, 7, 6, 6, 6, 6))))
                .andExpect(jsonPath("$.statusPerPlayer.TWO.allowedToSeedFromNormalPit.*", equalTo(Arrays.asList(true, true, true, true, true, true))));

        this.mvc.perform(post("/api/play")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"player\" : \"TWO\", \"pitId\" : 0}"))
                .andExpect(status().isAccepted());

        this.mvc.perform(get("/api/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusPerPlayer.ONE.stonesInKalahaPit").value(1))
                .andExpect(jsonPath("$.statusPerPlayer.ONE.stonesInNormalPits.*", equalTo(Arrays.asList(7, 6, 0, 7, 7, 7))))
                .andExpect(jsonPath("$.statusPerPlayer.ONE.allowedToSeedFromNormalPit.*", equalTo(Arrays.asList(true, true, false, true, true, true))))
                .andExpect(jsonPath("$.statusPerPlayer.TWO.stonesInKalahaPit").value(1))
                .andExpect(jsonPath("$.statusPerPlayer.TWO.stonesInNormalPits.*", equalTo(Arrays.asList(0, 8, 7, 7, 7, 7))))
                .andExpect(jsonPath("$.statusPerPlayer.TWO.allowedToSeedFromNormalPit.*", equalTo(Arrays.asList(false, false, false, false, false, false))));


    }

    private void verifyGameStatusIsInitial() throws Exception {
        this.mvc.perform(get("/api/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("It's ON!"))
                .andExpect(jsonPath("$.statusPerPlayer.ONE.stonesInKalahaPit").value(0))
                .andExpect(jsonPath("$.statusPerPlayer.ONE.stonesInNormalPits.*", equalTo(Arrays.asList(6, 6, 6, 6, 6, 6))))
                .andExpect(jsonPath("$.statusPerPlayer.ONE.allowedToSeedFromNormalPit.*", equalTo(Arrays.asList(true, true, true, true, true, true))))
                .andExpect(jsonPath("$.statusPerPlayer.TWO.stonesInKalahaPit").value(0))
                .andExpect(jsonPath("$.statusPerPlayer.TWO.stonesInNormalPits.*", equalTo(Arrays.asList(6, 6, 6, 6, 6, 6))))
                .andExpect(jsonPath("$.statusPerPlayer.TWO.allowedToSeedFromNormalPit.*", equalTo(Arrays.asList(false, false, false, false, false, false))));
    }

    @Test
    public void testRestarting() throws Exception {

        this.mvc.perform(post("/api/play")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"player\" : \"ONE\", \"pitId\" : 2}"))
                .andExpect(status().isAccepted());

        this.mvc.perform(post("/api/restart")).andExpect(status().isOk());

        verifyGameStatusIsInitial();
    }


}
