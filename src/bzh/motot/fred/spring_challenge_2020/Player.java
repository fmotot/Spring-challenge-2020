package bzh.motot.fred.spring_challenge_2020;










import java.util.*;
import java.io.*;
import java.math.*;
import java.util.stream.Collectors;
import java.util.Map.Entry;

/**
 * Grab the pellets as fast as you can!
 **/
class Player {
	public static long start;

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int width = in.nextInt(); // size of the grid
        int height = in.nextInt(); // top left corner is (x=0, y=0)
        Board board = Board.set(width, height);
        if (in.hasNextLine()) {
            in.nextLine();
        }
        for (int i = 0; i < height; i++) {
            board.addRow(i, in.nextLine()); // one line of the grid: space " " is floor, pound "#" is wall
        }

        // game loop
        int turn = 1;
        while (true) {
            int myScore = in.nextInt();
            start = System.currentTimeMillis();
            System.err.println("début du tour");
            int opponentScore = in.nextInt();
            int visiblePacCount = in.nextInt(); // all your pacs and enemy pacs in sight
            List<Integer> myPacsRemaining = new ArrayList<Integer>();
            List<Integer> oppPacsLocationKnown = new ArrayList<Integer>();
            for (int i = 0; i < visiblePacCount; i++) {
                int pacId = in.nextInt(); // pac number (unique within a team)
                boolean mine = in.nextInt() != 0; // true if this pac is yours
                if (mine) {
                	myPacsRemaining.add(pacId);
                }
                else {
                	oppPacsLocationKnown.add(pacId);
                }
                int x = in.nextInt(); // position in the grid
                int y = in.nextInt(); // position in the grid
                String typeId = in.next(); // unused in wood leagues
                int speedTurnsLeft = in.nextInt(); // unused in wood leagues
                int abilityCooldown = in.nextInt(); // unused in wood leagues
                new Pac(mine, pacId, x, y, typeId, speedTurnsLeft, abilityCooldown);

                // on met la valeur de la pastille de la case à 0 car un Pac est dessus 
                if (turn != 1) {
                	board.setPelletValue(x, y, 0);
                }
            }
            Pac.removeKilledPacs(myPacsRemaining);
            Pac.lostPacs(oppPacsLocationKnown);
            
            
            // TODO la vue de mes Pacs contre les pellets visibles pour mettre à 0 ceux qui n'en contiennent plus
            int visiblePelletCount = in.nextInt(); // all pellets in sight
            Set<Square> visiblePellets = new HashSet<Square>();
            for (int i = 0; i < visiblePelletCount; i++) {

            	Square square = board.getSquare(in.nextInt(), in.nextInt());
            	square.setPelletValue(in.nextInt());
            	visiblePellets.add(square);
            }

            Square.setVisiblePellets(visiblePellets);

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");
            System.out.println(Pac.getOrders()); // MOVE <pacId> <x> <y>
            System.err.print("Fin du tour, ");
            Player.time();
            turn++;
        }
    }
    
    public static void time() {
    	System.err.println("temps passé : " + (System.currentTimeMillis() - start));
    }
}