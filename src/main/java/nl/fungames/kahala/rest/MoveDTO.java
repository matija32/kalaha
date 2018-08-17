package nl.fungames.kahala.rest;

import lombok.Getter;
import lombok.Setter;
import nl.fungames.kahala.core.Player;

@Getter
@Setter
public class MoveDTO {
    private Player player;
    private int pitId;
}
