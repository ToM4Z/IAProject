package Game;

import java.awt.Toolkit;

import javax.swing.ImageIcon;

public class Items_Factory {

	private static Items_Factory instance = null;

	public static Items_Factory getInstance() {
		if (instance == null)
			instance = new Items_Factory();
		return instance;
	}

	private ImageIcon floor, next, here, selected;
	private ImageIcon reb_ball, red_star;
	private ImageIcon yellow_ball, yellow_star;
	private ImageIcon blue_ball, blue_star;
	private ImageIcon green_ball, green_star;
	private ImageIcon low, high;
	private ImageIcon arrowL, arrowR, arrowU, arrowD;
	private ImageIcon curveDL, curveDR, curveUL, curveUR;

	private Items_Factory() {
		Toolkit tool = Toolkit.getDefaultToolkit();

		floor = new ImageIcon(tool.createImage(Settings.Floor));
		next = new ImageIcon(tool.createImage(Settings.Next));
		here = new ImageIcon(tool.createImage(Settings.Here));
		selected = new ImageIcon(tool.createImage(Settings.Selected));

		reb_ball = new ImageIcon(tool.createImage(Settings.Balls + "red_ball.png"));
		red_star = new ImageIcon(tool.createImage(Settings.Stars + "red_star.png"));

		yellow_ball = new ImageIcon(tool.createImage(Settings.Balls + "yellow_ball.png"));
		yellow_star = new ImageIcon(tool.createImage(Settings.Stars + "yellow_star.png"));

		blue_ball = new ImageIcon(tool.createImage(Settings.Balls + "blue_ball.png"));
		blue_star = new ImageIcon(tool.createImage(Settings.Stars + "blue_star.png"));

		green_ball = new ImageIcon(tool.createImage(Settings.Balls + "green_ball.png"));
		green_star = new ImageIcon(tool.createImage(Settings.Stars + "green_star.png"));
		
		low = new ImageIcon(tool.createImage(Settings.Path + "low.png"));
		high = new ImageIcon(tool.createImage(Settings.Path+ "high.png"));
		
		arrowL = new ImageIcon(tool.createImage(Settings.Path + "arrowL.png"));
		arrowR = new ImageIcon(tool.createImage(Settings.Path + "arrowR.png"));
		arrowU = new ImageIcon(tool.createImage(Settings.Path + "arrowU.png"));
		arrowD = new ImageIcon(tool.createImage(Settings.Path + "arrowD.png"));
		
		curveDL = new ImageIcon(tool.createImage(Settings.Path + "curveDL.png"));
		curveDR = new ImageIcon(tool.createImage(Settings.Path + "curveDR.png"));
		curveUL = new ImageIcon(tool.createImage(Settings.Path + "curveUL.png"));
		curveUR = new ImageIcon(tool.createImage(Settings.Path + "curveUR.png"));
		
	}
	
	public ImageIcon getBall(int color) {
		return getBall(Color.getColor(color));
	}
	
	public ImageIcon getBall(Color color) {
		switch(color) {
		case red: return getReb_ball();
		case yellow: return getYellow_ball();
		case green: return getGreen_ball();
		case blue: return getBlue_ball();
		default: return null;
		}
	}
	
	public ImageIcon getStar(int color) {
		return getStar(Color.getColor(color));
	}
	
	public ImageIcon getStar(Color color) {
		switch(color) {
		case red: return getRed_star();
		case yellow: return getYellow_star();
		case green: return getGreen_star();
		case blue: return getBlue_star();
		default: return null;
		}
	}
	
	public ImageIcon getHere() {
		return here;
	}

	public ImageIcon getSelected() {
		return selected;
	}

	public ImageIcon getLow() {
		return low;
	}

	public ImageIcon getHigh() {
		return high;
	}

	public ImageIcon getArrowL() {
		return arrowL;
	}

	public ImageIcon getArrowR() {
		return arrowR;
	}

	public ImageIcon getArrowU() {
		return arrowU;
	}

	public ImageIcon getArrowD() {
		return arrowD;
	}

	public ImageIcon getCurveDL() {
		return curveDL;
	}

	public ImageIcon getCurveDR() {
		return curveDR;
	}

	public ImageIcon getCurveUL() {
		return curveUL;
	}

	public ImageIcon getCurveUR() {
		return curveUR;
	}

	public ImageIcon getFloor() {
		return floor;
	}

	public ImageIcon getNext() {
		return next;
	}

	public ImageIcon getReb_ball() {
		return reb_ball;
	}

	public ImageIcon getRed_star() {
		return red_star;
	}

	public ImageIcon getYellow_ball() {
		return yellow_ball;
	}

	public ImageIcon getYellow_star() {
		return yellow_star;
	}

	public ImageIcon getBlue_ball() {
		return blue_ball;
	}

	public ImageIcon getBlue_star() {
		return blue_star;
	}

	public ImageIcon getGreen_ball() {
		return green_ball;
	}

	public ImageIcon getGreen_star() {
		return green_star;
	}

}
