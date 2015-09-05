import java.rmi.RemoteException;


public class MazeController implements MazeInterface{

	public String sayHello() throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("This is Server");
		return null;
	}

}
