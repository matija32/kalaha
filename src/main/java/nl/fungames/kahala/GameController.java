package nl.fungames.kahala;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nl.fungames.kahala.core.Game;
import nl.fungames.kahala.core.GameStatus;

@RestController
@RequestMapping("/api")
public class GameController {

    private Game game = new Game();

    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }

    @GetMapping(path = "/status")
    public GameStatus status() {
        return new GameStatus();
    }
}
