package CH.niv.astarmain;

public class Cell {
    private int F;
    private int G;
    private int H;
    private cellstate cellState;
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

    public cellstate getCellstate() {
        return cellState;
    }
    public void setCellstate(cellstate cell_state) {
        this.cellState = cell_state;
    }

    public Cell getParentCell() {
        return parentCell;
    }
    public void setParentCell(Cell parentCell) {
        this.parentCell = parentCell;
    }

    public Cell(cellstate cellState, int x, int y){
        setCellstate(cellState);
        setX(x);
        setY(y);
        setUid();
    }

    public void calculateF(){
        setF(getH() + getG());
    }
}
