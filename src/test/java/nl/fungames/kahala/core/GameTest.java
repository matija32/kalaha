package nl.fungames.kahala.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class GameTest {

    @Test
    public void testAtStartBothPlayersHaveSixPitsWithSixStones(){

        List<Integer> startingPits = Arrays.asList(6, 6, 6, 6, 6, 6);

        Game game = new Game();
        Map<Player, PlayerStatus> statusPerPlayer = game.getStatus().getStatusPerPlayer();

        assertEquals(startingPits, statusPerPlayer.get(Player.ONE).getStonesInNormalPits());
        assertEquals(startingPits, statusPerPlayer.get(Player.TWO).getStonesInNormalPits());
    }

    @Test
    public void testAtStartBothPlayersHaveEmptyKahalas(){
        int startingNumberOfStonesInKahala = 0;

        Game game = new Game();
        Map<Player, PlayerStatus> statusPerPlayer = game.getStatus().getStatusPerPlayer();

        assertEquals(startingNumberOfStonesInKahala, statusPerPlayer.get(Player.ONE).getStonesInKahalaPit());
        assertEquals(startingNumberOfStonesInKahala, statusPerPlayer.get(Player.TWO).getStonesInKahalaPit());
    }

}