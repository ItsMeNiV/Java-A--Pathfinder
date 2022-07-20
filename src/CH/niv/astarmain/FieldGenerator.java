package CH.niv.astarmain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class FieldGenerator {

	private int width;
	private int height;
	private Cell[][] field;

	public FieldGenerator(int width, int height) {
		this.width = width;
		this.height = height;
		this.field = new Cell[height][width];

	}

	public Cell[][] getField() {
		Random rand = new Random(System.currentTimeMillis());
		int total = width * height;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				field[y][x] = new Cell(Celltype.UNWALKABLE, x, y);
			}
		}
		int xStart = rand.nextInt(width);
		int yStart = rand.nextInt(height);
		Cell currentCell = field[yStart][xStart];
		currentCell.setCellType(Celltype.STARTINGPOINT);
		Stack<Cell> stack = new Stack<>();
		int visited = 1;
		while (visited < total) {
			List<Cell> neighbourList = getNeighbours(currentCell);
			if (!neighbourList.isEmpty()) { // 1 or more neighbours with walls intact
				Cell randNeighbour = neighbourList.get(rand.nextInt(neighbourList.size()));
				clearWallBetween(currentCell, randNeighbour);
				stack.push(currentCell);
				currentCell = randNeighbour;
				visited++;
				if (visited >= total)
					placeEndPoint(findCellByState(Celltype.STARTINGPOINT));
			} else {
				if (!stack.isEmpty()) {
					currentCell = stack.pop();
				} else {
					placeEndPoint(findCellByState(Celltype.STARTINGPOINT));
					return field;
				}
			}
		}
		return field;
	}

	private List<Cell> getNeighbours(Cell searchCell) {
		List<Cell> retList = new ArrayList<>();
		int x = searchCell.getx();
		int y = searchCell.gety();

		if (findCellByCoordinates(x, y - 2) != null
				&& findCellByCoordinates(x, y - 2).getCelltype() == Celltype.UNWALKABLE) { // Top
			retList.add(findCellByCoordinates(x, y - 2));
		}
		if (findCellByCoordinates(x - 2, y) != null
				&& findCellByCoordinates(x - 2, y).getCelltype() == Celltype.UNWALKABLE) { // Left
			retList.add(findCellByCoordinates(x - 2, y));
		}
		if (findCellByCoordinates(x + 2, y) != null
				&& findCellByCoordinates(x + 2, y).getCelltype() == Celltype.UNWALKABLE) { // Right
			retList.add(findCellByCoordinates(x + 2, y));
		}
		if (findCellByCoordinates(x, y + 2) != null
				&& findCellByCoordinates(x, y + 2).getCelltype() == Celltype.UNWALKABLE) { // Bottom
			retList.add(findCellByCoordinates(x, y + 2));
		}

		return retList;
	}

	private void placeEndPoint(Cell startcell) {
		Random rand = new Random(System.currentTimeMillis());
		int startx = startcell.getx();
		int starty = startcell.gety();
		int geny = 0;
		int genx = 0;
		do {
			genx = rand.nextInt(width);
			geny = rand.nextInt(height);
		} while ((startx == genx && geny == starty) || field[geny][genx].getCelltype() == Celltype.UNWALKABLE);
		field[geny][genx].setCellType(Celltype.ENDINGPOINT);
	}

	private Cell findCellByCoordinates(int xcoord, int ycoord) {
		if (xcoord >= width || xcoord < 0 || ycoord >= height || ycoord < 0) {
			return null;
		} else {
			return field[ycoord][xcoord];
		}
	}

	private Cell findCellByState(Celltype searchType) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (field[y][x].getCelltype() == searchType) {
					return field[y][x];
				}
			}
		}
		return null;
	}

	private void clearWallBetween(Cell cell1, Cell cell2) {
		if (cell2.getCelltype() != Celltype.STARTINGPOINT || cell2.getCelltype() != Celltype.ENDINGPOINT)
			cell2.setCellType(Celltype.WALKABLE);
		int x = ((cell1.getx() + cell2.getx()) / 2);
		int y = ((cell1.gety() + cell2.gety()) / 2);
		field[y][x].setCellType(Celltype.WALKABLE);
	}

}
