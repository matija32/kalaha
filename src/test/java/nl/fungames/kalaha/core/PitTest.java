package nl.fungames.kalaha.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class PitTest {

    @Test
    public void testNoStonesInitially(){
        Pit pit = new Pit();
        assertEquals(0, pit.countStones());
    }

    @Test
    public void testAddingStones() {
        Pit pit = new Pit();
        pit.add(10);
        assertEquals(10, pit.countStones());
    }

    @Test
    public void testTakingAllStones_SomeStonesAreInPit() {
        Pit pit = new Pit();
        pit.add(10);

        int stonesInHand = pit.takeAll();
        assertEquals(10, stonesInHand);
        assertEquals(0, pit.countStones());
    }

    @Test
    public void testTakingAllStones_NoStonesAreInPit() {
        Pit pit = new Pit();

        int stonesInHand = pit.takeAll();
        assertEquals(0, stonesInHand);
        assertEquals(0, pit.countStones());
    }

    @Test
    public void testOwnership() {
        Pit pit = new Pit();

        pit.setOwner(Player.TWO);

        assertFalse(pit.isOwnedBy(Player.ONE));
        assertTrue(pit.isOwnedBy(Player.TWO));
    }

    @Test
    public void testEmptyPits() {
        Pit pit = new Pit();
        pit.add(3);

        assertFalse(pit.isEmpty());

        pit.takeAll();

        assertTrue(pit.isEmpty());
    }

}