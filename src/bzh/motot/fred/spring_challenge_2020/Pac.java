package bzh.motot.fred.spring_challenge_2020;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

public class Pac {
	public final int PAPER = 1;
	public final int ROCK = 2;
	public final int SCISSORS = 3;

	private static List<Pac> myList = new ArrayList<Pac>();
	private static List<Pac> oppList = new ArrayList<Pac>();
	public static List<String> orderList;

	private int pacId; // pac number (unique within a team)
	private int x; // position in the grid
	private int y; // position in the grid
	private int typeId; // unused in wood leagues
	private int speedTurnsLeft; // unused in wood leagues
	private int abilityCooldown; // unused in wood leagues

	/**
	 * @param pacId
	 * @param mine
	 * @param x
	 * @param y
	 * @param typeId
	 * @param speedTurnsLeft
	 * @param abilityCooldown
	 */
	public Pac(boolean mine, int pacId, int x, int y, String typeId, int speedTurnsLeft, int abilityCooldown) {

		this.pacId = pacId;
		this.x = x;
		this.y = y;
		this.setTypeId(typeId);
		this.speedTurnsLeft = speedTurnsLeft;
		this.abilityCooldown = abilityCooldown;

//		System.err.println(this.toString());
		if (mine) {
			if (Pac.getMyList().contains(this)) {
				Pac.updateMyList(this);
			} else {
				Pac.addMyList(this);
			}
		} else {
			if (Pac.getOppList().contains(this)) {
				Pac.updateOppList(this);
			} else {
				Pac.addOppList(this);
			}
		}
	}

	/**
	 * renvoie la liste des Squares visibles de tous les Pacs
	 * 
	 * @return
	 */
	public static Set<Square> getAllVisibleSquares() {
		Set<Square> list = new HashSet<Square>();

		for (Pac pac : myList) {
			list.addAll(pac.getVisibleSquares());
		}

		return list;
	}

	/**
	 * renvoie la liste des Squares visibles du Pac
	 * 
	 * @return
	 */
	private Set<Square> getVisibleSquares() {
		Set<Square> list = new HashSet<Square>();
		Square location = this.getLocation();

		Queue<Square> queue = new LinkedList<Square>();
		queue.add(location);

		while (queue.size() > 0) {
			Square current = queue.poll();

			for (Square square : current.getContiguousSquares()) {
				if (!list.contains(square) && (square.X == location.X || square.Y == location.Y)) {
					queue.add(square);
				}
			}
			list.add(current);
		}

		return list;
	}

	/**
	 * Renvois les ordres pour tous les Pacs
	 * 
	 * @return
	 */
	public static String getOrders() {
		String orders = "";

		System.err.println("Opp Pacs");
		for (Pac pac : oppList) {
			System.err.println(pac);
		}

		System.err.println("My Pacs");
		orderList = new ArrayList<String>();
		for (Pac pac : myList) {
			System.err.println(pac);
			String order = pac.getOrder();
			System.err.println("Ordre pour " + pac.pacId + " : " + order);
			orderList.add(order);
		}
		
		for (String order : orderList) {
			orders += order + " | ";
		}

		return orders;
	}

	public Square getLocation() {
		return Board.getInstance().getLocation(this);
	}

	/**
	 * indique la cible pour le Pac
	 * 
	 * @return
	 */
	private String getOrder() {
		String order = "";
		List<List<Square>> paths = new ArrayList<List<Square>>();

		int x = -1;
		int y = -1;

		// on liste les pacs ennemis avec position connu avec leur distance
		Map<Pac, List<Square>> enemies = new HashMap<Pac, List<Square>>();
		for (Pac pac : oppList) {
			try {
				if (pac.getX() != -1) {
					enemies.put(pac, this.getLocation().shortestPathWithoutBlock(pac.getLocation()));
				}
			} catch (PathNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// TODO voir les cas ou 2 pac ennemi ou plus sont à proximité
		// TODO garder en mémoire plus longtemps la position d'un Pac ennemi (voir extrapoler ses nouvelles positions possibles)
		// TODO si ennemi peut aller sur prochaine case, vérifier si WIN
		// TODO avancer des 2 cases aussi pour les nearest et les attaques ?

		// s'il y a un pac à moins de X cases, je vois si je peux l'attaquer
		for (Entry<Pac, List<Square>> entry : enemies.entrySet()) {
			if (entry.getValue().size() < 5) {
				order = this.attackPac(entry);
			}
		}
		
		if (order.equals("")) {

			if (this.getAbilityCooldown() == 0) {
				// j'accélère si je peux
				order = "SPEED " + this.getPacId();
			} else {
				// sinon je me déplace
				order = "MOVE " + this.getPacId() + " ";
				

				if (Square.getListBigPellet().size() > 0) {
					// vers la grosse pastille la plus proche si elle existe
					for (Square bigPellet : Square.getListBigPellet()) {
						try {
							paths.add(this.getLocation().shortestPathWithBlock(bigPellet));
						} catch (PathNotFoundException e) {
							// TODO Auto-generated catch block
							System.err.println(e.getMessage());
						}
					}

					List<Square> path = null;
					if (paths.size() > 0) {
						path = paths.stream().min(new Comparator<List>() {

							@Override
							public int compare(List o1, List o2) {
								if (o1.size() > o2.size()) {
									return 1;
								} else if (o1.size() < o2.size()) {
									return -1;
								}
								return 0;
							}
						}).get();
						// je me dirige vers la prochaine pastille sur mon chemin
						if (this.getSpeedTurnsLeft() > 0 && path.size() > 2) {
							x = path.get(2).X;
							y = path.get(2).Y;
						} else {
							x = path.get(1).X;
							y = path.get(1).Y;
						}
					}
				}

				if (x == -1) {
					// la première pastille connue la plus proche
					Square target = null;

					try {
						target = this.getLocation().nearestPellet();
						
						// TODO modifier nearest
						target = this.getLocation().shortestPathWithBlock(target).get(1);
					} catch (PathNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						target = this.getLocation();
					}
					x = target.X;
					y = target.Y;
				}
				order += x + " " + y;
			}
		}
		
		return order;
	}

	private String attackPac(Entry<Pac, List<Square>> entry) {
		String switchPac = "";
		Pac pac = entry.getKey();
		List<Square> path = entry.getValue();

		if (this.getAbilityCooldown() == 0) {

			switch (this.getTypeId()) {
			case ROCK:
				if (pac.getTypeId() == ROCK) {
					switchPac = "PAPER";
				} else if (pac.getTypeId() == PAPER) {
					switchPac = "SCISSORS";
				}
				System.err.println(switchPac);
				break;
			case PAPER:
				if (pac.getTypeId() == SCISSORS) {
					switchPac = "ROCK";
				} else if (pac.getTypeId() == PAPER) {
					switchPac = "SCISSORS";
				}
				break;
			case SCISSORS:
				if (pac.getTypeId() == SCISSORS) {
					switchPac = "ROCK";
				} else if (pac.getTypeId() == ROCK) {
					switchPac = "PAPER";
				}
				break;
			default:
				System.err.println("Erreur de type");
			}
		}

		if (switchPac.equals("")) {
			if (this.win(pac) && pac.abilityCooldown != 0) {
				switchPac = "MOVE " + this.getPacId() + " " + path.get(1).X + " " + path.get(1).Y;
			}
		} else {
			switchPac = "SWITCH " + this.getPacId() + " " + switchPac;
		}

		return switchPac;
	}

	private boolean win(Pac enemy) {
		boolean win = false;
		if ((this.getTypeId() == ROCK && enemy.getTypeId() == SCISSORS)
				|| (this.getTypeId() == SCISSORS && enemy.getTypeId() == PAPER)
				|| (this.getTypeId() == PAPER && enemy.getTypeId() == ROCK)) {
			win = true;
		}

		return win;
	}

	/**
	 * Retire les Pacs morts dans la liste de mes Pacs
	 * 
	 * @param listremaining mes Pacs restant
	 */
	public static void removeKilledPacs(List<Integer> listremaining) {
		List<Pac> killed = new ArrayList<Pac>();

		for (Pac pac : myList) {
			if (!listremaining.contains(pac.getPacId())) {
				killed.add(pac);
			}
		}

		myList.removeAll(killed);
	}

	/**
	 * met à -1 la position x du pac ennemi perdu de vue
	 * 
	 * @param oppPacsLocationKnown
	 */
	public static void lostPacs(List<Integer> oppPacsLocationKnown) {
		List<Pac> lost = new ArrayList<Pac>();

		for (Pac pac : oppList) {
			if (!oppPacsLocationKnown.contains(pac.getPacId())) {
				pac.setX(-1);
			}
		}
	}

	/**
	 * retourne si le Pac est égal à un autre
	 */
	public boolean equals(Object pac) {
		boolean equal = false;

		if (pac instanceof Pac) {
			if (this.pacId == ((Pac) pac).getPacId()) {
				equal = true;
			}
		}

		return equal;
	}

	/**
	 * @return the myList
	 */
	public static List<Pac> getMyList() {
		return myList;
	}

	/**
	 * @param pac the Pac to add
	 */
	public static void addMyList(Pac pac) {
		Pac.myList.add(pac);
	}

	/**
	 * @param pac the Pac to update
	 */
	public static void updateMyList(Pac pac) {
		Pac.myList.set(Pac.getMyList().indexOf((Pac) pac), pac);
	}

	/**
	 * @return the oppList
	 */
	public static List<Pac> getOppList() {
		return oppList;
	}

	/**
	 * @param pac the Pac to add
	 */
	public static void addOppList(Pac pac) {
		Pac.oppList.add(pac);
	}

	/**
	 * @param pac the Pac to update
	 */
	public static void updateOppList(Pac pac) {
		Pac.oppList.set(Pac.getOppList().indexOf((Pac) pac), pac);
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * @return the typeId
	 */
	public int getTypeId() {
		return typeId;
	}

	/**
	 * @param typeId the typeId to set
	 */
	public void setTypeId(String typeId) {
		switch (typeId) {
		case "ROCK":
			this.typeId = ROCK;
			break;
		case "PAPER":
			this.typeId = PAPER;
			break;
		case "SCISSORS":
			this.typeId = SCISSORS;
			break;
		}
	}

	/**
	 * @return the speedTurnsLeft
	 */
	public int getSpeedTurnsLeft() {
		return speedTurnsLeft;
	}

	/**
	 * @param speedTurnsLeft the speedTurnsLeft to set
	 */
	public void setSpeedTurnsLeft(int speedTurnsLeft) {
		this.speedTurnsLeft = speedTurnsLeft;
	}

	/**
	 * @return the abilityCooldown
	 */
	public int getAbilityCooldown() {
		return abilityCooldown;
	}

	/**
	 * @param abilityCooldown the abilityCooldown to set
	 */
	public void setAbilityCooldown(int abilityCooldown) {
		this.abilityCooldown = abilityCooldown;
	}

	/**
	 * @return the pacId
	 */
	public int getPacId() {
		return pacId;
	}

	@Override
	public String toString() {
		return "Pac [pacId=" + pacId + ", x=" + x + ", y=" + y + ", typeId=" + typeId + ", speedTurnsLeft="
				+ speedTurnsLeft + ", abilityCooldown=" + abilityCooldown + "]";
	}

}
