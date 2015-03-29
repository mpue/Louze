package de.pueski.louze.objects;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class Pieces {

	private static final Log log = LogFactory.getLog(Pieces.class);
	
	public static final BufferedImage[][] images = new BufferedImage[2][PieceType.values().length];

	public static final String[] colors = { "white","black" }; 
	
	public static final int WHITE = 0;
	public static final int BLACK = 1;
	
	static {

		URL u = null;

		for (int i=0; i < 2; i++) {
			
			for (int j = 0; j < PieceType.values().length; j++) {

				try {					
					String name = "pieces/"+PieceType.values()[j].toString().toLowerCase() +"_"+colors[i] + ".png";															
					u = Thread.currentThread().getContextClassLoader().getResource(name);
					log.info("Loading image "+u.toString());
					
					Image b = ImageIO.read(u).getScaledInstance(Piece.WIDTH, Piece.HEIGHT, Image.SCALE_SMOOTH);
					
					BufferedImage bdest = new BufferedImage(Piece.WIDTH, Piece.HEIGHT,BufferedImage.TYPE_INT_ARGB);
					
					Graphics2D g = bdest.createGraphics();
					g.drawImage(b,0,0,null);
					
					images[i][j] = bdest;

				}
				catch (IOException e) {
					e.printStackTrace();
				}

			}
			
		}
		

	}
	
}
