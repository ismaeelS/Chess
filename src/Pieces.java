import java.util.ArrayList;
import javafx.util.Pair;

public class Pieces {
	protected boolean color;
	protected String name;
	protected int x;
	protected int y;
	protected int id;
	protected boolean alive = true;
	protected boolean moved = false;
	
	public Pieces() {
	}

	public boolean getColor() {
		return this.color;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getId() {
		return this.id;
	}
	
	public boolean isAlive() {
		return this.alive;
	}
	
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	
	public boolean hasMoved() {
		return this.moved;
	}
	
	public void setMoved(boolean moved) {
		this.moved = moved;
	}
	
	/*
	 * prints the instance variables of the piece in the format:
	 * [color] [name] at ([x],[y]) with id: [id] and alive:[isalive?] and has moved:[hasmoved?]
	 */
	public void info() {
		if (color) {
			System.out.print("White ");
		}
		else {
			System.out.print("Black ");
		}
		System.out.println(name+" at ("+x+","+y+") with id: "+id+" and alive:"+alive+" and has moved:"+moved);
	}
	
	/*
	 * moves piece but checks but restricts movement based on whose turn it is
	 */
	boolean move(Board board, int x, int y) {
		if (board.checkmate) {
			System.out.println("The board is in checkmate.");
			return false;
		}
		
		if (board.currentPlayer && !this.getColor()) {
			System.out.println("It's white's turn but black is trying to move.");
			return false;
		}
		else if  (!board.currentPlayer && this.getColor()){
			System.out.println("It's black's turn but white is trying to move.");
		}
		
		boolean pieceHasMoved = false;
		if (board.currentPlayer == this.color) {
			pieceHasMoved = this.canMove(board, x, y, true);
			if (pieceHasMoved) {
				board.currentPlayer = !board.currentPlayer;
			}
			else {
				System.out.println("\tThis piece cannot move there.");
			}
		}
		else {
			System.out.println("\tNot proper color's turn");
			if (board.currentPlayer) {
				System.out.println("\tIt is white's turn");
			}
			else {
				System.out.println("\tIt is black's turn");
			}
		}
		return pieceHasMoved;
	}
	
	/*
	 * See if piece has the ability to move to selected space. If possible, go to movePieceSuccessfully()
	 * this method gets overridden by every piece
	 */
	boolean canMove(Board board, int x, int y, boolean movePiece) {
		System.out.println("gets to Board.canMove()");
		return true;
	}
	
	boolean canMove(Board board, Pieces target, boolean movePiece) {
		if (movePiece) {
			return this.move(board, target.getX(), target.getY());
		}
		return canMove(board, target.getX(), target.getY(), movePiece);
	}
	
	boolean canMove(Board board, int id, boolean movePiece) {
		int x = Board.idToXY(id).getKey();
		int y = Board.idToXY(id).getValue();
		
		if (movePiece) {
			return this.move(board, x, y);
		}
		return canMove(board, x, y, movePiece);
	}
	
	/*
	 * crude way to move a piece bypassing chess rules
	 */
	public void forceMove(Board board, int x, int y) {
		if (board.getPieceAtXY(x, y) != null) {
			board.takePiece(x,y);
		}
		
		board.assignPieceXY(this, x, y, this.getX(), this.getY());
	}
	
	/*
	 * returns an array list of coordinates if the piece is attacking the opposing king
	 * the opposing player need to block at least one of these to get out of check
	 * this method is overridden by every piece (even the king but unnecessarily)
	 */
	public ArrayList<Pair<Integer, Integer>> pointsOfAttack(Board board) {
		return null;
	}
	
	public void printPointsOfAttack(Board board) {
		ArrayList<Pair<Integer, Integer>> spotsToPrint = this.pointsOfAttack(board);
		if (spotsToPrint == null) {
			System.out.println("No spots of attack from "+this.getId());
		}
		else {
			for (Pair <Integer, Integer> boardSquare : spotsToPrint) {
				System.out.print("("+boardSquare.getKey()+","+boardSquare.getValue()+") ");
			}
			System.out.println();
		}
	}
	
	/*
	 * check if this piece can get their king out of check 
	 */
	public boolean canSaveFromCheck(Board board) {
		/*
		 *this for loop will actually only be run once per call since the instigators.size() > 2 check
		 *in Board.checkIfInCheckmate(), but I wrote this before I realized that. Keeping like this 
		 *because allows for more flexibility if I want to alter the rules of chess 
		 */
		
		for (Pieces attacker: board.instigators) {
			ArrayList<Pair<Integer, Integer>> instigatorSpots = attacker.pointsOfAttack(board);
			if (instigatorSpots == null) {
				continue;
			}
			for (Pair <Integer, Integer> boardSquare : instigatorSpots) {
				if (this.canMove(board, boardSquare.getKey(), boardSquare.getValue(), false)) {
					System.out.println(this.getId()+" can block "+attacker.getId()+" by moving to this space: ("+boardSquare.getKey()+","+boardSquare.getValue()+") ");
					return true;
				}
			}
		}
		return false;
	}

	//check if i can replace with Board.withinBounds()
	boolean checkProperBounds(Pieces piece, int x, int y) {
		boolean movesAndIsInbounds = ((piece.x != x) || (piece.y != y)) && ((x >= 0 && x < 8) && (y >= 0 && y < 8));
		return movesAndIsInbounds;
	}
	
	/*
	 * attempt to move a target without putting own king in check
	 * figure out if in check or checkmate
	 */
	public boolean movePieceWithCheckChecks(Board board, int targetX, int targetY, int curX, int curY, boolean movePiece) {
		Pieces currentPiece = this;
		
		if (Board.squares[targetX][targetY] == null) {
			if (movePiece) {
				board.assignPieceXY(currentPiece, targetX, targetY, curX, curY);
				
				boolean thisMovePutOwnColorInCheck = board.checkIfInCheck(currentPiece.getColor(), true);
				
				if (thisMovePutOwnColorInCheck) {
//					System.out.println("traitor");
					board.assignPieceXY(currentPiece, curX, curY, targetX, targetY);
					return false;
				}
				boolean putsEnemyInCheck = board.checkIfInCheck(!currentPiece.getColor(), false);
				
				if (putsEnemyInCheck) {
					board.declareCheck(currentPiece.getColor(), this.getName());
				}
				else {
					board.instigators.clear();
//					System.out.println("instigators cleared");
				}
				
				boolean putsEnemyInCheckMate = board.checkIfInCheckmate(board, !currentPiece.getColor(), false);
				if (putsEnemyInCheckMate) {
					board.gameOver(this.getColor());
				}
			}
			else {
				board.assignPieceXY(currentPiece, targetX, targetY, curX, curY);
				
				boolean thisMovePutOwnColorInCheck = board.checkIfInCheck(currentPiece.getColor(), true);
				board.assignPieceXY(currentPiece, curX, curY, targetX, targetY);
				if (thisMovePutOwnColorInCheck) {
//					System.out.println("hypothetical traitor");
					return false;
				}
			}
			return true;
		}
		else {
			boolean attack = !Board.squares[targetX][targetY].getColor() == (this.getColor());
			if (attack) {
				if (movePiece) {
					board.takePiece(targetX,targetY);
					board.assignPieceXY(currentPiece, targetX, targetY, curX, curY);
					
					boolean thisMovePutOwnColorInCheck = board.checkIfInCheck(currentPiece.getColor(), true);
					
					if (thisMovePutOwnColorInCheck) {
						undoLastCapture(board, targetX, targetY, curX, curY, currentPiece);
						return false;
					}
					
					boolean putsEnemyInCheck = board.checkIfInCheck(!currentPiece.getColor(), false);
					
					if (putsEnemyInCheck) {
						board.declareCheck(currentPiece.getColor(), this.getName());
					}
					else {
						board.instigators.clear();
//						System.out.println("instigators cleared");
					}
					
					boolean putsEnemyInCheckMate = board.checkIfInCheckmate(board, !currentPiece.getColor(), false);
					if (putsEnemyInCheckMate) {
						board.gameOver(this.getColor());
					}
				}
				else {
					board.takePiece(targetX,targetY);
					board.assignPieceXY(currentPiece, targetX, targetY, curX, curY);
					
					boolean thisMovePutOwnColorInCheck = board.checkIfInCheck(currentPiece.getColor(), true);
					undoLastCapture(board, targetX, targetY, curX, curY, currentPiece);
					if (thisMovePutOwnColorInCheck) {
						return false;
					}
				}
				return true;
			}
			return false;
		}
	}

	public void undoLastCapture(Board board, int targetX, int targetY, int curX, int curY, Pieces currentPiece) {
		board.assignPieceXY(currentPiece, curX, curY, targetX, targetY);
		Pieces lastTaken = board.takenPieces.get(board.takenPieces.size()-1);		
//		System.out.println("last died: "+lastTaken.getId()+" "+lastTaken.getX()+" "+lastTaken.getY());
		Board.squares[lastTaken.getX()][lastTaken.getY()] = lastTaken;
		lastTaken.alive = true;
		board.takenPieces.remove(board.takenPieces.size()-1);
	}
}
