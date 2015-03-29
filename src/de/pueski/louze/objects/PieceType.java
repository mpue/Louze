package de.pueski.louze.objects;


/**
 * The <code>PieceType</code> defines all available pieces 
 * and their values in a chess game.
 * 
 * <p>
 * The values for the pieces are
 * <ul>
 * 	<li>pawn   = 1</li>
 *  <li>knight = 2</li>
 *  <li>king   = 3</li>
 * 	<li>bishop = 5</li>
 *  <li>rook   = 6</li>
 *  <li>queen  = 7</li>
 * </ul>
 * </p>
 * <p>
 * The advantage of this is that if you AND the piece value with 4, and get a non-zero result, 
 * you know that is a sliding piece. If the result of the AND is zero, this is not a sliding piece. 
 * Next, if this is a sliding piece and you AND the piece value with a 1, and the result is non-zero, 
 * you have a piece that can slide diagonally if you AND the piece value with a 2 and the result is 
 * non-zero, you have a piece that slides vertically/horizontally like a rook. Note that the queen
 * has both of these bits set and that it can slide in all 8 directions. This may or may not be 
 * useful in your program, but since it still only requires 3 bits to represent a piece, 
 * there is nothing to lose by using this.
 * </p>
 * <p>
 * taken from <a href="http://www.cis.uab.edu/hyatt/boardrep.html">http://www.cis.uab.edu/hyatt/boardrep.html</a>
 * </p>
 * @author Matthias Pueski (06.07.2010)
 *
 */

public enum PieceType {

	PAWN(1),
	KNIGHT(2),
	KING(3),
	BISHOP(5),
	ROOK(6),
	QUEEN(7);
	
	private int value;

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	private PieceType(int value){
		this.value = value;
	}
	
}
