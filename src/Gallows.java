import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class Gallows extends JPanel {
	private static final long serialVersionUID = 1L;
	
	// coordinate constants
	private static final int xPos = 175;
    private static final int yPos = 250;
	
	private int stages; // variable to hold the stage of the hanged man
	
	// constructor:
	public Gallows(int stages) {
		this.stages = stages;
	}
	
	// overrides paintComponent(), draws the lines
	@Override
    protected void paintComponent(Graphics drawing) {
		// set the line thickness - use Graphics2
		Graphics2D drawing2 = (Graphics2D) drawing;
		drawing2.setStroke(new BasicStroke(5));
        super.paintComponent(drawing2); // paint the panel
        
        // drawing the gallows:
        if (this.stages > 0) { // gallows
	        drawing2.drawLine(xPos - 125, yPos, xPos + 25, yPos); // base
	        drawing2.drawLine(xPos - 125, yPos, xPos - 125, yPos - 200); // vertical line
	        drawing2.drawLine(xPos - 125, yPos - 200, xPos, yPos - 200); // upper horizontal line
	        drawing2.drawLine(xPos, yPos - 200, xPos, yPos - 160); // hanging rope
	        drawing2.drawLine(xPos - 125, yPos - 180, xPos - 100, yPos - 200); // support
        }
        
        if (this.stages > 1) { // head
        	drawing2.drawOval(xPos - 20, yPos - 160, 40, 40);
        }
        if (this.stages > 2) { // body
        	drawing2.drawLine(xPos, yPos - 120, xPos, yPos - 70);
        }
        if (this.stages > 3) { // left arm
        	drawing2.drawLine(xPos, yPos - 110, xPos - 20, yPos - 110);
        }
        if (this.stages > 4) { // right arm
        	drawing2.drawLine(xPos, yPos - 110, xPos + 20, yPos - 110);
        }
        if (this.stages > 5) { // left leg
        	drawing2.drawLine(xPos, yPos - 70, xPos - 20, yPos - 50);
        }
        if (this.stages > 6) { // right leg
        	drawing2.drawLine(xPos, yPos - 70, xPos + 20, yPos - 50);
        }
    }
	
	// draw the next piece of the gallows based on the lives remaining
    public void drawNextGallowPiece(int lives) {
        if (lives >= 0) {
            this.stages++;
            repaint(); // update the drawn gallows
        }
    }
    
    // reset the gallows to blank
    public void reset() {
    	this.stages = 0;
    	repaint();
    }
}
