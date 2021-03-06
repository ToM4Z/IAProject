package Game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Atoms.Ball;
import Atoms.CelleUguali;
import Atoms.Empty;
import Atoms.End;
import Atoms.Path;
import Atoms.Star;
import Atoms.Start;
import Atoms.Used;
import Atoms.adjAllDir;
import Atoms.adjHalfDir;
import it.unical.mat.embasp.base.Handler;
import it.unical.mat.embasp.base.InputProgram;
import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import it.unical.mat.embasp.languages.asp.AnswerSet;
import it.unical.mat.embasp.languages.asp.AnswerSets;
import it.unical.mat.embasp.platforms.desktop.DesktopHandler;
import it.unical.mat.embasp.specializations.dlv2.desktop.DLV2DesktopService;

public class PanelGame extends JPanel {
	private static final long serialVersionUID = 1L;

	private final InputProgram facts;
	private static Handler handlerAI, handlerPath;

	private JPanel griglia, manager, loadingPanel;
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
	private boolean createPath = false, viewGrid = false;

	public PanelGame() {
		factory = Items_Factory.getInstance();

		initUI();
		reset();

		facts = new ASPInputProgram();

		handlerAI = new DesktopHandler(new DLV2DesktopService("lib/dlv2"));
		InputProgram encoding = new ASPInputProgram();
		encoding.addFilesPath("encodings/First");
		handlerAI.addProgram(encoding);

		try {
			InputProgram factsAI = new ASPInputProgram();

			factsAI.addObjectInput(new CelleUguali());
			factsAI.addObjectInput(new adjAllDir(10, 10, 10, 10, "s"));
			factsAI.addObjectInput(new adjHalfDir(10, 10, 10, 10, "s"));

			handlerAI.addProgram(factsAI);
			AnswerSets sets = (AnswerSets) handlerAI.startSync();

			for (Object obj : sets.getAnswersets().get(0).getAtoms())
				if (obj instanceof adjAllDir) {
					adjAllDir a = (adjAllDir) obj;
					if (a.getR1() == 10)
						continue;
					facts.addObjectInput(a);
//					System.out.println(a);
				} else if (obj instanceof adjHalfDir) {
					adjHalfDir a = (adjHalfDir) obj;
					if (a.getR1() == 10)
						continue;
					facts.addObjectInput(a);
//					System.out.println(a);
				} else if (obj instanceof CelleUguali) {
					facts.addObjectInput((CelleUguali) obj);
//					System.out.println((CelleUguali) obj);
				}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		handlerAI.removeAll();
		encoding.clearFilesPaths();
		encoding.addFilesPath("encodings/IA");
		handlerAI.addProgram(encoding);
		handlerAI.addProgram(facts);

		handlerPath = new DesktopHandler(new DLV2DesktopService("lib/dlv2"));
		encoding = new ASPInputProgram();
		encoding.addFilesPath("encodings/path");
		handlerPath.addProgram(encoding);
		handlerPath.addProgram(facts);

		path = new LinkedList<>();
	}

	private void next() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					fase = !fase;

					switchScreen(true);
					
					if (fase) {
						System.out.println("-----------Fase1-----------");
						// FASE 1
						// calcolo dove spostare una pallina e
						// disegno il path su schermo

						InputProgram factsAI = new ASPInputProgram();
						InputProgram factsPath = new ASPInputProgram();

						factsAI.addObjectInput(new Start(10, 10));
						factsAI.addObjectInput(new End(10, 10));

						for (Star s : stars) {
//						System.out.println(s.toString());
							factsAI.addObjectInput(s);
						}

						for (int i = 0; i < cell.length; i++)
							for (int j = 0; j < cell.length; j++) {
								if (cell[i][j] != 0) {
									Ball x = new Ball(j, i, cell[i][j]);
									factsAI.addObjectInput(x);
//								System.out.println(x.toString());
								} else {
									Empty x = new Empty(j, i);
									factsAI.addObjectInput(x);
									factsPath.addObjectInput(x);
//								System.out.println(x.toString());
								}
							}

						System.out.println(factsAI.getPrograms());
						System.out.println(facts.getPrograms());

						handlerAI.addProgram(factsAI);
						AnswerSets sets = (AnswerSets) handlerAI.startSync();
						handlerAI.removeProgram(factsAI);
						System.out.println(sets.getAnswerSetsString());
						System.out.println(sets.getErrors());

						if (sets.getAnswersets().size() == 0) {

							if (resetOrCloseDialog() == 1)
								System.exit(0);
							else
								reset();
							return;
						}

						start = null;
						end = null;

						for (Object obj : sets.getAnswersets().get(0).getAtoms()) {

							if (obj instanceof Start) {
								if (((Start) obj).getX() == 10)
									continue;

								start = (Start) obj;
								System.out.println(start.toString());
							} else if (obj instanceof End) {
								if (((End) obj).getX() == 10)
									continue;

								end = (End) obj;
								System.out.println(end.toString());
							}
						}

						if (start == null) {
							System.out.println("ERROR: The \"start\" atom is not found!");
							return;
						}

						jcell[start.getY()][start.getX()].setIcon(combineImages(
								factory.getBall(cell[start.getY()][start.getX()]), factory.getSelected()));
						jcell[end.getY()][end.getX()].setIcon(factory.getHere());

						if (createPath) {
							factsPath.addObjectInput(start);
							factsPath.addObjectInput(end);
							factsPath.addObjectInput(new Used(10, 10));
							handlerPath.addProgram(factsPath);

							sets = (AnswerSets) handlerPath.startSync();
							handlerPath.removeProgram(factsPath);
							System.out.println(sets.getAnswerSetsString());

							if (sets.getAnswersets().size() == 0) {
								System.out.println("ERROR: DLV doesn't create path");
								System.out.println(sets.getErrors());
								return;
							}

							AnswerSet s = sets.getAnswersets().get(sets.getAnswersets().size() - 1);
							System.out.println(s.getAnswerSet());
							creaPath(s);
						}

					} else {
						System.out.println("Fase2");
						// FASE 2

						// la pallina viene spostata

						// se ho fatto scoppiare un insieme di 5 palline, per questo turno, non verranno
						// spawnate altre palline

						// altrimenti spawno le palline e torno al rigo 64

						if (!path.isEmpty()) {
							for (Path p : path) {
								jcell[p.getY()][p.getX()].setIcon(null);
								for (Star s : stars)
									if (s.getX() == p.getX() && s.getY() == p.getY())
										jcell[s.getY()][s.getX()].setIcon(factory.getStar(s.getV()));
							}
							path.clear();
						}else
							path.clear();

						cell[end.getY()][end.getX()] = cell[start.getY()][start.getX()];
						cell[start.getY()][start.getX()] = 0;
						jcell[end.getY()][end.getX()].setIcon(factory.getBall(cell[end.getY()][end.getX()]));
						jcell[start.getY()][start.getX()].setIcon(null);

						if (!FilaCompletata()) {
							for (Star s : stars) {
								if (end.getX() == s.getX() && end.getY() == s.getY())
									continue;
								setCell(s.getY(), s.getX(), s.getV());
							}
							stars.clear();

							FilaCompletata();

							chooseWhereSpawnBalls();
						} else {
							scoreLabel.setText("Scores " + (++scores));

							for (Star s : stars)
								if (end.getX() == s.getX() && end.getY() == s.getY()) {
									stars.remove(s);
									break;
								}
						}
					}

					switchScreen(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	private void creaPath(AnswerSet s) throws Exception {
		List<Used> used = new LinkedList<>();

		for (Object obj : s.getAtoms()) {
			if (obj instanceof Used) {
				Used o = (Used) obj;
				if (o.getX() == 10)
					continue;
				used.add(o);
//				System.out.println(o);
			}
		}

		path.add(new Path(0, start.getX(), start.getY()));
		used.remove(new Used(start.getX(), start.getY()));

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

				path.removeAll(path);
				i = 0;
				x = start.getX();
				y = start.getY();

				System.out.println("ERROR: Can't create Path!");
				for (Used u : used)
					System.out.println(u);
				return;
			}
			path.add(new Path(i, x, y));
			used.remove(new Used(x, y));
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

			boolean find = false;
			for (Star st : stars)
				if (st.getX() == now.getX() && st.getY() == now.getY()) {
					find = true;
					jcell[now.getY()][now.getX()].setIcon(combineImages(factory.getStar(st.getV()), img));
				}
			if (!find)
				jcell[now.getY()][now.getX()].setIcon(img);

		}
		jcell[end.getY()][end.getX()].setIcon(factory.getHere());
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

			while (cell[x][y] != Color.nullo.getVal() || stars.contains(new Star(y, x, c))) {
				x = random.nextInt(9);
				y = random.nextInt(9);
			}

			stars.add(new Star(y, x, c));
			jcell[x][y].setIcon(factory.getStar(c));
		}
	}

	private void initUI() {
		
		griglia = new JPanel();
		griglia.setPreferredSize(new Dimension(500, 480));
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

		manager = new JPanel();
		manager.setLayout(new BorderLayout());

		JButton next = new JButton(factory.getNext());
		next.setBorderPainted(false);
		next.setContentAreaFilled(false);
		next.setOpaque(true);

		next.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					next();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		JButton pathButton = new JButton("Path Off");
		pathButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				if (fase)
//					return;
				createPath = !createPath;
				if (createPath)
					((JButton) e.getSource()).setText("Path On");
				else
					((JButton) e.getSource()).setText("Path Off");
			}
		});

		JButton gridButton = new JButton("XY Off");
		gridButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				viewGrid = !viewGrid;

				if (viewGrid)
					((JButton) e.getSource()).setText("XY On");
				else
					((JButton) e.getSource()).setText("XY Off");

				griglia.removeAll();
				for (int i = 0; i < 9; ++i)
					for (int j = 0; j < 9; ++j) {
						g.gridx = i;
						g.gridy = j;
						if (viewGrid)
							griglia.add(new JLabel(j + "," + i), g);
						griglia.add(jcell[i][j], g);
						griglia.add(new JLabel(factory.getFloor()), g);
					}
			}
		});

		scoreLabel = new JLabel("Scores 0");

		loadingPanel = new JPanel();

		JLabel loadingLabel = new JLabel(factory.getLoading());
		loadingPanel.add(loadingLabel, BorderLayout.SOUTH);

		JPanel dx = new JPanel();
		dx.setLayout(new GridLayout(2, 1));
		dx.add(pathButton);
		dx.add(gridButton);

		manager.add(scoreLabel, BorderLayout.WEST);
		manager.add(next, BorderLayout.CENTER);
		manager.add(dx, BorderLayout.EAST);		

		add(manager, BorderLayout.SOUTH);

	}

	private void reset() {

		for (int i = 0; i < cell.length; i++)
			for (int j = 0; j < cell.length; j++) {
				cell[i][j] = 0;
				jcell[i][j].setIcon(null);
			}

		random = new Random();

		for (int init = 0, x, y, c; init < 5; init++) { // piazzo le prime 5 palline
			x = random.nextInt(9);
			y = random.nextInt(9);
			c = random.nextInt(4) + 1;

			while (cell[x][y] != Color.nullo.getVal()) {
				x = random.nextInt(9);
				y = random.nextInt(9);
			}

			setCell(x, y, c);
		}

		stars = new LinkedList<>();
		chooseWhereSpawnBalls(); // scelgo dove spawneranno le prossime 3 palline

		fase = false;
	}

	private void switchScreen(boolean loading) {
		if (loading) {
			remove(manager);
			add(loadingPanel, BorderLayout.SOUTH);
		} else {
			remove(loadingPanel);
			add(manager, BorderLayout.SOUTH);
		}
		repaint();
	}

	private int resetOrCloseDialog() {
		Object[] options = { "Yes", "No" };
		return JOptionPane.showOptionDialog(this.getParent(),
				"<html><body><div width='200px' align='center'>GAME OVER<br>" + "Scores : " + scores
						+ "<br>Retry?</div></body></html>",
				"BallLines", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

	}

	private void setCell(int x, int y, int c) {
		cell[x][y] = c;
		jcell[x][y].setIcon(factory.getBall(c));
	}
}
