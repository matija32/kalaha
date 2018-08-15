package nl.fungames.kahala.core;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameStatus {

    private boolean ongoing = false;
    private String message = "hello from the Game Status";
}
