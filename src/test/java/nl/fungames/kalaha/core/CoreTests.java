package nl.fungames.kalaha.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CoreTests {

    @Test
    public void testQuickWin(){
        Game game = new Game(new Board(2, 2));

        game.play(Player.ONE, 0);
        game.play(Player.ONE, 1);

        assertEquals(Messages.ENDGAME_PLAYER_2_WON, game.getStatus().getMessage());
    }

    @Test
    public void testLongGame(){
        Game game = new Game(new Board(2, 2));

        game.play(Player.ONE, 1);
        game.play(Player.TWO, 0);
        game.play(Player.ONE, 0);
        game.play(Player.TWO, 0);
        game.play(Player.ONE, 1);

        assertEquals(Messages.ENDGAME_PLAYER_2_WON, game.getStatus().getMessage());
    }
}
