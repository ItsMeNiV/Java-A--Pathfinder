package CH.niv.astarmain;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Mainframe {

    private JFrame _frame;
    private BufferedImage _field;
    private ColorPanel _panel;
    private Container _pane;

    public static int WINDOW_WIDTH = 700;
    public static int WINDOW_HEIGHT = 600;
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
    }

    private void initialize(){
        _field = new BufferedImage(70, 50, BufferedImage.TYPE_INT_RGB);
        _frame = new JFrame();
        _frame.setTitle("A*-Pathfinder");
        _frame.setResizable(false);
        _frame.setBounds(100, 100, WINDOW_WIDTH, WINDOW_HEIGHT);
        _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _pane = _frame.getContentPane();
        _panel = new ColorPanel(_field);
        _pane.add(_panel);
    }

    public void drawPixel(int x, int y, Color color){
        _field.setRGB(x, y, color.getRGB());
        update();
    }

    public void update(){
        _panel.repaint();
        _pane.repaint();
    }

}
