package Game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Atoms.Cell;
import Atoms.End;
import Atoms.Path;
import Atoms.Spostamento;
import Atoms.Star;
import Atoms.Start;
import Atoms.Used;
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
	private int[][] cell; // [colonna][riga]
	private JLabel[][] jcell;
	private Items_Factory factory;
	private Random random;
	private boolean fase;
	private List<Path> path;
	private List<Star> stars;
	private Start start = null;
	private End end = null;
	private JLabel scoreLabel;
	private int scores = 0;

	private static Handler handlerAI, handlerPath;

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

		scoreLabel = new JLabel("Scores 0");

		add(scoreLabel, BorderLayout.CENTER);
		add(next, BorderLayout.CENTER);

		// INIT

		random = new Random();

		for (int init = 0, x, y, c; init < 5; init++) { // piazzo le prime 5 palline
			x = random.nextInt(9);
			y = random.nextInt(9);
			c = random.nextInt(4) + 1;

			while (cell[x][y] != Color.nullo.getVal()) {
				x = random.nextInt(9);
				y = random.nextInt(9);
			}

			cell[x][y] = c;
			jcell[x][y].setIcon(factory.getBall(c));

		}

		stars = new LinkedList<>();
		chooseWhereSpawnBalls(); // scelgo dove spawneranno le prossime 3 palline

		fase = false;

		handlerAI = new DesktopHandler(new DLV2DesktopService("lib/dlv2"));
		InputProgram encoding = new ASPInputProgram();
		encoding.addFilesPath("encodings/gamenew");
		handlerAI.addProgram(encoding);

		handlerPath = new DesktopHandler(new DLV2DesktopService("lib/dlv2"));
		encoding = new ASPInputProgram();
		encoding.addFilesPath("encodings/path");
		handlerPath.addProgram(encoding);
	}

	private void next() {
		fase = !fase;

		if (fase) {
			System.out.println("Fase1");
			// FASE 1
			// calcolo dove spostare una pallina e
			// disegno il path su schermo

			InputProgram factsAI = new ASPInputProgram();
			InputProgram factsPath = new ASPInputProgram();
			try {
				for (int i = 0; i < cell.length; i++)
					for (int j = 0; j < cell.length; j++) {
						Cell x = new Cell(j, i, cell[i][j]);

						if (x.getValue() > 10) {
							factsAI.addObjectInput(new Star(j, i, x.getValue() - 10));
							x.setValue(0);
						}
						System.out.println(x.toString());
						factsAI.addObjectInput(x);
						factsPath.addObjectInput(x);
					}
				factsAI.addObjectInput(new Spostamento(10, 10, 10, 10, 10));
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			handlerAI.addProgram(factsAI);

			AnswerSets sets = (AnswerSets) handlerAI.startSync();
			handlerAI.removeProgram(factsAI);

			if (sets.getAnswersets().size() == 0) {
				System.out.println("ZEROAI");
				System.out.println(sets.getErrors());
				return;
			}

			try {
				start = null;
				end = null;

				for (Object obj : sets.getAnswersets().get(sets.getAnswersets().size() - 1).getAtoms()) {

					if (obj instanceof Spostamento) {
						Spostamento o = (Spostamento) obj;
						if (o.getX1() == 10)
							continue;
						start = new Start(o.getX1(), o.getY1());
						end = new End(o.getX2(), o.getY2());
					}
				}

				System.out.println(start);
				System.out.println(end);

				factsPath.addObjectInput(start);
				factsPath.addObjectInput(end);
				factsPath.addObjectInput(new Used(10, 10));
				handlerPath.addProgram(factsPath);

			} catch (Exception e1) {
				e1.printStackTrace();
			}

			sets = (AnswerSets) handlerPath.startSync();
			handlerPath.removeProgram(factsPath);

			if (sets.getAnswersets().size() == 0) {
				System.out.println("ZEROPATH");
				System.out.println(sets.getErrors());
				return;
			}

			AnswerSet s = sets.getAnswersets().get(sets.getAnswersets().size() - 1);

			List<Used> used = new LinkedList<>();
			path = new LinkedList<>();

			try {

				for (Object obj : s.getAtoms()) {
					if (obj instanceof Used) {
						Used o = (Used) obj;
						if (o.getX() == 10)
							continue;
						used.add(o);
						// System.out.println(o);
					}
				}

				path.add(new Path(0, start.getX(), start.getY()));
				used.remove(new Used(start.getX(), start.getY()));

				jcell[start.getY()][start.getX()].setIcon(
						combineImages(factory.getBall(cell[start.getY()][start.getX()]), factory.getSelected()));

				for (int i = 1, x = start.getX(), y = start.getY(); x != end.getX() || y != end.getY(); ++i) {
					boolean pass = false;

					if (used.contains(new Used(x + 1, y))) {
						++x;
						pass = true;
					} else if (used.contains(new Used(x - 1, y))) {
						--x;
						pass = true;
					} else if (used.contains(new Used(x, y + 1))) {
						++y;
						pass = true;
					} else if (used.contains(new Used(x, y - 1))) {
						--y;
						pass = true;
					}
					if (!pass) {
						/*
						 * path.removeAll(path); i = 0; x = start.getX(); y = start.getY();
						 */
						System.out.println("CRASH");
						for (Used u : used)
							System.out.println(u);
						return;
					}
					path.add(new Path(i, x, y));
					used.remove(new Used(x, y));
					System.out.println(path.get(path.size() - 1));
					// Thread.sleep(100);
				}

				for (int i = 1; i < path.size() - 1; ++i) {
					Path prec = path.get(i - 1);
					Path now = path.get(i);
					Path next = path.get(i + 1);

					ImageIcon img = null;

					if (now.getX() != prec.getX()) {

						if (now.getX() == prec.getX() + 1) {
							if (now.getY() != next.getY()) {
								if (now.getY() == next.getY() + 1)
									img = factory.getCurveUL();
								else
									img = factory.getCurveUR();
							} else
								img = factory.getHigh();

						} else {
							if (now.getY() != next.getY()) {
								if (now.getY() == next.getY() + 1)
									img = factory.getCurveDL();
								else
									img = factory.getCurveDR();
							} else
								img = factory.getHigh();
						}

					} else {
						if (now.getY() == prec.getY() + 1) {
							if (now.getX() != next.getX()) {
								if (now.getX() == next.getX() + 1)
									img = factory.getCurveUL();
								else
									img = factory.getCurveDL();
							} else
								img = factory.getLow();

						} else {
							if (now.getX() != next.getX()) {
								if (now.getX() + 1 == next.getX())
									img = factory.getCurveDR();
								else
									img = factory.getCurveUR();
							} else
								img = factory.getLow();
						}
					}

					if (cell[now.getY()][now.getX()] > 10)
						jcell[now.getY()][now.getX()]
								.setIcon(combineImages(factory.getStar(cell[now.getY()][now.getX()] - 10), img));
					else
						jcell[now.getY()][now.getX()].setIcon(img);

				}
				jcell[end.getY()][end.getX()].setIcon(factory.getHere());

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			System.out.println("Fase2");
			// FASE 2

			// la pallina viene spostata

			// se ho fatto scoppiare un insieme di 5 palline, per questo turno, non verranno
			// spawnate altre palline

			// altrimenti spawno le palline e torno al rigo 64
			for (Path p : path) {
				jcell[p.getY()][p.getX()].setIcon(null);
				for (Star s : stars)
					if (s.getX() == p.getX() && s.getY() == p.getY())
						jcell[s.getY()][s.getX()].setIcon(factory.getStar(s.getV()));
			}
			path.clear();

			cell[end.getY()][end.getX()] = cell[start.getY()][start.getX()];
			cell[start.getY()][start.getX()] = 0;
			jcell[end.getY()][end.getX()].setIcon(factory.getBall(cell[end.getY()][end.getX()]));

			if (!FilaCompletata()) {
				for (Star s : stars) {
					if (end.getX() == s.getX() && end.getY() == s.getY())
						continue;
					cell[s.getY()][s.getX()] = s.getV();
					jcell[s.getY()][s.getX()].setIcon(factory.getBall(s.getV()));
				}
				stars.clear();

				FilaCompletata();
				
				chooseWhereSpawnBalls();
			} else {
				scoreLabel.setText("Scores " + (++scores));

				for (Star s : stars)
					if (end.getX() == s.getX() && end.getY() == s.getY())
						stars.remove(s);
			}
		}
	}

	private boolean FilaCompletata() {
		for (int i = 0; i < cell.length; ++i) {
			for (int j = 0; j < cell.length; ++j) {
				if (cell[i][j] != 0) {

					// CONTROLLO VERTICALE

					int k = j + 1, cont = 1;
					while (k < cell.length && cell[i][k] == cell[i][j]) {
						++k;
						++cont;
					}
					if (cont >= 5) {
						for (int f = j, c = 0; c < cont; ++f, ++c) {
							cell[i][f] = 0;
							jcell[i][f].setIcon(null);
						}
						return true;
					}

					// CONTROLLO ORIZZONTALE

					k = i + 1;
					cont = 1;
					while (k < cell.length && cell[k][j] == cell[i][j]) {
						++k;
						++cont;
					}
					if (cont >= 5) {
						for (int f = i, c = 0; c < cont; ++f, ++c) {
							cell[f][j] = 0;
							jcell[f][j].setIcon(null);
						}
						return true;
					}

					// CONTROLLO DIAGONALE PRINC

					k = i + 1;
					int l = j + 1;
					cont = 1;
					while (k < cell.length && l < cell.length && cell[k][l] == cell[i][j]) {
						++k;
						++l;
						++cont;
					}
					if (cont >= 5) {
						for (int f = i, z = j, c = 0; c < cont; ++f, ++z, ++c) {
							cell[f][z] = 0;
							jcell[f][z].setIcon(null);
						}
						return true;
					}

					// CONTROLLO DIAGONALE OPPOSTA

					k = i - 1;
					l = j + 1;
					cont = 1;
					while (k >= 0 && l < cell.length && cell[k][l] == cell[i][j]) {
						--k;
						++l;
						++cont;
					}
					if (cont >= 5) {
						for (int f = i, z = j, c = 0; c < cont; --f, ++z, ++c) {
							cell[f][z] = 0;
							jcell[f][z].setIcon(null);
						}
						return true;
					}
				}
			}
		}
		return false;
	}

	private ImageIcon combineImages(ImageIcon img1, ImageIcon img2) {
		BufferedImage combinedImage = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = combinedImage.createGraphics();
		g.drawImage(img1.getImage(), 0, 0, null);
		g.drawImage(img2.getImage(), 0, 0, null);
		g.dispose();
		return new ImageIcon(combinedImage);
	}

	// scelgo dove spawneranno le prossime 3 palline
	private void chooseWhereSpawnBalls() {
		for (int i = 0, x, y, c; i < 3; ++i) {

			x = random.nextInt(9);
			y = random.nextInt(9);
			c = random.nextInt(4) + 1;

			while (cell[x][y] != Color.nullo.getVal()) {
				x = random.nextInt(9);
				y = random.nextInt(9);
			}

			cell[x][y] = c + 10;
			jcell[x][y].setIcon(factory.getStar(c));
			stars.add(new Star(y, x, c));
		}
	}

	@SuppressWarnings("unused")
	private void PrintMatrix(char[][] c) {
		for (int j = 0; j < c[0].length; j++) {
			for (int i = 0; i < c.length; i++)
				System.out.print(c[i][j] + " ");
			System.out.println();
		}
	}
}
