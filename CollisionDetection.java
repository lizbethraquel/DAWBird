package dawbird;

import java.awt.Rectangle;
import javax.swing.JLabel;

public class CollisionDetection {
	private JLabel lblBird;
	private JLabel lblColumnUp;
	private JLabel lblColumnDown;
	private int gameHeight;

	public CollisionDetection(JLabel lblBird, JLabel lblColumnUp, JLabel lblColumnDown, int gameHeight) {
		this.lblBird = lblBird;
		this.lblColumnUp = lblColumnUp;
		this.lblColumnDown = lblColumnDown;
		this.gameHeight = gameHeight;
	}

	public boolean checkCollisions() {

		Rectangle birdRect = new Rectangle(lblBird.getX(), lblBird.getY(), lblBird.getWidth(), lblBird.getHeight());
		Rectangle columnUpRect = new Rectangle(lblColumnUp.getX(), lblColumnUp.getY(), lblColumnUp.getWidth(),
				lblColumnUp.getHeight());
		Rectangle columnDownRect = new Rectangle(lblColumnDown.getX(), lblColumnDown.getY(), lblColumnDown.getWidth(),
				lblColumnDown.getHeight());

		if (birdRect.intersects(columnUpRect) || birdRect.intersects(columnDownRect)) {
			return true; 
		}

		if (lblBird.getY() <= 0 || lblBird.getY() >= gameHeight - lblBird.getHeight()) {
			return true;
		}

		return false; 
	}
}