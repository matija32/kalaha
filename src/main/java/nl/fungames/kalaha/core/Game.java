package nl.fungames.kalaha.core;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Game {

    private Board board;
    private final Supplier<Board> boardSupplier;
    private Player playerThatHasTheTurn;

    public Game() {
        this(() -> new Board(6, 6));
    }

    Game(Supplier<Board> boardSupplier){
        this.boardSupplier = boardSupplier;
        restart();
    }

    public void restart() {
        this.board = boardSupplier.get();
        this.playerThatHasTheTurn = Player.ONE;
    }

    public void play(Player player, int pitIndex) {
        if (player != playerThatHasTheTurn) {
            throw new IllegalArgumentException("It's not Player " + player + "'s turn!");
        }

        Pit lastFilledPit = board.sow(player, pitIndex);

        if (lastFilledPit.countStones() == 1 && lastFilledPit.isOwnedBy(playerThatHasTheTurn)){
            board.moveStonesToOwnKalaha(lastFilledPit);
            board.moveStonesToOpponentsKalaha(board.getPitOppositeOf(lastFilledPit));
        }

        if (!lastFilledPit.isKalaha()) {
            playerThatHasTheTurn = playerThatHasTheTurn == Player.ONE ? Player.TWO : Player.ONE;
        }

        if (!thereAreStonesInNormalPitsOf(player)){
            moveAllToKalahas(Player.ONE);
            moveAllToKalahas(Player.TWO);
        }
    }

    private void moveAllToKalahas(Player player) {
        board.getNormalPitsFor(player).forEach(pit -> board.moveStonesToOwnKalaha(pit));
    }

    public GameStatus getStatus(){
        Map<Player, PlayerStatus> statusPerPlayer = new HashMap<>();
        statusPerPlayer.put(Player.ONE, createPlayerStatus(Player.ONE));
        statusPerPlayer.put(Player.TWO, createPlayerStatus(Player.TWO));

        if (gameIsOngoing()) {
            return new GameStatus(Messages.GAME_ONGOING, statusPerPlayer);
        }
        else {
            return new GameStatus(determineEndgameMesage(), statusPerPlayer);
        }
    }

    private String determineEndgameMesage() {
        int player1Kahala = board.getKalahaPitFor(Player.ONE).countStones();
        int player2Kahala = board.getKalahaPitFor(Player.TWO).countStones();
        if (player1Kahala > player2Kahala) {
            return Messages.ENDGAME_PLAYER_1_WON;
        }
        if (player1Kahala < player2Kahala) {
            return Messages.ENDGAME_PLAYER_2_WON;
        }
        else {
            return Messages.ENDGAME_DRAW;
        }
    }

    private boolean gameIsOngoing() {
        return thereAreStonesInNormalPitsOf(Player.ONE) && thereAreStonesInNormalPitsOf(Player.TWO);
    }

    private boolean thereAreStonesInNormalPitsOf(Player player) {
        return board.getNormalPitsFor(player).stream().anyMatch(pit -> pit.countStones() > 0);
    }

    private PlayerStatus createPlayerStatus(Player player) {
        return new PlayerStatus(
                board.getKalahaPitFor(player).countStones(),
                board.getNormalPitsFor(player).stream().map(Pit::countStones).collect(Collectors.toList()),
                board.getNormalPitsFor(player).stream().map(pit -> pit.countStones() > 0 && playerThatHasTheTurn == player).collect(Collectors.toList()));
    }

    Player whoseTurnIsIt() {
        return playerThatHasTheTurn;
    }
}
