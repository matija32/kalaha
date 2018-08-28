package nl.fungames.kalaha.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

public class BoardTests {

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

        Pit kalahaPit1 = board.getKalahaPitFor(Player.ONE);
        assertEquals(Player.ONE, kalahaPit1.getOwner());
        assertTrue(kalahaPit1.isKalaha());

        Pit kalahaPit2 = board.getKalahaPitFor(Player.TWO);
        assertEquals(Player.TWO, kalahaPit2.getOwner());
        assertTrue(kalahaPit2.isKalaha());

        assertEquals(4, board.getNormalPitsFor(Player.ONE).size());
        assertEquals(4, board.getNormalPitsFor(Player.TWO).size());

        assertTrue(board.getNormalPitsFor(Player.ONE).stream().allMatch(pit -> pit.isOwnedBy(Player.ONE) && !pit.isKalaha()));
        assertTrue(board.getNormalPitsFor(Player.TWO).stream().allMatch(pit -> pit.isOwnedBy(Player.TWO) && !pit.isKalaha()));
    }

    @Test
    public void testSowing_FillsPlayer1Kalaha() {
        Board board = new Board(6, 6);

        Pit lastFilledPit = board.sow(Player.ONE, 5);

        verifyBoardState(board,
                0, Arrays.asList(6, 7, 7, 7, 7, 7),
                Arrays.asList(6, 6, 6, 6, 6, 0), 1);

        assertEquals(board.getNormalPitsFor(Player.TWO).get(4), lastFilledPit);

    }

    @Test
    public void testSowing_FillsPlayer2Kalaha() {
        Board board = new Board(6, 6);

        Pit lastFilledPit = board.sow(Player.TWO, 5);

        verifyBoardState(board,
                1, Arrays.asList(0, 6, 6, 6, 6, 6),
                Arrays.asList(7, 7, 7, 7, 7, 6), 0);

        assertEquals(board.getNormalPitsFor(Player.ONE).get(4), lastFilledPit);
    }

    @Test
    public void testSowing_SkipsPlayer2Kalaha() {
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
    public void testSowing_SkipsPlayer1Kalaha() {
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

    @Test(expected = IllegalArgumentException.class)
    public void testSowing_NotPossibleFromEmptyPit() {
        Board board = new Board( 3, 0);

        board.getNormalPitsFor(Player.TWO).get(0).takeAll();

        board.sow(Player.TWO, 0);
    }

    @Test
    public void testMovingStonesToOwnKalaha() {
        Board board = new Board( 3, 3);

        verifyBoardState(board,
                0, Arrays.asList(3, 3, 3),
                Arrays.asList(3, 3, 3), 0);

        board.moveStonesToOwnKalaha(board.getNormalPitsFor(Player.ONE).get(0));
        board.moveStonesToOwnKalaha(board.getNormalPitsFor(Player.ONE).get(2));
        board.moveStonesToOwnKalaha(board.getNormalPitsFor(Player.TWO).get(2));

        verifyBoardState(board,
                3, Arrays.asList(0, 3, 3),
                Arrays.asList(0, 3, 0), 6);
    }

    @Test
    public void testMovingStonesToOpponentsKalaha() {
        Board board = new Board( 3, 3);

        verifyBoardState(board,
                0, Arrays.asList(3, 3, 3),
                Arrays.asList(3, 3, 3), 0);

        board.moveStonesToOpponentsKalaha(board.getNormalPitsFor(Player.ONE).get(1));
        board.moveStonesToOpponentsKalaha(board.getNormalPitsFor(Player.TWO).get(0));
        board.moveStonesToOpponentsKalaha(board.getNormalPitsFor(Player.TWO).get(1));


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

        Pit oppositePit4 = board.getPitOppositeOf(board.getKalahaPitFor(Player.ONE));
        assertEquals(board.getKalahaPitFor(Player.TWO), oppositePit4);

    }

    @Test
    public void testRevertingToStartingState(){
        Board board = new Board(6, 6);

        board.sow(Player.ONE, 0);
        board.sow(Player.TWO, 0);

        board.revertToStartingState();

        verifyBoardState(board,
                0, Arrays.asList(6, 6, 6, 6, 6, 6),
                Arrays.asList(6, 6, 6, 6, 6, 6), 0);
    }

    private void verifyBoardState(
            Board board,
            int expectedKalahaPitPlayer2, List<Integer> expectedNormalPitsPlayer2Reversed,
            List<Integer> expectedNormalPitsPlayer1, int expectedKalahaPitPlayer1) {

        assertEquals(expectedKalahaPitPlayer1, board.getKalahaPitFor(Player.ONE).countStones());
        assertEquals(expectedKalahaPitPlayer2, board.getKalahaPitFor(Player.TWO).countStones());

        assertEquals(expectedNormalPitsPlayer1, board.getNormalPitsFor(Player.ONE).stream().map(Pit::countStones).collect(Collectors.toList()));

        List<Integer> actualNormalPitsPlayer2 = board.getNormalPitsFor(Player.TWO).stream().map(Pit::countStones).collect(Collectors.toList());
        Collections.reverse(actualNormalPitsPlayer2);

        assertEquals(expectedNormalPitsPlayer2Reversed, actualNormalPitsPlayer2);
    }

}