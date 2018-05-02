package Atoms;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("isReachable")
public class isReachable {

	@Param(0)
	private int n;
	
	public isReachable() {
		n=0;
	}
	
	public isReachable(int nn) {
		n=nn;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}
	
	@Override
	public String toString() {
		return "isReachable("+n+").";
	}
}
