package CH.niv.astarmain;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Mainframe {

    private JFrame _frame;
    private BufferedImage _field;
    private ColorPanel _panel;
    private Container _pane;

    private Cell[][] _genfield;

    public static int WINDOW_WIDTH = 700;
    public static int WINDOW_HEIGHT = 600;
    private final int ROWS = 50;
    private final int COLS = 70;
    public final Color STARTING_POINT_COLOR = Color.green;
    public final Color ENDING_POINT_COLOR = Color.red;
    public final Color DEFAULT_COLOR = Color.gray;
    public final Color UNWALKABLE_COLOR = Color.black;
    public final Color OPENLIST_COLOR = Color.cyan;
    public final Color CLOSEDLIST_COLOR = Color.blue;
    public final Color CURRENTCELL_COLOR = Color.yellow;
    public final Color PATH_COLOR = new Color(139,69,19); //Brown

    public Mainframe(){
        initialize();
        _frame.setVisible(true);
        generateField();
        startPathFinding();
    }

    private void generateField(){
        FieldGenerator fg = new FieldGenerator(COLS, ROWS);
        _genfield = fg.getField();
        drawField(_genfield);
        update();
    }

    private void startPathFinding(){
        PathFinder pf = new PathFinder(_genfield, this);
        pf.start();
    }

    private void initialize(){
        _field = new BufferedImage(70, 50, BufferedImage.TYPE_INT_RGB);
        _frame = new JFrame();
        _frame.setTitle("A*-Pathfinder");
        _frame.setResizable(false);
        _frame.setBounds(100, 100, WINDOW_WIDTH, WINDOW_HEIGHT + 10);
        _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _pane = _frame.getContentPane();
        _panel = new ColorPanel(_field);
        _pane.add(_panel);
    }

    public void drawPixel(int x, int y, Color color){
        _field.setRGB(x, y, color.getRGB());
        update();
    }

    public void drawField(Cell[][] field){
        for(int y = 0; y < ROWS; y++){
            for(int x = 0; x < COLS; x++){
                switch(field[y][x].getCell_state()){
                    case STARTINGPOINT:
                        drawPixel(x, y, STARTING_POINT_COLOR);
                        break;
                    case ENDINGPOINT:
                        drawPixel(x, y, ENDING_POINT_COLOR);
                        break;
                    case WALKABLE:
                        drawPixel(x, y, DEFAULT_COLOR);
                        break;
                    case UNWALKABLE:
                        drawPixel(x, y, UNWALKABLE_COLOR);
                        break;
                }
            }
        }
    }

    public void resetField(Color color){
        for(int x = 0; x < 70; x++){
            for(int y = 0; y < 50; y++){
                _field.setRGB(x, y, color.getRGB());
            }
        }
        update();
    }

    public void update(){
        _panel.repaint();
        _pane.repaint();
    }

    public static void main(String[] args){
        new Mainframe();
    }
}
