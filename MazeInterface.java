import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MazeInterface extends Remote{
	String sayHello() throws RemoteException;
	
	public PlayerInfo joinGame(PlayerInfo playerInfo) throws RemoteException;
	
	public boolean move(PlayerInfo playerInfo, String direction) throws RemoteException;
	
	
}
