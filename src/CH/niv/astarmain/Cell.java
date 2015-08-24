package CH.niv.astarmain;

public class Cell {
    private int _F;
    private int _G;
    private int _H;
    private cellstate _cellState;
    private int _x;
    private int _y;
    private Cell _parentCell;
    private int _uid;

    static int UNIQUE_ID = 0;

    private void setUid(){
        UNIQUE_ID++;
        _uid = UNIQUE_ID;
    }
    public int getUid(){
        return _uid;
    }

    public int getx() {
        return _x;
    }
    public void setX(int X) {
        _x = X;
    }

    public int gety() {
        return _y;
    }
    public void setY(int Y) {
        _y = Y;
    }

    public int getF() {
        calculateF();
        return _F;
    }
    private void setF(int f) {
        _F = f;
    }

    public int getG() {
        return _G;
    }
    public void setG(int g) {
        _G = g;
    }

    public int getH() {
        return _H;
    }
    public void setH(int h) {
        _H = h;
    }

    public cellstate getCellstate() {
        return _cellState;
    }
    public void setCellstate(cellstate cell_state) {
        this._cellState = cell_state;
    }

    public Cell getParentCell() {
        return _parentCell;
    }
    public void setParentCell(Cell parentCell) {
        this._parentCell = parentCell;
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
