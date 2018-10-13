import java.util.ArrayList;

import javafx.util.Pair;

public class Queen extends Pieces{

	public Queen() {
		// TODO Auto-generated constructor stub
		this.name = "Queen";
	}
	
	public Queen(boolean color, int x, int y, int id) {
		this.color = color;
		this.x = x;
		this.y = y;
		this.name = "Queen";
		this.id = id;
	}
	
	@Override
	boolean canMove(Board board, int targetX, int targetY, boolean movePiece) {
//		System.out.println("Queen movement");
		if (!checkProperBounds(this,targetX,targetY)) {
			return false;
		}
		
		int curX = this.getX();
		int curY = this.getY();
		
		int distanceX = Math.abs(curX - targetX);
		int distanceY = Math.abs(curY - targetY);
		
		int stepX = 1, stepY = 1;
		
		if (targetX < curX) {
			stepX = -1;
		}
		if (targetY < curY) {
			stepY = -1;
		}		
		if (distanceX == 0) {
			stepX = 0;
		}
		if (distanceY == 0) {
			stepY = 0;
		}
		
		int stepperX, stepperY;
		//check if any piece between target and current positions
//		System.out.println("Starts at "+curX+","+curY+" id:"+this.id);
		for (stepperX = curX+stepX, stepperY = curY+stepY;
				(stepperX != targetX) || (stepperY != targetY); stepperX+=stepX, stepperY+=stepY) {
			if (!board.withinBounds(stepperX, stepperY) || Board.squares[stepperX][stepperY] != null) {
				return false;
			}
		}
		
		//figure out why the following line is important in checks (bishop, dog, queen)
		if (stepperX != targetX || stepperY != targetY) {
			return false;
		}
//		System.out.println("can move Queen " + Board.DRACULA++);
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
		
		spots.add(new Pair<Integer, Integer> (this.getX(), this.getY()));
		
		int curX = this.getX();
		int curY = this.getY();
		int distanceX = Math.abs(curX - targetX);
		int distanceY = Math.abs(curY - targetY);
		
		int stepX = 1, stepY = 1;
		
		if (targetX < curX) {
			stepX = -1;
		}
		if (targetY < curY) {
			stepY = -1;
		}		
		if (distanceX == 0) {
			stepX = 0;
		}
		if (distanceY == 0) {
			stepY = 0;
		}
		
		for (int stepperX = curX+stepX, stepperY = curY+stepY;
				(stepperX != targetX) || (stepperY != targetY); stepperX+=stepX, stepperY+=stepY) {
			spots.add(new Pair<Integer, Integer> (stepperX, stepperY));
		}
		
		return spots;
	}
}
