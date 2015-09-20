import java.io.Serializable;


public class PlayerInfo implements Serializable{
	  int PlayerID;
	  String PlayerName;
	  int PlayerPosition;
	  int collectedTreasure;
	  boolean Primary;
	  boolean Secondary;
	  boolean Active,winner;
	  private String responseFromServer; 
	  
	  public String getResponseFromServer() {
		return responseFromServer;
	}

	public void setResponseFromServer(String responseFromServer) {
		this.responseFromServer = responseFromServer;
	}

	public boolean isWinner() {
		return winner;
	}

	public void setWinner(boolean winner) {
		this.winner = winner;
	}
	int PlayerPort;


	public int getPlayerPort() {
		return PlayerPort;
	}

	public void setPlayerPort(int playerPort) {
		PlayerPort = playerPort;
	}

	public boolean isActive() {
		return Active;
	}

	public void setActive(boolean active) {
		Active = active;
	}

	public boolean isSecondary() {
		return Secondary;
	}

	public boolean isPrimary() {
		return Primary;
	}

	public void setPrimary(boolean primary) {
		Primary = primary;
	}

	public void setSecondary(boolean secondary) {
		Secondary = secondary;
	}

	public int getCollectedTreasure() {
		return collectedTreasure;
	}

	public void setCollectedTreasure(int collectedTreasure) {
		this.collectedTreasure = collectedTreasure;
	}

	public int getPlayerPosition() {
		return PlayerPosition;
	}
	
	public void setPlayerPosition(int playerPosition) {
		PlayerPosition = playerPosition;
	}
	public int getPlayerID() {
		return PlayerID;
	}
	public void setPlayerID(int playerID) {
		PlayerID = playerID;
	}
	public String getPlayerName() {
		return PlayerName;
	}
	public void setPlayerName(String playerName) {
		PlayerName = playerName;
	}
	
	  
	
}
