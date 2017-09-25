package river;

/*
 * (1) Create a private method called getGameObject that returns a game object
 *     associated with an id. Use the new method to simplify the getName,
 *     getLocation, getSound, and loadBoat methods.
 * 
 * (2) Create two private methods: boatHasPassenger and getBoatPassenger, that
 *     check if the boat has a passenger (Wolf, Goose, or Beans - does not apply
 *     to Farmer) and returns the passenger on the boat, respectively. Use them
 *     to simplify loadBoat and unloadBoat. Note that getBoatPassenger should be
 *     an *accessor* method - in other words, it should simply report who the boat's
 *     passenger is; it should NOT remove that passenger. getBoatPassenger should
 *     return a gameObject.
 * 
 * (3) Implement a method called oppositeLocation that returns the START if the
 *     current location is FINSH and returns FINISH if the current location is
 *     START. Use it to simplify rowBoat.
 *     
 * (4) Rename the method getCurrentLocation to getBoatLocation
 * 
 * (5) It turns out we do not actually need the field currentLocation as it is
 *     always the same as the player's location. Remove this field.
 *
 * (6) The two enum types are currently inner classes. Make them regular classes
 *     in package river. Change the constants in Item from TOP, MID, BOTTOM, and
 *     PLAYER to WOLF, GOOSE, BEANS, and FARMER. Change the constants START and
 *     FINISH in Location to LEFT_BANK and RIGHT_BANK.
 * 
 * (7) Make all GameObject fields final. Change the names top, mid, bottom, and
 *     player to wolf, goose, beans, and farmer.
 */

public class GameEngine {

    private final GameObject wolf;
    private final GameObject goose;
    private final GameObject beans;
    private final GameObject farmer;
    private Location boatLocation;
    
    // Add. #1 
    private GameObject passenger;

    public GameEngine() {
        wolf = new Wolf();
        goose = new Goose();
        beans = new Beans();
        farmer = new Farmer();
        boatLocation = Location.LEFT_BANK;
    }

    public String getName(Item id) {
    		return getGameObject(id).getName();
    }

    public Location getLocation(Item id) {
    		return getGameObject(id).getLocation();
    }

    public String getSound(Item id) {
    		return getGameObject(id).getSound();
    }

    public Location getBoatLocation() {
        return boatLocation;
    }
    
    public void loadDriver() {
    		farmer.setLocation(Location.BOAT);
    }
    
    public void unloadDriver() {
    		farmer.setLocation(getBoatLocation());
    }
    
    public void loadPassenger(Item id) {
    		GameObject go = getGameObject(id);
    		if (!hasPassenger() && go.getLocation() == getBoatLocation()) {
    			setPassenger(getGameObject(id));
    			getPassenger().setLocation(Location.BOAT);
    		}
    }
    
    public void unloadPassenger() {
    		if (hasPassenger()) {
    			getPassenger().setLocation(getBoatLocation());
    			setPassenger(null);
    		}
    }
    
    public void rowBoat() {
        assert (boatLocation != Location.BOAT);
        
        boatLocation = oppositeLocation();
    }

    public boolean gameIsWon() {
        return wolf.getLocation() == Location.RIGHT_BANK
                && goose.getLocation() == Location.RIGHT_BANK
                && beans.getLocation() == Location.RIGHT_BANK
                && farmer.getLocation() == Location.RIGHT_BANK;
    }

    public boolean gameIsLost() {
        if (goose.getLocation() == Location.BOAT) {
            return false;
        }
        if (goose.getLocation() != farmer.getLocation()
        			&& goose.getLocation() != getBoatLocation()
                && goose.getLocation() == wolf.getLocation()) {
            return true;
        }
        if (goose.getLocation() != farmer.getLocation()
        			&& goose.getLocation() != getBoatLocation()
                && goose.getLocation() == beans.getLocation()) {
            return true;
        }
        return false;
    }
    
    // P1
    private GameObject getGameObject(Item id) {
    		switch(id) {
    		case WOLF: 
    			return wolf;
    		case GOOSE: 
    			return goose;
    		case BEANS: 
    			return beans;
    		default: 
    			return farmer;
    		}
    }
    
    public GameObject getPassenger() {
    		return passenger;
    }
    
    public boolean hasPassenger() {
    		return passenger != null;
    }
    
    public void setPassenger(GameObject passenger) {
    		this.passenger = passenger;
    }
    
    public Location oppositeLocation() {    		
    		if (boatLocation == Location.RIGHT_BANK) {
    			return Location.LEFT_BANK;
    		} else if (boatLocation == Location.LEFT_BANK) {
    			return Location.RIGHT_BANK;
    		} else {
    			//TODO: GE.3 check if this ok
    			throw new RuntimeException("Invalid boat location: " + boatLocation);
    		}
    }
    
    public void resetGame() {
        wolf.setLocation(Location.LEFT_BANK);
        goose.setLocation(Location.LEFT_BANK);
        beans.setLocation(Location.LEFT_BANK);
        farmer.setLocation(Location.LEFT_BANK);
        boatLocation = Location.LEFT_BANK;
        setPassenger(null);
    }
}
