package nl.fungames.kalaha.core;

class Pit {
    private int stones = 0;

    private Player owner;
    private boolean kalaha;

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

    public boolean isOwnedBy(Player player) {
        return player == owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public boolean isKalaha() {
        return kalaha;
    }

    public void setAsKalaha(boolean kalaha) {
        this.kalaha = kalaha;
    }

    public boolean isEmpty() {
        return stones == 0;
    }
}
