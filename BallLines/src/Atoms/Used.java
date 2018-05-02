package Atoms;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("giveUsed")
public class Used {

	@Param(0)
	private int x;
	
	@Param(1)
	private int y;
	
	public Used() {
		x=y=0;
	}
	
	public Used(int xx, int yy) {
		x=xx;
		y=yy;
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
		return "used("+x+","+y+").";
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Used) {
			Used o = (Used) obj;
			return o.getX() == x && o.getY() == y;
		}
		return false;
	}
}
