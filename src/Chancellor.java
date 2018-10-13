import java.util.ArrayList;

import javafx.util.Pair;

public class Chancellor extends Pieces{

	public Chancellor() {
		// TODO Auto-generated constructor stub
		this.name = "Chancellor";
	}
	
	public Chancellor(boolean color, int x, int y, int id) {
		this.color = color;
		this.x = x;
		this.y = y;
		this.name = "Chancellor";
		this.id = id;
	}
	
	@Override
	boolean canMove(Board board, int targetX, int targetY, boolean movePiece) {
		if (!checkProperBounds(this, targetX, targetY)) {
			return false;
		}
		
		int curX = this.getX();
		int curY = this.getY();
		
		int distanceX = Math.abs(curX - targetX);
		int distanceY = Math.abs(curY - targetY);
		
		//try rook movement
		if (((distanceX == 0)^(distanceY==0))) {
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
			
			for (int stepperX = curX+stepX, stepperY = curY+stepY; (stepperX != targetX) || (stepperY != targetY); stepperX+=stepX, stepperY+=stepY) {
				if (Board.squares[stepperX][stepperY] != null) {
					return false;
				}
			}
			
			return movePieceWithCheckChecks(board, targetX, targetY, curX, curY, movePiece);
		}

		//try knight movement
		if (!(((distanceX == 1) && (distanceY == 2)) || ((distanceX == 2) && (distanceY == 1)))) {
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
		int distanceX = Math.abs(curX - targetX);
		int distanceY = Math.abs(curY - targetY);
		
		//if not even checking the king, no points of attack
		if (!this.canMove(board, targetX, targetY, false)) {
			return null;
		}
		
		spots.add(new Pair<Integer, Integer> (curX, curY));
		
		//try rook movement
		if (((distanceX == 0)^(distanceY==0))) {
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
			
			for (int stepperX = curX+stepX, stepperY = curY+stepY; (stepperX != targetX) || (stepperY != targetY); stepperX+=stepX, stepperY+=stepY) {
				spots.add(new Pair<Integer, Integer> (stepperX, stepperY));
			}
		}
		
		return spots;
	}
}
