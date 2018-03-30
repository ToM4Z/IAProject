package Game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.unical.mat.embasp.base.Handler;
import it.unical.mat.embasp.base.InputProgram;
import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import it.unical.mat.embasp.languages.asp.AnswerSet;
import it.unical.mat.embasp.languages.asp.AnswerSets;
import it.unical.mat.embasp.platforms.desktop.DesktopHandler;
import it.unical.mat.embasp.specializations.dlv.desktop.DLVDesktopService;

public class PanelGame extends JPanel {
	private static final long serialVersionUID = 1L;

	private JPanel griglia;
	private int[][] cell;			// [colonna][riga]
	private JLabel[][] jcell;
	private Items_Factory factory;
	private Random random;
	private boolean fase;
	
	private static Handler handler;

	public PanelGame() {
		factory = Items_Factory.getInstance();

		griglia = new JPanel();
		griglia.setPreferredSize(new Dimension(500, 500));
		griglia.setLayout(new GridBagLayout());
		GridBagConstraints g = new GridBagConstraints();

		cell = new int[9][9];
		jcell = new JLabel[9][9];

		for (int i = 0; i < 9; ++i)
			for (int j = 0; j < 9; ++j) {
				cell[i][j] = 0;
				g.gridx = i;
				g.gridy = j;
				griglia.add(jcell[i][j] = new JLabel(), g);
				griglia.add(new JLabel(factory.getFloor()), g);
			}

		add(griglia, BorderLayout.NORTH);

		JButton next = new JButton(factory.getNext());
		next.setBorderPainted(false);
		next.setContentAreaFilled(false);
		next.setOpaque(true);

		next.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				next();
			}
		});

		add(next, BorderLayout.CENTER);

		//		INIT
		
		random = new Random();
		for (int init = 0, x, y, c; init < 5; init++) {		// piazzo le prime 5 palline
			x = random.nextInt(9);
			y = random.nextInt(9);
			c = random.nextInt(6)+1;
			
			while(cell[x][y] != Color.nullo.getVal()) {
				x = random.nextInt(9);
				y = random.nextInt(9);
			}
			
			cell[x][y] = c;
			jcell[x][y].setIcon(factory.getBall(Color.getColor(c)));
		}

		chooseWhereSpawnBalls();	// scelgo dove spawneranno le prossime 3 palline
//		PrintMatrix();
		
		fase = false;
		handler = new DesktopHandler(new DLVDesktopService("lib/dlv.mingw.exe"));
	}
	
	private void next() {
		fase = !fase;
		
		if(fase) {
			// FASE 1
			// calcolo dove spostare una pallina e il path
	
			// viene disegnato il path su schermo
			
			
			InputProgram facts= new ASPInputProgram();
			for(int i=0;i<cell.length;i++)				
				for(int j=0;j<cell.length;j++)					
					try {
						System.out.println("added "+i+" "+j);
						facts.addObjectInput(new Cell(i, j, cell[i][j]));
					} catch (Exception e) {
						e.printStackTrace();
					}
			handler.addProgram(facts);
			InputProgram encoding= new ASPInputProgram();
			encoding.addFilesPath("encodings/path");
			handler.addProgram(encoding);
			AnswerSets sets = (AnswerSets) handler.startSync();
			for(AnswerSet s : sets.getAnswersets()) {
				try {
					System.out.println(s.toString());
					Set<Object> o = s.getAtoms();
					System.out.println(o.size());
					for(Object obj:s.getAtoms()){
						if(! (obj instanceof Edge))continue;
						Edge edge = (Edge) obj;
						System.out.println(edge.getX()+","+edge.getY()+","+edge.getX1()+","+edge.getY1());
					}
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
			
			
		}else {
									// FASE 2
							
									// la pallina viene spostata
							
									// se ho fatto scoppiare un insieme di 5 palline, per questo turno, non verranno
									// spawnate altre palline
							
									// altrimenti spawno le palline e torno al rigo 64
		}
	}

	// scelgo dove spawneranno le prossime 3 palline
	private void chooseWhereSpawnBalls() {
		for(int i=0,x,y,c;i<3;++i) {

			x = random.nextInt(9);
			y = random.nextInt(9);
			c = random.nextInt(6)+1;
			
			while(cell[x][y] != Color.nullo.getVal()) {
				x = random.nextInt(9);
				y = random.nextInt(9);
			}
			
			cell[x][y] = -c;
			jcell[x][y].setIcon(factory.getStar(Color.getColor(c)));
		}
	}
	
	private void PrintMatrix() {
		for (int j = 0; j < cell[0].length; j++) {
			for (int i = 0; i < cell.length; i++)
				System.out.print(cell[i][j]);
			System.out.println();
		}
	}
}
