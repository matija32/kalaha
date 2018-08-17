package nl.fungames.kahala.core;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import org.junit.Test;

public class GameTest {

    @Test
    public void testInitialGameStatus(){
        Game game = new Game(() -> createFilledMockBoard(
                0, Arrays.asList(6, 6, 6, 6, 6, 6),
                Arrays.asList(6, 6, 6, 6, 6, 6), 0)
        );

        verifyGameStatus(game,
                0, Arrays.asList(6, 6, 6, 6, 6, 6),
                Arrays.asList(6, 6, 6, 6, 6, 6), 0);

    }

    private List<Pit> createMockPits(List<Integer> stonesInPits) {
        ArrayList<Pit> pits = new ArrayList<>();
        stonesInPits.forEach(stoneInPit -> pits.add(createMockPit(stoneInPit)));
        return pits;
    }

    private Pit createKahalaPit() {
        Pit pit = mock(Pit.class);
        when(pit.isKahala()).thenReturn(true);
        return pit;
    }

    private Pit createMockPit(int stones) {
        Pit pit = mock(Pit.class);
        when(pit.countStones()).thenReturn(stones);
        return pit;
    }

    @Test
    public void testGameplay_EachPlayerMakesAMoveEndingInFullOpponentsPit() {
        Board boardMock = mock(Board.class);
        Pit player1NormalPit = createMockPit(4);
        Pit player2NormalPit = createMockPit(4);
        when(boardMock.sow(Player.ONE, 3)).thenReturn(player2NormalPit);
        when(boardMock.sow(Player.TWO, 5)).thenReturn(player1NormalPit);

        Game game = new Game(() -> boardMock);

        game.play(Player.ONE, 3);

        verify(boardMock, times(1)).sow(Player.ONE, 3);
        assertEquals(Player.TWO, game.whoseTurnIsIt());

        game.play(Player.TWO, 5);

        verify(boardMock, times(1)).sow(Player.TWO, 5);
        assertEquals(Player.ONE, game.whoseTurnIsIt());
    }

    @Test
    public void testGameplay_MakingAMoveEndingInOwnKahalaPit() {
        Board boardMock = mock(Board.class);
        Pit player1Kahala = createKahalaPit();
        when(boardMock.sow(Player.ONE, 3)).thenReturn(player1Kahala);

        Game game = new Game(() -> boardMock);

        game.play(Player.ONE, 3);

        assertEquals(Player.ONE, game.whoseTurnIsIt());
    }


    @Test
    public void testGameplay_MakingAMoveEndingInOwnEmptyPit() {
        Board boardMock = mock(Board.class);
        Pit player1NormalPit = createMockPit(1);
        when(player1NormalPit.isOwnedBy(Player.ONE)).thenReturn(true);
        when(boardMock.sow(Player.ONE, 3)).thenReturn(player1NormalPit);

        Pit player2OppositePit = createMockPit(3);
        when(boardMock.getPitOppositeOf(player1NormalPit)).thenReturn(player2OppositePit);

        Game game = new Game(() -> boardMock);

        game.play(Player.ONE, 3);

        verify(boardMock, times(1)).moveStonesToOwnKahala(player1NormalPit);
        verify(boardMock, times(1)).moveStonesToOpponentsKahala(player2OppositePit);

        assertEquals(Player.TWO, game.whoseTurnIsIt());
    }

    @Test
    public void testGameplay_MakingAMoveEndingInOpponentsEmptyPit() {
        Board boardMock = mock(Board.class);
        Pit player2NormalPit = createMockPit(1);
        when(player2NormalPit.isOwnedBy(Player.ONE)).thenReturn(false);
        when(boardMock.sow(Player.ONE, 3)).thenReturn(player2NormalPit);

        Game game = new Game(() -> boardMock);

        game.play(Player.ONE, 3);

        verify(boardMock, never()).moveStonesToOwnKahala(any());
        verify(boardMock, never()).moveStonesToOpponentsKahala(any());

        assertEquals(Player.TWO, game.whoseTurnIsIt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGameplay_CannotPlayOnOtherPlayersTurn() {
        Supplier mockBoardSupplier = mock(Supplier.class);
        Game game = new Game(mockBoardSupplier);

        game.play(Player.TWO, 4);
    }

    @Test
    public void testResettingTheBoard() {
        Supplier mockBoardSupplier = mock(Supplier.class);
        Game game = new Game(mockBoardSupplier);

        game.restart();
        game.restart();
        game.restart();

        verify(mockBoardSupplier, times(4)).get();
    }


    Board createFilledMockBoard(
            int expectedKahalaPitPlayer2, List<Integer> expectedNormalPitsPlayer2Reversed,
            List<Integer> expectedNormalPitsPlayer1, int expectedKahalaPitPlayer1){

        Board boardMock = mock(Board.class);

        Pit kahalaPit1 = createMockPit(expectedKahalaPitPlayer1);
        when(boardMock.getKahalaPitFor(eq(Player.ONE))).thenReturn(kahalaPit1);
        Pit kahalaPit2 = createMockPit(expectedKahalaPitPlayer2);
        when(boardMock.getKahalaPitFor(eq(Player.TWO))).thenReturn(kahalaPit2);

        List<Pit> normalPits1 = createMockPits(expectedNormalPitsPlayer1);
        when(boardMock.getNormalPitsFor(eq(Player.ONE))).thenReturn(normalPits1);

        ArrayList<Integer> expectedNormalPitsPlayer2 = new ArrayList<>(expectedNormalPitsPlayer2Reversed);
        Collections.reverse(expectedNormalPitsPlayer2);

        List<Pit> normalPits2 = createMockPits(expectedNormalPitsPlayer2);
        when(boardMock.getNormalPitsFor(eq(Player.TWO))).thenReturn(normalPits2);

        return boardMock;
    }


    private void verifyGameStatus(
            Game game,
            int expectedKahalaPitPlayer2, List<Integer> expectedNormalPitsPlayer2Reversed,
            List<Integer> expectedNormalPitsPlayer1, int expectedKahalaPitPlayer1) {

        assertEquals(expectedKahalaPitPlayer1, game.getStatus().getStatusPerPlayer().get(Player.ONE).getStonesInKahalaPit());
        assertEquals(expectedKahalaPitPlayer2, game.getStatus().getStatusPerPlayer().get(Player.TWO).getStonesInKahalaPit());

        assertEquals(expectedNormalPitsPlayer1, game.getStatus().getStatusPerPlayer().get(Player.ONE).getStonesInNormalPits());

        List<Integer> actualNormalPitsPlayer2 = game.getStatus().getStatusPerPlayer().get(Player.TWO).getStonesInNormalPits();
        Collections.reverse(actualNormalPitsPlayer2);

        assertEquals(expectedNormalPitsPlayer2Reversed, actualNormalPitsPlayer2);
    }

}