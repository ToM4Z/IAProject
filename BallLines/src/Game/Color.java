package Game;

public enum Color {
	red(1),yellow(2),blue(3),fucsia(4),violet(5),green(6),nullo(0);
	
	private int numVal;

    Color(int numVal) {
        this.numVal = numVal;
    }

    public int getVal() {
        return numVal;
    }
    
    public static Color getColor(int x) {
    	switch(x) {
    	case 1: return red;
    	case 2: return yellow;
    	case 3: return blue;
    	case 4: return fucsia;
    	case 5: return violet;
    	case 6: return green;
    	default: return nullo;
    	}
    }
}
