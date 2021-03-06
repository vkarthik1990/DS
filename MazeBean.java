import java.awt.List;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JLabel;


public class MazeBean implements Serializable {
	
	private int GridSize,Treasure,TreasureCount;
	private long GameStartTime=0L;
	private Map<Integer, PlayerInfo> PlayerList=new ConcurrentHashMap<Integer, PlayerInfo>();
	static boolean Gameover=false;
	boolean joinFlag=true;
	static int DefaultPrimaryPort=2015;

	//Storing Primary and Secondary IPAddress and port for use in crash handling scenarios
	private int primaryPortNum;
	private String primaryIPaddress="";
	private int secondaryPortNum;
	private String secondaryIPaddress="";
	
	
	public boolean isJoinFlag() {
		return joinFlag;
	}

	public void setJoinFlag(boolean joinFlag) {
		this.joinFlag = joinFlag;
	}
	
	public int getPrimaryPortNum() {
		return primaryPortNum;
	}

	public void setPrimaryPortNum(int primaryPortNum) {
		this.primaryPortNum = primaryPortNum;
	}

	public String getPrimaryIPaddress() {
		return primaryIPaddress;
	}

	public void setPrimaryIPaddress(String primaryIPaddress) {
		this.primaryIPaddress = primaryIPaddress;
	}

	public int getSecondaryPortNum() {
		return secondaryPortNum;
	}

	public void setSecondaryPortNum(int secondaryPortNum) {
		this.secondaryPortNum = secondaryPortNum;
	}

	public String getSecondaryIPaddress() {
		return secondaryIPaddress;
	}

	public void setSecondaryIPaddress(String secondaryIPaddress) {
		this.secondaryIPaddress = secondaryIPaddress;
	}

	
	public boolean isGameover() {
		return Gameover;
	}

	public void setGameover(boolean gameover) {
		Gameover = gameover;
	}

	public long getGameStartTime() {
		return GameStartTime;
	}

	public void setGameStartTime(long gameStartTime) {
		GameStartTime = gameStartTime;
	}

	private boolean IsPlayerActive;
	private boolean IsJoinRequest=false;
	Random randomize = new Random();
	private Map<Integer, String> MazeGrid;
	
	
	
	public Map<Integer, String> getMazeGrid() {
		return MazeGrid;
	}

	public void setMazeGrid(Map<Integer, String> mazeGrid) {
		MazeGrid = mazeGrid;
	}

	private int[][] coordinates;
	
	
	
	public boolean isIsJoinRequest() {
		return IsJoinRequest;
	}

	public void setIsJoinRequest(boolean isJoinRequest) {
		IsJoinRequest = isJoinRequest;
	}

	public boolean isIsPlayerActive() {
		return IsPlayerActive;
	}

	public void setIsPlayerActive(boolean isPlayerActive) {
		IsPlayerActive = isPlayerActive;
	}
	
	public int getGridSize() {
		return this.GridSize;
	}

	public Map<Integer, PlayerInfo> getPlayerList() {
		return this.PlayerList;
	}

	public int getTreasureCount() {
		return TreasureCount;
	}

	public void setTreasureCount(int treasureCount) {
		TreasureCount = treasureCount;
	}

	public void setPlayerList(Map<Integer, PlayerInfo> players) {
		this.PlayerList=players;
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
	    	
	
	public synchronized void AddPlayertoMaze(PlayerInfo PlayerObj){
		int randRow,randColumn,counter = 0;
		boolean IsSuccess=false;
				// Intialize the Player Positions
				
			while(!IsSuccess){
				randRow = randomize.nextInt(this.GridSize);
				randColumn = randomize.nextInt(this.GridSize);
				
				if(!("X").equals(MazeGrid.get(coordinates[randRow][randColumn])) && ("0").equals(MazeGrid.get(coordinates[randRow][randColumn]))){
					MazeGrid.put(coordinates[randRow][randColumn], PlayerObj.getPlayerName()+"["+PlayerObj.getCollectedTreasure()+"]");
					PlayerObj.setPlayerPosition(coordinates[randRow][randColumn]);
					IsSuccess=true;
				}
				else if(("X").equals(MazeGrid.get(coordinates[randRow][randColumn]))){
					MazeGrid.put(coordinates[randRow][randColumn], PlayerObj.getPlayerName()+"["+PlayerObj.getCollectedTreasure()+"]");
					PlayerObj.collectedTreasure++;
					this.setTreasureCount(this.getTreasureCount()-1);
					IsSuccess=true;
					System.out.println(">>>> Hurray ! You have collected a tressure without moving....");
					PlayerObj.setPlayerPosition(coordinates[randRow][randColumn]);
				}
			}
	}
	
	
	public void InitializeGrid(){
		// Initialize the Mazegrid
				MazeGrid = new HashMap<Integer, String>();
				int temp = 0;
				int N=this.getGridSize();
				coordinates = new int[N][N];
				
				for (int i = 0; i < N; i++) {
					for (int j = 0; j < N; j++) {
						coordinates[i][j] = temp;
						MazeGrid.put(temp, "0");
						temp++;
					}
					System.out.println();
				}
				RandomTressurePos();
	}
	
	public void RandomTressurePos(){
		int randRow, randColumn, counter = 0;
			// Intialize the Random Tressure Positions
				while (counter < this.getTreasure()) {
					randRow = randomize.nextInt(this.GridSize);
					randColumn = randomize.nextInt(this.GridSize);
					if (MazeGrid.get(coordinates[randRow][randColumn]) != "X") {
						MazeGrid.put(coordinates[randRow][randColumn], "X");
						++counter;
					}
				}
	}
	
	
	/* Player Direction controls are placed here */
	public void PlayerMoves(PlayerInfo PlayerObj, String direction, int GridSize) {
			
		PlayerObj=MazeP2P.Beanobj.getPlayerList().get(PlayerObj.getPlayerID());
		ArrayList playercoordinates = getRowcolumns(PlayerObj.getPlayerPosition());
		int currentRow = (int) playercoordinates.get(0);
		int currentColumn = (int) playercoordinates.get(1);
		int newRow = currentRow;
		int newColumn = currentColumn;
		int CollectedTreassure = PlayerObj.getCollectedTreasure();

		if (direction.equalsIgnoreCase("w") && (currentRow - 1) != -1) // Top
		{
			newRow = currentRow - 1;
			if (MazeGrid.get(coordinates[newRow][newColumn]).equals("0")
					|| MazeGrid.get(coordinates[newRow][newColumn]).equals("X")) {
				if (MazeGrid.get(coordinates[newRow][newColumn]).equals("X")) {
					PlayerObj.setCollectedTreasure(CollectedTreassure + 1);
					MazeP2P.Beanobj.setTreasureCount(MazeP2P.Beanobj.getTreasureCount()-1);
				}
				MazeGrid.put(coordinates[currentRow][currentColumn], "0");
				MazeGrid.put(coordinates[newRow][newColumn],
						PlayerObj.getPlayerName()+"["+PlayerObj.getCollectedTreasure()+"]");
				PlayerObj.setPlayerPosition(coordinates[newRow][newColumn]);
				MazeP2P.MazeGUIobj.DisplayMazeGrid(MazeGrid);

			} else {
				newRow = currentRow;
				System.out.println(">>>> Move Unsuccessfull!  Player ["
						+ PlayerObj.getPlayerName() + "]   Cannot move Up");
			}
		} else if (direction.equalsIgnoreCase("a") && (currentColumn - 1) != -1) // Left
		{
			newColumn = currentColumn - 1;
			if (MazeGrid.get(coordinates[newRow][newColumn]).equals("0")
					|| MazeGrid.get(coordinates[newRow][newColumn]).equals("X")) {
				if (MazeGrid.get(coordinates[newRow][newColumn]).equals("X")){
					PlayerObj.setCollectedTreasure(CollectedTreassure + 1);
					MazeP2P.Beanobj.setTreasureCount(MazeP2P.Beanobj.getTreasureCount()-1);
				}
				MazeGrid.put(coordinates[currentRow][currentColumn], "0");
				MazeGrid.put(coordinates[newRow][newColumn],
						PlayerObj.getPlayerName()+"["+PlayerObj.getCollectedTreasure()+"]");
				PlayerObj.setPlayerPosition(coordinates[newRow][newColumn]);
				MazeP2P.MazeGUIobj.DisplayMazeGrid(MazeGrid);

			} else {
				newColumn = currentColumn;
				System.out.println(">>>> Move Unsuccessfull!    Player ["
						+ PlayerObj.getPlayerName() + "]    Cannot move Left");
			}
		} else if (direction.equalsIgnoreCase("s") && currentRow + 1 < GridSize) // down
		{
			newRow = currentRow + 1;
			if (MazeGrid.get(coordinates[newRow][newColumn]).equals("0")
					|| MazeGrid.get(coordinates[newRow][newColumn]).equals("X")) {
				if (MazeGrid.get(coordinates[newRow][newColumn]).equals("X")){
					PlayerObj.setCollectedTreasure(CollectedTreassure + 1);
					MazeP2P.Beanobj.setTreasureCount(MazeP2P.Beanobj.getTreasureCount()-1);
				}
				
				MazeGrid.put(coordinates[currentRow][currentColumn], "0");
				MazeGrid.put(coordinates[newRow][newColumn],PlayerObj.getPlayerName()+"["+PlayerObj.getCollectedTreasure()+"]");
				PlayerObj.setPlayerPosition(coordinates[newRow][newColumn]);
				MazeP2P.MazeGUIobj.DisplayMazeGrid(MazeGrid);
			} else {
				newRow = currentRow;
				System.out.println(">>>> Move Unsuccessfull!    Player ["
						+ PlayerObj.getPlayerName() + "]    Cannot move Down");
			}
		} else if (direction.equalsIgnoreCase("d")
				&& (currentColumn + 1) < GridSize)// right
		{
			newColumn = currentColumn + 1;
			if (MazeGrid.get(coordinates[newRow][newColumn]).equals("0")
					|| MazeGrid.get(coordinates[newRow][newColumn]).equals("X")) {
				if (MazeGrid.get(coordinates[newRow][newColumn]).equals("X")){
					PlayerObj.setCollectedTreasure(CollectedTreassure + 1);
				MazeP2P.Beanobj.setTreasureCount(MazeP2P.Beanobj.getTreasureCount()-1);}
				MazeGrid.put(coordinates[currentRow][currentColumn], "0");
				MazeGrid.put(coordinates[newRow][newColumn],
						PlayerObj.getPlayerName()+"["+PlayerObj.getCollectedTreasure()+"]");
				PlayerObj.setPlayerPosition(coordinates[newRow][newColumn]);
				MazeP2P.MazeGUIobj.DisplayMazeGrid(MazeGrid);
			} else {
				newColumn = currentColumn;
				System.out
						.println("Move Unsuccessfull!   ["
								+ PlayerObj.getPlayerName()
								+ "]     Cannot move Right");
			}
		} else if(direction.equalsIgnoreCase("n")){
			System.out.println(" \n["+ PlayerObj.getPlayerName() + "]  pressed No Move !  ");
		}		
		else {
			System.out.println(">>>> Move Unsuccessful !   ["
					+ PlayerObj.getPlayerName() + "]     is at the GRID EDGES");
		}
		MazeP2P.Beanobj.getPlayerList().put(PlayerObj.getPlayerID(), PlayerObj);

	}
	
	public ArrayList getRowcolumns(int position) {
		ArrayList PositionCoordinates = new ArrayList();

		for (int i = 0; i < this.getGridSize(); i++) {
			for (int j = 0; j < this.getGridSize(); j++) {
				if (coordinates[i][j] == position) {
					PositionCoordinates.add(i);
					PositionCoordinates.add(j);
				}
			}
		}
		return PositionCoordinates;
	}
	
	public void getinitDetails(PlayerInfo playerObj){
		Scanner ScannerObj = new Scanner(System.in);
		//Get the GridSize from the User....
		System.out.println(">>>> Enter your MazeGrid Size: ");
		String GSize = ScannerObj.next();
		try{
			while(!MazeP2P.Val.isvalidGridSize(GSize)){
				System.out.println(">>>> Please Enter a Valid GridSize....  ");
				GSize = ScannerObj.next();
			}
			this.setGridSize(Integer.parseInt(GSize));
		}catch(Exception e){
			System.out.println("Exception: "+e);
		}
		
		//Get the Treasure Count from the User....
		System.out.println(">>>> Enter your No. of Treasures: ");
		String NoTreasures = ScannerObj.next();
		try{
			while(!MazeP2P.Val.isvalidTreasure(this.GridSize, NoTreasures)){
				System.out.println(">>>> Please Enter a Valid No of Treasures....  ");
				NoTreasures = ScannerObj.next();
			}
			this.setTreasure(Integer.parseInt(NoTreasures));
			this.setTreasureCount(Integer.parseInt(NoTreasures));
		}catch(Exception e){
			System.out.println("Exception: "+e);
		}

	}
	
	
	public void DisplayMazeGrid(Map<Integer, String> mazeGrid) {
		Iterator iterator = mazeGrid.keySet().iterator();
		while (iterator.hasNext()) {
			int key = (int) iterator.next();
			String value = mazeGrid.get(key).toString();
			// System.out.println(key + " " + value);
			if (key % (this.getGridSize()) == 0) {
				System.out.println("");
			}
			System.out.print(value + "\t");
		}
	}
	
	
	public void selectSecondaryServer(){
		boolean sec_Selected=false;
		for(Entry<Integer, PlayerInfo> Player :MazeP2P.Beanobj.getPlayerList().entrySet()){
			PlayerInfo PlayerObj=Player.getValue();
			//Select a player who is not a primary or secondary
			if(!PlayerObj.isPrimary() && !PlayerObj.isSecondary()){
				PlayerObj.setSecondary(true);
				PlayerObj.Secondary=true;
				System.out.println(">>>> ["+PlayerObj.getPlayerName()+"] Became Secondary Server");
				sec_Selected=true;
				secondaryPortNum=PlayerObj.getPlayerPort();
				MazeP2P.Beanobj.getPlayerList().put(PlayerObj.getPlayerID(), PlayerObj);
				Registry P2PRegistry;
				try{
					P2PRegistry =LocateRegistry.getRegistry("",secondaryPortNum);
					MazeP2PInterface peerStub=(MazeP2PInterface) P2PRegistry.lookup("Peer2peer");
					//System.out.println("Before update"+secondaryPortNum);
					//System.out.println("After update sec port : "+ MazeP2P.Beanobj.getSecondaryPortNum());
					peerStub.UpdateMazeState(MazeP2P.Beanobj);
					broadcastPort();
					
					
				}catch(RemoteException | NotBoundException e){
					System.out.println("Exception on peer Update SelectSecondary");
				}
				break;
			}
			
		}if(!sec_Selected){System.out.println(">>>> One player is left so secondary player cannot be created");
		System.out.println(">>>> ALL PLAYERS LEFT GAME END");
		System.exit(2);
		}
	}
	
	public void broadcastPort(){
		Registry P2PRegistry;
		try{
		for(Entry<Integer, PlayerInfo> Player :MazeP2P.Beanobj.getPlayerList().entrySet()){
			PlayerInfo PlayerObj=Player.getValue();
			if(PlayerObj.isPrimary()||PlayerObj.isSecondary())continue;
			P2PRegistry =LocateRegistry.getRegistry("",PlayerObj.getPlayerPort());
			MazeP2PInterface peerStub=(MazeP2PInterface) P2PRegistry.lookup("Peer2peer");	
			peerStub.UpdateServerInfo(MazeP2P.Beanobj);
		}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
