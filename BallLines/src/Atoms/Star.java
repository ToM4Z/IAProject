package Atoms;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("star")
public class Star {

	@Param(0)
	private int x;
	@Param(1)
	private int y;
	@Param(2)
	private int v;
	
	public Star() {
		x=y=v=0;
	}
	
	public Star(int xx,int yy, int vv) {
		x=xx;
		y=yy;
		v=vv;
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
	
	
}
