package Atoms;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("ball")
public class Ball{

	@Param(0)
	private int x;
	@Param(1)
	private int y;
	@Param(2)
	private int v;

	public Ball() {
		x = y = v = 0;
	}

	public Ball(int xx, int yy, int vv) {
		x = xx;
		y = yy;
		v = vv;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getV() {
		return v;
	}

	public void setV(int v) {
		this.v = v;
	}

	@Override
	public String toString() {
		return "ball(" + x + "," + y + "," + v + ").";
	}
}
