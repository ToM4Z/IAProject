package Atoms;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("giveEnd")
public class End {

	@Param(0)
	private int x;
	@Param(1)
	private int y;

	public End() {
		x = y = 0;
	}

	public End(int xx, int yy) {
		x = xx;
		y = yy;
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

	@Override
	public String toString() {
		return "end(" + x + "," + y + ").";
	}
}
