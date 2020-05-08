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
	private List<Square> squares = new ArrayList<Square>();

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
	 * @return the listPellet
	 */
	public static List<Square> getListPellet() {
		return listPellet;
	}

	/**
	 * @return the list
	 */
	public static List<Square> getList() {
		return list;
	}

	/**
	 * @return the listBigPellet
	 */
	public static List<Square> getListBigPellet() {
		return listBigPellet;
	}

	/**
	 * @return the squares
	 */
	public List<Square> getSquares() {
		return squares;
	}

	/**
	 * @param squares the squares to set
	 */
	public void addSquare(Square square) {
		this.squares.add(square);
	}

}
