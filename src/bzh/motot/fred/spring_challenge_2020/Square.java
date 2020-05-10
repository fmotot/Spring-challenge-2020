package bzh.motot.fred.spring_challenge_2020;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * représente une case de chemin sur le plateau (vertex)
 * 
 * @author fmoto
 *
 */
public class Square {
	/**
	 * La liste de toutes les cases
	 */
	private static List<Square> list = new ArrayList<Square>();

	/**
	 * La liste de toutes les cases contenant une grosse pastille
	 */
	private static List<Square> listBigPellet = new ArrayList<Square>();
	private static List<Square> listPellet = new ArrayList<Square>();

	public final int X;
	public final int Y;

	private int pelletValue = 1;
	private List<Square> contiguousSquares = new ArrayList<Square>();

	private int cost;
	private int heuristic;
	private Square cameFrom;

	public Square(int x, int y) {
		this.X = x;
		this.Y = y;
		Square.list.add(this);
		Square.listPellet = Square.list.stream().collect(Collectors.toList());
	}

	/**
	 * parmi les squares visibles de tous les pac, on met à 0 la pastille de ceux
	 * qui ne sont pas dans la liste des pastilles visibles
	 * 
	 * @param visiblePellets
	 */
	public static void setVisiblePellets(Set<Square> visiblePellets) {
		Set<Square> list = Pac.getAllVisibleSquares();
		list.removeAll(visiblePellets);

		for (Square square : list) {
			square.setPelletValue(0);
		}
	}

	public Square nearestPellet() throws PathNotFoundException {
		initPathFinding();
		Square nearest = null;
		Queue<Square> list = new LinkedList<Square>();
		list.add(this);

		while (nearest == null) {
			Square current = list.poll();

			try {
				for (Square square : current.contiguousSquares) {
					if (!list.contains(square) && square.isFree() && square.getCameFrom() == null) {
						list.add(square);
						square.setCameFrom(current);
					}
				}
			} catch (NullPointerException e) {
				// TODO: handle exception
				throw new PathNotFoundException("Pas de pellet à portée");
			}

			if (current.getPelletValue() > 0) {
				nearest = current;
			}
		}

		return nearest;
	}

	/**
	 * renvoie le chemin le plus court depuis start vers target
	 * 
	 * @param target
	 * @param start
	 * @return
	 * @throws PathNotFoundException
	 */
	public List<Square> shortestPathWithBlock(Square target) throws PathNotFoundException {
		return this.shortestPath(target, true);
	}

	public List<Square> shortestPath(Square target, boolean withBlock) throws PathNotFoundException {
		Square.initPathFinding();
		
		if (target == null) {
			throw new PathNotFoundException("Impossible de trouver un chemin vers nulle part");
		}
		
		Queue<Square> open = new PriorityQueue<Square>(new Comparator<Square>() {

			@Override
			public int compare(Square o1, Square o2) {
				if (o1.getHeuristic() <= o2.getHeuristic()) {
					return -1;
				} else if (o1.getHeuristic() > o2.getHeuristic()) {
					return 1;
				}
				return 0;
			}
		});

		this.setCost(0);
		this.setHeuristic(this.calcHeuristic(target));
		open.add(this);

		while (open.size() > 0) {
			Square current = open.poll();

			if (current == target) {
				// on récupère le chemin
				return current.getPath();
			}

			for (Square square : current.contiguousSquares) {
				int cost = current.getCost() + 1;

				// si le square contient un Pac alors on ne va pas par là
				boolean isFree = withBlock ? square.isFree() : true;

				if (isFree && cost < square.getCost()) {
					square.setCameFrom(current);
					square.setCost(cost);
					square.setHeuristic(square.getCost() + square.calcHeuristic(target));

					if (!open.contains(square)) {
						open.add(square);
					}
				}
			}
		}

		throw new PathNotFoundException(
				"Aucun chemin trouvé de " + this.X + " " + this.Y + " vers " + target.X + " " + target.Y);
	}

	public List<Square> shortestPathWithoutBlock(Square target) throws PathNotFoundException {
		return this.shortestPath(target, false);
	}

	/**
	 * indique si le Square est libre (aucun Pac dessus)
	 * 
	 * @return
	 */
	private boolean isFree() {
		for (Pac pac : Pac.getMyList()) {
			if (this == pac.getLocation()) {
				return false;
			}
		}
		
		for (String str : Pac.orderList) {
			String[] order = str.split(" ");
//			System.err.println(Arrays.asList(order));
			if (order[0].equals("MOVE")) {
				if (this.X == Integer.parseInt(order[2]) && this.Y == Integer.parseInt(order[3])) {
					return false;
				}
			}
		}

		for (Pac pac : Pac.getOppList()) {
			if (this == pac.getLocation()) {
				return false;
			}
		}

		return true;
	}

	/**
	 * calcule la distance minimal s'il n'y a pas de mur (idéal)
	 * 
	 * @param target
	 * @return
	 */
	private int calcHeuristic(Square target) {
		int heuristic = Math.abs(this.Y - target.Y);

		Square min = this.X <= target.X ? this : target;
		Square max = this.X >= target.X ? this : target;

		heuristic += Math.min((min.X - max.X + Board.getInstance().WIDTH), Math.abs(this.X - target.X));

		return heuristic;
	}

	/**
	 * initialise les variables des Squares pour le pathfinding
	 */
	private static void initPathFinding() {
		for (Square square : Square.getList()) {
			square.setCameFrom(null);
			square.setCost(9999);
			square.setHeuristic(9999);
		}
	}

	/**
	 * recrée le chemin depuis la liste des chemins d'où vient le Square
	 * 
	 * @param cameFrom For node n, cameFrom[n] is the node immediately preceding it
	 *                 on the cheapest path from start
	 * @param current  le node depuis lequel on recontruit le chemin
	 * @return
	 */
	private List<Square> getPath() {
		LinkedList<Square> path = new LinkedList<Square>();
		Square current = this;

		while (current != null) {
			path.addFirst(current);
			current = current.getCameFrom();
		}
		return path;
	}

	/**
	 * @return the pelletValue
	 */
	public int getPelletValue() {
		return pelletValue;
	}

	/**
	 * @param pelletValue the pelletValue to set
	 */
	public void setPelletValue(int pelletValue) {
		if (this.getPelletValue() == 10 && pelletValue == 0) {
			Square.listBigPellet.remove(this);
		} else if (this.getPelletValue() == 1 && pelletValue == 10) {
			Square.listBigPellet.add(this);
			Square.listPellet.remove(this);
		} else if (this.getPelletValue() == 1 && pelletValue == 0) {
			Square.listPellet.remove(this);
		}
		this.pelletValue = pelletValue;
	}

	/**
	 * Retourne la liste des squares contenant un pellet à 1
	 * 
	 * @return the listPellet
	 */
	public static List<Square> getListPellet() {
		return listPellet;
	}

	/**
	 * Retourne la liste de tous les Square
	 * 
	 * @return the list
	 */
	public static List<Square> getList() {
		return list;
	}

	/**
	 * Retourne la liste des squares contenant un pellet à 10
	 * 
	 * @return the listBigPellet
	 */
	public static List<Square> getListBigPellet() {
		return listBigPellet;
	}

	/**
	 * Retourne la liste des squares contigües
	 * 
	 * @return the squares
	 */
	public List<Square> getContiguousSquares() {
		return contiguousSquares;
	}

	/**
	 * Ajoute le Square à la liste des Square contigües et inversement
	 * 
	 * @param square
	 */
	public void addContiguousSquare(Square square) {
		this.contiguousSquares.add(square);
		square.contiguousSquares.add(this);
	}

	/**
	 * @return the cost
	 */
	public int getCost() {
		return cost;
	}

	/**
	 * @param cost the cost to set
	 */
	public void setCost(int cost) {
		this.cost = cost;
	}

	/**
	 * @return the heuristic
	 */
	public int getHeuristic() {
		return heuristic;
	}

	/**
	 * @param heuristic the heuristic to set
	 */
	public void setHeuristic(int heuristic) {
		this.heuristic = heuristic;
	}

	/**
	 * @return the cameFrom
	 */
	public Square getCameFrom() {
		return cameFrom;
	}

	/**
	 * @param cameFrom the cameFrom to set
	 */
	public void setCameFrom(Square cameFrom) {
		this.cameFrom = cameFrom;
	}

	@Override
	public String toString() {
		return "Square [X=" + X + ", Y=" + Y + ", pellet=" + pelletValue + "]";
	}

}
