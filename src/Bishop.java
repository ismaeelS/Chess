import java.util.ArrayList;

import javafx.util.Pair;
public class Bishop extends Pieces{

	public Bishop() {
		// TODO Auto-generated constructor stub
		name = "Bishop";
	}
	
	public Bishop(boolean color, int x, int y, int id) {
		this.color = color;
		this.x = x;
		this.y = y;
		this.name = "Bishop";
		this.id = id;
	}
	
	@Override
	boolean canMove(Board board, int targetX, int targetY, boolean movePiece) {
		if (!checkProperBounds(this, targetX,targetY)) {
			return false;
		}
		
		int curX = this.getX();
		int curY = this.getY();
		
		int distanceX = Math.abs(curX - targetX);
		int distanceY = Math.abs(curY - targetY);
		
		if ((distanceX == 0) || (distanceY == 0)) { //must change both
			return false;
		}
		
		int stepX = 1;
		int stepY = 1;
		
		if (targetX < curX) {
			stepX = -1;
		}
		if (targetY < curY) {
			stepY = -1;
		}
		
		int stepperX, stepperY;
		for(stepperX = curX+stepX, stepperY = curY+stepY;
				((stepperX!=targetX) && (stepperY!=targetY)); stepperX+=stepX, stepperY+=stepY) {
			if (Board.squares[stepperX][stepperY] != null) {
				return false;
			}
		}
		
		//figure out why the following line is important in checks (bishop, dog, queen)
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
		
		int curX = this.getX();
		int curY = this.getY();
		int targetX = kingToKill.getX();
		int targetY = kingToKill.getY();
		//if not even checking the king, no points of attack
		if (!this.canMove(board, targetX, targetY, false)) {
			return null;
		}
		
		int stepX = 1;
		int stepY = 1;
		
		if (targetX < curX) {
			stepX = -1;
		}
		if (targetY < curY) {
			stepY = -1;
		}
		
		spots.add(new Pair<Integer, Integer> (curX, curY));
		
		for(int stepperX = curX+stepX, stepperY = curY+stepY;
				((stepperX!=targetX) && (stepperY!=targetY)); stepperX+=stepX, stepperY+=stepY) {
			spots.add(new Pair<Integer, Integer> (stepperX, stepperY));
		}
		
		return spots;
	}
}
