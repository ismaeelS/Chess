import java.util.ArrayList;

import javafx.util.Pair;

public class King extends Pieces{

	boolean inCheck = false;
	
	public King() {
		// TODO Auto-generated constructor stub
		this.name = "King";
	}
	
	public King(boolean color, int x, int y, int id) {
		this.color = color;
		this.x = x;
		this.y = y;
		this.name = "King";
		this.id = id;
	}
	
	@Override
	boolean canMove(Board board, int targetX, int targetY, boolean movePiece) {
//		System.out.println("can move King " + Board.DRACULA++);
		if (!checkProperBounds(this,targetX,targetY)) {
			return false;
		}
		
		int curX = this.getX();
		int curY = this.getY();
		
		int distanceX = Math.abs(curX - targetX);
		int distanceY = Math.abs(curY - targetY);
		
		//can only move 1 square adjacent/diagonal
		if (!(((distanceX == 1) && (distanceY == 1)) || ((distanceX+distanceY==1)&&((distanceX==1)^(distanceY==1))))) {
			return false;
		}

		return movePieceWithCheckChecks(board, targetX, targetY, curX, curY, movePiece);
	}
	
	@Override
	public ArrayList<Pair<Integer, Integer>> pointsOfAttack(Board board) {
		return null;
	}
}
