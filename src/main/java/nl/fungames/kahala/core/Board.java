package nl.fungames.kahala.core;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Board {

    private final ArrayList<Pit> allPits = new ArrayList<>();

    private final int normalPitsPerPlayer;

    Board(int normalPitsPerPlayer, int startingNumberOfStonesInNormalPits){
        this.normalPitsPerPlayer = normalPitsPerPlayer;
        createPits(startingNumberOfStonesInNormalPits);
    }

    private void createPits(int startingNumberOfStonesInNormalPits) {

        int numberOfKahalaPitsPerPlayer = 1;
        int totalNumberOfPits = 2*(normalPitsPerPlayer + numberOfKahalaPitsPerPlayer);

        int kahalaIndexPitForPlayer1 = normalPitsPerPlayer;
        int kahalaPitIndexForPlayer2 = 2*normalPitsPerPlayer + 1;

        for (int i = 0; i < totalNumberOfPits; i++) {
            Pit pit = new Pit();
            allPits.add(pit);

            boolean isKahalaPit = (i == kahalaIndexPitForPlayer1) || (i == kahalaPitIndexForPlayer2);
            pit.setAsKahala(isKahalaPit);

            if (!isKahalaPit){
                pit.add(startingNumberOfStonesInNormalPits);
            }

            boolean belongsToPlayer1 = i <= kahalaIndexPitForPlayer1;
            pit.setOwner(belongsToPlayer1 ? Player.ONE : Player.TWO);
        }
    }

    Pit getKahalaPitFor(Player player) {
        return allPits.stream().filter(pit -> pit.isOwnedBy(player) && pit.isKahala()).findAny().get();
    }

    List<Pit> getNormalPitsFor(Player player){
        return allPits.stream().filter(pit -> pit.isOwnedBy(player) && !pit.isKahala()).collect(Collectors.toList());
    }

    private List<Pit> getPits(int firstPit, int lastPit) {
        return allPits.subList(firstPit, lastPit+1);
    }

    Pit sow(Player player, int pitIndex){
        int kahalaIndexToSkip = allPits.indexOf(getPitOppositeOf(getKahalaPitFor(player)));
        int startingPitIndex = allPits.indexOf(getNormalPitsFor(player).get(pitIndex));

        int stonesInHand = allPits.get(startingPitIndex).takeAll();
        int currentPit = getNextPitToSow(startingPitIndex, kahalaIndexToSkip);

        while (true){
            stonesInHand--;
            allPits.get(currentPit).add(1);

            if (stonesInHand == 0) {
                break;
            }

            currentPit = getNextPitToSow(currentPit, kahalaIndexToSkip);
        }

        return allPits.get(currentPit);
    }

    private int getNextPitToSow(int currentPit, int kahalaToSkip) {
        if (currentPit + 1 == kahalaToSkip) {
            return getNextPitToSow(currentPit+1, kahalaToSkip);
        }
        else return (currentPit + 1) % allPits.size();
    }

    void moveStonesToOwnKahala(Pit pit) {
        moveStonesToKahala(pit, pit.getOwner());
    }

    void moveStonesToOpponentsKahala(Pit pit) {
        moveStonesToKahala(pit, pit.isOwnedBy(Player.ONE) ? Player.TWO : Player.ONE);
    }

    private void moveStonesToKahala(Pit pit, Player player) {
        int stonesInHand = pit.takeAll();
        Pit kahala = getKahalaPitFor(player);
        kahala.add(stonesInHand);
    }

    Pit getPitOppositeOf(Pit pit) {
        int totalNumberOfPits = allPits.size();
        int pitIndex = allPits.indexOf(pit);

        int oppositePitIndex = pit.isKahala() ?
                        (pitIndex + totalNumberOfPits /2) % totalNumberOfPits :
                        (2*normalPitsPerPlayer - pitIndex);

        return allPits.get(oppositePitIndex);
    }
}
