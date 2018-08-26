package nl.fungames.kalaha.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import nl.fungames.kalaha.core.Player;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KalahaApplicationTests {

    @Autowired
    private GameController gameController;

	@Test
	public void testContextLoading() {

	}

	@Test
    public void testPlayingMoves() {
	    gameController.status();

        gameController.play(new MoveDto(Player.ONE, 0));
    }

    @Test
    public void testFinishingGame() {

    }

    @Test
    public void testRestarting(){

    }


}
