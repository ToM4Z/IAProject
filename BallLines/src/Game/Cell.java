package Game;
import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("cell")
public class Cell {

	@Param(0)
	private int x;
	@Param(1)
	private int y;
	@Param(2)
	private int v;
	
	public Cell(int x,int y,int v){
		this.x=x;
		this.y=y;
		this.v=v;
	}
	
	public Cell() {
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
