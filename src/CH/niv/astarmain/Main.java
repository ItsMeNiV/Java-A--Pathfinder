package CH.niv.astarmain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    private final int ROWS = 5;
    private final int COLS = 7;
    private final int G_HORVER = 10;
    private final int G_DIAG = 14;
    private List<Cell> openList = new ArrayList<Cell>();
    private List<Cell> closedList = new ArrayList<Cell>();
    private Cell[][] _field = new Cell[ROWS][COLS];

    public Main(){
        generateField();
        searchPath();
    }

    private void searchPath(){
        Cell startingCell = findCellByState(cellstate.STARTINGPOINT);
        openList.add(startingCell); //Add starting point to openlist
        Cell currentCell = startingCell;
        Cell nextCell = currentCell;
        do {
            currentCell = nextCell;
            nextCell = null;
            openList.remove(currentCell);
            closedList.add(currentCell);
            List<Cell> neighbourlist = getNeighbours(currentCell);
            for (Cell neighbour : neighbourlist){
                if(listContainsCell(neighbour, openList)){
                    int neighbourCurrentG = neighbour.getG();
                    int currentCellG = currentCell.getG();
                    int moveG = calculateG(currentCell, neighbour);
                    if(currentCellG + moveG < neighbourCurrentG){ //This path is better!
                        neighbour.setParentCell(currentCell);
                        neighbour.setG(calculateG(currentCell, neighbour));
                        neighbour.calculateF();
                    }
                }else{
                    openList.add(neighbour);
                    neighbour.setParentCell(currentCell);
                }
            }
            nextCell = searchForLowestF(openList);
        }while(!listContainsCell(findCellByState(cellstate.ENDINGPOINT), closedList)); //Repeat until endingcell is in closedlist
        List<String> path = new ArrayList<String>();
        Cell pathCell = findCellByState(cellstate.ENDINGPOINT);
        do{
            int currx = pathCell.getx();
            int curry = pathCell.gety();
            path.add("X: " + Integer.toString(currx) + " Y: " + Integer.toString(curry));
            if(pathCell.getParentCell() == findCellByState(cellstate.STARTINGPOINT))
                break;
            else
                pathCell = pathCell.getParentCell();
        }while(true);
        path.add("X: " + Integer.toString(findCellByState(cellstate.STARTINGPOINT).getx()) + "Y: " + Integer.toString(findCellByState(cellstate.STARTINGPOINT).gety()));
        Collections.reverse(path);
        System.out.println("The fastest Path is: ");
        for(String s : path){
            System.out.println(s + "\n");
        }
    }

    private Cell searchForLowestF(List<Cell> celllist){
        Cell lowestF = celllist.get(0);
        for (Cell cell : celllist) {
            if (cell.getF() < lowestF.getF() && !listContainsCell(cell, closedList))
                lowestF = cell;
        }
        return lowestF;
    }

    private void generateField(){
        for (int y = 0; y < ROWS; y++){
            for (int x = 0; x < COLS; x++){
                if(y == 2 && x == 1) {
                    _field[y][x] = new Cell(cellstate.STARTINGPOINT, x, y);
                } else if(y == 2 && x == 5){
                    _field[y][x] = new Cell(cellstate.ENDINGPOINT, x, y);
                } else if(x == 3 &&(y == 1 || y == 2 || y == 3)){
                    _field[y][x] = new Cell(cellstate.UNWALKABLE, x, y);
                } else{
                    _field[y][x] = new Cell(cellstate.WALKABLE, x, y);
                }
            }
        }
        for (int y = 0; y < ROWS; ++y) {
            for (int x = 0; x < COLS; ++x) {
                _field[y][x].setH(calculateH(_field[y][x]));
            }
        }
    }

    private int calculateH(Cell cell){
        Cell endingCell = findCellByState(cellstate.ENDINGPOINT);
        int x = cell.getx();
        int y = cell.gety();
        int endx = endingCell.getx();
        int endy = endingCell.gety();
        int xdiff = Math.abs(endx - x);
        int ydiff = Math.abs(endy - y);
        return xdiff + ydiff;
    }

    private int calculateG(Cell currCell, Cell neighbourCell){
        int diffX = currCell.getx() - neighbourCell.getx();
        int diffY = currCell.gety() - neighbourCell.gety();
        if(diffX == 0 ||diffY == 0){ //Horizontal/Vertical
            return G_HORVER;
        }else{ //Diagonal
            return G_DIAG;
        }
    }

    private Cell findCellByState(cellstate searchstate){
        for (int y = 0; y < ROWS; y++){
            for (int x = 0; x < COLS; x++) {
                if(_field[y][x].getCell_state() == searchstate){
                    return _field[y][x];
                }
            }
        }
        return null;
    }

    //Get all neighbours which aren't on the closedlist yet.
    private ArrayList<Cell> getNeighbours(Cell searchcell){
        ArrayList<Cell> returnList = new ArrayList<Cell>();
        int x = searchcell.getx();
        int y = searchcell.gety();
        if(findCellByCoordinates(x-1, y-1) != null && !listContainsCell(findCellByCoordinates(x - 1, y - 1), closedList)) { //Top left
            returnList.add(findCellByCoordinates(x - 1, y - 1));
            findCellByCoordinates(x - 1, y - 1).setG(G_DIAG);
        }
        if(findCellByCoordinates(x, y-1) != null && !listContainsCell(findCellByCoordinates(x, y - 1), closedList)) { //Top
            returnList.add(findCellByCoordinates(x, y - 1));
            findCellByCoordinates(x, y - 1).setG(G_HORVER);
        }
        if(findCellByCoordinates(x+1, y-1) != null && !listContainsCell(findCellByCoordinates(x + 1, y - 1), closedList)) { //Top right
            returnList.add(findCellByCoordinates(x + 1, y - 1));
            findCellByCoordinates(x + 1, y - 1).setG(G_DIAG);
        }
        if(findCellByCoordinates(x-1, y) != null && !listContainsCell(findCellByCoordinates(x - 1, y), closedList)) { //Left
            returnList.add(findCellByCoordinates(x - 1, y));
            findCellByCoordinates(x - 1, y).setG(G_HORVER);
        }
        if(findCellByCoordinates(x+1, y) != null && !listContainsCell(findCellByCoordinates(x + 1, y), closedList)) { //Right
            returnList.add(findCellByCoordinates(x + 1, y));
            findCellByCoordinates(x + 1, y).setG(G_HORVER);
        }
        if(findCellByCoordinates(x-1, y+1) != null && !listContainsCell(findCellByCoordinates(x - 1, y + 1), closedList)) { //Bottom left
            returnList.add(findCellByCoordinates(x - 1, y + 1));
            findCellByCoordinates(x - 1, y + 1).setG(G_DIAG);
        }
        if(findCellByCoordinates(x, y+1) != null && !listContainsCell(findCellByCoordinates(x, y + 1), closedList)) { //Bottom
            returnList.add(findCellByCoordinates(x, y + 1));
            findCellByCoordinates(x, y + 1).setG(G_HORVER);
        }
        if(findCellByCoordinates(x+1, y+1) != null && !listContainsCell(findCellByCoordinates(x + 1, y + 1), closedList)) { //Bpttom right
            returnList.add(findCellByCoordinates(x + 1, y + 1));
            findCellByCoordinates(x + 1, y + 1).setG(G_DIAG);
        }
        for(int i = 0; i < returnList.size(); i++){
            if(!listContainsCell(returnList.get(i), openList))
                returnList.get(i).setParentCell(searchcell);
        }
        return returnList;
    }

    private Cell findCellByCoordinates(int xcoord, int ycoord){
        if(xcoord >= COLS || xcoord < 0 || ycoord >= ROWS || ycoord < 0 || _field[ycoord][xcoord].getCell_state() == cellstate.UNWALKABLE) {
            return null;
        }else{
            return _field[ycoord][xcoord];
        }
    }

    boolean listContainsCell(Cell c, List<Cell> checkList){
        for(Cell cell : checkList){
            if(c.getUid() == cell.getUid())
                return true;
        }
        return false;
    }

    public static void main(String[] args){
        new Main();
    }

}
