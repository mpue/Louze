/**

	Weberknecht CMSManager - Open Source Content Management
	Written and maintained by Matthias Pueski 
	
	Copyright (c) 2009 Matthias Pueski
	
	This program is free software; you can redistribute it and/or
	modify it under the terms of the GNU General Public License
	as published by the Free Software Foundation; either version 2
	of the License, or (at your option) any later version.
	
	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with this program; if not, write to the Free Software
	Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

*/
package de.pueski.louze;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;

import de.pueski.louze.components.GameState;
import de.pueski.louze.models.ChessPanelModel;
import de.pueski.louze.objects.Board;
import de.pueski.louze.objects.MovementType;
import de.pueski.louze.objects.Piece;
import de.pueski.louze.objects.PieceType;



public class LouzeUtil {

	/**
	 * Checks if one rectangle collides another diregarding their rotation,
	 * in fact we assume, that they both have a rotation of 0.
	 * 
	 * @param r1 
	 * @param r2
	 * 
	 * @return true if the two guys collide
	 */
	public static boolean collides(Rectangle r1, Rectangle r2) {
		
		if ((r1.getY() +  r1.getHeight()  >= r2.getY() &&           // r1 bottom collides top of the r2
			 r1.getY() <  r2.getY() && 	
			 r1.getX() +  r1.getWidth()  >= r2.getX() && // r1 is between the left and the right 
			 r1.getX() <= r2.getX()+  r2.getWidth()) ) {
			 return true;
		}
		else if ((r1.getY() <= r2.getY() +  r2.getHeight()    && // r1 top collides bottom of the r2
			 r1.getY() +  r1.getHeight() > r2.getY() && 	
			 r1.getX() +  r1.getWidth()   >= r2.getX() && // r1 is between the left and the right 
			 r1.getX() <= r2.getX() +  r2.getWidth())) {
			return true;	
		}
		else if ((r1.getX() +  r1.getWidth()  >= r2.getX() && // r1 collides left side of the r2
			r1.getX() <  r2.getX() && 	
			r1.getY() +  r1.getHeight()  >= r2.getY() && // r1 is between the top and bottom of the r2 
			r1.getY() <= r2.getY()+  r2.getHeight()) ) {
			return true;
		}
		else if ((r1.getX() <= r2.getX() +  r2.getWidth()    && // r1 collides right side of the r2
			r1.getX() +  r1.getWidth() > r2.getX() && 	
			r1.getY() +  r1.getHeight()   >= r2.getY() && // r1 is between the top and bottom of the r2  
			r1.getY() <= r2.getY() +  r2.getHeight())) {
			return true;	
		}
		
		return false;
		
	}
	
	public static boolean isValidMove(Piece piece, int x , int y, ChessPanelModel m) {
		
		int newX = piece.getXLoc();
		int newY = piece.getYLoc();
		
		piece.setXLoc(x*Piece.WIDTH);
		piece.setYLoc(y*Piece.HEIGHT);
		
		ArrayList<int[]> moves = getValidMovesFor(piece, m);
		
		piece.setXLoc(newX);
		piece.setYLoc(newY);
		
		for (int[] move : moves) {
			
			if ((move[0] == newX/Piece.WIDTH) && (move[1] == newY/Piece.HEIGHT)) {
				return true;
			}
		}
		
		return false;
		
	}
	
	/**
	 * Gives an array of all valid moves of a given piece based on a model.
	 * 
	 * @param piece the piece to get the moves for
	 * @param m the model of the chess board
	 * @return a list of integer pairs indicating possible positions
	 */
	public static ArrayList<int[]> getValidMovesFor(Piece piece, ChessPanelModel m) {
		
		ArrayList<int[]> validMoves = new ArrayList<int[]>();
		
		Piece[][] board = m.getBoard();
		
		int x = piece.getXLoc() / Piece.WIDTH;
		int y = piece.getYLoc() / Piece.HEIGHT;
		
		if (piece.getType().equals(PieceType.BISHOP)) {
			getMovesForSlidingDiagonally(piece, validMoves, board, x, y);
		}
		else if (piece.getType().equals(PieceType.KING)) {
			getMovesForKing(piece, validMoves, board, x, y);
		}
		else if (piece.getType().equals(PieceType.PAWN)) {			
			getMovesForPawn(piece, validMoves, board, x, y);
		}
		else if (piece.getType().equals(PieceType.ROOK)) {
			getMovesForSlidingHorizontallyVertically(piece, validMoves, board, x, y);
		}
		else if (piece.getType().equals(PieceType.QUEEN)) {
			getMovesForSlidingDiagonally(piece, validMoves, board, x, y);
			getMovesForSlidingHorizontallyVertically(piece, validMoves, board, x, y);
		}
		else if (piece.getType().equals(PieceType.KNIGHT)) {
			getMovesForKnight(piece, validMoves, board, x, y);
		}
		
		return validMoves;
		
	}

	private static void getMovesForSlidingHorizontallyVertically(Piece piece, ArrayList<int[]> validMoves, Piece[][] board, int x, int y) {
		
		Color ownColor;

		if (piece.getColor().equals(Color.WHITE)) {
			ownColor = Color.WHITE;
		}
		else {
			ownColor = Color.BLACK;
		}
		
		// left
		
		int i = y;
		int j = x-1;

		if (j > 0 && (board[j][i] != null) && !board[j][i].getColor().equals(ownColor))
			validMoves.add(new int[] { j, i });
		
		while (j >= 0 && board[j][i] == null) {
			
			if (j > 0 && board[j-1][i] != null && !board[j-1][i].getColor().equals(ownColor)) {
				validMoves.add(new int[] { j, i });
				validMoves.add(new int[] { j-1, i });
				break;
			}
			
			validMoves.add(new int[] { j, i });
			j--;
			
		}
		
		// right

		i = y;
		j = x+1;

		if (j < 8 && (board[j][i] != null) && !board[j][i].getColor().equals(ownColor))
			validMoves.add(new int[] { j, i });
		
		while (j < 8 && board[j][i] == null) {
			
			if (j < 7 && board[j+1][i] != null && !board[j+1][i].getColor().equals(ownColor)) {
				validMoves.add(new int[] { j, i });
				validMoves.add(new int[] { j+1, i });
				break;
			}
			
			validMoves.add(new int[] { j, i });
			j++;
		}

		// top

		i = y - 1;
		j = x;

		
		if (i > 0 && (board[j][i] != null) && !board[j][i].getColor().equals(ownColor))
			validMoves.add(new int[] { j, i });
		
		while (i >= 0 && board[j][i] == null) {
			
			if (i > 0 && board[j][i-1] != null && !board[j][i-1].getColor().equals(ownColor)) {
				validMoves.add(new int[] { j, i });
				validMoves.add(new int[] { j, i-1 });
				break;
			}
			
			validMoves.add(new int[] { j, i });
			i--;
			
		}
		
		// bottom

		i = y + 1;
		j = x;
		
		if (i < 8 && (board[j][i] != null) && !board[j][i].getColor().equals(ownColor))
			validMoves.add(new int[] { j, i });
		
		while (i < 8 && board[j][i] == null) {
			
			if (i < 7 && board[j][i+1] != null && !board[j][i+1].getColor().equals(ownColor)) {
				validMoves.add(new int[] { j, i });
				validMoves.add(new int[] { j, i+1 });
				break;
			}
			
			validMoves.add(new int[] { j, i });
			i++;
			
		}		

	}

	private static void getMovesForSlidingDiagonally(Piece piece, ArrayList<int[]> validMoves, Piece[][] board, int x, int y) {

		Color ownColor;

		if (piece.getColor().equals(Color.WHITE)) {
			ownColor = Color.WHITE;
		}
		else {
			ownColor = Color.BLACK;
		}
		
		/**
		 * Diagonally top-left
		 */
		
		int i = y-1;
		int j = x-1;

		if (i > 0 && j > 0 && (board[j][i] != null) && !board[j][i].getColor().equals(ownColor))
			validMoves.add(new int[] { j, i });
		
		while (j >= 0 && i >= 0 && (board[j][i] == null)) {
			
			if (j > 0 && i > 0 &&  board[j-1][i-1] != null && !board[j-1][i-1].getColor().equals(ownColor)) {
				validMoves.add(new int[] { j, i });
				validMoves.add(new int[] { j-1, i-1 });
				break;
			}
			
			validMoves.add(new int[] { j, i });

			i--;
			j--;
			
		}

		/**
		 * Diagonally bottom right
		 */
		
		i = y+1;
		j = x+1;
		
		if (i < 7 && j < 7 && (board[j][i] != null) && !board[j][i].getColor().equals(ownColor))
			validMoves.add(new int[] { j, i });

		while (j < 8 && i < 8 && (board[j][i] == null)) {
			
			if (j < 7 && i < 7 &&  board[j+1][i+1] != null && !board[j+1][i+1].getColor().equals(ownColor)) {
				validMoves.add(new int[] { j, i });
				validMoves.add(new int[] { j+1, i+1 });
				break;
			}
			
			validMoves.add(new int[] { j, i });

			i++;
			j++;
			
		}
		
		/**
		 * Diagonally top-right
		 */

		i = y-1;
		j = x+1;

		if (i > 0 && j < 7 && (board[j][i] != null) && !board[j][i].getColor().equals(ownColor))
			validMoves.add(new int[] { j, i });
		
		while (j < 8 && i >= 0 && (board[j][i] == null)) {
			
			if (j < 7 && i > 0 &&  board[j+1][i-1] != null && !board[j+1][i-1].getColor().equals(ownColor)) {
				validMoves.add(new int[] { j, i });
				validMoves.add(new int[] { j+1, i-1 });
				break;
			}
			
			validMoves.add(new int[] { j, i });

			i--;
			j++;
			
		}

		/**
		 * Diagonally bottom-left
		 */

		i = y+1;
		j = x-1;

		if (i < 7 && j > 0 && (board[j][i] != null) && !board[j][i].getColor().equals(ownColor))
			validMoves.add(new int[] { j, i });
		
		while (j >= 0 && i < 8 && (board[j][i] == null)) {

			if (j > 0 && i < 7 &&  board[j-1][i+1] != null && !board[j-1][i+1].getColor().equals(ownColor)) {
				validMoves.add(new int[] { j, i });
				validMoves.add(new int[] { j-1, i+1 });
				break;
			}
			
			validMoves.add(new int[] { j, i });

			i++;
			j--;
			
		}
		
		
	}

	private static void getMovesForKnight(Piece piece, ArrayList<int[]> validMoves, Piece[][] board, int x, int y) {

		Color ownColor;

		if (piece.getColor().equals(Color.WHITE)) {
			ownColor = Color.WHITE;
		}
		else {
			ownColor = Color.BLACK;
		}
		
		for (int i = 0; i < 8; i++) {

			for (int j = 0; j < 8; j++) {
				
				if (((j == x - 1 && i == y - 2) ||
					 (j == x - 1 && i == y + 2) ||
					 (j == x + 1 && i == y - 2) ||
					 (j == x + 1 && i == y + 2) ||
					 (j == x - 2 && i == y - 1) ||
					 (j == x - 2 && i == y + 1) ||
					 (j == x + 2 && i == y - 1) ||
					 (j == x + 2 && i == y + 1) 
 
					 ) && (board[j][i] == null || !board[j][i].getColor().equals(ownColor)))
					validMoves.add(new int[] { j, i });
			}
		
		}
		
		
	}

	private static void getMovesForKing(Piece piece, ArrayList<int[]> validMoves, Piece[][] board, int x, int y) {

		Color ownColor;

		if (piece.getColor().equals(Color.WHITE)) {
			ownColor = Color.WHITE;
		}
		else {
			ownColor = Color.BLACK;
		}
		
		for (int i = 0; i < 8; i++) {

			for (int j = 0; j < 8; j++) {
				
				/**
				 * The king can move one step in any direction, but only if the
				 * target field is empty or occupied by an opposite piece
				 */
				
				// Diagonal

				if ((j == x - 1 || j == x + 1) && (i == y-1 || i == y + 1) && (board[j][i] == null || !board[j][i].getColor().equals(ownColor)))
					validMoves.add(new int[] { j, i });
				
				// up and down
				
				if (j == x && (i == y+1 || i == y-1) && (board[j][i] == null || !board[j][i].getColor().equals(ownColor)))
					validMoves.add(new int[] { j, i });
				
				// left and right
				
				if (i == y &&(j == x - 1 || j == x + 1) && (board[j][i] == null || !board[j][i].getColor().equals(ownColor)))
					validMoves.add(new int[] { j, i });
				
			}
			
		}
		
	}

	private static void getMovesForPawn(Piece piece, ArrayList<int[]> validMoves, Piece[][] board, int x, int y) {

		int direction;
		int startRow;

		Color ownColor;

		if (piece.getColor().equals(Color.WHITE)) {
			direction = -1;
			ownColor = Color.WHITE;
			startRow = Board.B;
		}
		else {
			direction = 1;
			ownColor = Color.BLACK;
			startRow = Board.G;
		}

		/**
		 * Examine row above, since a pawn can only move one step ahead. The
		 * only exception occurs, if the pawn is located at its original
		 * position, then he can move two steps, but only if theres no other
		 * piece in between.
		 * 
		 */

		for (int i = 0; i < 8; i++) {

			for (int j = 0; j < 8; j++) {

				if (i == y + direction) {

					/**
					 * diagonally left one step.
					 * 
					 * the pawn is only allowed to step diagonally if he can
					 * beat an opponent piece
					 */

					if (j >= 0 && j == x - 1 && board[j][i] != null && !board[j][i].getColor().equals(ownColor)) {
						validMoves.add(new int[] { j, i });
					}
					/**
					 * diagonally right one step.
					 * 
					 * the pawn is only allowed to step diagonally if he can
					 * beat an opponent piece
					 */

					if (j <= 8 && j == x + 1 && board[j][i] != null && !board[j][i].getColor().equals(ownColor)) {
						validMoves.add(new int[] { j, i });
					}

					/**
					 * The pawn can take two steps if he is in start position
					 * and no opponent piece stands right in front of him
					 */

					if ((j == x && startRow == y) && board[j][i + direction] == null && board[j][i] == null) {
						validMoves.add(new int[] { j, i + direction });
					}

					/**
					 * The standard action, the pawn takes one step ahead
					 */

					if ((j == x && y * (-direction) <= startRow * (-direction)) && board[j][i] == null) {
						validMoves.add(new int[] { j, i });
					}

				}

			}

		}
	}
	
	public static MovementType getMovementType(PieceType type) {
		
		int value = type.getValue();
		
		if ((value & 4) != 0) {
			
			if      ((value & 1) != 0 && (value & 2) == 0) {
				return MovementType.SLIDING_DIAGONALLY;
			}
			else if ((value & 1) == 0 && (value & 2) != 0)
				return MovementType.SLIDING_HORIZONTAL_VERTICAL;
			else if ((value & 1) != 0 && (value & 2) != 0)
				return MovementType.QUEEN;
			
			return MovementType.SLIDING;	
			
		}
		
		else return MovementType.SIMPLE;
		
		
	}

	public static GameState checkGameState(ChessPanelModel model, Piece piece) {

		Color ownColor = model.getCurrentPlayer();

		Piece king = null;
		
		for (Piece p : model.getPieces()) {
			if (p.getColor().equals(ownColor) && p.getType().equals(PieceType.KING)) {
				king = p;
				break;
			}
		}
			
		for (Piece otherPiece : model.getPieces()) {

			if (otherPiece.getColor().equals(ownColor))
				continue;
			
			ArrayList<int[]> validMoves = getValidMovesFor(otherPiece, model);
			
			for (int[] move : validMoves) {
				if (move[0] == king.getXLoc()/Piece.WIDTH && move[1] == king.getYLoc()/Piece.HEIGHT)
					return GameState.CHECK;
			}
			
		}
			
		
		return GameState.RUNNING;
	}
	
}
