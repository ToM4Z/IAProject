package Game;

import java.awt.Dimension;

import javax.swing.JFrame;

public class Launcher extends JFrame{
	private static final long serialVersionUID = 1L;

	public Launcher() {
		setPreferredSize(new Dimension(500,600));
		setSize(500,600);
		setLocation(400,100);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("BallLines");
		setVisible(true);
		
		setContentPane(new PanelGame());
		pack();
	}	
	
	public static void main(String[] args) {
		new Launcher();
	}
}
