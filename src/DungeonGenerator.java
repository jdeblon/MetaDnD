import java.util.ArrayList;


/*
    1. Fill the whole map with solid earth
    2. Dig out a single room in the center of the map
    3. Pick a wall of any room
    4. Decide upon a new feature to build
    5. See if there is room to add the new feature through the chosen wall
    6. If yes, continue. If no, go back to step 3
    7. Add the feature through the chosen wall
    8. Go back to step 3, until the dungeon is complete
    9. Add the up and down staircases at random points in map
    10. Finally, sprinkle some monsters and items liberally over dungeon
 */

enum Tile {
    Unpassable, Difficult, Passable, Wall, Chest, Monster, Obstacle
}


public class DungeonGenerator {

	private static final int MIN_DUNGEON_SIZE = 10;
	private static final int MAX_DUNGEON_SIZE = 100;
	private static final int MAX_ROOM_SIZE = 50;
	private static final int MIN_ROOM_SIZE = 2;
	private static final int MIN_CORRIDOR_LENGTH = 2;
	private int numberOfRooms;
	private int numberOfCorridors;
	private ArrayList<Room> m_rooms;
	private Tile[][] m_grid;
	private int m_size;
	private RNG m_rng;

	public DungeonGenerator(int size) 	{

		// if it's outside the size bounds, then abandon ship
		if (size < MIN_DUNGEON_SIZE) {
			return;
		}
		if (size > MAX_DUNGEON_SIZE) {
			return;
		}

		// if it's not even, then make it even.
		if (size % 2 == 1) {
			size += 1;
		}
		this.m_size = size;
		this.m_grid = new Tile[size][size];

		// 1. Fill the whole map with solid earth
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				this.m_grid[i][j] = Tile.Unpassable;
			}
		}
		this.numberOfRooms = 0;
		this.numberOfCorridors = 0;
	}

	public void GenerateDungeon() {
		// first iteration:
		// 2. Dig out a single room in the center of the map
		int center = m_size / 2;
		int firstRoomSize = this.m_rng.GetRandomInteger(MIN_ROOM_SIZE, center);
		for (int i = 0; i < firstRoomSize; i++) {
			for (int j = 0; j < firstRoomSize; j++) {
				this.m_grid[center + i][center + j] = Tile.Passable;
			}
		}
		Room firstRoom = new Room(center, center, firstRoomSize, firstRoomSize);
		this.m_rooms.add(firstRoom);
		this.numberOfRooms++;

		// 3. Pick a wall of this room
		Pair<Integer, Integer> chosenWall = chooseWall();

		// 3.5 (first iteration only) Build a corridor.
		Room firstCorridor = createCorridor(chosenWall, center);

		this.m_rooms.add(firstCorridor);
		this.numberOfCorridors++;

		//break out of this when dungeon is complete.
		while (true) {
			chosenWall = chooseWall();

			// if this wall is outside the grid, then skip it.
			if (chosenWall.getElement0() > this.m_size || chosenWall.getElement1() > this.m_size) {
				continue;
			}

			// 4. Decide upon a new feature to build
			int roomIsCorridor = this.m_rng.GetRandomInteger(0, 1); // 0 = room, 1 = corridor
			Room newRoom;
			if (roomIsCorridor == 1) {
				newRoom = createCorridor(chosenWall, center);
			}
			else {
				newRoom = new Room(	chosenWall.getElement0(),
									chosenWall.getElement1(),
									center,
									center);
			}

			boolean validRoom = validateRoom(newRoom);
			// if it's not valid, then try again.
			if (!validRoom) {
				continue;
			}
			else {
				placeRoom(newRoom);
			}

		}


	}

	// randomly select a wall from the rooms and walls available.
	public Pair<Integer, Integer> chooseWall() {
		int roomToChoose = this.m_rng.GetRandomInteger(0, this.m_rooms.size());
		Room chosenRoom = this.m_rooms.get(roomToChoose);
		ArrayList<Pair<Integer, Integer>> potentialWalls = chosenRoom.getWalls();
		int wallToChoose = this.m_rng.GetRandomInteger(0, potentialWalls.size());
		return potentialWalls.get(wallToChoose);
	}

	// create a corridor of between
	public Room createCorridor(Pair<Integer, Integer> wall, int maxLength) {
		int corridorIsVertical = this.m_rng.GetRandomInteger(0, 1); // 0 = hor, 1 = vert
		int firstCorridorLength;
		int firstCorridorWidth;
		if (corridorIsVertical == 1) {
			firstCorridorLength = this.m_rng.GetRandomInteger(MIN_CORRIDOR_LENGTH, maxLength);
			firstCorridorWidth = 1;
		}
		else {
			firstCorridorWidth = this.m_rng.GetRandomInteger(MIN_CORRIDOR_LENGTH, maxLength);
			firstCorridorLength = 1;
		}
		return new Room(wall.getElement0(),
						wall.getElement1(),
						firstCorridorLength,
						firstCorridorWidth);
	}

	// determine whether the room chosen is a valid placement for a room
	public boolean validateRoom(Room toValidate) {
		int currentX = toValidate.getX();
		int currentY = toValidate.getY();
		// if all of the spaces that this room seeks to occupy are unoccupied, then we're golden.
		for (int i = 0; i < toValidate.getLength(); i++) {
			for (int j = 0; j < toValidate.getWidth(); j++) {
				if (this.m_grid[currentX + i][currentY + j] != Tile.Unpassable) {
					return false;
				}
			}
		}
		return true;
	}

	// place the room into the grid.
	public void placeRoom(Room toPlace) {
		int currentX = toPlace.getX();
		int currentY = toPlace.getY();
		for (int i = 0; i < toPlace.getLength(); i++) {
			for (int j = 0; j < toPlace.getWidth(); j++) {
				this.m_grid[currentX + i][currentY + j] = Tile.Passable;
			}
		}
	}

	// Debug method
	public void printDungeon() {
		for (int i = 0; i < this.m_size; i++) {
			for (int j = 0; j < this.m_size; j++) {
				System.out.println(this.m_grid[i][j]);
			}
		}
	}
}
