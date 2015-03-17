import java.util.ArrayList;


public class Room {

	private int x_location; // x location of the upper left corner
	private int y_location; // y location of the upper left corner
	private int length;
	private int width;		// size of the room
	private ArrayList<Pair<Integer, Integer>> walls;

	public Room(int x, int y, int length, int width) {
		this.x_location = x;
		this.y_location = y;
		this.width = width;
		this.length = length;

		// 4 sides, each may have a feature at each wall section
		int numberOfWalls = (4 * length) + (4 * width);
		for (int i = 0; i < numberOfWalls; i++) {
			walls.add(new Pair<Integer, Integer>(x + i, y + i));
		}
	}

	public ArrayList<Pair<Integer, Integer>> getWalls() {
		return walls;
	}

	public int getX(){
		return this.x_location;
	}
	public int getY() {
		return this.y_location;
	}
	public int getLength() {
		return this.length;
	}
	public int getWidth() {
		return this.width;
	}
}
