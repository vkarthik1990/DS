import java.rmi.Remote;
import java.rmi.RemoteException;


public interface MazeP2PInterface  extends Remote {


	public void UpdateMazeState(MazeBean MazeBeanObj) throws RemoteException;
	
	public void UpdateServerInfo(MazeBean MazeBeanObj) throws RemoteException;
	
}
