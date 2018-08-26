package nl.fungames.kalaha.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nl.fungames.kalaha.core.Game;
import nl.fungames.kalaha.core.GameStatus;

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
        return game.getStatus();
    }

    @PostMapping(path = "/play")
    public ResponseEntity<String> play(
            @RequestBody MoveDTO move){
        game.play(move.getPlayer(), move.getPitId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(path = "/restart")
    public void restart(){
        game.restart();
    }

}
