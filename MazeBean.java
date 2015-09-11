import java.awt.List;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MazeBean implements Serializable {
	
	int GridSize,Treasure,TreasureCount;
	ArrayList PlayerList=new ArrayList();
	
	public int getGridSize() {
		return this.GridSize;
	}

	public ArrayList getPlayerList() {
		return this.PlayerList;
	}

	public int getTreasureCount() {
		return TreasureCount;
	}

	public void setTreasureCount(int treasureCount) {
		TreasureCount = treasureCount;
	}

	public void setPlayerList(int playerID) {
		this.PlayerList.add(playerID);
	}

	public void setGridSize(int gridSize) {
		this.GridSize = gridSize;
	}

	public int getTreasure() {
		return this.Treasure;
	}

	public void setTreasure(int treasure) {
		this.Treasure = treasure;
	}
	    	
	
	
	
	
	
}
