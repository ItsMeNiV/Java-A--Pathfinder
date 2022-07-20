package CH.niv.astarmain;

public class Cell {
	private int f;
	private int g;
	private int h;
	private Celltype cellType;
	private int x;
	private int y;
	private Cell parentCell;
	private int uid;

	static int uniqueId = 0;

	private void setUid() {
		Cell.uniqueId++;
		this.uid = Cell.uniqueId;
	}

	public int getUid() {
		return this.uid;
	}

	public int getx() {
		return this.x;
	}

	public void setX(int X) {
		this.x = X;
	}

	public int gety() {
		return this.y;
	}

	public void setY(int Y) {
		this.y = Y;
	}

	public int getF() {
		calculateF();
		return this.f;
	}

	private void setF(int f) {
		this.f = f;
	}

	public int getG() {
		return this.g;
	}

	public void setG(int g) {
		this.g = g;
	}

	public int getH() {
		return this.h;
	}

	public void setH(int h) {
		this.h = h;
	}

	public Celltype getCelltype() {
		return this.cellType;
	}

	public void setCellType(Celltype cellType) {
		this.cellType = cellType;
	}

	public Cell getParentCell() {
		return this.parentCell;
	}

	public void setParentCell(Cell parentCell) {
		this.parentCell = parentCell;
	}

	public Cell(Celltype cellType, int x, int y) {
		setCellType(cellType);
		setX(x);
		setY(y);
		setUid();
	}

	public void calculateF() {
		setF(getH() + getG());
	}
}
