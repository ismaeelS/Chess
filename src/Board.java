import java.util.ArrayList;
import javafx.util.Pair;

public class Board {
	boolean blackInCheck;
	boolean whiteInCheck;
	boolean checkmate = false;
	boolean currentPlayer = true; //true = white | false = black
	final static int NUMTILES = 8;
	final static Pieces[][] squares = new Pieces[NUMTILES][NUMTILES]; //the board
	final ArrayList<Pieces> piecesInfo = new ArrayList<Pieces>(); //list of each piece from top left to bottom right
	final ArrayList<Pieces> takenPieces = new ArrayList<Pieces>(); //pieces removed from the board
	final ArrayList<Pieces> instigators = new ArrayList<Pieces>(); //pieces that are currently causing a check
	
	boolean testing = false; //used in testing
	static int DRACULA = 0; //used in testing
	
	public Board() {
		int xIncrementer = 0;
		int yIncrementer = 0;
		int idIncrementer = 0;
		boolean playerColor = false;
		idIncrementer = initializeMainPieces(xIncrementer, yIncrementer, idIncrementer, playerColor);
		
		xIncrementer++;
		yIncrementer = 0;
		for (;yIncrementer < squares.length; yIncrementer++, idIncrementer++) {
			piecesInfo.add(new Pawn(playerColor, xIncrementer, yIncrementer, idIncrementer));
			squares[xIncrementer][yIncrementer] = piecesInfo.get(idIncrementer);
		}

		xIncrementer = 6;
		yIncrementer = 0;
		playerColor = true;
		for (;yIncrementer < squares.length; yIncrementer++, idIncrementer++) {
			piecesInfo.add(new Pawn(playerColor, xIncrementer, yIncrementer, idIncrementer));
			squares[xIncrementer][yIncrementer] = piecesInfo.get(idIncrementer);
		}
		
		xIncrementer++;
		yIncrementer = 0;
		idIncrementer = initializeMainPieces(xIncrementer, yIncrementer, idIncrementer, playerColor);	
	}

	//method to initialize major black and white pieces
	private int initializeMainPieces(int xIncrementer, int yIncrementer, int idIncrementer, boolean playerColor) {
		piecesInfo.add(new Rook(playerColor, xIncrementer, yIncrementer, idIncrementer));
		squares[xIncrementer][yIncrementer++] = piecesInfo.get(idIncrementer++);
		
		piecesInfo.add(new Knight(playerColor, xIncrementer, yIncrementer, idIncrementer));
		squares[xIncrementer][yIncrementer++] = piecesInfo.get(idIncrementer++);
		
		piecesInfo.add(new Bishop(playerColor, xIncrementer, yIncrementer, idIncrementer));
		squares[xIncrementer][yIncrementer++] = piecesInfo.get(idIncrementer++);
		
		piecesInfo.add(new Queen(playerColor, xIncrementer, yIncrementer, idIncrementer));
		squares[xIncrementer][yIncrementer++] = piecesInfo.get(idIncrementer++);
		
		piecesInfo.add(new King(playerColor, xIncrementer, yIncrementer, idIncrementer));
		squares[xIncrementer][yIncrementer++] = piecesInfo.get(idIncrementer++);
		
		piecesInfo.add(new Bishop(playerColor, xIncrementer, yIncrementer, idIncrementer));
		squares[xIncrementer][yIncrementer++] = piecesInfo.get(idIncrementer++);
		
		piecesInfo.add(new Knight(playerColor, xIncrementer, yIncrementer, idIncrementer));
		squares[xIncrementer][yIncrementer++] = piecesInfo.get(idIncrementer++);
		
		piecesInfo.add(new Rook(playerColor, xIncrementer, yIncrementer, idIncrementer));
		squares[xIncrementer][yIncrementer] = piecesInfo.get(idIncrementer++);
		return idIncrementer;
	}
	/*
	 * following methods to covert from and to XY and id of squares. may use for the GUI
	 */
	static Pair<Integer, Integer> idToXY(int id) {
		int x = (id - (id % 8)) / 8;
		int y = id % 8;
		
		return new Pair<Integer, Integer>(x, y);
	}
	
	static int XYtoID(int x, int y) {
		return (x*8) + y;
	}
	
	/*
	 * prints the board with with this format: |x,y colorNameId|
	 * for empty spaces: |x,y XXXXXXXX|
	 */
	public void printBoard() {
		for(int i = 0; i < squares.length; i++) {
			for(int j = 0; j < squares.length; j++) {
				System.out.print("|");
				Pieces curpiece = squares[i][j];
				if (curpiece != null) {
					System.out.print(curpiece.getX() + "," + curpiece.getY() + " "); //change so that I use getName()
					if (curpiece.getColor()) {
						System.out.print("w");
					}
					else {
						System.out.print("b");
					}
					if (curpiece.getName().equals("Pawn")) {
						System.out.print(".");
					}
					System.out.print(curpiece.getName());
					if (curpiece.getId() < 10) {
						System.out.print('0');
					}
					System.out.print(curpiece.getId());
				}
				else {
					System.out.print(i+"," +j + " XXXXXXXX");
				}
			}
			System.out.println("|");
		}
		System.out.print("Board in check: ["+(blackInCheck||whiteInCheck)+"] black in check ["+blackInCheck+"] white in check ["+whiteInCheck+"] current player: ");
		if (currentPlayer ) {
			System.out.println("white");
		}
		else {
			System.out.println("black");
		}
		System.out.println();
		
	}
	
	/*
	 * prints the board with this format: |x,y id|
	 * for empty spaces: |x,y   |
	 */
	public void printBoardCompact() {
		for(int i = 0; i < squares.length; i++) {
			for(int j = 0; j < squares.length; j++) {
				System.out.print("|");
				Pieces curpiece = squares[i][j];
				if (curpiece != null) {
					System.out.print(curpiece.getX()+","+curpiece.getY()+" ");
					if(curpiece.id < 10) {
						System.out.print("0");
					}
					System.out.print(curpiece.getId());
				}
				else {
					System.out.print(i+","+j+"   ");
				}
			}
			System.out.println("|");
		}
		System.out.println();
	}
	
	
	/*
	 * prints the piece taken in order of oldest taken to most recent taken
	 */
	public void listTakenPieces() {
		String output = "";
		for (Pieces currentTakenPiece : takenPieces) {
			System.out.println(currentTakenPiece.getX() + " " + currentTakenPiece.y + " " + currentTakenPiece.color);
			if (currentTakenPiece.getColor()) {
				output+="w";
			}
			else {
				output+="b";
			}
			output+=currentTakenPiece.name + ", ";
		}
		if (output.length() > 1) {
			System.out.println(output.substring(0, output.length()-2));
		}
		else {
			System.out.println("No pieces have been taken.");
		}
	}
	
	/*
	 * prints all the instance variables for all pieces in the format:
	 * colorAliveNameId(x,y)
	 */
	public void listPiecesInfo(ArrayList<Pieces> piecesList, String listName) {
		String output = listName+":\n";
		for (Pieces currentPiece : piecesList) {
			if (currentPiece == null) {
				output+="null, ";
			}
			else {
				if (currentPiece.getColor()) {
					output+="w";
				}
				else {
					output+="b";
				}
				if (currentPiece.isAlive()) {
					output+="alive";
				}
				else {
					output+="dead";
				}
				output+=currentPiece.name+currentPiece.getId()+"("+currentPiece.x+","+currentPiece.y+") ";
			}
		}
		if (output.length() > listName.length()+2) {
			System.out.println(output.substring(0, output.length()-2));
		}
		else {
			System.out.println("No pieces are currently in this list.");
		}
	}
	
	public Pieces getPieceById(int id) {
		return piecesInfo.get(id);
	}
	
	public Pieces getPieceAtXY(int x, int y) {
		if (!withinBounds(x,y)) {
			System.out.print("Out of bounds");
			return null;
		}
		if (squares[x][y] == null) {
			return null;
		}
		return squares[x][y];
	}
	
	public Pieces getPieceAtXY(int id) {
		int x = idToXY(id).getKey();
		int y = idToXY(id).getValue();
		
		if (!withinBounds(x,y)) {
			System.out.print("Out of bounds");
			return null;
		}
		if (squares[x][y] == null) {
			return null;
		}
		return squares[x][y];
	} 
	
	public void assignPieceXY(Pieces piece, int x, int y, int curX, int curY) {
		squares[x][y] = piece;
		piece.x = x;
		piece.y = y;
		if (((curX >= 0) && (curX < 8)) && ((curY >= 0) && (curY < 8))) {
			squares[curX][curY] = null;
		}
		piece.setMoved(true);
//		piece.hasMoved = true;
	}
	
	public void takePiece(int x, int y) {
		squares[x][y].x = x;
		squares[x][y].y = y;
		squares[x][y].setAlive(false);
//		squares[x][y].alive = false;
		takenPieces.add(squares[x][y]);
		squares[x][y] = null;
	}
	
	/*
	 * allows board to directly move a piece as opposed to moving through the piece
	 */
	public boolean movePieceByIdToXY(int id, int x, int y) {
		System.out.println("gets to movePieceByIdToXY");
		return getPieceById(id).move(this, x, y);
	}
	
	public boolean movePieceByPieceToID(Pieces piece, int id) {
		int x = idToXY(id).getKey();
		int y = idToXY(id).getValue();
		
		return piece.move(this, x, y);
	}
	
	/*
	 * remove a piece from the game with no history of it existing
	 * used mainly for altering starting layout of the board
	 */
	public void deletePiece(int id) {
		squares[piecesInfo.get(id).x][piecesInfo.get(id).y] = null;
		piecesInfo.set(id, null);
	}
	
	/*
	 * adds a piece to the starting layout if the id is larger than the starting default highest id of 31
	 * else if a piece has been deleted, can put the new piece in its place
	 * else replace a piece and all interactions with the new piece
	 */
	public void replacePiece(int id, Pieces newPiece) {
		if (newPiece == null) {
			return;
		}
		if (id >= piecesInfo.size()) {
			piecesInfo.add(newPiece);
		}
		else if (piecesInfo.get(id) == null) {
			newPiece.id = id;
			squares[newPiece.x][newPiece.y] = newPiece;
			piecesInfo.set(id, newPiece);
		}
		else {
			Pieces oldPiece = piecesInfo.get(id);
			newPiece.alive = oldPiece.alive;
			newPiece.setMoved(oldPiece.hasMoved());
//			newPiece.hasMoved = oldPiece.hasMoved;
			newPiece.id = oldPiece.id;
			newPiece.x = oldPiece.x;
			newPiece.y = oldPiece.y;
			squares[newPiece.x][newPiece.y] = newPiece;
			piecesInfo.set(id, newPiece);
		}
	}
	
	//check if i can replace with Pieces.checkProperBounds()
	public boolean withinBounds(int x, int y) {
		return ((x >= 0 && x < 8) && (y >= 0 && y < 8));
	}
	
	//HALP check necessity of hypotheticalMove
	//receive board state and a color. check if king of that color is in check (white = true)
	public boolean checkIfInCheck(boolean color, boolean hypotheticalMove) {
		boolean kingInCheck = false;
		//maybe change next lines to search for king instead of assigning those ids
		Pieces kingToCheck = (color) ? piecesInfo.get(28): piecesInfo.get(4);
		for (Pieces piece:piecesInfo) {
			if ((piece == null) || (piece.getColor() == color) || (!piece.alive)) {
//				System.out.println("this piece is null or of the same color as the king we're examining");
				continue;
			}
			else {
				if (piece.canMove(this, kingToCheck.x, kingToCheck.y, false)) {
					if (!hypotheticalMove) {
						if (color) {
							System.out.print("White");
						}
						else {
							System.out.print("Black");
						}
						System.out.println(" checked by "+piece.getName()+" "+piece.getId());
						instigators.add(piece);
					}
					kingInCheck = true;
				}
			}
		}
//		this.listPiecesInfo(instigators, "instigators");
		
		if (!hypotheticalMove) {
			whiteInCheck = (kingInCheck && color) ? true: false;
			blackInCheck = (kingInCheck && !color) ? true: false;
			if (whiteInCheck && blackInCheck) {
				System.out.println(("Something went wrong"));
			}
		}

		return kingInCheck;
	}
	
	public void declareCheck(boolean color, String attackerName) {
//		System.out.println("assassin");
		if (color) {
			blackInCheck = true;
		}
		else {
			whiteInCheck = true;
		}
		System.out.println("Board is now in check after " + attackerName + "'s move.");
	}
	
	public boolean checkIfInCheckmate(Board board, boolean color, boolean hypotheticalMove) {
		Pieces kingToCheck = (color) ? piecesInfo.get(28): piecesInfo.get(4);
		
		boolean isChecked = checkIfInCheck(color, true);
		if (!isChecked) {
			return false;
		}

		//check to see if king can make a legal move
		boolean kingCanMove = false;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (i == 0 && j == 0) {
					continue;
				}
				
				int newX = kingToCheck.getX() + i;
				int newY = kingToCheck.getY() + j;

				if (kingToCheck.canMove(board, newX, newY, false)) {
					System.out.println("King can escape check by going to ("+newX+","+newY+")");
					kingCanMove = true;
				}
			}
		}
		
		//if more than two pieces checking the king, the king has to move
		if (instigators.size() > 1) {
			System.out.println("Double check and king cannot move. Checkmate.");
			return true;
		}
		
		//check if a piece can capture when getting checked by one piece
		boolean kingInCheckmate = true;
		
		for (Pieces piece:piecesInfo) {
			if ((piece == null) || (piece.getColor() != color) || (!piece.isAlive()) || (piece == kingToCheck)) {
				continue;
			}

			if (piece.canSaveFromCheck(board)) {
				kingInCheckmate = false;
			}
		}
		
		if (kingCanMove) {
			return false;
		}
		checkmate = kingInCheckmate;
		return kingInCheckmate;
	}
	
	/*
	 * called in the case of a check mate. ends the game.
	 */
	public void gameOver(boolean winningColor) {
		if (winningColor) {
			System.out.println("White won");
		}
		else {
			System.out.println("Black won");
		}
//		System.exit(0);
	}
	
	public void clear() {
		for(int i = 0; i < squares.length; i++) {
			for(int j = 0; j < squares.length; j++) {
				Pieces curpiece = squares[i][j];
				if (curpiece != null) {
					curpiece = null;
				}
			}
		}
//		squares = null; //can't since squares final
		piecesInfo.clear();
//		piecesInfo = null; //final
		takenPieces.clear();
//		takenPieces = null; //final
		instigators.clear();
//		instigators = null; //final
	}
}