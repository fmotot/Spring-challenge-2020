package bzh.motot.fred.spring_challenge_2020;

import java.util.ArrayList;
import java.util.List;

/**
 * représente une case de chemin sur le plateau (vertex)
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
	
	private int pelletValue;
	private List<Square> contiguousSquares = new ArrayList<Square>();

	public Square(int x, int y, int pelletValue) {
		this(x, y);
		this.pelletValue = pelletValue;
		Square.list.add(this);
		
	}
	
	public Square(int x, int y) {
		this.X = x;
		this.Y = y;
		Square.list.add(this);
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
		} 
		else if (this.getPelletValue() == 0 && pelletValue == 10) {
			Square.listBigPellet.add(this);
		}
		else if (this.getPelletValue() == 1 && pelletValue == 0) {
			Square.listPellet.remove(this);
		} 
		else if (this.getPelletValue() == 0 && pelletValue == 1) {
			Square.listPellet.add(this);
		}
		this.pelletValue = pelletValue;
	}

	/**
	 * Retourne la liste des squares contenant un pellet à 1
	 * @return the listPellet
	 */
	public static List<Square> getListPellet() {
		return listPellet;
	}

	/**
	 * Retourne la liste de tous les Square
	 * @return the list
	 */
	public static List<Square> getList() {
		return list;
	}

	/**
	 * Retourne la liste des squares contenant un pellet à 10
	 * @return the listBigPellet
	 */
	public static List<Square> getListBigPellet() {
		return listBigPellet;
	}

	/**
	 * Retourne la liste des squares contigües
	 * @return the squares
	 */
	public List<Square> getContiguousSquares() {
		return contiguousSquares;
	}

	/**
	 * Ajoute le Square à la liste des Square contigües et inversement
	 * @param square 
	 */
	public void addContiguousSquare(Square square) {
		this.contiguousSquares.add(square);
		square.contiguousSquares.add(this);
	}

}
