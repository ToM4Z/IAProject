package Atoms;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("celleUguali")
public class CelleUguali {

	@Param(0)
	private int r1;
	@Param(1)
	private int c1;
	@Param(2)
	private int r2;
	@Param(3)
	private int c2;
	
	
	public CelleUguali() {
		r1=c1=r2=c2=0;
	}
	
	public CelleUguali(int rr1, int cc1, int rr2, int cc2) {
		r1=rr1;
		c1=cc1;
		r2=rr2;
		c2=cc2;
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
	
	@Override
	public String toString() {
		return "celleUguali("+r1+","+c1+","+r2+","+c2+").";
	}
}
