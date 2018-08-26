package nl.fungames.kalaha.core;


import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class GameStatus {

    private String message;
    private Map<Player, PlayerStatus> statusPerPlayer;


}
