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
import java.util.Scanner;


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
		
		
		//Get the Player Name from the User....
		System.out.println("Welcome! Enter Player Name:");
		Scanner ScannerObj = new Scanner(System.in);
		String PlayerName = ScannerObj.next();
		try{
			while(PlayerName=="1"){
				System.out.println("Please Enter a Valid Name....  ");
				PlayerName = ScannerObj.next();
			}
			Playerobj.setPlayerName(PlayerName);
		}catch(Exception e){
			System.out.println("Exception: "+e);
		}
		

		//Get the GridSize from the User....
		System.out.println("Enter your MazeGrid Size: ");
		int GridSize = Integer.parseInt(ScannerObj.next());
		try{
			while(GridSize==0){
				System.out.println("Please Enter a Valid GridSize....  ");
				GridSize = Integer.parseInt(ScannerObj.next());
			}
			Beanobj.setGridSize(GridSize);
		}catch(Exception e){
			System.out.println("Exception: "+e);
		}
		
		//Get the Treasure Count from the User....
		System.out.println("Enter your No. of Treasures: ");
		int NoTreasures = Integer.parseInt(ScannerObj.next());
		try{
			while(NoTreasures==0){
				System.out.println("Please Enter a Valid No of Treasures....  ");
				NoTreasures = Integer.parseInt(ScannerObj.next());
			}
			Beanobj.setTreasure(NoTreasures);
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
