package nl.fungames.kalaha.rest;

import lombok.Getter;
import lombok.Setter;
import nl.fungames.kalaha.core.Player;

@Getter
@Setter
public class MoveDTO {
    private Player player;
    private int pitId;
}
