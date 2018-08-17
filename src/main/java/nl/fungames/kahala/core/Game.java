package nl.fungames.kahala.core;


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

    public Player whoseTurnIsIt() { return playerThatHasTheTurn; }

    public void play(Player player, int pitIndex) {
        Pit lastFilledPit = board.sow(player, pitIndex);

        if (lastFilledPit.countStones() == 1 && lastFilledPit.isOwnedBy(playerThatHasTheTurn)){
            board.moveStonesToOwnKahala(lastFilledPit);
            board.moveStonesToOpponentsKahala(board.getPitOppositeOf(lastFilledPit));
        }

        if (!lastFilledPit.isKahala()) {
            playerThatHasTheTurn = playerThatHasTheTurn == Player.ONE ? Player.TWO : Player.ONE;
        }
    }

    public GameStatus getStatus(){
        GameStatus gameStatus = new GameStatus();

        gameStatus.getStatusPerPlayer().put(
                Player.ONE,
                new PlayerStatus(
                        board.getKahalaPitFor(Player.ONE).countStones(),
                        board.getNormalPitsFor(Player.ONE).stream().map(Pit::countStones).collect(Collectors.toList())));

        gameStatus.getStatusPerPlayer().put(
                Player.TWO,
                new PlayerStatus(
                        board.getKahalaPitFor(Player.TWO).countStones(),
                        board.getNormalPitsFor(Player.TWO).stream().map(Pit::countStones).collect(Collectors.toList())));

        gameStatus.setFinished(false);
        gameStatus.setMessage("The game is ON!");

        return gameStatus;
    }

}
