package nl.fungames.kahala.core;


import java.util.Arrays;

public class Game {

    public void startNew() {

    }

    public GameStatus getStatus(){
        GameStatus gameStatus = new GameStatus();

        gameStatus.getStatusPerPlayer().put(Player.ONE, new PlayerStatus());
        gameStatus.getStatusPerPlayer().put(Player.TWO, new PlayerStatus());

        gameStatus.getStatusPerPlayer().get(Player.ONE).setStonesInNormalPits(Arrays.asList(6,6,6,6,6,6));
        gameStatus.getStatusPerPlayer().get(Player.TWO).setStonesInNormalPits(Arrays.asList(6,6,6,6,6,6));

        return gameStatus;
    }

}
