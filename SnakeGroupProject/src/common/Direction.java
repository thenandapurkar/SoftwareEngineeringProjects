package common;

import java.awt.*;

public enum Direction {
	
	UP, DOWN, LEFT, RIGHT;
	
	public Point moveFrom(Point point) {
		switch (this) {
		case UP:
			return new Point(point.x, point.y - 1);
		case DOWN:
			return new Point(point.x, point.y + 1);
		case LEFT:
			return new Point(point.x - 1, point.y);
		case RIGHT:
			return new Point(point.x + 1, point.y);
		default:
			return null;
		}
	}
	
	public Direction getOpposite() {
		switch (this) {
		case UP:
			return DOWN;
		case DOWN:
			return UP;
		case LEFT:
			return RIGHT;
		case RIGHT:
			return LEFT;
		default:
			return null;
		}
	}
}
