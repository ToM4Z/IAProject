package Atoms;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("isReachable")
public class isReachable {

	@Param(0)
	private int v;
	
	public isReachable() {
		v=0;
	}
	
	public isReachable(int v1) {
		v=v1;
	}
	
	public int getV() {
		return v;
	}
	
	public void setV(int v) {
		this.v = v;
	}
}
