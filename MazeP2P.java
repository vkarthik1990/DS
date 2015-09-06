import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class MazeP2P {
	MazeInterface MazeStub;
	MazeController MazeObj=new MazeController();
	
	public void InitializeMaze() throws UnknownHostException, NotBoundException, RemoteException, AlreadyBoundException
	{
		int port=1099;
		String HostIP="";
		Registry MazeRegistry;
		try{
			MazeRegistry =LocateRegistry.getRegistry(HostIP,port);
			MazeStub=(MazeInterface) MazeRegistry.lookup("MazeP2P");
		}catch(RemoteException e)
		{
			try{
				MazeStub = (MazeInterface) UnicastRemoteObject.exportObject(MazeObj, 0);
				MazeRegistry=LocateRegistry.createRegistry(port);
				MazeRegistry.bind("MazeP2P", MazeStub);
			}catch (AlreadyBoundException ex) {
				System.out.println("AlreadyBoundException");
			} catch(RemoteException ex) {
				System.out.println("RemoteException");
			}

		}
		MazeRegistry =LocateRegistry.getRegistry(HostIP,port);
		MazeStub=(MazeInterface) MazeRegistry.lookup("MazeP2P");
		MazeStub.sayHello();
		
		
	}
	
	public void GetinitDetails(PlayerInfo Playerobj,MazeBean Beanobj){
		
		System.out.println("Welcome! Enter Player Name:");
		BufferedReader PlayerName = new BufferedReader(new InputStreamReader(System.in));
		try{
			if(PlayerName.readLine()!=null)
			{
				Playerobj.setPlayerName(PlayerName.readLine());
			}
			
		}catch(Exception e){
			System.out.println("Exception: "+e);
		}
		
		System.out.println("Enter your MazeGrid Size: ");
		BufferedReader GridSize = new BufferedReader(new InputStreamReader(System.in));
		try{
			Beanobj.setGridSize((Integer.parseInt(GridSize.readLine())));
		}catch(Exception e){
			System.out.println("Exception: "+e);
		}
		
		System.out.println("Enter your No. of Treasures: ");
		BufferedReader NoTreasures = new BufferedReader(new InputStreamReader(System.in));
		try{
			Beanobj.setTreasure((Integer.parseInt(NoTreasures.readLine())));
		}catch(Exception e){
			System.out.println("Exception: "+e);
		}
		
	}

	public static void main(String[] args) throws UnknownHostException, RemoteException, NotBoundException, AlreadyBoundException {
		MazeP2P P2PObj=new MazeP2P();
		
		PlayerInfo Playerobj=new PlayerInfo();
		MazeBean Beanobj=new MazeBean();
		P2PObj.GetinitDetails(Playerobj,Beanobj);
		P2PObj.InitializeMaze();
		MazeGUI MazeGUIobj=new MazeGUI(Playerobj,Beanobj);
	}

}
