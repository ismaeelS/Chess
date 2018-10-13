import java.util.ArrayList;

import javafx.util.Pair;

public class Rook extends Pieces{

	public Rook() {
		// TODO Auto-generated constructor stub
		this.name = "Rook";
	}
	
	public Rook(boolean color, int x, int y, int id) {
		this.color = color;
		this.x = x;
		this.y = y;
		this.name = "Rook";
		this.id = id;
	}
	
	@Override
	boolean canMove(Board board, int targetX, int targetY, boolean movePiece) {
		if (!checkProperBounds(this,targetX,targetY)) {
			return false;
		}
		
		int curX = this.getX();
		int curY = this.getY();
		
		int distanceX = Math.abs(curX - targetX);
		int distanceY = Math.abs(curY - targetY);
		
		//make sure only one axis is moving
		if (!((distanceX == 0)^(distanceY==0))) {
			return false;
		}
		
		int stepX = 1, stepY = 1;
		
		if ((targetX < curX) || (targetY < curY)) {
			stepX = stepY = -1;
		}
		
		if (distanceX == 0) {
			stepX = 0;
		}
		else {
			stepY = 0;
		}
		
		int stepperX, stepperY;
		for (stepperX = curX+stepX, stepperY = curY+stepY; (stepperX != targetX) || (stepperY != targetY); stepperX+=stepX, stepperY+=stepY) {
			if (Board.squares[stepperX][stepperY] != null) {
				return false;
			}
		}
		
		//added this after the bishop stuff. see if needed
		if (stepperX!=targetX || stepperY!=targetY) {
//			System.out.println("RETURNING FALSE HERE");
			return false;
		}
		
		return movePieceWithCheckChecks(board, targetX, targetY, curX, curY, movePiece);
	}
	
	@Override
	public ArrayList<Pair<Integer, Integer>> pointsOfAttack(Board board) {
		ArrayList<Pair<Integer, Integer>> spots = new ArrayList<Pair<Integer, Integer>>();
		Pieces kingToKill = (color) ? board.piecesInfo.get(4): board.piecesInfo.get(28);
		
		int targetX = kingToKill.getX();
		int targetY = kingToKill.getY();
		
		if (!this.canMove(board, targetX, targetY, false)) {
			return null;
		}
		
		int curX = this.getX();
		int curY = this.getY();
		int distanceX = Math.abs(curX - targetX);
//		int distanceY = Math.abs(curY - targetY);
		int stepX = 1, stepY = 1;
		
		spots.add(new Pair<Integer, Integer> (curX, curY));
		
		if ((targetX < curX) || (targetY < curY)) {
			stepX = stepY = -1;
		}
		
		if (distanceX == 0) {
			stepX = 0;
		}
		else {
			stepY = 0;
		}
		
		for (int stepperX = curX+stepX, stepperY = curY+stepY; (stepperX != targetX) || (stepperY != targetY); stepperX+=stepX, stepperY+=stepY) {
			spots.add(new Pair<Integer, Integer> (stepperX, stepperY));
		}
		
		return spots;
	}
}
