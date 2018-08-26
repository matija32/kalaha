package nl.fungames.kalaha.core;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Board {

    private ArrayList<Pit> allPits;

    private final int startingNumberOfStonesInNormalPits;
    private final int normalPitsPerPlayer;

    Board(int normalPitsPerPlayer, int startingNumberOfStonesInNormalPits){
        this.normalPitsPerPlayer = normalPitsPerPlayer;
        this.startingNumberOfStonesInNormalPits = startingNumberOfStonesInNormalPits;
        initializePits();
    }

    private void initializePits() {

        allPits = new ArrayList<>();

        int numberOfKalahaPitsPerPlayer = 1;
        int totalNumberOfPits = 2*(normalPitsPerPlayer + numberOfKalahaPitsPerPlayer);

        int kalahaIndexPitForPlayer1 = normalPitsPerPlayer;
        int kalahaPitIndexForPlayer2 = 2*normalPitsPerPlayer + 1;

        for (int i = 0; i < totalNumberOfPits; i++) {
            Pit pit = new Pit();
            allPits.add(pit);

            boolean isKalahaPit = (i == kalahaIndexPitForPlayer1) || (i == kalahaPitIndexForPlayer2);
            pit.setAsKalaha(isKalahaPit);

            if (!isKalahaPit){
                pit.add(startingNumberOfStonesInNormalPits);
            }

            boolean belongsToPlayer1 = i <= kalahaIndexPitForPlayer1;
            pit.setOwner(belongsToPlayer1 ? Player.ONE : Player.TWO);
        }
    }

    Pit getKalahaPitFor(Player player) {
        return allPits.stream().filter(pit -> pit.isOwnedBy(player) && pit.isKalaha()).findAny().get();
    }

    List<Pit> getNormalPitsFor(Player player){
        return allPits.stream().filter(pit -> pit.isOwnedBy(player) && !pit.isKalaha()).collect(Collectors.toList());
    }

    Pit sow(Player player, int pitIndex){
        Pit startingPit = getNormalPitsFor(player).get(pitIndex);
        if (startingPit.isEmpty()) {
            throw new IllegalArgumentException("Cannot sow from an empty pit");
        }

        int startingPitIndex = allPits.indexOf(startingPit);
        int kalahaIndexToSkip = allPits.indexOf(getPitOppositeOf(getKalahaPitFor(player)));

        int stonesInHand = allPits.get(startingPitIndex).takeAll();
        int currentPit = getNextPitToSow(startingPitIndex, kalahaIndexToSkip);

        while (true){
            stonesInHand--;
            allPits.get(currentPit).add(1);

            if (stonesInHand == 0) {
                break;
            }

            currentPit = getNextPitToSow(currentPit, kalahaIndexToSkip);
        }

        return allPits.get(currentPit);
    }



    private int getNextPitToSow(int currentPit, int kalahaToSkip) {
        if (currentPit + 1 == kalahaToSkip) {
            return getNextPitToSow(currentPit+1, kalahaToSkip);
        }
        else return (currentPit + 1) % allPits.size();
    }

    void moveStonesToOwnKalaha(Pit pit) {
        moveStonesToKalaha(pit, pit.getOwner());
    }

    void moveStonesToOpponentsKalaha(Pit pit) {
        moveStonesToKalaha(pit, pit.isOwnedBy(Player.ONE) ? Player.TWO : Player.ONE);
    }

    private void moveStonesToKalaha(Pit pit, Player player) {
        int stonesInHand = pit.takeAll();
        Pit kalaha = getKalahaPitFor(player);
        kalaha.add(stonesInHand);
    }

    Pit getPitOppositeOf(Pit pit) {
        int totalNumberOfPits = allPits.size();
        int pitIndex = allPits.indexOf(pit);

        int oppositePitIndex = pit.isKalaha() ?
                        (pitIndex + totalNumberOfPits /2) % totalNumberOfPits :
                        (2*normalPitsPerPlayer - pitIndex);

        return allPits.get(oppositePitIndex);
    }

    public void revertToStartingState() {
        this.initializePits();
    }
}
