package CH.niv.astarmain;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PathFinder implements Runnable{

    private String _file = "field.txt";
    private final int ROWS = 50;
    private final int COLS = 70;
    private final int G_HORVER = 10;
    private final int G_DIAG = 14;
    private List<Cell> openList = new ArrayList<Cell>();
    private List<Cell> closedList = new ArrayList<Cell>();
    private Cell[][] _field = new Cell[ROWS][COLS];
    private Mainframe _m;

    public PathFinder(Cell[][] field, Mainframe m){
        _field = field;
        _m = m;
    }

    public void start(){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
                    neighbour.setG(calculateG(currentCell, neighbour));
                    openList.add(neighbour);
                    neighbour.setParentCell(currentCell);
                }
            }
            nextCell = searchForLowestF(openList);
            for(Cell c : openList){
                _m.drawPixel(c.getx(), c.gety(), _m.OPENLIST_COLOR);
            }
            for(Cell c : closedList){
                _m.drawPixel(c.getx(), c.gety(), _m.CLOSEDLIST_COLOR);
            }
            _m.drawPixel(currentCell.getx(), currentCell.gety(), _m.CURRENTCELL_COLOR);
            _m.update();
            try {
                Thread.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }while(!listContainsCell(findCellByState(cellstate.ENDINGPOINT), closedList)); //Repeat until endingcell is in closedlist
        List<String> path = new ArrayList<String>();
        Cell pathCell = findCellByState(cellstate.ENDINGPOINT);
        do{
            int currx = pathCell.getx();
            int curry = pathCell.gety();
            _m.drawPixel(currx, curry, _m.PATH_COLOR);
            path.add("X: " + Integer.toString(currx) + " Y: " + Integer.toString(curry));
            if(pathCell.getParentCell() == findCellByState(cellstate.STARTINGPOINT))
                break;
            else
                pathCell = pathCell.getParentCell();
        }while(true);
        _m.drawPixel(findCellByState(cellstate.STARTINGPOINT).getx(), findCellByState(cellstate.STARTINGPOINT).gety(), _m.STARTING_POINT_COLOR);
        _m.drawPixel(findCellByState(cellstate.ENDINGPOINT).getx(), findCellByState(cellstate.ENDINGPOINT).gety(), _m.ENDING_POINT_COLOR);
    }

    private Cell searchForLowestF(List<Cell> celllist){
        if(!celllist.isEmpty()) {
            Cell lowestF = celllist.get(0);
            for (Cell cell : celllist) {
                if (cell.getF() < lowestF.getF() && !listContainsCell(cell, closedList))
                    lowestF = cell;
            }
            return lowestF;
        }else{
            if(!listContainsCell(findCellByState(cellstate.ENDINGPOINT), closedList)){
                JOptionPane.showMessageDialog(new JFrame(), "Couldn't find path.", "Error", JOptionPane.PLAIN_MESSAGE);
                System.exit(0);
            }
        }
        return null;
    }

    private void generateField(){
        BufferedReader reader = null;
        String readstring = null;
        char c = 'O';
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(_file)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (int y = 0; y < ROWS; y++){
            try {
                readstring = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (int x = 0; x < COLS; x++){
                c = readstring.charAt(x);
                switch(c){
                    case 'O': //Walkable
                        _field[y][x] = new Cell(cellstate.WALKABLE, x, y);
                        _m.drawPixel(x, y, _m.DEFAULT_COLOR);
                        break;
                    case 'X': //Unwalkable
                        _field[y][x] = new Cell(cellstate.UNWALKABLE, x, y);
                        _m.drawPixel(x, y, _m.UNWALKABLE_COLOR);
                        break;
                    case 'S': //Startingpoint
                        _field[y][x] = new Cell(cellstate.STARTINGPOINT, x, y);
                        _m.drawPixel(x, y, _m.STARTING_POINT_COLOR);
                        break;
                    case 'E': //Endpoint
                        _field[y][x] = new Cell(cellstate.ENDINGPOINT, x, y);
                        _m.drawPixel(x, y, _m.ENDING_POINT_COLOR);
                        break;
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
        if(diffX == 0 || diffY == 0){ //Horizontal/Vertical
            return currCell.getG() + G_HORVER;
        }else{ //Diagonal
            return currCell.getG() + G_DIAG;
        }
    }

    private Cell findCellByState(cellstate searchstate){
        for (int y = 0; y < ROWS; y++){
            for (int x = 0; x < COLS; x++) {
                if(_field[y][x].getCellstate() == searchstate){
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
        }
        if(findCellByCoordinates(x, y-1) != null && !listContainsCell(findCellByCoordinates(x, y - 1), closedList)) { //Top
            returnList.add(findCellByCoordinates(x, y - 1));
        }
        if(findCellByCoordinates(x+1, y-1) != null && !listContainsCell(findCellByCoordinates(x + 1, y - 1), closedList)) { //Top right
            returnList.add(findCellByCoordinates(x + 1, y - 1));
        }
        if(findCellByCoordinates(x-1, y) != null && !listContainsCell(findCellByCoordinates(x - 1, y), closedList)) { //Left
            returnList.add(findCellByCoordinates(x - 1, y));
        }
        if(findCellByCoordinates(x+1, y) != null && !listContainsCell(findCellByCoordinates(x + 1, y), closedList)) { //Right
            returnList.add(findCellByCoordinates(x + 1, y));
        }
        if(findCellByCoordinates(x-1, y+1) != null && !listContainsCell(findCellByCoordinates(x - 1, y + 1), closedList)) { //Bottom left
            returnList.add(findCellByCoordinates(x - 1, y + 1));
        }
        if(findCellByCoordinates(x, y+1) != null && !listContainsCell(findCellByCoordinates(x, y + 1), closedList)) { //Bottom
            returnList.add(findCellByCoordinates(x, y + 1));
        }
        if(findCellByCoordinates(x+1, y+1) != null && !listContainsCell(findCellByCoordinates(x + 1, y + 1), closedList)) { //Bpttom right
            returnList.add(findCellByCoordinates(x + 1, y + 1));
        }
        for(int i = 0; i < returnList.size(); i++){
            if(!listContainsCell(returnList.get(i), openList))
                returnList.get(i).setParentCell(searchcell);
        }
        return returnList;
    }

    private Cell findCellByCoordinates(int xcoord, int ycoord){
        if(xcoord >= COLS || xcoord < 0 || ycoord >= ROWS || ycoord < 0 || _field[ycoord][xcoord].getCellstate() == cellstate.UNWALKABLE) {
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

    @Override
    public void run() {
        start();
    }
}
