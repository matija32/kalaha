package nl.fungames.kalaha.core;

import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerStatus {

    private int stonesInKalahaPit = 0;
    private List<Integer> stonesInNormalPits = Collections.emptyList();
    private List<Boolean> allowedToSeedFromNormalPit = Collections.emptyList();

}
