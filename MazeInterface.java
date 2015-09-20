import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MazeInterface extends Remote{
	String sayHello() throws RemoteException;
	
	public PlayerInfo joinGame(PlayerInfo playerInfo) throws RemoteException;
	
	public MazeBean move(PlayerInfo playerInfo, String direction,int GridSize) throws RemoteException;
	
	public MazeBean getMazeBean(PlayerInfo playerInfo) throws RemoteException;
	
	public MazeBean computeWinner() throws RemoteException;
	
	public MazeBean quitGame(PlayerInfo playerInfo) throws RemoteException;
	
	
	
}
