import java.util.ArrayList;

import javafx.util.Pair;

public class Dog extends Pieces{
	public Dog() {
		// TODO Auto-generated constructor stub
		name = "Dog";
	}
	
	public Dog(boolean color, int x, int y, int id) {
		this.color = color;
		this.x = x;
		this.y = y;
		this.name = "Dog";
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
		
		//VERIFY THIS WORKS HALP
		//try bishop movement
		if ((!curColor && (targetX < curX)) || ((curColor) && ((targetX > curX)))) {
			int stepX = 1;
			int stepY = 1;
			int startX = curX;
			int startY = curY;
			
			if (targetX < curX) {
				stepX = -1;
			}
			if (targetY < curY) {
				stepY = -1;
			}
			
			int stepperX, stepperY;
			for(stepperX = startX+stepX, stepperY = startY+stepY; 
					((stepperX!=targetX) && (stepperY!=targetY)); stepperX+=stepX, stepperY+=stepY) {
				if (Board.squares[stepperX][stepperY] != null) {
					return false;
				}
			}
			
			//figure out why the following line is important in checks (bishop, dog, queen)
			if (stepperX!=targetX || stepperY!=targetY) {
				return false;
			}
			
//			System.out.println(stepperX+","+stepperY+" "+targetX+","+targetY);

			return movePieceWithCheckChecks(board, targetX, targetY, curX, curY, movePiece);
		}
		
		//try pawn movement
		if (((distanceX > 2) || (distanceY > 1)) || (distanceX + distanceY == 0)) {
			return false;
		}
				
		//check following to see if deals with checks taken and given properly HALP
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
		
		int curX = this.getX();
		int curY = this.getY();
		int targetX = kingToKill.getX();
		int targetY = kingToKill.getY();
		boolean curColor = this.getColor();
		
		//if not even checking the king, no points of attack
		if (!this.canMove(board, targetX, targetY, false)) {
			return null;
		}
		
		spots.add(new Pair<Integer, Integer> (curX, curY));
		
		//bishop movement
		if ((!curColor && (targetX < curX)) || ((curColor) && ((targetX > curX)))) {
			int stepX = 1;
			int stepY = 1;
			int startX = curX;
			int startY = curY;
			
			if (targetX < curX) {
				stepX = -1;
			}
			if (targetY < curY) {
				stepY = -1;
			}
			
			int stepperX, stepperY;
			for(stepperX = startX+stepX, stepperY = startY+stepY; 
					((stepperX!=targetX) && (stepperY!=targetY)); stepperX+=stepX, stepperY+=stepY) {
				spots.add(new Pair<Integer, Integer> (stepperX, stepperY));
			}
		}
		
		//don't have to check pawn-like points of attack since that's only own space which is already added (i think)
		
		return spots;
	}
}
