import java.rmi.RemoteException;
import java.util.Random;


public class MazeController implements MazeInterface{

Random randomize=new Random();

	public String sayHello() throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("This is Server");
		return null;
	}
	
	public PlayerInfo joinGame(PlayerInfo playerInfo){
		playerInfo.setPlayerID(randomize.nextInt(4000 - 3000 ) + 3000);
		
		return playerInfo;
		
	}
	
	public boolean move(PlayerInfo playerInfo, String direction){
		//GUIObj.PlayerMoves(playerInfo,direction,GridSize)
		return false;
		
	}

}
