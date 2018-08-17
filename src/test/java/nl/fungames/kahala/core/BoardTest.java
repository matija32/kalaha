package nl.fungames.kahala.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

public class BoardTest {

    @Test
    public void testDefaultInitialBoardSetup() {

        Board board = new Board(6, 6);

        verifyBoardState(board,
                0, Arrays.asList(6, 6, 6, 6, 6, 6),
                Arrays.asList(6, 6, 6, 6, 6, 6), 0);
    }

    @Test
    public void testInitialPits() {

        Board board = new Board( 4, 5);

        Pit kahalaPit1 = board.getKahalaPitFor(Player.ONE);
        assertEquals(Player.ONE, kahalaPit1.getOwner());
        assertTrue(kahalaPit1.isKahala());

        Pit kahalaPit2 = board.getKahalaPitFor(Player.TWO);
        assertEquals(Player.TWO, kahalaPit2.getOwner());
        assertTrue(kahalaPit2.isKahala());

        assertEquals(4, board.getNormalPitsFor(Player.ONE).size());
        assertEquals(4, board.getNormalPitsFor(Player.TWO).size());

        assertTrue(board.getNormalPitsFor(Player.ONE).stream().allMatch(pit -> pit.isOwnedBy(Player.ONE) && !pit.isKahala()));
        assertTrue(board.getNormalPitsFor(Player.TWO).stream().allMatch(pit -> pit.isOwnedBy(Player.TWO) && !pit.isKahala()));
    }

    @Test
    public void testSowing_FillsPlayer1Kahala() {
        Board board = new Board(6, 6);

        Pit lastFilledPit = board.sow(Player.ONE, 5);

        verifyBoardState(board,
                0, Arrays.asList(6, 7, 7, 7, 7, 7),
                Arrays.asList(6, 6, 6, 6, 6, 0), 1);

        assertEquals(board.getNormalPitsFor(Player.TWO).get(4), lastFilledPit);

    }

    @Test
    public void testSowing_FillsPlayer2Kahala() {
        Board board = new Board(6, 6);

        Pit lastFilledPit = board.sow(Player.TWO, 5);

        verifyBoardState(board,
                1, Arrays.asList(0, 6, 6, 6, 6, 6),
                Arrays.asList(7, 7, 7, 7, 7, 6), 0);

        assertEquals(board.getNormalPitsFor(Player.ONE).get(4), lastFilledPit);
    }

    @Test
    public void testSowing_SkipsPlayer2Kahala() {
        ArrayList<Pit> pits = new ArrayList<>();
        Board board = new Board( 2, 0);

        board.getNormalPitsFor(Player.ONE).get(0).add(6);

        verifyBoardState(board,
                0, Arrays.asList(0, 0),
                Arrays.asList(6, 0), 0);

        Pit lastFilledPit = board.sow(Player.ONE, 0);

        verifyBoardState(board,
                0, Arrays.asList(1, 1),
                Arrays.asList(1, 2), 1);

        assertEquals(board.getNormalPitsFor(Player.ONE).get(1), lastFilledPit);
    }

    @Test
    public void testSowing_SkipsPlayer1Kahala() {
        ArrayList<Pit> pits = new ArrayList<>();
        Board board = new Board( 2, 0);

        board.getNormalPitsFor(Player.TWO).get(0).add(5);

        verifyBoardState(board,
                0, Arrays.asList(0, 5),
                Arrays.asList(0, 0), 0);

        Pit lastFilledPit = board.sow(Player.TWO, 0);

        verifyBoardState(board,
                1, Arrays.asList(1, 1),
                Arrays.asList(1, 1), 0);

        assertEquals(board.getNormalPitsFor(Player.TWO).get(0), lastFilledPit);
    }

    @Test
    public void testSowing_OnlyPlayers1Pits() {
        ArrayList<Pit> pits = new ArrayList<>();
        Board board = new Board( 3, 0);

        board.getNormalPitsFor(Player.ONE).get(0).add(2);

        verifyBoardState(board,
                0, Arrays.asList(0, 0, 0),
                Arrays.asList(2, 0, 0), 0);

        Pit lastFilledPit = board.sow(Player.ONE, 0);

        verifyBoardState(board,
                0, Arrays.asList(0, 0, 0),
                Arrays.asList(0, 1, 1), 0);

        assertEquals(board.getNormalPitsFor(Player.ONE).get(2), lastFilledPit);
    }

    @Test
    public void testSowing_OnlyPlayers2Pits() {
        Board board = new Board( 3, 0);

        board.getNormalPitsFor(Player.TWO).get(0).add(2);

        verifyBoardState(board,
                0, Arrays.asList(0, 0, 2),
                Arrays.asList(0, 0, 0), 0);

        Pit lastFilledPit = board.sow(Player.TWO, 0);

        verifyBoardState(board,
                0, Arrays.asList(1, 1, 0),
                Arrays.asList(0, 0, 0), 0);

        assertEquals(board.getNormalPitsFor(Player.TWO).get(2), lastFilledPit);

    }

    @Test
    public void testMovingStonesToOwnKahala() {
        Board board = new Board( 3, 3);

        verifyBoardState(board,
                0, Arrays.asList(3, 3, 3),
                Arrays.asList(3, 3, 3), 0);

        board.moveStonesToOwnKahala(board.getNormalPitsFor(Player.ONE).get(0));
        board.moveStonesToOwnKahala(board.getNormalPitsFor(Player.ONE).get(2));
        board.moveStonesToOwnKahala(board.getNormalPitsFor(Player.TWO).get(2));

        verifyBoardState(board,
                3, Arrays.asList(0, 3, 3),
                Arrays.asList(0, 3, 0), 6);
    }

    @Test
    public void testMovingStonesToOpponentsKahala() {
        Board board = new Board( 3, 3);

        verifyBoardState(board,
                0, Arrays.asList(3, 3, 3),
                Arrays.asList(3, 3, 3), 0);

        board.moveStonesToOpponentsKahala(board.getNormalPitsFor(Player.ONE).get(1));
        board.moveStonesToOpponentsKahala(board.getNormalPitsFor(Player.TWO).get(0));
        board.moveStonesToOpponentsKahala(board.getNormalPitsFor(Player.TWO).get(1));


        verifyBoardState(board,
                3, Arrays.asList(3, 0, 0),
                Arrays.asList(3, 0, 3), 6);
    }

    @Test
    public void testGettingOppositePit() {
        Board board = new Board(6, 6);

        Pit oppositePit1 = board.getPitOppositeOf(board.getNormalPitsFor(Player.ONE).get(0));
        assertEquals(board.getNormalPitsFor(Player.TWO).get(5), oppositePit1);

        Pit oppositePit2 = board.getPitOppositeOf(board.getNormalPitsFor(Player.TWO).get(3));
        assertEquals(board.getNormalPitsFor(Player.ONE).get(2), oppositePit2);

        Pit oppositePit3 = board.getPitOppositeOf(board.getNormalPitsFor(Player.ONE).get(1));
        assertEquals(board.getNormalPitsFor(Player.TWO).get(4), oppositePit3);

        Pit oppositePit4 = board.getPitOppositeOf(board.getKahalaPitFor(Player.ONE));
        assertEquals(board.getKahalaPitFor(Player.TWO), oppositePit4);

    }


    private void verifyBoardState(
            Board board,
            int expectedKahalaPitPlayer2, List<Integer> expectedNormalPitsPlayer2Reversed,
            List<Integer> expectedNormalPitsPlayer1, int expectedKahalaPitPlayer1) {

        assertEquals(expectedKahalaPitPlayer1, board.getKahalaPitFor(Player.ONE).countStones());
        assertEquals(expectedKahalaPitPlayer2, board.getKahalaPitFor(Player.TWO).countStones());

        assertEquals(expectedNormalPitsPlayer1, board.getNormalPitsFor(Player.ONE).stream().map(Pit::countStones).collect(Collectors.toList()));

        List<Integer> actualNormalPitsPlayer2 = board.getNormalPitsFor(Player.TWO).stream().map(Pit::countStones).collect(Collectors.toList());
        Collections.reverse(actualNormalPitsPlayer2);

        assertEquals(expectedNormalPitsPlayer2Reversed, actualNormalPitsPlayer2);
    }

}