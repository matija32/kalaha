package nl.fungames.kahala.core;

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

    private int stonesInKahalaPit = 0;
    private List<Integer> stonesInNormalPits = Collections.emptyList();

}
