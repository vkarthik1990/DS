import java.rmi.RemoteException;
import java.util.Map.Entry;


public class playerAlive {
	PlayerInfo playerObj;
	boolean response;
	public playerAlive(PlayerInfo player){
		this.playerObj=player;
		
		//Updates the server that im active.
		Thread imActive = new Thread(new PlayerActive());	
		imActive.start();

		
	}
	
	
	
	public class PlayerActive implements Runnable{
		@Override
		public void run() {
			while(!MazeP2P.Beanobj.isGameover() && playerObj.isActive()){
				try {
					//System.out.println(playerObj.PlayerName+"isactive ................"+playerObj.isActive());
					//imAlive(playerObj.getPlayerID());
					MazeP2P.P2PObj.getMazeStubsafe().updatePlayerStatus(playerObj.getPlayerID());
					Thread.sleep(3000);
				} catch (RemoteException | InterruptedException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
			}
			
			System.out.println(">>>> Game over thread stopped."+MazeP2P.Beanobj.isGameover());
			
		}	
		 
	 }

	public void imAlive(Integer playerId) throws RemoteException{
			MazeP2P.Beanobj.getPlayerList().get(playerId).setActive(true);
			MazeP2P.Beanobj.getPlayerList().get(playerId).setLastActiveTime(System.currentTimeMillis());
			//System.out.println("["+playerObj.getPlayerName()+"]Updated last active time successfully");
			

	}


}
