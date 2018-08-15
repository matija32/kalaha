package nl.fungames.kahala.core;

import java.util.Arrays;
import java.util.List;

public class Game {

    public void startNew() {

    }

    public GameStatus getStatus(){
        return new GameStatus();
    }


    List<Integer> getPitsFor(Player player) {
        return Arrays.asList(6, 6, 6, 6, 6, 6);
    }

    int getKahalaFor(Player player) {
        return 0;
    }
}
