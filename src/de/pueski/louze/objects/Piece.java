package de.pueski.louze.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Piece {

	public static final int HEIGHT = 50;
	public static final int WIDTH = 50;

	private int xLoc;
	private int yLoc;
	
	private int index;
	
	private Color  color;	
	private PieceType type;

	public Piece(int xLoc, int yLoc, PieceType type, Color color) {
		this.xLoc = xLoc;
		this.yLoc = yLoc;
		this.color = color;
		this.type = type;
	}

	/**
	 * @return the xLoc
	 */
	public int getXLoc() {
		return xLoc;
	}

	/**
	 * @param loc the xLoc to set
	 */
	public void setXLoc(int loc) {
		xLoc = loc;
	}

	/**
	 * @return the yLoc
	 */
	public int getYLoc() {
		return yLoc;
	}

	/**
	 * @param loc the yLoc to set
	 */
	public void setYLoc(int loc) {
		yLoc = loc;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {

		Piece p = new Piece(xLoc, yLoc,type,color);
		return p;

	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	
	/**
	 * @param color the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	

	/**
	 * @return the type
	 */
	public PieceType getType() {
		return type;
	}

	
	/**
	 * @param type the type to set
	 */
	public void setType(PieceType type) {
		this.type = type;
	}
	
	
	/**
	 * Checks if a given point with the coordinates (x,y) is inside the bounding rectangle of this
	 * piece
	 * 
	 * @param x the x-coordinate
	 * @param y the y-coordinate
	 * @return
	 */
	public boolean isInside(int x, int y) {
		return x >= getXLoc() && x <= getXLoc() + WIDTH && y >= getYLoc() && y <= getYLoc() + HEIGHT ? true : false;
	}

	public void draw(int x, int y, Graphics g) {

		Graphics2D g2 = (Graphics2D) g;
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		g2.setRenderingHints(rh);
		g2.setColor(Color.WHITE);
		
		if (color.equals(Color.WHITE))		
			g2.drawImage(Pieces.images[Pieces.WHITE][type.ordinal()], x, y, null);
		else
			g2.drawImage(Pieces.images[Pieces.BLACK][type.ordinal()], x, y, null);
	}
	
	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
		    return true;
		}
		if ((obj != null) && (obj.getClass().equals(this.getClass()))) {
			
			Piece piece = (Piece) obj;		
			
			if (piece == null)
				return false;

			if (piece.getXLoc() == xLoc &&
				piece.getYLoc() == yLoc &&
				piece.getColor().equals(color) &&
				piece.getType().equals(type))
				return true;
			
		}
		
		return false;
		
	}	
	
}
