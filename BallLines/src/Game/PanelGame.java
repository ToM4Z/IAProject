package Game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Atoms.Cell;
import Atoms.Path;
import Atoms.isReachable;
import it.unical.mat.embasp.base.Handler;
import it.unical.mat.embasp.base.InputProgram;
import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import it.unical.mat.embasp.languages.asp.AnswerSet;
import it.unical.mat.embasp.languages.asp.AnswerSets;
import it.unical.mat.embasp.platforms.desktop.DesktopHandler;
import it.unical.mat.embasp.specializations.dlv2.desktop.DLV2DesktopService;

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
		
//		chooseWhereSpawnBalls();	// scelgo dove spawneranno le prossime 3 palline
//		PrintMatrix();
		
		fase = false;
		handler = new DesktopHandler(new DLV2DesktopService("lib/dlv2"));
		InputProgram encoding= new ASPInputProgram();
		encoding.addFilesPath("encodings/path");
		handler.addProgram(encoding);
	}
	
	@SuppressWarnings("unused")
	private void next() {
		fase = !fase;
		
		if(fase) {System.out.println("Fase1");
			// FASE 1
			// calcolo dove spostare una pallina e il path	
			// viene disegnato il path su schermo
			
			
			InputProgram facts= new ASPInputProgram();
			try {
				for(int i=0;i<cell.length;i++)				
					for(int j=0;j<cell.length;j++) {
						Cell x = new Cell(j, i, cell[i][j]);
						System.out.println(x.toString());
						facts.addObjectInput(x);
					}
				facts.addObjectInput(new isReachable());
				facts.addObjectInput(new Path());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			handler.addProgram(facts);
						
			AnswerSets sets = (AnswerSets) handler.startSync();
			
			int size = sets.getAnswersets().size();
			if(size == 0) {
				System.out.println("ZERO");
				System.out.println(sets.getErrors());
				return;
			}
			AnswerSet s = sets.getAnswersets().get(sets.getAnswersets().size()-1);
			List<Path> path = new LinkedList<>();
			boolean placed = false;
			try {	
				
				for(Object obj : s.getAtoms()) {
					if(obj instanceof isReachable) {
						isReachable r = (isReachable) obj;
						if(r.getV()!=0)
							placed = true;
					}else if(obj instanceof Path) {
						Path o = (Path) obj;
						path.add(o);
					}
				}
				path.sort(new Comparator<Path>() {

					@Override
					public int compare(Path p0, Path p1) {
						if(p0.getN()<p1.getN())
							return -1;
						return 1;
					}
					
				});
				
				for(Path p : path)
					System.out.println(p.toString());
					
				
				for(int i=1; i<path.size()-1; ++i) {
					Path prec = path.get(i-1);
					Path now = path.get(i);
					Path next = path.get(i+1);

					
					if(now.getX() != prec.getX()) {		
						
						if(now.getX() == prec.getX()+1) {
							
							if(now.getY() != next.getY()) {
								if(now.getY() == next.getY()+1)
									jcell[now.getY()][now.getX()].setIcon(factory.getCurveUL());
								else
									jcell[now.getY()][now.getX()].setIcon(factory.getCurveUR());
							}else
								jcell[now.getY()][now.getX()].setIcon(factory.getHigh());
							
						}else{
							
							if(now.getY() != next.getY()) {
								if(now.getY() == next.getY()+1)
									jcell[now.getY()][now.getX()].setIcon(factory.getCurveDL());
								else
									jcell[now.getY()][now.getX()].setIcon(factory.getCurveDR());
							}else
								jcell[now.getY()][now.getX()].setIcon(factory.getHigh());
							
						}
					}else{
						if(now.getY() == prec.getY()+1) {
							
							if(now.getX() != next.getX()) {
								if(now.getX() == next.getX()+1)
									jcell[now.getY()][now.getX()].setIcon(factory.getCurveUL());
								else
									jcell[now.getY()][now.getX()].setIcon(factory.getCurveDL());
							}else
								jcell[now.getY()][now.getX()].setIcon(factory.getLow());
							
						}else{
							
							if(now.getX() != next.getX()) {
								if(now.getX() == next.getX()+1)
									jcell[now.getY()][now.getX()].setIcon(factory.getCurveDR());
								else
									jcell[now.getY()][now.getX()].setIcon(factory.getCurveUR());
							}else
								jcell[now.getY()][now.getX()].setIcon(factory.getLow());
							
						}
					}
				}								
				
				
			} catch (Exception e) {
				e.printStackTrace();
			} 
			
		}else {System.out.println("Fase2");
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
			
			cell[x][y] = c+10;
			jcell[x][y].setIcon(factory.getStar(Color.getColor(c)));
		}
	}
	
	@SuppressWarnings("unused")
	private void PrintMatrix(char[][] c) {
		for (int j = 0; j < c[0].length; j++) {
			for (int i = 0; i < c.length; i++)
				System.out.print(c[i][j]+" ");
			System.out.println();
		}
	}
}
