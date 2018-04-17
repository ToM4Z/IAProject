package Atoms;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("path")
public class Path {

	@Param(0)
	private int n;
	
	@Param(1)
	private int x;

	@Param(2)
	private int y;
	
	public Path() {
		n=x=y=0;
	}
	
	public Path(int n1,int x1,int y1) {
		n=n1;
		x=x1;
		y=y1;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
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
		return "path("+n+","+x+","+y+").";
	}
}
