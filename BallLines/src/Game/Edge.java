package Game;
import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("edge")
public class Edge {

	
	@Param(0)
	private int x;
	@Param(1)
	private int y;
	@Param(2)
	private int x1;
	@Param(3)
	private int y1;
	
	public Edge(final int xx,final int yy,final int xx1,final int yy1){
		this.x=xx;
		this.y=yy;
		this.x1=xx1;
		this.y1=yy1;
	}
	
	public Edge() {
		x=y=x1=y1=0;
	}

	public int getX() {
		return x;
	}

	public void setX(final int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(final int y) {
		this.y = y;
	}

	public int getX1() {
		return x1;
	}

	public void setX1(final int x1) {
		this.x1 = x1;
	}

	public int getY1() {
		return y1;
	}

	public void setY1(final int y1) {
		this.y1 = y1;
	}

	@Override
	public String toString() {
		return "Edge("+x+","+y+","+x1+","+y1+")";
	}	
}
