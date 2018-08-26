package nl.fungames.kalaha.core;


import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameStatus {

    private boolean finished = false;
    private String message = "";
    private Map<Player, PlayerStatus> statusPerPlayer = new HashMap<>();
    private Player nextOneToPlay;
}
