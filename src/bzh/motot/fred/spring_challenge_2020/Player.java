package bzh.motot.fred.spring_challenge_2020;










import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Grab the pellets as fast as you can!
 **/
class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int width = in.nextInt(); // size of the grid
        int height = in.nextInt(); // top left corner is (x=0, y=0)
        if (in.hasNextLine()) {
            in.nextLine();
        }
        for (int i = 0; i < height; i++) {
            String row = in.nextLine(); // one line of the grid: space " " is floor, pound "#" is wall
        }

        // game loop
        int xMove = -1;
        int yMove = -1;
        while (true) {
            int myScore = in.nextInt();
            System.err.println("myScore : " + myScore);
            int opponentScore = in.nextInt();
            System.err.println("opponentScore : " + opponentScore);
            int visiblePacCount = in.nextInt(); // all your pacs and enemy pacs in sight
            System.err.println("visiblePacCount : " + visiblePacCount);
            for (int i = 0; i < visiblePacCount; i++) {
                int pacId = in.nextInt(); // pac number (unique within a team)
                   System.err.println("pacId : " + pacId);
                boolean mine = in.nextInt() != 0; // true if this pac is yours
                   System.err.println("mine : " + mine);
                int x = in.nextInt(); // position in the grid
                   System.err.println(x);
                int y = in.nextInt(); // position in the grid
                   System.err.println(y);
                String typeId = in.next(); // unused in wood leagues
                int speedTurnsLeft = in.nextInt(); // unused in wood leagues
                int abilityCooldown = in.nextInt(); // unused in wood leagues
                if (mine && xMove == x && yMove == y){
                    xMove = -1;
                    yMove = -1;
                }
            }
            
            int visiblePelletCount = in.nextInt(); // all pellets in sight
            int[][] pellets = new int[visiblePelletCount][2];
            for (int i = 0; i < visiblePelletCount; i++) {
                int x = in.nextInt();
                System.err.println(x);
                int y = in.nextInt();
                System.err.println(y);
                int value = in.nextInt(); // amount of points this pellet is worth
                System.err.println("value : " + value);

                pellets[i][0] = x;
                pellets[i][1] = y;
                if (value == 10){
                    xMove = x;
                    yMove = y;
                }
            }


            if (xMove == -1){
                int[] pellet = pellets[new Random().nextInt(visiblePelletCount)];
                xMove = pellet[0];
                yMove = pellet[1];
            }

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");

            System.out.println("MOVE 0 " + xMove + " " + yMove); // MOVE <pacId> <x> <y>
        }
    }
}