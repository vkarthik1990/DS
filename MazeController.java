import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.TreeMap;


public class MazeController implements MazeInterface{

Random randomize=new Random();


	public String sayHello() throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("This is Server");
		return null;
	}
	
	public PlayerInfo joinGame(PlayerInfo playerObj ){

		playerObj.setPlayerID(randomize.nextInt(4000 - 3000 ) + 3000);
		if(MazeP2P.Beanobj.getGameStartTime()>System.currentTimeMillis() || MazeP2P.Beanobj.getGameStartTime()==0L) //Allows the player to join only if the current time is less than gamestarting time
		{
			synchronized(MazeP2P.Beanobj)
			{
				if(MazeP2P.Beanobj.getPlayerList().isEmpty())
				{
					
					
					//Initilize
					initializePrimaryPlayer(playerObj);
					
					//Game active/End thread
					monitorGameStatus();
					
					//update secondary server thread
					updateSecondary();
					
					
					//IsActiveChecker thread
					isAlive();
					
					Thread timerThread = new Thread(new MazeThread());	
					MazeP2P.Beanobj.setGameStartTime(System.currentTimeMillis() + 10000);
					timerThread.start();
					
				}
				else if(MazeP2P.Beanobj.getPlayerList().size()==1){
					
					playerObj.setSecondary(true);
					MazeP2P.Beanobj.getPlayerList().put(playerObj.getPlayerID(), playerObj);
				}
				else
				{
					MazeP2P.Beanobj.getPlayerList().put(playerObj.getPlayerID(), playerObj);
				}
				MazeP2P.Beanobj.AddPlayertoMaze( playerObj);
				System.out.println("Player ["+playerObj.getPlayerName()+"] is set into the server and his PLAYER_ID is : ["+playerObj.getPlayerID()+"]");
			}
		}else{
			System.out.println("Sorry ["+playerObj.getPlayerName()+"] game Already started ! Please try after sometime.");
			MazeP2P.Beanobj.setGameStartTime(0);
			playerObj.setPlayerName("");
		}
		
		
		
		
		return playerObj;
		
}

	
	private void isAlive() {
		// TODO Auto-generated method stub
		
	}

	private void monitorGameStatus() {
		// TODO Auto-generated method stub
		
	}

	private void initializePrimaryPlayer(PlayerInfo playerobj) {
		playerobj.setPrimary(true);
		MazeP2P.Beanobj.getinitDetails(playerobj);
		MazeP2P.Beanobj.InitializeGrid();
		MazeP2P.Beanobj.getPlayerList().put(playerobj.getPlayerID(), playerobj);
		MazeP2P.Beanobj.setIsJoinRequest(true);		
	}

	private void updateSecondary() {
		// TODO Auto-generated method stub
		
	}

	public MazeBean move(PlayerInfo playerInfo, String direction,int GridSize){
		//GUIObj.PlayerMoves(playerInfo,direction,GridSize)
		MazeP2P.Beanobj.PlayerMoves(playerInfo, direction, GridSize);
		return MazeP2P.Beanobj;
		
	}
	
	 public class MazeThread implements Runnable{
		 int WaitTime=10000;
		@Override
		public void run() {
			try {
				System.out.println("Game will start in 20s");
				Thread.sleep(WaitTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				System.out.println("Exception Arises in invoking the Maze timer thread");
				e.printStackTrace();
			}
			
		}
		 
	 }

	@Override
	public MazeBean getMazeBean(PlayerInfo playerInfo) throws RemoteException {
		return MazeP2P.Beanobj;
	}

	
	public MazeBean quitGame(PlayerInfo playerInfo){
		playerInfo=MazeP2P.Beanobj.getPlayerList().get(playerInfo.getPlayerID());
		MazeP2P.Beanobj.getMazeGrid().put(playerInfo.getPlayerPosition(), "0");
		MazeP2P.Beanobj.setMazeGrid(MazeP2P.Beanobj.getMazeGrid());
		playerInfo.setResponseFromServer("Player ["+playerInfo.getPlayerName()+"] has quited the game.");
		MazeP2P.Beanobj.getPlayerList().put(playerInfo.getPlayerID(), playerInfo);	
		MazeP2P.Beanobj.getPlayerList().remove(playerInfo.getPlayerID());
		return MazeP2P.Beanobj;
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public MazeBean computeWinner() throws RemoteException {
		// TODO Auto-generated method stub
		TreeMap PlayerTressureMap=new TreeMap();
		PlayerInfo PlayerObj;
		
		for(Entry<Integer, PlayerInfo> Player :MazeP2P.Beanobj.getPlayerList().entrySet()){
			PlayerObj=Player.getValue();
			PlayerTressureMap.put(PlayerObj.getPlayerID(), PlayerObj.getCollectedTreasure());
			
		}
		PlayerTressureMap=(TreeMap) SortMap(PlayerTressureMap);
		int winnerPlayerID=(int) PlayerTressureMap.firstKey();
	
		PlayerObj=MazeP2P.Beanobj.getPlayerList().get(winnerPlayerID);
		PlayerObj.getPlayerName();
		PlayerObj.getPlayerID();
		PlayerObj.setWinner(true);
		MazeP2P.Beanobj.getPlayerList().put(winnerPlayerID, PlayerObj);		
		return MazeP2P.Beanobj;
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map SortMap(TreeMap unsortedPlayermap) {	 
		@SuppressWarnings("rawtypes")
		List tempList = new LinkedList(unsortedPlayermap.entrySet());
	 
		Collections.sort(tempList, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o1)).getValue())
							.compareTo(((Map.Entry) (o2)).getValue());
			}
		});
	 
		TreeMap sortedPlayermap = new TreeMap();
		for (Iterator it = tempList.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedPlayermap.put(entry.getKey(), entry.getValue());
		}
		return sortedPlayermap;
	}
	
}

 
