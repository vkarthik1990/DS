import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;


public class MazeP2P {
	public static MazeBean Beanobj;
	public static MazeInterface MazeStub;
	public PlayerInfo Playerobj;
	public static MazeP2P P2PObj;
	public static MazeGUI MazeGUIobj;
	public static MazeP2PInterface PeerStub;
	public static Validate Val;
	
	public MazeInterface getMazeStub() {
		return MazeStub;
	}

	public void setMazeStub(MazeInterface mazeStub) {
		MazeStub = mazeStub;
	}

	MazeController MazeObj=new MazeController();
	
	public void InitializeMaze(PlayerInfo Playerobj) throws UnknownHostException, NotBoundException, RemoteException, AlreadyBoundException
	{
		//PlayerInfo Playerobj=new PlayerInfo();
		this.getPlayerName(Playerobj);
		int port=2015;
		String HostIP="";
		Registry MazeRegistry,P2PRegistry;
		try{
			MazeRegistry =LocateRegistry.getRegistry(HostIP,port);
			MazeStub=(MazeInterface) MazeRegistry.lookup("MazeP2P");
			
			//For second client onwards
			try{
				System.setProperty("java.rmi.server.hostname", "");
				while(!CheckPortAvailability(port)) port++;
				
				MazeP2PImpl PeerUpdate=new MazeP2PImpl();
				PeerStub = (MazeP2PInterface) UnicastRemoteObject.exportObject(PeerUpdate, 0);	
				//Creating a Peer to peer rmi
				P2PRegistry=LocateRegistry.createRegistry(port);
				P2PRegistry.bind("Peer2peer", PeerStub);
			}catch (AlreadyBoundException ex) {
				System.out.println("[Exception]: AlreadyBoundException");
			} catch(RemoteException ex) {
				System.out.println("[Exception]: RemoteException");
			}
		}catch(RemoteException e)
		{	
			try{
				MazeStub = (MazeInterface) UnicastRemoteObject.exportObject(MazeObj, 0);				
				MazeRegistry=LocateRegistry.createRegistry(port);
				MazeRegistry.bind("MazeP2P", MazeStub);
			}catch (AlreadyBoundException ex) {
				System.out.println("[Exception]: AlreadyBoundException");
			} catch(RemoteException ex) {
				System.out.println("[Exception]: RemoteException");
			}

		}catch(NotBoundException ex){
				System.out.println("[Exception]: NotBoundException");
		}
		
		
		
		try {
			Playerobj.setPlayerPort(port);
			Playerobj=MazeStub.joinGame(Playerobj);
			if(!("").equals(Playerobj.getPlayerName())){
				
				MazeP2P.Beanobj=MazeStub.getMazeBean(Playerobj);
				MazeP2P.Beanobj.DisplayMazeGrid(MazeP2P.Beanobj.getMazeGrid());
				
				while(!(MazeP2P.Beanobj.getGameStartTime()<System.currentTimeMillis())){
					//20 Seconds wait till game start...
				}
				System.out.println("20 SECONDS OVER GAMESTARTS !");
				
				MazeP2P.Beanobj=MazeStub.getMazeBean(Playerobj);
				
				Playerobj=MazeP2P.Beanobj.getPlayerList().get(Playerobj.getPlayerID());
						
				
				
				MazeP2P.Beanobj.DisplayMazeGrid(MazeP2P.Beanobj.getMazeGrid());
				
				MazeP2P.MazeGUIobj=new MazeGUI(Playerobj);
				
				Thread gameStatusThread=new Thread(new gameStatusThread(Playerobj,MazeGUIobj)); 
				gameStatusThread.start();
				
			}
			
			
			
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	}
	
	public void getPlayerName(PlayerInfo Playerobj){
		Beanobj=new MazeBean();
		
		//Get the Player Name from the User....
		System.out.println("Welcome! Enter Player Name:");
		Scanner ScannerObj = new Scanner(System.in);
		String PlayerName = ScannerObj.next();
		try{
			while(!MazeP2P.Val.isvalidName(PlayerName)){
				System.out.println("Please Enter a Valid Name....  ");
				PlayerName = ScannerObj.next();
			}
			Playerobj.setPlayerName(PlayerName);
		}catch(Exception e){
			System.out.println("Exception: "+e);
		}
	}
		public boolean CheckPortAvailability(int port){
			try {  
				ServerSocket socketobj = new ServerSocket(port);  
				socketobj.close();  
				socketobj = null;  
				return true;  
			} catch (IOException e) 
			{  
				return false; 
			}
		
	}
		
		
		

	public static void main(String[] args) throws UnknownHostException, RemoteException, NotBoundException, AlreadyBoundException {
		P2PObj=new MazeP2P();
		
		PlayerInfo Playerobj=new PlayerInfo();
		
		Val =new Validate();
		
		P2PObj.InitializeMaze(Playerobj);
		
	}

}
