package nl.fungames.kalaha.core;

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
import java.util.Map;

import org.junit.Test;

public class GameTest {

    @Test
    public void testGameStatus_Initial(){
        Game game = new Game(createFilledMockBoard(
                0, Arrays.asList(6, 6, 6, 6, 6, 6),
                Arrays.asList(6, 6, 6, 6, 6, 6), 0)
        );

        verifyStonesInPits(game,
                0, Arrays.asList(6, 6, 6, 6, 6, 6),
                Arrays.asList(6, 6, 6, 6, 6, 6), 0);
        verifyPitsThatCanBeSowed(game,
                Arrays.asList(false, false, false, false, false, false),
                Arrays.asList(true,  true,  true,  true,  true,  true));

    }

    private List<Pit> createMockPits(List<Integer> stonesInPits) {
        ArrayList<Pit> pits = new ArrayList<>();
        stonesInPits.forEach(stoneInPit -> pits.add(createMockPit(stoneInPit)));
        return pits;
    }

    private Pit createKalahaPit() {
        Pit pit = mock(Pit.class);
        when(pit.isKalaha()).thenReturn(true);
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

        Game game = new Game(boardMock);

        game.play(Player.ONE, 3);

        verify(boardMock, times(1)).sow(Player.ONE, 3);
        assertEquals(Player.TWO, game.whoseTurnIsIt());

        game.play(Player.TWO, 5);

        verify(boardMock, times(1)).sow(Player.TWO, 5);
        assertEquals(Player.ONE, game.whoseTurnIsIt());
    }

    @Test
    public void testGameplay_MakingAMoveEndingInOwnKalahaPit() {
        Board boardMock = mock(Board.class);
        Pit player1Kalaha = createKalahaPit();
        when(boardMock.sow(Player.ONE, 3)).thenReturn(player1Kalaha);

        Game game = new Game(boardMock);

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

        Game game = new Game(boardMock);

        game.play(Player.ONE, 3);

        verify(boardMock, times(1)).moveStonesToOwnKalaha(player1NormalPit);
        verify(boardMock, times(1)).moveStonesToOpponentsKalaha(player2OppositePit);

        assertEquals(Player.TWO, game.whoseTurnIsIt());
    }

    @Test
    public void testGameplay_MakingAMoveEndingInOpponentsEmptyPit() {
        Board boardMock = mock(Board.class);
        Pit player2NormalPit = createMockPit(1);
        when(player2NormalPit.isOwnedBy(Player.ONE)).thenReturn(false);
        when(boardMock.sow(Player.ONE, 3)).thenReturn(player2NormalPit);

        Game game = new Game(boardMock);

        game.play(Player.ONE, 3);

        verify(boardMock, never()).moveStonesToOwnKalaha(any());
        verify(boardMock, never()).moveStonesToOpponentsKalaha(any());

        assertEquals(Player.TWO, game.whoseTurnIsIt());
    }

    @Test
    public void testGameplay_Player1BeginsTheGame(){
        Game game = new Game(mock(Board.class));
        assertEquals(Player.ONE, game.whoseTurnIsIt());
    }

    @Test
    public void testGameplay_OnlyPlayerOnTurnCanSow(){
        Board boardMock = createFilledMockBoard(
                0, Arrays.asList(0, 0, 0, 1, 1, 1),
                Arrays.asList(0, 6, 0, 6, 0, 6), 0);
        Pit player2NormalPit = createMockPit(5);
        when(player2NormalPit.isOwnedBy(Player.TWO)).thenReturn(true);
        when(boardMock.sow(Player.ONE, 1)).thenReturn(player2NormalPit);

        Game game = new Game(boardMock);

        assertEquals(Player.ONE, game.whoseTurnIsIt());
        verifyPitsThatCanBeSowed(game,
                Arrays.asList(false, false, false, false, false, false),
                Arrays.asList(false, true,  false, true,  false, true));

        game.play(Player.ONE, 1);

        assertEquals(Player.TWO, game.whoseTurnIsIt());
        verifyPitsThatCanBeSowed(game,
                Arrays.asList(false, false, false, true,  true,  true),
                Arrays.asList(false, false, false, false, false, false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGameplay_CannotPlayOnOtherPlayersTurn() {
        Game game = new Game(mock(Board.class));

        game.play(Player.TWO, 4);
    }

    @Test
    public void testGameplay_MakingALastMove(){
        Board boardMock = createFilledMockBoard(
                9, Arrays.asList(0, 2, 3),
                Arrays.asList(0, 0, 2), 11);

        when(boardMock.sow(Player.ONE, 2)).thenAnswer(invocation -> {
            setUpMockBoard(boardMock,
                    9, Arrays.asList(0, 2, 4),
                    Arrays.asList(0, 0, 0), 12);
            return boardMock.getNormalPitsFor(Player.TWO).get(0);
        });

        Game game = new Game(boardMock);

        game.play(Player.ONE, 2);

        Pit player2Pit0 = boardMock.getNormalPitsFor(Player.TWO).get(0);
        verify(boardMock, times(1)).moveStonesToOwnKalaha(player2Pit0);
        Pit player2Pit1 = boardMock.getNormalPitsFor(Player.TWO).get(1);
        verify(boardMock, times(1)).moveStonesToOwnKalaha(player2Pit1);
    }

    @Test
    public void testGameStatus_Player1Won(){
        Board boardMock = createFilledMockBoard(
                9, Arrays.asList(0, 0, 0),
                Arrays.asList(0, 0, 0), 11);
        Game game = new Game(boardMock);

        assertEquals(Messages.ENDGAME_PLAYER_1_WON, game.getStatus().getMessage());
    }

    @Test
    public void testGameStatus_Player2Won(){
        Board boardMock = createFilledMockBoard(
                13, Arrays.asList(0, 0, 0),
                Arrays.asList(0, 0, 0), 11);
        Game game = new Game(boardMock);

        assertEquals(Messages.ENDGAME_PLAYER_2_WON, game.getStatus().getMessage());
    }

    @Test
    public void testGameStatus_Draw(){
        Board boardMock = createFilledMockBoard(
                13, Arrays.asList(0, 0, 0),
                Arrays.asList(0, 0, 0), 13);
        Game game = new Game(boardMock);

        assertEquals(Messages.ENDGAME_DRAW, game.getStatus().getMessage());
    }

    @Test
    public void testGameStatus_Ongoing() {
        Board boardMock = createFilledMockBoard(
                0, Arrays.asList(0, 0, 0, 1, 1, 1),
                Arrays.asList(0, 6, 0, 6, 0, 6), 0);
        Game game = new Game(boardMock);

        assertEquals(Messages.GAME_ONGOING, game.getStatus().getMessage());
    }

    @Test
    public void testStartingNewGame() {
        Board boardMock = mock(Board.class);
        Game game = new Game(boardMock);

        game.startNew();
        game.startNew();
        game.startNew();

        verify(boardMock, times(4)).revertToStartingState();
    }


    private Board createFilledMockBoard(
            int expectedKalahaPitPlayer2, List<Integer> expectedNormalPitsPlayer2Reversed,
            List<Integer> expectedNormalPitsPlayer1, int expectedKalahaPitPlayer1){

        Board boardMock = mock(Board.class);

        return setUpMockBoard(boardMock, expectedKalahaPitPlayer2, expectedNormalPitsPlayer2Reversed, expectedNormalPitsPlayer1, expectedKalahaPitPlayer1);
    }

    private Board setUpMockBoard(Board boardMock,
                                 int expectedKalahaPitPlayer2,
                                 List<Integer> expectedNormalPitsPlayer2Reversed,
                                 List<Integer> expectedNormalPitsPlayer1,
                                 int expectedKalahaPitPlayer1) {
        Pit kalahaPit1 = createMockPit(expectedKalahaPitPlayer1);
        when(boardMock.getKalahaPitFor(eq(Player.ONE))).thenReturn(kalahaPit1);
        Pit kalahaPit2 = createMockPit(expectedKalahaPitPlayer2);
        when(boardMock.getKalahaPitFor(eq(Player.TWO))).thenReturn(kalahaPit2);

        List<Pit> normalPits1 = createMockPits(expectedNormalPitsPlayer1);
        when(boardMock.getNormalPitsFor(eq(Player.ONE))).thenReturn(normalPits1);

        ArrayList<Integer> expectedNormalPitsPlayer2 = new ArrayList<>(expectedNormalPitsPlayer2Reversed);
        Collections.reverse(expectedNormalPitsPlayer2);

        List<Pit> normalPits2 = createMockPits(expectedNormalPitsPlayer2);
        when(boardMock.getNormalPitsFor(eq(Player.TWO))).thenReturn(normalPits2);

        return boardMock;
    }

    private void verifyStonesInPits(
            Game game,
            int expectedKalahaPitPlayer2, List<Integer> expectedNormalPitsPlayer2Reversed,
            List<Integer> expectedNormalPitsPlayer1, int expectedKalahaPitPlayer1) {

        Map<Player, PlayerStatus> statusPerPlayer = game.getStatus().getStatusPerPlayer();
        assertEquals(expectedKalahaPitPlayer1, statusPerPlayer.get(Player.ONE).getStonesInKalahaPit());
        assertEquals(expectedKalahaPitPlayer2, statusPerPlayer.get(Player.TWO).getStonesInKalahaPit());

        assertEquals(expectedNormalPitsPlayer1, statusPerPlayer.get(Player.ONE).getStonesInNormalPits());

        List<Integer> actualNormalPitsPlayer2 = statusPerPlayer.get(Player.TWO).getStonesInNormalPits();
        Collections.reverse(actualNormalPitsPlayer2);

        assertEquals(expectedNormalPitsPlayer2Reversed, actualNormalPitsPlayer2);
    }

    private void verifyPitsThatCanBeSowed(Game game,
                                          List<Boolean> canSowPitsOfPlayer2,
                                          List<Boolean> canSowPitsOfPlayer1) {
        Map<Player, PlayerStatus> statusPerPlayer = game.getStatus().getStatusPerPlayer();

        assertEquals(canSowPitsOfPlayer1, statusPerPlayer.get(Player.ONE).getAllowedToSeedFromNormalPit());

        List<Boolean> allowedToSeedFromNormalPitOfPlayer2 = statusPerPlayer.get(Player.TWO).getAllowedToSeedFromNormalPit();
        Collections.reverse(allowedToSeedFromNormalPitOfPlayer2);
        assertEquals(canSowPitsOfPlayer2, allowedToSeedFromNormalPitOfPlayer2);
    }
}