package bzh.motot.fred.spring_challenge_2020;

/**
 * représente le graphe composé de vertex Square
 * @author fmoto
 *
 */
public class Board {
	public final int WIDTH;
	public final int HEIGHT;
	
	private Square[][] board;
	
	public Board(int width, int height) {
		this.HEIGHT = height;
		this.WIDTH = width;
		
		this.board = new Square[this.WIDTH][this.HEIGHT];
	}
	
	/**
	 * Ajoute la ligne numéro rowNb en créant les Squares si des cases ne contiennent pas de mur 
	 * @param rowNb
	 * @param rowStr
	 */
	public void addRow(int rowNb, String rowStr) {
		char[] row = rowStr.toCharArray();
		
		for(int x = 0; x < row.length; x++) {
			if (row[x] == ' '){
				this.board[x][rowNb] = new Square(x, rowNb);
			}
		}
	}

	public void setPelletValue(int x, int y, int pelletValue) {
		this.board[x][y].setPelletValue(pelletValue);
	}
}
