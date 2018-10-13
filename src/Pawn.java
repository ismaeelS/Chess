import java.util.ArrayList;

import javafx.util.Pair;

public class Pawn extends Pieces{

	
	public Pawn() {
		// TODO Auto-generated constructor stub
		name = "Pawn";
	}
	
	public Pawn(boolean color, int x, int y, int id) {
		this.color = color;
		this.x = x;
		this.y = y;
		this.name = "Pawn";
		this.id = id;
	}
	
	@Override
	boolean canMove(Board board, int targetX, int targetY, boolean movePiece) {
		if (!checkProperBounds(this,targetX,targetY)) {
			return false;
		}
		
		int curX = this.getX();
		int curY = this.getY();
		boolean curColor = this.getColor();
		
		int distanceX = Math.abs(curX - targetX);
		int distanceY = Math.abs(curY - targetY);
		
		if (((distanceX > 2) || (distanceY > 1)) || (distanceX + distanceY == 0)) {
			return false;
		}
		
		if ((!curColor && (targetX < curX)) || ((curColor) && ((targetX > curX)))) {
			return false;
		}
				
		if (distanceX == 1) { //moves by one
			if (distanceY == 0) {
				if (Board.squares[targetX][targetY] == null) {
					if (movePiece) {
						board.assignPieceXY(this, targetX, targetY, curX, curY);
					}
					return true;
				}
				return false;
			}
			if (distanceY == 1) {
				boolean attack = Board.squares[targetX][targetY].getColor() != (this.getColor());
				if (attack) {
					if (movePiece) {
						board.takePiece(targetX,targetY);
						board.assignPieceXY(this, targetX, targetY, curX, curY);
					}
					return true;
					
				}
				return false;
			}
		}
		if (distanceX == 2) {
			if (distanceY != 0 || this.hasMoved()) {
				return false;
			}
			else {
				if (Board.squares[targetX][targetY] == null) {
					if (movePiece) {
						board.assignPieceXY(this, targetX, targetY, curX, curY);
					}
					return true;
				}
				return false;
			}
		}
		
		return false;
	}
	
	@Override
	public ArrayList<Pair<Integer, Integer>> pointsOfAttack(Board board) {
		ArrayList<Pair<Integer, Integer>> spots = new ArrayList<Pair<Integer, Integer>>();
		Pieces kingToKill = (color) ? board.piecesInfo.get(4): board.piecesInfo.get(28);
		
		if (!this.canMove(board, kingToKill.getX(), kingToKill.getY(), false)) {
			return null;
		}
		
		spots.add(new Pair<Integer, Integer> (this.getX(), this.getY()));
		return spots;
	}

}
