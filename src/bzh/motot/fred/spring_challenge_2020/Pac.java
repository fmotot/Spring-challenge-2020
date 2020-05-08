package bzh.motot.fred.spring_challenge_2020;

import java.util.ArrayList;
import java.util.List;

public class Pac {
	private static List<Pac> myList = new ArrayList<Pac>();
	private static List<Pac> oppList = new ArrayList<Pac>();

	private int pacId; // pac number (unique within a team)
	private int x; // position in the grid
	private int y; // position in the grid
	private String typeId; // unused in wood leagues
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
		this.typeId = typeId;
		this.speedTurnsLeft = speedTurnsLeft;
		this.abilityCooldown = abilityCooldown;

		if (mine) {
			if (Pac.getMyList().contains(this)) {
				Pac.updateMyList(this);
			} else {
				Pac.addMyList(this);
			}
		} else {
			if (Pac.getOppList().contains(this)) {
				Pac.updateOppList(this);
			}
			else {
				Pac.addOppList(this);
			}
		}
	}
	
	/**
	 * Renvois les ordres pour tous les Pacs
	 * @return
	 */
	public static String getOrders() {
		String orders = "";
		int x = -1;
		int y = -1;
		
		for (Pac pac : myList) {
			if (orders.length() > 0) {
				orders += " | ";
			}
			if (Square.getListBigPellet().size() > 0) {
				Square target = Square.getListBigPellet().get(0);
				x = target.X;
				y = target.Y;
			}
			else {
				Square target = Square.getListPellet().get(0);
				x = target.X;
				y = target.Y;
			}
			orders += "MOVE " + pac.getPacId() + " " + x + " " + y;
			
		}
		
		return orders;
	}

	/**
	 * Retire les Pacs morts dans la liste de mes Pacs
	 * @param listremaining		mes Pacs restant
	 */
	public static void removeKilledPacs(List<Integer> listremaining) {
		List<Pac> killed = new ArrayList<Pac>();
		
		for (Pac pac : myList) {
			if (!listremaining.contains(pac.getPacId())){
				killed.add(pac);
			}
		}
		
		myList.removeAll(killed);
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
	public String getTypeId() {
		return typeId;
	}

	/**
	 * @param typeId the typeId to set
	 */
	public void setTypeId(String typeId) {
		this.typeId = typeId;
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
