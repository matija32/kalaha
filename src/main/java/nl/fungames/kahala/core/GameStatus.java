package nl.fungames.kahala.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameStatus {

    private boolean ongoing = false;
    private boolean player1ToMove = true;

    private List<Integer> player1NormalPits = Collections.emptyList();
    private List<Integer> player2NormalPits = Collections.emptyList();

    private int player1KahalaPit = 0;
    private int player2KahalaPit = 0;
}
