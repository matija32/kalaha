package nl.fungames.kahala.core;

import java.util.Collections;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerStatus {

    private int stonesInKahalaPit = 0;
    private List<Integer> stonesInNormalPits = Collections.emptyList();

}
