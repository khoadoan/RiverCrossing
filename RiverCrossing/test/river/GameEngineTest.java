package river;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/*
 * (1) Most test methods start off by creating a local variable named engine.
 *     Instead of doing this, declare a (private) field named engine and 
 *     initialize it in the setUp method.
 * 
 * (2) Create a private method called transport, that loads the boat with an
 *     item, rows the boat, and unloads the boat. Use this method wherever you
 *     can to replace code that loads, rows, then unloads the boat. 
 */

public class GameEngineTest {
	private GameEngine engine; 
	
	public static void transport(Item id, GameEngine engine) {
		engine.loadDriver();
		engine.loadPassenger(id);
		engine.rowBoat();
		engine.unloadPassenger();
		engine.unloadDriver();
	}
	
    @Before
    public void setUp() throws Exception {
    		engine = new GameEngine();
    }

    @Test
    public void testObjects() {
        GameObject farmer = new Farmer();
        GameObject wolf = new Wolf();
        GameObject goose = new Goose();
        GameObject beans = new Beans();
        Assert.assertEquals("Farmer", farmer.getName());
        Assert.assertEquals(Location.LEFT_BANK, farmer.getLocation());
        Assert.assertEquals("", farmer.getSound());
        Assert.assertEquals("Wolf", wolf.getName());
        Assert.assertEquals(Location.LEFT_BANK, wolf.getLocation());
        Assert.assertEquals("Howl", wolf.getSound());
        Assert.assertEquals("Goose", goose.getName());
        Assert.assertEquals(Location.LEFT_BANK, goose.getLocation());
        Assert.assertEquals("Honk", goose.getSound());
        Assert.assertEquals("Beans", beans.getName());
        Assert.assertEquals(Location.LEFT_BANK, beans.getLocation());
        Assert.assertEquals("", beans.getSound());
    }
    
    @Test
    public void testMidTransport() {
        Assert.assertEquals(Location.LEFT_BANK, engine.getLocation(Item.GOOSE));
        engine.loadPassenger(Item.GOOSE);
        Assert.assertEquals(Location.BOAT, engine.getLocation(Item.GOOSE));
        engine.rowBoat();
        Assert.assertEquals(Location.BOAT, engine.getLocation(Item.GOOSE));
        engine.unloadPassenger();
        engine.unloadDriver();
        Assert.assertEquals(Location.RIGHT_BANK, engine.getLocation(Item.GOOSE));
    }

    @Test
    public void testWinningGame() {
        // transport the goose
    		transport(Item.GOOSE, engine);
        Assert.assertFalse(engine.gameIsLost());
        Assert.assertFalse(engine.gameIsWon());

        // go back alone
        engine.loadDriver();
        engine.rowBoat();
        Assert.assertFalse(engine.gameIsLost());
        Assert.assertFalse(engine.gameIsWon());

        // transport the wolf
        transport(Item.WOLF, engine);
        Assert.assertFalse(engine.gameIsLost());
        Assert.assertFalse(engine.gameIsWon());

        // transport the goose
        transport(Item.GOOSE, engine);
        Assert.assertFalse(engine.gameIsLost());
        Assert.assertFalse(engine.gameIsWon());

        // transport the beans
        transport(Item.BEANS, engine);
        Assert.assertFalse(engine.gameIsLost());
        Assert.assertFalse(engine.gameIsWon());

        // go back alone
        engine.loadDriver();
        engine.rowBoat();
        Assert.assertFalse(engine.gameIsLost());
        Assert.assertFalse(engine.gameIsWon());

        // transport the goose
        transport(Item.GOOSE, engine);
        Assert.assertFalse(engine.gameIsLost());
        Assert.assertTrue(engine.gameIsWon());
    }

    @Test
    public void testLosingGame() {
        // transport the goose
    		transport(Item.GOOSE, engine);
        Assert.assertFalse(engine.gameIsLost());
        Assert.assertFalse(engine.gameIsWon());

        // go back alone
        engine.loadDriver();
        engine.rowBoat();
        Assert.assertFalse(engine.gameIsLost());
        Assert.assertFalse(engine.gameIsWon());

        // transport the wolf
        transport(Item.WOLF, engine);
        Assert.assertFalse(engine.gameIsLost());
        Assert.assertFalse(engine.gameIsWon());

        // go back alone
        engine.loadDriver();
        engine.rowBoat();
        Assert.assertTrue(engine.gameIsLost());
        Assert.assertFalse(engine.gameIsWon());
    }

    @Test
    public void testError() {
        // transport the goose
    		transport(Item.GOOSE, engine);
        Assert.assertFalse(engine.gameIsLost());
        Assert.assertFalse(engine.gameIsWon());

        // save the state
        Location topLoc = engine.getLocation(Item.WOLF);
        Location midLoc = engine.getLocation(Item.GOOSE);
        Location bottomLoc = engine.getLocation(Item.BEANS);
        Location playerLoc = engine.getLocation(Item.FARMER);

        engine.loadPassenger(Item.WOLF);

        // check that the state has not changed
        Assert.assertEquals(topLoc, engine.getLocation(Item.WOLF));
        Assert.assertEquals(midLoc, engine.getLocation(Item.GOOSE));
        Assert.assertEquals(bottomLoc, engine.getLocation(Item.BEANS));
        Assert.assertEquals(playerLoc, engine.getLocation(Item.FARMER));
    }
    
    @Test
	public void testGameEngineGetters() {
		Assert.assertEquals("Wolf", engine.getName(Item.WOLF));
		Assert.assertEquals("Howl", engine.getSound(Item.WOLF));
		Assert.assertEquals(Location.LEFT_BANK, engine.getLocation(Item.WOLF));
		Assert.assertEquals(Location.LEFT_BANK, engine.getBoatLocation());
    }
    
    
    @Test
    public void testResetGame() {
        // transport the goose
    		transport(Item.GOOSE, engine);
        Assert.assertFalse(engine.gameIsLost());
        Assert.assertFalse(engine.gameIsWon());

        // go back alone
        engine.loadDriver();
        engine.rowBoat();
        Assert.assertFalse(engine.gameIsLost());
        Assert.assertFalse(engine.gameIsWon());
        
        engine.resetGame();
        assertAllOnLocation(Location.LEFT_BANK);
        
        // transport the wolf, goose, beans
        transport(Item.WOLF, engine);
        engine.loadDriver();
        engine.rowBoat();
        transport(Item.GOOSE, engine);
        engine.loadDriver();
        engine.rowBoat();
        transport(Item.BEANS, engine);
        
        engine.resetGame();
        assertAllOnLocation(Location.LEFT_BANK);
    }
    
    private void assertAllOnLocation(Location loc) {
        Assert.assertEquals(loc, engine.getLocation(Item.GOOSE));
        Assert.assertEquals(loc, engine.getLocation(Item.BEANS));
        Assert.assertEquals(loc, engine.getLocation(Item.WOLF));
        Assert.assertEquals(loc, engine.getLocation(Item.FARMER));
        Assert.assertEquals(loc, engine.getBoatLocation());
    }
}
