package CH.niv.astarmain;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class PathFinder implements Runnable {

	private static final int ROWS = 50;
	private static final int COLS = 70;
	private static final int G_HORVER = 10;
	private static final int G_DIAG = 14;

	private List<Cell> openList = new ArrayList<>();
	private List<Cell> closedList = new ArrayList<>();
	private Cell[][] field = new Cell[ROWS][COLS];
	private Mainframe mainframe;
	private Cell startingCell;
	private Cell endingCell;

	public PathFinder(Cell[][] field, Mainframe mainframe) {
		this.field = field;
		this.mainframe = mainframe;
	}

	public void start() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		searchPath();
	}

	private void searchPath() {
		// Setup
		startingCell = findCellByState(Celltype.STARTINGPOINT);
		endingCell = findCellByState(Celltype.ENDINGPOINT);
		if (startingCell != null && endingCell != null) {
			openList.add(startingCell); // Add starting point to openlist

			// Search logic
			doSearch(startingCell);

			// Path drawing logic
			drawPath();
		}
	}

	private void doSearch(Cell currentCell) {
		openList.remove(currentCell);
		closedList.add(currentCell);
		List<Cell> neighbourlist = getNeighbours(currentCell);
		for (Cell neighbour : neighbourlist) {
			if (openList.contains(neighbour)) {
				int neighbourCurrentG = neighbour.getG();
				int currentCellG = currentCell.getG();
				int moveG = calculateG(currentCell, neighbour);
				if (currentCellG + moveG < neighbourCurrentG) { // This path is better!
					neighbour.setParentCell(currentCell);
					neighbour.setG(calculateG(currentCell, neighbour));
					neighbour.calculateF();
				}
			} else {
				neighbour.setG(calculateG(currentCell, neighbour));
				openList.add(neighbour);
				neighbour.setParentCell(currentCell);
			}
		}
		for (Cell c : openList) {
			mainframe.drawPixel(c.getx(), c.gety(), Color.cyan);
		}
		for (Cell c : closedList) {
			mainframe.drawPixel(c.getx(), c.gety(), Color.blue);
		}
		mainframe.drawPixel(currentCell.getx(), currentCell.gety(), Color.yellow);
		mainframe.update();
		try {
			Thread.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Repeat until endingcell is in closedlist
		Cell endingPointCell = endingCell;
		if (endingPointCell != null && !closedList.contains(endingPointCell)) {
			Cell nextCell = searchForLowestF(openList);
			doSearch(nextCell);
		}
	}

	private void drawPath() {
		Cell pathCell = endingCell;
		do {
			int currX = pathCell.getx();
			int currY = pathCell.gety();
			mainframe.drawPixel(currX, currY, new Color(139, 69, 19));
			pathCell = pathCell.getParentCell();
		} while (pathCell != startingCell);

		mainframe.drawPixel(startingCell.getx(), startingCell.gety(), startingCell.getCelltype());
		mainframe.drawPixel(endingCell.getx(), endingCell.gety(), endingCell.getCelltype());
	}

	private Cell searchForLowestF(List<Cell> cellList) {
		if (!cellList.isEmpty()) {
			Cell lowestF = cellList.get(0);
			for (Cell cell : cellList) {
				if (cell.getF() < lowestF.getF() && !closedList.contains(cell))
					lowestF = cell;
			}
			return lowestF;
		} else {
			if (!closedList.contains(endingCell)) {
				JOptionPane.showMessageDialog(new JFrame(), "Couldn't find path.", "Error", JOptionPane.PLAIN_MESSAGE);
				System.exit(0);
			}
		}
		return null;
	}

	private int calculateG(Cell currCell, Cell neighbourCell) {
		int diffX = currCell.getx() - neighbourCell.getx();
		int diffY = currCell.gety() - neighbourCell.gety();
		if (diffX == 0 || diffY == 0) { // Horizontal/Vertical
			return currCell.getG() + G_HORVER;
		} else { // Diagonal
			return currCell.getG() + G_DIAG;
		}
	}

	private Cell findCellByState(Celltype searchstate) {
		for (int y = 0; y < ROWS; y++) {
			for (int x = 0; x < COLS; x++) {
				if (field[y][x].getCelltype() == searchstate) {
					return field[y][x];
				}
			}
		}
		return null;
	}

	// Get all neighbours which aren't on the closedlist yet.
	private ArrayList<Cell> getNeighbours(Cell searchCell) {
		ArrayList<Cell> returnList = new ArrayList<>();
		int x = searchCell.getx();
		int y = searchCell.gety();

		addCellToListIfNotInClosedList(returnList, findCellByCoordinates(x - 1, y - 1)); // top left
		addCellToListIfNotInClosedList(returnList, findCellByCoordinates(x, y - 1)); // top
		addCellToListIfNotInClosedList(returnList, findCellByCoordinates(x + 1, y - 1)); // top right
		addCellToListIfNotInClosedList(returnList, findCellByCoordinates(x - 1, y)); // left
		addCellToListIfNotInClosedList(returnList, findCellByCoordinates(x + 1, y)); // right
		addCellToListIfNotInClosedList(returnList, findCellByCoordinates(x - 1, y + 1)); // bottom left
		addCellToListIfNotInClosedList(returnList, findCellByCoordinates(x, y + 1)); // bottom
		addCellToListIfNotInClosedList(returnList, findCellByCoordinates(x + 1, y + 1)); // bottom right

		for (int i = 0; i < returnList.size(); i++) {
			if (!openList.contains(returnList.get(i))) {
				returnList.get(i).setParentCell(searchCell);
			}
		}
		return returnList;
	}

	private void addCellToListIfNotInClosedList(List<Cell> list, Cell cell) {
		if (cell != null && !closedList.contains(cell)) { // Bottom
			list.add(cell);
		}
	}

	private Cell findCellByCoordinates(int xcoord, int ycoord) {
		if (xcoord >= COLS || xcoord < 0 || ycoord >= ROWS || ycoord < 0
				|| field[ycoord][xcoord].getCelltype() == Celltype.UNWALKABLE) {
			return null;
		} else {
			return field[ycoord][xcoord];
		}
	}

	boolean listContainsCell(Cell c, List<Cell> checkList) {
		for (Cell cell : checkList) {
			if (c.getUid() == cell.getUid())
				return true;
		}
		return false;
	}

	@Override
	public void run() {
		start();
		mainframe.enableFieldGenerating();
	}
}
