package de.pueski.louze.components;

/**
 * @author Matthias Pueski (08.07.2010)
 *
 */

public interface MovementNotifier {

	public void moved(String move);
	
	public String opponentMoved();
	
}
