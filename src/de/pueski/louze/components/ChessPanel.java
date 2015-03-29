package de.pueski.louze.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import de.pueski.louze.LouzeUtil;
import de.pueski.louze.app.Louze;
import de.pueski.louze.models.ChessPanelModel;
import de.pueski.louze.objects.Piece;

@SuppressWarnings("unused")
public class ChessPanel extends JPanel {

	private static final long	serialVersionUID	= -6034450048001248857L;

	public  static final int	EDITOR_WIDTH		= 400;
	public  static final int	EDITOR_HEIGHT		= 400;
	private static final int 	SELECTION_BORDER    = 1;

	private ChessPanelModel 	model;
	
	private final boolean[][] highlightedFields = new boolean[8][8];

	private int 				oldX;
	private int      			oldY;
	
	private int					currentX			= 0;
	private int					currentY			= 0;
	
	private int					offsetX				= 0;
	private int					offsetY				= 0;

	private int					xgrid				= Piece.WIDTH;
	private int					ygrid				= Piece.HEIGHT;

	boolean						snapToGrid			= true;
	boolean						gridVisible			= false;

	private boolean				button1Pressed		= false;
	private boolean				button2Pressed		= false;
	private boolean				button3Pressed		= false;

	private int					dragStartX;
	private int					dragStartY;

	private int					dragStopX;
	private int					dragStopY;
	
	private boolean 			dragging;

	boolean						refreshed			= false;

	private Image				backgroundImage;
	
	protected boolean			ctrlPressed			= false;

	private EditorState			state;	

	protected Rectangle			selectionBorder		= new Rectangle();
	protected Rectangle border = new Rectangle(0, 0, EDITOR_WIDTH, EDITOR_HEIGHT);
	

	public ChessPanel(ChessPanelModel model) {

		super();

		this.model = model;
		
		state = EditorState.NOTHING_SELECTED;

		Dimension size = new Dimension(EDITOR_WIDTH, EDITOR_HEIGHT);
		setBounds(new Rectangle(size));
		setPreferredSize(size);

		addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseDragged(MouseEvent e) {
				handleMouseDragged(e);
			}

		});

		addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				handleMousePressed(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				handleMouseReleased(e);
			}

		});
		
		hookContextMenu();
	}

	private void handleMouseDragged(MouseEvent e) {
		
		if (button1Pressed)
			dragging = true;
		
		if (model.getCurrentMovingPiece() != null) {
			
			int newX = e.getX() - offsetX - Piece.WIDTH/2;
			int newY = e.getY() - offsetY - Piece.HEIGHT/2;

			if (snapToGrid) {

				if (newX % xgrid >= 5)
					newX = newX + (xgrid - (newX % xgrid));

				if (newY % ygrid >= 5)
					newY = newY + (ygrid - (newY % ygrid));

			}

			if (newX + Piece.WIDTH > EDITOR_WIDTH)
				newX = EDITOR_WIDTH - Piece.WIDTH;
			else if (newX < 0)
				newX = 0;

			if (newY + Piece.HEIGHT > EDITOR_HEIGHT) {
				newY = EDITOR_HEIGHT - Piece.HEIGHT;
			}
			else if (newY < 0)
				newY = 0;

			
			model.getCurrentMovingPiece().setXLoc(newX);
			model.getCurrentMovingPiece().setYLoc(newY);
			

		}
		else {

			for (Piece piece : model.getPieces()) {

				if (e.getX() >= piece.getXLoc() && e.getX() <= piece.getXLoc() + Piece.WIDTH && e.getY() >= piece.getYLoc()
						&& e.getY() <= piece.getYLoc() + Piece.HEIGHT) {

					if (button1Pressed && model.getSelectedPiece() != null) {

						model.setCurrentMovingPiece(piece);

						currentX = e.getX();
						currentY = e.getY();

						offsetX = currentX - model.getCurrentMovingPiece().getXLoc();
						offsetY = currentY - model.getCurrentMovingPiece().getYLoc();

					}

				}

			}

		}

		invalidate();
		repaint();

	}

	private void handleMousePressed(MouseEvent e) {

		if (e.isPopupTrigger()) {
			handleContextClick(e);
		}

		if (e.getClickCount() == 2) {
			try {

			}
			catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		if (e.isControlDown()) {
			ctrlPressed = true;
		}
		else {
			ctrlPressed = false;
		}

		if (e.getButton() == 1) {

			button1Pressed = true;
			model.setSelectedPiece(null);
			
			for (Piece piece : model.getPieces()) {
				
				if (piece.isInside(e.getX(), e.getY())) {
					
					if (!piece.getColor().equals(model.getCurrentPlayer()))
						return;
					
					model.setSelectedPiece(piece);
					
					oldX = model.getSelectedPiece().getXLoc();
					oldY = model.getSelectedPiece().getYLoc();
					
					invalidate();
					repaint();

				}

			}

			if (model.getSelectedPiece() == null) {

				selectionBorder.setLocation(0, 0);
				selectionBorder.setSize(0, 0);
				
				dragStartX = e.getX();
				dragStartY = e.getY();

			}

		}
		else if (e.getButton() == 2) {
			button2Pressed = true;
		}
		else if (e.getButton() == 3) {
			button3Pressed = true;
		}

		invalidate();
		repaint();

	}

	private void handleMouseReleased(MouseEvent e) {

		if (model.getSelectedPiece() != null)
			state = EditorState.SINGLE_BRICK_SELECTED;
		else
			state = EditorState.NOTHING_SELECTED;

		if (e.isPopupTrigger()) {
			handleContextClick(e);
		}

		if (e.getButton() == 1) {
			
			if (dragging) {
				dragging = false;
				model.setSelectedPiece(null);
			}
			
			if (model.getCurrentMovingPiece() != null)
				if (!model.movePiece(model.getCurrentMovingPiece(),oldX,oldY,Louze.getInstance().getNotifier())) {
					model.getCurrentMovingPiece().setXLoc(oldX);
					model.getCurrentMovingPiece().setYLoc(oldY);
				}
			
			
			model.dumpBoard();
			
			button1Pressed = false;
			model.setCurrentMovingPiece(null);
		}
		else if (e.getButton() == 2) {
			button2Pressed = false;
		}
		else if (e.getButton() == 3) {
			button3Pressed = false;
		}

		invalidate();
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {

		g.clearRect(0, 0, EDITOR_HEIGHT+10, EDITOR_WIDTH+10);

		Graphics2D g2 = (Graphics2D) g;

		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2.setRenderingHints(rh);

		if (backgroundImage != null)
			g2.drawImage(backgroundImage, 0, 0, this);

		if (gridVisible)
			drawGrid(g);

		if (model.getPieces() != null) {

			for (Piece piece : model.getPieces()) {

				piece.draw(piece.getXLoc(), piece.getYLoc(),g);
			
			}

			if (model.getSelectedPiece() != null) {

				g2.setColor(Color.GREEN);

				Rectangle r = new Rectangle(model.getSelectedPiece().getXLoc() - SELECTION_BORDER / 2, model.getSelectedPiece().getYLoc()
						- SELECTION_BORDER / 2, Piece.WIDTH + SELECTION_BORDER, Piece.HEIGHT + SELECTION_BORDER);
				g2.draw(r);
				
				if (model.getSelectedPiece() != null && !dragging)				
					drawMovingHints(g2);
			}

		}

		g2.setColor(Color.BLACK);
		g2.draw(border);

	}

	private void drawMovingHints(Graphics2D g2) {
		
		clearHighlights();
		
		ArrayList<int[]> possibleMoves = LouzeUtil.getValidMovesFor(model.getSelectedPiece(), model); 
		
		for (int[] move : possibleMoves) {
			
			highlightedFields[move[0]][move[1]] = true;
			
		}
		
		Color c = new Color(255,0,0,128);
		
		g2.setColor(c);
		
		
		for (int x = 0; x < 8; x++) {
			
			for(int y = 0; y < 8; y++) {
			
				if (highlightedFields[x][y]) {
					
					g2.fillRect(x*Piece.WIDTH, y*Piece.HEIGHT,Piece.WIDTH,Piece.HEIGHT);
					
				}
				
			}
			
		}
		
	}

	private void clearHighlights() {
		
		for (int x = 0; x < 8; x++) {
			
			for(int y = 0; y < 8; y++) {
				
				highlightedFields[x][y] = false;
				
			}
		}
		
	}
	
	private void drawGrid(Graphics g) {

		g.setColor(Color.LIGHT_GRAY);

		for (int i = xgrid; i < EDITOR_WIDTH; i += xgrid)
			g.drawLine(i, 0, i, EDITOR_HEIGHT);

		for (int i = ygrid; i < EDITOR_HEIGHT; i += ygrid)
			g.drawLine(0, i, EDITOR_WIDTH, i);

	}

	private void hookContextMenu() {

	}

	private void handleContextClick(MouseEvent e) {

		if (e.isPopupTrigger()) {
			if (model.getSelectedPiece() != null) {
				// popupMenu.show((Component) e.getSource(), e.getX(), e.getY());
			}
		}

	}

	/**
	 * @return the backgroundImage
	 */
	public Image getBackgroundImage() {
	
		return backgroundImage;
	}

	/**
	 * @param backgroundImage the backgroundImage to set
	 */
	public void setBackgroundImage(Image backgroundImage) {

		this.backgroundImage = backgroundImage;
	}

}
