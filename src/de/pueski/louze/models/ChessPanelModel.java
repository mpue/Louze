package de.pueski.louze.models;

import java.awt.Color;
import java.util.ArrayList;

import de.pueski.louze.LouzeUtil;
import de.pueski.louze.app.Louze;
import de.pueski.louze.components.GNUChessMovementNotifier;
import de.pueski.louze.components.GameState;
import de.pueski.louze.components.MovementNotifier;
import de.pueski.louze.objects.Board;
import de.pueski.louze.objects.Piece;
import de.pueski.louze.objects.PieceType;

public class ChessPanelModel {

	private final Piece[][] board;
	private ArrayList<Piece> pieces;
	private Color currentPlayer;
	private Piece currentMovingPiece;
	private Piece selectedPiece;
	private GameState gameState;

	public ChessPanelModel() {

		board = new Piece[8][8];
		pieces = new ArrayList<Piece>();
		currentPlayer = Color.WHITE;
		gameState = GameState.RUNNING;

	}

	public void addPiece(Piece piece) {

		board[piece.getXLoc()][piece.getYLoc()] = piece;

		piece.setXLoc(piece.getXLoc() * Piece.WIDTH);
		piece.setYLoc(piece.getYLoc() * Piece.HEIGHT);

		pieces.add(piece);

	}

	public boolean movePiece(Piece piece, int oldX, int oldY, MovementNotifier moveNotifier) {
		
		int x = piece.getXLoc() / Piece.WIDTH;
		int y = piece.getYLoc() / Piece.HEIGHT;

		int _x = oldX / Piece.WIDTH;
		int _y = oldY / Piece.HEIGHT;

		System.out.println("movePiece : Moving "+piece.getType()+" from "+_x+","+_y+" to "+x+","+y);
		
		if (!LouzeUtil.isValidMove(piece, _x, _y, this))
			return false;

		// already an opponent piece there, get it!

		boolean canMove = false;

		Piece opponent = null;

		if (board[x][y] != null && !board[x][y].getColor().equals(piece.getColor())) {

			try {
				opponent = (Piece) board[x][y].clone();
			}
			catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}

			pieces.remove(board[x][y]);
		}

		board[_x][_y] = null;
		board[x][y] = piece;

		gameState = LouzeUtil.checkGameState(this, piece);

		if (gameState.equals(GameState.CHECK))
			canMove = false;
		else
			canMove = true;

		if (canMove) {

			
			StringBuffer move = new StringBuffer();
			
			move.append(Board.modelToBoardLetter.get(oldX/Piece.WIDTH));
			move.append(Board.modelToBoardValue.get(oldY/Piece.HEIGHT));
			move.append(Board.modelToBoardLetter.get(x));
			move.append(Board.modelToBoardValue.get(y));
			
			if (currentPlayer.equals(Color.WHITE)) 
				moveNotifier.moved(move.toString().toLowerCase());

			if (currentPlayer.equals(Color.WHITE))
				currentPlayer = Color.BLACK;
			else
				currentPlayer = Color.WHITE;
			
			
		}
		else {
			board[_x][_y] = piece;
			board[x][y] = opponent;
			pieces.add(opponent);
		}
		
		return canMove;

	}

	/**
	 * @return the pieces
	 */
	public ArrayList<Piece> getPieces() {
		return pieces;
	}

	/**
	 * @param pieces the pieces to set
	 */
	public void setPieces(ArrayList<Piece> pieces) {
		this.pieces = pieces;
	}

	public void makeMove(String move) {

		move = move.toUpperCase();
		
		String sourceValue = move.substring(0, 2);
		String destValue = move.substring(2);
		
		String sourceX = sourceValue.substring(0,1);		
		String sourceY = sourceValue.substring(1,2);
		
		String destX = destValue.substring(0,1);
		String destY = destValue.substring(1,2);
		
		int xs = Board.boardMap.get(sourceX);
		int ys = Board.boardMap.get(sourceY);
		
		int xd = Board.boardMap.get(destX);
		int yd = Board.boardMap.get(destY);
		
		Piece p = null;
		
		for (Piece piece : pieces) {
			
			if ((piece.getXLoc() / Piece.WIDTH == xs) && (piece.getYLoc() / Piece.HEIGHT == ys)) {
				p = piece;
				p.setYLoc(yd * Piece.HEIGHT);
				p.setXLoc(xd * Piece.WIDTH);
				break;
			}
			
		}
		
		movePiece(p, xs*Piece.WIDTH, ys*Piece.HEIGHT,Louze.getInstance().getNotifier());
		
	}

	public void dumpBoard() {

		for (int by = 0; by < 8; by++) {

			for (int bx = 0; bx < 8; bx++) {

				if (board[bx][by] != null) {

					if (board[bx][by].getType().equals(PieceType.BISHOP)) {

						if (board[bx][by].getColor().equals(Color.WHITE)) {
							System.out.print("B");
						}
						else {
							System.out.print("b");
						}

					}
					else if (board[bx][by].getType().equals(PieceType.KING)) {

						if (board[bx][by].getColor().equals(Color.WHITE)) {
							System.out.print("K");
						}
						else {
							System.out.print("k");
						}

					}
					else if (board[bx][by].getType().equals(PieceType.KNIGHT)) {

						if (board[bx][by].getColor().equals(Color.WHITE)) {
							System.out.print("N");
						}
						else {
							System.out.print("n");
						}

					}
					else if (board[bx][by].getType().equals(PieceType.PAWN)) {

						if (board[bx][by].getColor().equals(Color.WHITE)) {
							System.out.print("P");
						}
						else {
							System.out.print("p");
						}

					}
					else if (board[bx][by].getType().equals(PieceType.QUEEN)) {

						if (board[bx][by].getColor().equals(Color.WHITE)) {
							System.out.print("Q");
						}
						else {
							System.out.print("q");
						}

					}
					else if (board[bx][by].getType().equals(PieceType.ROOK)) {

						if (board[bx][by].getColor().equals(Color.WHITE)) {
							System.out.print("R");
						}
						else {
							System.out.print("r");
						}

					}

					System.out.print(" ");
				}
				else {
					System.out.print("X ");
				}

			}

			System.out.println("");

		}

		System.out.println("");

	}

	/**
	 * @return the currentMovingPiece
	 */
	public Piece getCurrentMovingPiece() {
		return currentMovingPiece;
	}

	/**
	 * @param currentMovingPiece the currentMovingPiece to set
	 */
	public void setCurrentMovingPiece(Piece currentMovingPiece) {
		this.currentMovingPiece = currentMovingPiece;
	}

	/**
	 * @return the selectedPiece
	 */
	public Piece getSelectedPiece() {
		return selectedPiece;
	}

	/**
	 * @param selectedPiece the selectedPiece to set
	 */
	public void setSelectedPiece(Piece selectedPiece) {
		this.selectedPiece = selectedPiece;
	}

	/**
	 * @return the board
	 */
	public Piece[][] getBoard() {
		return board;
	}

	/**
	 * @return the currentPlayer
	 */
	public Color getCurrentPlayer() {
		return currentPlayer;
	}

	/**
	 * @param currentPlayer the currentPlayer to set
	 */
	public void setCurrentPlayer(Color currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	/**
	 * @return the gameState
	 */
	public GameState getGameState() {
		return gameState;
	}

	/**
	 * @param gameState the gameState to set
	 */
	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

}
