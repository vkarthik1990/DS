import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MazeInterface extends Remote{
	String sayHello() throws RemoteException;
}
