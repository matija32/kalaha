package nl.fungames.kalaha.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.fungames.kalaha.core.Player;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MoveDto {
    private Player player;
    private int pitId;
}
