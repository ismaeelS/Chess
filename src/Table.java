import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.SwingUtilities;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import javafx.util.Pair;

/*
 * GUI made with help from this series: https://www.youtube.com/watch?v=w9HR4VJ8DAw
 */
public class Table {
	private final JFrame gameFrame;
	private final BoardPanel boardPanel;
	public final Board chessBoard;
	
	private Pieces sourceTile;
	private Pieces targetTile;
	private Pieces pieceToMove;
	
	private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(600, 600);
	private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(300, 450);
	private static final Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);
	
	private Color FIRST_TILE_COLOR = Color.decode("#FFFACD");
	private Color SECOND_TILE_COLOR = Color.decode("#593E1A");
	
	public Table() {
		this.gameFrame = new JFrame("JChess");
		this.gameFrame.setLayout(new BorderLayout());
		final JMenuBar tableMenuBar = createTableMenuBar();
		this.gameFrame.setJMenuBar(tableMenuBar);
		this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
		this.chessBoard = new Board();
		
		this.boardPanel = new BoardPanel();
		this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
		this.gameFrame.setVisible(true);
	}

	private JMenuBar createTableMenuBar() {
		final JMenuBar tableMenuBar = new JMenuBar();
		tableMenuBar.add(createFileMenu());
		return tableMenuBar;
	}

	private JMenu createFileMenu() {
		final JMenu fileMenu = new JMenu("File");

		final JMenuItem openPGN = new JMenuItem("Load PGN File");
		openPGN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("open pgn file lol");
			}
		});
		fileMenu.add(openPGN);
		
		final JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		fileMenu.add(exitMenuItem);
		
		return fileMenu;
	}
	
	private class BoardPanel extends JPanel {
		final List<TilePanel> boardTiles;  
		
		BoardPanel() {
			super(new GridLayout(8,8));
			this.boardTiles = new ArrayList<TilePanel>();
			
			int idCounter = 0;
			for (int i = 0; i < Board.NUMTILES; i++) {
				for (int j = 0; j < Board.NUMTILES; j++) {
					final TilePanel tilePanel = new TilePanel(this, idCounter++);
					this.boardTiles.add(tilePanel);
					add(tilePanel);
				}
			}
			setPreferredSize(BOARD_PANEL_DIMENSION);
			validate();
		}
		
		public void drawBoard(final Board board) {
			removeAll();
			
			for (final TilePanel currentTile : boardTiles) {
				currentTile.drawTile(board);
				add(currentTile);
			}
			validate();
			repaint();
		}
	}
	
	private class TilePanel extends JPanel {
		private final int tileId;
		
		TilePanel(final BoardPanel boardPanel, final int tileId) {
			super(new GridBagLayout());
			this.tileId = tileId;
			setPreferredSize(TILE_PANEL_DIMENSION);
			assignTileColor();
			assignTileIcon(chessBoard);
			
			addMouseListener(new MouseListener() {
				@Override
				public void mouseClicked(MouseEvent m) {
					if (SwingUtilities.isRightMouseButton(m) || m.isControlDown()) { //figure out how to call w/o module
						sourceTile = null;
						targetTile = null;
						pieceToMove = null;
					}
					else if (SwingUtilities.isLeftMouseButton(m)) {
						if (sourceTile == null) {
							//hypothetical first move
							sourceTile = chessBoard.getPieceAtXY(tileId); //null if click on empty tile
							pieceToMove = sourceTile;
							if (pieceToMove == null) {
								sourceTile = null;
							}
							else {
								System.out.println("Selected "+chessBoard.getPieceAtXY(tileId).getName());
							}
						}
						else {
							//hypothetical second move
							targetTile = chessBoard.getPieceAtXY(tileId);
							if (sourceTile == targetTile) {
								System.out.println("Clicked the same piece. Unselected "+chessBoard.getPieceAtXY(tileId).getName());
								sourceTile = null;
								targetTile = null;
								pieceToMove = null;
								return;
							}
							boolean sourceMoved = false;
							if (targetTile == null) { //if target empty, see if can move then move
								sourceMoved =  sourceTile.canMove(chessBoard, tileId, true);
							}
							else {
								sourceMoved = sourceTile.canMove(chessBoard, targetTile, true);
							}
							if (sourceMoved) {
//								chessBoard.printBoard();
								//piece has moved to target
							}
							else {
//								System.out.println("This piece cannot move");
							}
							sourceTile = null;
							targetTile = null;
							pieceToMove = null;
						}
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								boardPanel.drawBoard(chessBoard);
							}
						});
					}
				}

				@Override
				public void mouseEntered(MouseEvent m) {
					
				}

				@Override
				public void mouseExited(MouseEvent m) {
					
				}

				@Override
				public void mousePressed(MouseEvent m) {
					
				}

				@Override
				public void mouseReleased(MouseEvent m) {
					
				}
			});
			validate();
		}
		
		public void drawTile(final Board board) {
			assignTileColor();
			assignTileIcon(board);
			validate();
			repaint();
		}
		
		private void assignTileIcon(Board board) {
			this.removeAll();
			Pieces currentPiece = board.getPieceAtXY(this.tileId);
			if (currentPiece != null) {
				try {
					String filePath = "src/PieceIcons/";
					if (currentPiece.getColor()) {
						filePath+="W";
					}
					else {
						filePath+="B";
					}
					filePath += currentPiece.getName()+".gif";
					
					final BufferedImage image = ImageIO.read(new File(filePath));
					add(new JLabel(new ImageIcon(image)));
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private void assignTileColor() {
			if (this.tileId % 2 == 0) {
				setBackground(FIRST_TILE_COLOR);
			}
			else {
				setBackground(SECOND_TILE_COLOR);
			}
			if ((this.tileId+1) % 8 == 0) {
				Color temp = FIRST_TILE_COLOR;
				FIRST_TILE_COLOR = SECOND_TILE_COLOR;
				SECOND_TILE_COLOR = temp;
			}
		}
	}
	
	
}
