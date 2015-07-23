package CH.niv.astarmain;

public class Cell {
    private int F;
    private int G;
    private int H;
    private cellstate cell_state;
    private int x;
    private int y;
    private Cell parentCell;
    private int uid;

    static int UNIQUE_ID = 0;

    private void setUid(){
        UNIQUE_ID++;
        uid = UNIQUE_ID;
    }
    public int getUid(){
        return uid;
    }

    public int getx() {
        return x;
    }
    public void setX(int X) {
        x = X;
    }

    public int gety() {
        return y;
    }
    public void setY(int Y) {
        y = Y;
    }

    public int getF() {
        calculateF();
        return F;
    }
    private void setF(int f) {
        F = f;
    }

    public int getG() {
        return G;
    }
    public void setG(int g) {
        G = g;
    }

    public int getH() {
        return H;
    }
    public void setH(int h) {
        H = h;
    }

    public cellstate getCell_state() {
        return cell_state;
    }
    public void setCell_state(cellstate cell_state) {
        this.cell_state = cell_state;
    }

    public Cell getParentCell() {
        return parentCell;
    }
    public void setParentCell(Cell parentCell) {
        this.parentCell = parentCell;
    }

    public Cell(cellstate Cell_state, int x, int y){
        setCell_state(Cell_state);
        setX(x);
        setY(y);
        setUid();
    }

    public void calculateF(){
        setF(getH() + getG());
    }
}
