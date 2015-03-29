package de.pueski.louze.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.pueski.louze.components.ChessPanel;
import de.pueski.louze.components.GNUChessMovementNotifier;
import de.pueski.louze.models.ChessPanelModel;
import de.pueski.louze.objects.Board;
import de.pueski.louze.objects.Piece;
import de.pueski.louze.objects.PieceType;


public class Louze {
	
	private static final Log log = LogFactory.getLog(Louze.class);
	
	private JFrame frame;
	
	private ChessPanelModel model;		
	private ChessPanel chessPanel;
	private GNUChessMovementNotifier notifier;

	
	/**
	 * @return the notifier
	 */
	public GNUChessMovementNotifier getNotifier() {
		return notifier;
	}



	private static Louze INSTANCE;
	
	protected Louze() {
		
		frame = new JFrame();		
		frame.setLayout(new BorderLayout());		
		frame.setTitle("Louze V0.01");
		
		model = new ChessPanelModel();		
		chessPanel = new ChessPanel(model);
		notifier = new GNUChessMovementNotifier();
		
		populateBoard(model);
		
		URL u = Thread.currentThread().getContextClassLoader().getResource("board.png");
		
		try {
			
			Image b = ImageIO.read(u).getScaledInstance(ChessPanel.EDITOR_WIDTH, ChessPanel.EDITOR_HEIGHT, Image.SCALE_SMOOTH);
			
			BufferedImage bdest = new BufferedImage(ChessPanel.EDITOR_WIDTH, ChessPanel.EDITOR_WIDTH,BufferedImage.TYPE_INT_ARGB);			
			Graphics2D g = bdest.createGraphics();
			g.drawImage(b,0,0,null);
			
			chessPanel.setBackgroundImage(bdest);
			
		}
		catch (IOException e1) {
			log.info("Could not load background image.");
		}
		
		frame.setSize(new Dimension(410	,430));
		frame.add(chessPanel, BorderLayout.CENTER);
		
		
//		JConsole console = new JConsole();
//		Interpreter interpreter = new Interpreter(console);
//		new Thread(interpreter).start(); 
//		
//		JFrame consoleFrame = new JFrame();
//		consoleFrame.setLayout(new BorderLayout());
//		consoleFrame.setSize(new Dimension(410	,200));
//		consoleFrame.add(console, BorderLayout.CENTER);
//		consoleFrame.setVisible(true);
//		
//		consoleFrame.setLocation((int)frame.getLocation().getX()+frame.getWidth(), (int)frame.getLocation().getY());
		
		frame.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {			
				System.exit(0);
			}
			
		});
		frame.setLocationRelativeTo(frame.getRootPane());		
		frame.setVisible(true);		
		frame.setResizable(false);
		
		model.dumpBoard();
		
		
	}


	private void populateBoard(ChessPanelModel model) {
		
		for (int i = 0; i < 8;i++) {
			model.addPiece(new Piece(i,Board.TWO,PieceType.PAWN,Color.WHITE));	
		}
		
		model.addPiece(new Piece(Board.A,Board.ONE,PieceType.ROOK,Color.WHITE));
		model.addPiece(new Piece(Board.B,Board.ONE,PieceType.KNIGHT,Color.WHITE));
		model.addPiece(new Piece(Board.C,Board.ONE,PieceType.BISHOP,Color.WHITE));		
		model.addPiece(new Piece(Board.D,Board.ONE,PieceType.KING,Color.WHITE));
		model.addPiece(new Piece(Board.E,Board.ONE,PieceType.QUEEN,Color.WHITE));
		model.addPiece(new Piece(Board.F,Board.ONE,PieceType.BISHOP,Color.WHITE));
		model.addPiece(new Piece(Board.G,Board.ONE,PieceType.KNIGHT,Color.WHITE));
		model.addPiece(new Piece(Board.H,Board.ONE,PieceType.ROOK,Color.WHITE));
		
		for (int i = 0; i < 8;i++) {
			model.addPiece(new Piece(i,Board.SEVEN,PieceType.PAWN,Color.BLACK));	
		}
		
		model.addPiece(new Piece(Board.A,Board.EIGHT,PieceType.ROOK,Color.BLACK));
		model.addPiece(new Piece(Board.B,Board.EIGHT,PieceType.KNIGHT,Color.BLACK));
		model.addPiece(new Piece(Board.C,Board.EIGHT,PieceType.BISHOP,Color.BLACK));
		model.addPiece(new Piece(Board.D,Board.EIGHT,PieceType.KING,Color.BLACK));
		model.addPiece(new Piece(Board.E,Board.EIGHT,PieceType.QUEEN,Color.BLACK));
		model.addPiece(new Piece(Board.F,Board.EIGHT,PieceType.BISHOP,Color.BLACK));
		model.addPiece(new Piece(Board.G,Board.EIGHT,PieceType.KNIGHT,Color.BLACK));
		model.addPiece(new Piece(Board.H,Board.EIGHT,PieceType.ROOK,Color.BLACK));

		
	}
	
	public static Louze getInstance() {
		if (INSTANCE == null)
			INSTANCE = new Louze();
		return INSTANCE;
	}
	
	
	public static void main(String[] args) {
		
		Louze.getInstance();
		
	}
	
	/**
	 * @return the model
	 */
	public ChessPanelModel getModel() {
		return model;
	}


	
	/**
	 * @return the chessPanel
	 */
	public ChessPanel getChessPanel() {
		return chessPanel;
	}

}
