package CH.niv.astarmain;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.WindowConstants;

public class Mainframe {

	private static final int WINDOW_WIDTH = 750;
	private static final int WINDOW_HEIGHT = 650;
	private static final int ROWS = 50;
	private static final int COLS = 70;
	private static final Color STARTING_POINT_COLOR = Color.green;
	private static final Color ENDING_POINT_COLOR = Color.red;
	private static final Color DEFAULT_COLOR = Color.gray;
	private static final Color UNWALKABLE_COLOR = Color.black;

	private JFrame frame;
	private BufferedImage field;
	private ColorPanel panel;
	private Container pane;
	private JMenuItem startItem;
	private JMenuItem generateFieldItem;
	private Cell[][] genfield;

	public Mainframe() {
		initialize();
		generateField();
	}

	public void displayFrame() {
		frame.setVisible(true);
	}

	private void generateField() {
		Cell.uniqueId = 0;
		FieldGenerator fg = new FieldGenerator(COLS, ROWS);
		genfield = fg.getField();
		drawField(genfield);
		update();
		startItem.setEnabled(true);
	}

	private void startPathFinding() {
		generateFieldItem.setEnabled(false);
		PathFinder pf = new PathFinder(genfield, this);
		Thread t = new Thread(pf);
		t.start();
		startItem.setEnabled(false);
	}

	public void enableFieldGenerating() {
		generateFieldItem.setEnabled(true);
	}

	private void initialize() {
		JMenuBar menuBar = new JMenuBar();
		field = new BufferedImage(70, 50, BufferedImage.TYPE_INT_RGB);
		frame = new JFrame();
		pane = frame.getContentPane();
		panel = new ColorPanel(field, WINDOW_WIDTH, WINDOW_HEIGHT);
		startItem = new JMenuItem("Start pathfinding");
		generateFieldItem = new JMenuItem("Generate field");

		frame.setTitle("A*-Pathfinder");
		frame.setResizable(false);
		frame.setBounds(100, 100, WINDOW_WIDTH, WINDOW_HEIGHT + 36);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		startItem.addActionListener((ActionEvent e) -> startPathFinding());
		generateFieldItem.addActionListener((ActionEvent e) -> generateField());

		menuBar.add(startItem);
		menuBar.add(generateFieldItem);
		frame.setJMenuBar(menuBar);

		pane.add(panel);
	}

	public void drawPixel(int x, int y, Color color) {
		field.setRGB(x, y, color.getRGB());
		update();
	}

	public void drawPixel(int x, int y, Celltype cellType) {
		field.setRGB(x, y, getCellTypeColor(cellType).getRGB());
		update();
	}

	public void drawField(Cell[][] field) {
		for (int y = 0; y < ROWS; y++) {
			for (int x = 0; x < COLS; x++) {
				drawPixel(x, y, field[y][x].getCelltype());
			}
		}
	}

	public void resetField(Color color) {
		for (int x = 0; x < 70; x++) {
			for (int y = 0; y < 50; y++) {
				field.setRGB(x, y, color.getRGB());
			}
		}
		update();
	}

	public void update() {
		panel.repaint();
		pane.repaint();
	}

	private Color getCellTypeColor(Celltype cellType) {
		Color returnColor;
		switch (cellType) {
		case UNWALKABLE:
			returnColor = UNWALKABLE_COLOR;
			break;
		case STARTINGPOINT:
			returnColor = STARTING_POINT_COLOR;
			break;
		case ENDINGPOINT:
			returnColor = ENDING_POINT_COLOR;
			break;
		case WALKABLE:
		default:
			returnColor = DEFAULT_COLOR;
			break;
		}
		return returnColor;
	}

}
