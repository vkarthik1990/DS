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
	public MazeInterface getMazeStub() {
		return MazeStub;
	}

	public void setMazeStub(MazeInterface mazeStub) {
		MazeStub = mazeStub;
	}

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
	
	@SuppressWarnings("unused")
	public void GetinitDetails(PlayerInfo Playerobj,MazeBean Beanobj) throws NumberFormatException{
		
		
		//Get the Player Name from the User....
		System.out.println("Welcome! Enter Player Name:");
		Scanner ScannerObj = new Scanner(System.in);
		Validate val = new Validate();
		String PlayerName = ScannerObj.next();
		try{
			while(!val.isvalidName(PlayerName)){
				System.out.println("Please Enter a Valid Name....  ");
				PlayerName = ScannerObj.next();
			}
			Playerobj.setPlayerName(PlayerName);
		}catch(Exception e){
			System.out.println("Exception: "+e);
		}
		

		//Get the GridSize from the User....
		int GridSize=0;
		System.out.println("Enter your MazeGrid Size: ");
		try{
			String GridSizeString = ScannerObj.next();
			while(!val.isvalidGridSize(GridSizeString)){
				System.out.println("Please Enter a Valid GridSize....  ");
				GridSizeString = ScannerObj.next();
			}
			GridSize = Integer.parseInt(GridSizeString);
			Beanobj.setGridSize(GridSize);
		} catch(Exception e){
			System.out.println("Exception: "+e);
		}
		
		System.out.println("Enter the number of Treasures: ");
		try{
			String NoTreasuresString = ScannerObj.next();
			while(!val.isvalidTreasure(GridSize, NoTreasuresString)){
				System.out.println("Please Enter a Valid number of Treasures....  ");
				NoTreasuresString = ScannerObj.next();
			}
			int NoTreasures = Integer.parseInt(NoTreasuresString);
			Beanobj.setTreasure(NoTreasures);
		} catch(Exception e){
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
