package nl.fungames.kahala.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class GameTest {

    @Test
    public void testAtStartBothPlayersHaveSixPitsWithSixStones(){
        Game game = new Game();

        List<Integer> startingPits = Arrays.asList(6, 6, 6, 6, 6, 6);

        assertEquals(startingPits, game.getPitsFor(Player.ONE));
        assertEquals(startingPits, game.getPitsFor(Player.TWO));

    }

    @Test
    public void testAtStartBothPlayersHaveEmptyKahalas(){
        Game game = new Game();

        int startingNumberOfStonesInKahala = 0;

        assertEquals(startingNumberOfStonesInKahala, game.getKahalaFor(Player.ONE));
        assertEquals(startingNumberOfStonesInKahala, game.getKahalaFor(Player.TWO));
    }

}