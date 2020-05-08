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
	 * Ajoute la ligne numéro y en créant les Squares si des cases ne contiennent pas de murµ
	 * Lie les Squares entre eux
	 * @param y 		le numéro de la ligne
	 * @param rowStr	la chaîne de caractère constituant la ligne
	 */
	public void addRow(int y, String rowStr) {
		char[] row = rowStr.toCharArray();
		
		for(int x = 0; x < row.length; x++) {
			if (row[x] == ' '){
				this.board[x][y] = new Square(x, y);
				
				// Ajout des liens entre les Squares
				// sur l'axe horizontal
				if (x > 0) {
					if (this.board[x - 1][y] != null) {
						this.board[x - 1][y].addContiguousSquare(this.board[x][y]);
						if (x == WIDTH - 1) {
							if (this.board[0][y] != null) {
								this.board[0][y].addContiguousSquare(this.board[x][y]);
							}
						}
					}
				}
				// sur la ligne précédente
				if (y > 0) {
					if (this.board[x][y - 1] != null) {
						this.board[x][y - 1].addContiguousSquare(this.board[x][y]);
					}
				}
			}
		}
	}

	public void setPelletValue(int x, int y, int pelletValue) {
		this.board[x][y].setPelletValue(pelletValue);
	}
}
