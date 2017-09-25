package river;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Graphical interface for the River application
 * 
 * @author Gregory Kulczycki
 */
public class RiverGUI extends JPanel implements MouseListener {
    
    // ==========================================================
    // Fields (hotspots)
    // ==========================================================

    private final Rectangle leftFarmerRect = new Rectangle(80, 215, 50, 50);
    private final Rectangle leftWolfRect = new Rectangle(20, 215, 50, 50);
    private final Rectangle leftGooseRect = new Rectangle(20, 275, 50, 50);
    private final Rectangle leftBeansRect = new Rectangle(80, 275, 50, 50);
    private final Rectangle leftBoatRect = new Rectangle(140, 275, 110, 50);
    private final Rectangle leftBoatDriverRect = new Rectangle(140, 215, 50, 50);
    private final Rectangle leftBoatPassengerRect = new Rectangle(200, 215, 50, 50);

    private final Rectangle rightFarmerRect = new Rectangle(730, 215, 50, 50);
    private final Rectangle rightWolfRect = new Rectangle(670, 215, 50, 50);
    private final Rectangle rightGooseRect = new Rectangle(670, 275, 50, 50);
    private final Rectangle rightBeansRect = new Rectangle(730, 275, 50, 50);
    private final Rectangle rightBoatRect = new Rectangle(550, 275, 110, 50);
    private final Rectangle rightBoatDriverRect = new Rectangle(550, 215, 50, 50);
    private final Rectangle rightBoatPassengerRect = new Rectangle(610, 215, 50, 50);
    
    private final Rectangle restartRect = new Rectangle(300, 120, 200, 60);
    
    private Rectangle farmerRect, wolfRect, gooseRect, beansRect, boatRect;
    
    // ==========================================================
    // Private Fields
    // ==========================================================
    
    private GameEngine engine; // Model
    
    // ==========================================================
    // Constructor
    // ==========================================================
    
    public RiverGUI() {
        
        engine = new GameEngine();
        setActiveRectangles();
        addMouseListener(this);
        
    }
    
    // ==========================================================
    // Paint Methods (View)
    // ==========================================================

    @Override
    public void paintComponent(Graphics g) {
    		setActiveRectangles();
    	
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        g.setColor(Color.ORANGE);
        
		paintObject(g, farmerRect, Color.MAGENTA, null);
		paintObject(g, wolfRect, Color.CYAN, "W");        
		paintObject(g, gooseRect, Color.CYAN, "G");
		paintObject(g, beansRect, Color.CYAN, "B");
		paintObject(g, boatRect, Color.ORANGE, null);
		
		if (engine.gameIsLost()) {
			paintMessage("You Lost!", g);
			paintRestartButton(g);
		} else if (engine.gameIsWon()) {
			paintMessage("You Win!", g);
			paintRestartButton(g);
		} else {
			paintMessage("Game in Progress!", g);
		}
    }
    
    public void paintRestartButton(Graphics g) {
    		g.setColor(Color.ORANGE);
		g.fillRect(restartRect.x, restartRect.y, restartRect.width, restartRect.height);
		paintStringInRectangle("Restart", 
				restartRect.x, restartRect.y, restartRect.width, restartRect.height, g);
    }
    
    public void paintObject(Graphics g, Rectangle rect, Color color, String name) {
    		g.setColor(color);
    		g.fillRect(rect.x, rect.y, rect.width, rect.height);
    		if (name != null && name != "") {
    			paintStringInRectangle(name, rect.x, rect.y, rect.width, rect.height, g);
    		}
    }
    
    public void paintStringInRectangle(String str, int x, int y, int width, int height, Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Verdana", Font.BOLD, 36));
        FontMetrics fm = g.getFontMetrics();
        int strXCoord = x + width/2 - fm.stringWidth(str)/2;
        int strYCoord = y + height/2 + 36/2 - 4;
        g.drawString(str, strXCoord, strYCoord);
    }
    
    // ==========================================================
    // Startup Methods
    // ==========================================================
    
    /**
     * Create the GUI and show it.  For thread safety, 
     * this method should be invoked from the 
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        
        // Create and set up the window
        JFrame frame = new JFrame("RiverCrossing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create and set up the content pane
        RiverGUI newContentPane = new RiverGUI();
        newContentPane.setOpaque(true);        
        frame.setContentPane(newContentPane);
        
        // Display the window
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(RiverGUI::createAndShowGUI);
    }

    // ==========================================================
    // MouseListener Methods (Controller)
    // ==========================================================
    
    @Override
    public void mouseClicked(MouseEvent e) {
    		if (engine.gameIsLost() || engine.gameIsWon()) {
    			if (restartRect.contains(e.getPoint())) {
    				engine.resetGame();
    			}
    		} else {
        		setActiveRectangles();
            	
        		if (farmerRect.contains(e.getPoint())) {
        			loadOrUnloadDriver();
        		} else if (wolfRect.contains(e.getPoint())) {
        			loadOrUnloadPassenger(Item.WOLF);
        		} else if (gooseRect.contains(e.getPoint())) {
        			loadOrUnloadPassenger(Item.GOOSE);
        		} else if (beansRect.contains(e.getPoint())) {
        			loadOrUnloadPassenger(Item.BEANS);
        		} else if (boatRect.contains(e.getPoint())) {
        			if (engine.getLocation(Item.FARMER) == Location.BOAT) {
        				engine.rowBoat();
        			}
        		} else {
        			return;
        		}    			
    		}
    		
        repaint();
    }   
    
    private void loadOrUnloadDriver() {
    		if (engine.getLocation(Item.FARMER) != Location.BOAT) {
    			engine.loadDriver();
    		} else {
    			engine.unloadDriver();
    		}
    }
    
    private void loadOrUnloadPassenger(Item id) {
    		if (engine.getLocation(id) != Location.BOAT) {
    			engine.loadPassenger(id);
    		} else {
    			engine.unloadPassenger();
    		}
    }
    
    // P1
//    private void setActiveRectangles() {
//    		getActiveLocation
//    		switch(engine.getLocation(Item.FARMER)) {
//    		case LEFT_BANK: 
//    			farmerRect = leftFarmerRect; break;
//    		case RIGHT_BANK:
//    			farmerRect = rightFarmerRect; break; 
//    		case BOAT:
//    			farmerRect = engine.getBoatLocation() == Location.LEFT_BANK ? leftBoatDriverRect : rightBoatDriverRect; break;
//    		default:
//    		}
//    		
//    		switch(engine.getLocation(Item.GOOSE)) {
//    		case LEFT_BANK: 
//    			gooseRect = leftGooseRect; break;
//    		case RIGHT_BANK:
//    			gooseRect = rightGooseRect; break; 
//    		case BOAT:
//    			gooseRect = engine.getBoatLocation() == Location.LEFT_BANK ? leftBoatPassengerRect: rightBoatPassengerRect; break;
//    		default:
//    		}
//    		
//    		switch(engine.getLocation(Item.WOLF)) {
//    		case LEFT_BANK: 
//    			wolfRect = leftWolfRect; break;
//    		case RIGHT_BANK:
//    			wolfRect = rightWolfRect; break; 
//    		case BOAT:
//    			wolfRect = engine.getBoatLocation() == Location.LEFT_BANK ? leftBoatPassengerRect: rightBoatPassengerRect; break;
//    		default:
//    		}
//    		
//    		switch(engine.getLocation(Item.BEANS)) {
//    		case LEFT_BANK: 
//    			beansRect = leftBeansRect; break;
//    		case RIGHT_BANK:
//    			beansRect = rightBeansRect; break; 
//    		case BOAT:
//    			beansRect = engine.getBoatLocation() == Location.LEFT_BANK ? leftBoatPassengerRect: rightBoatPassengerRect; break;
//    		default:
//    		}
//    		
//    		switch(engine.getBoatLocation()) {
//    		case LEFT_BANK: 
//    			boatRect = leftBoatRect; break;
//    		case RIGHT_BANK:
//    			boatRect = rightBoatRect; break; 
//    		default:
//    		}
//    		
//    }
    
    private void setActiveRectangles() {
    		farmerRect = getActiveRect(Item.FARMER, leftFarmerRect, rightFarmerRect, leftBoatDriverRect, rightBoatDriverRect);
    		gooseRect = getActiveRect(Item.GOOSE, leftGooseRect, rightGooseRect, leftBoatPassengerRect, rightBoatPassengerRect);
    		wolfRect = getActiveRect(Item.WOLF, leftWolfRect, rightWolfRect, leftBoatPassengerRect, rightBoatPassengerRect);
    		beansRect = getActiveRect(Item.BEANS, leftBeansRect, rightBeansRect, leftBoatPassengerRect, rightBoatPassengerRect);
    		
		switch(engine.getBoatLocation()) {
		case LEFT_BANK: 
			boatRect = leftBoatRect; break;
		case RIGHT_BANK:
			boatRect = rightBoatRect; break; 
		default:
		}
		
}
    
    public Rectangle getActiveRect(Item id, 
    		Rectangle leftBankRect, Rectangle rightBankRect, 
    		Rectangle leftBoatRect, Rectangle rightBoatRect) {
	    	switch(engine.getLocation(id)) {
			case LEFT_BANK: 
				return leftBankRect;
			case RIGHT_BANK:
				return rightBankRect; 
			case BOAT:
				return engine.getBoatLocation() == Location.LEFT_BANK ? leftBoatRect: rightBoatRect; 
			default:
				return null;
			}
    }
    
    public void paintMessage(String message, Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Verdana", Font.BOLD, 36));
        FontMetrics fm = g.getFontMetrics();
        int strXCoord = 400 - fm.stringWidth(message)/2;
        int strYCoord = 100;
        g.drawString(message, strXCoord, strYCoord);
    }
    
    // ----------------------------------------------------------
    // None of these methods will be used
    // ----------------------------------------------------------
    
    @Override
    public void mousePressed(MouseEvent e) {
        //
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //
    }    
}
