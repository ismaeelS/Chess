import java.util.ArrayList;

import javafx.util.Pair;

public class Knight extends Pieces{

	public Knight() {
		// TODO Auto-generated constructor stub
		name = "Knight";
	}
	
	public Knight(boolean color, int x, int y, int id) {
		this.color = color;
		this.x = x;
		this.y = y;
		this.name = "Knight";
		this.id = id;
	}
	
	@Override
	boolean canMove(Board board, int targetX, int targetY, boolean movePiece) {
//		System.out.println("Knight movement");
		if (!checkProperBounds(this,targetX,targetY)) {
			return false;
		}
		
		int curX = this.getX();
		int curY = this.getY();
		
		int distanceX = Math.abs(curX - targetX);
		int distanceY = Math.abs(curY - targetY);
		
//		System.out.println(distanceX + " " + distanceY);
		//has to move 2 L/R and 1 U/D or 1 LR 2 UD
//		System.out.println("in here knight");
		if (!(((distanceX == 1) && (distanceY == 2)) || ((distanceX == 2) && (distanceY == 1)))) {
			return false;
		}

		return movePieceWithCheckChecks(board, targetX, targetY, curX, curY, movePiece);
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
