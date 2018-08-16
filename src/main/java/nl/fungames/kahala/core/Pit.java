package nl.fungames.kahala.core;

class Pit {
    private int stones = 0;

    private Player owner;
    private boolean kahala;

    int takeAll() {
        int stonesInHand = stones;

        stones = 0;

        return stonesInHand;
    }

    void add(int stones) {
        this.stones += stones;
    }

    int countStones() {
        return stones;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public boolean isKahala() {
        return kahala;
    }

    public void setAsKahala(boolean kahala) {
        this.kahala = kahala;
    }
}
