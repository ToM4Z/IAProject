package Atoms;
import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("cell")
public class Cell {


	@Param(0)
	private int row;
	@Param(1)
	private int column;
	@Param(2)
	private int value;
	
	public Cell(final int r,final int c,final int v){
		this.row=r;
		this.column=c;
		this.value=v;
	}
	
	public Cell() {
		row = column = value = 0;
	}

	public int getRow() {
		return row;
	}

	public void setRow(final int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(final int column) {
		this.column = column;
	}

	public int getValue() {
		return value;
	}

	public void setValue(final int value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "cell("+row+","+column+","+value+").";
	}

}
