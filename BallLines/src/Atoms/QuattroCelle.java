package Atoms;

import Game.Direction;
import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("giveQuattroCelle")
public class QuattroCelle {
	
	@Param(0)
	private int r1;
	@Param(1)
	private int c1;
	@Param(2)
	private int r2;
	@Param(3)
	private int c2;
	@Param(4)
	private int r3;
	@Param(5)
	private int c3;
	@Param(6)
	private int r4;
	@Param(7)
	private int c4;

	public QuattroCelle() {
		r1=c1=r2=c2=r3=c3=r4=c4=0;
	}
	
	public QuattroCelle(int rr1,int cc1,int rr2,int cc2,int rr3,int cc3,int rr4,int cc4) {
		r1=rr1;
		c1=cc1;
		r2=rr2;
		c2=cc2;
		r3=rr3;
		c3=cc3;
		r4=rr4;
		c4=cc4;
	}
	
	public Direction getDirection() {
		if(r1 == r2)
			return Direction.horizontal;
		if(c1 == c2)
			return Direction.vertical;
		if(c2 == c1+1)
			return Direction.diagonalDown;
		else
			return Direction.diagonalUp;
	}

	public int getR1() {
		return r1;
	}

	public void setR1(int r1) {
		this.r1 = r1;
	}

	public int getC1() {
		return c1;
	}

	public void setC1(int c1) {
		this.c1 = c1;
	}

	public int getR2() {
		return r2;
	}

	public void setR2(int r2) {
		this.r2 = r2;
	}

	public int getC2() {
		return c2;
	}

	public void setC2(int c2) {
		this.c2 = c2;
	}

	public int getR3() {
		return r3;
	}

	public void setR3(int r3) {
		this.r3 = r3;
	}

	public int getC3() {
		return c3;
	}

	public void setC3(int c3) {
		this.c3 = c3;
	}

	public int getR4() {
		return r4;
	}

	public void setR4(int r4) {
		this.r4 = r4;
	}

	public int getC4() {
		return c4;
	}

	public void setC4(int c4) {
		this.c4 = c4;
	}
	
	@Override
	public String toString() {
		return "quattrocelle("+r1+","+c1+","+r2+","+c2+","+r3+","+c3+","+r4+","+c4+").";
	}
	
}
