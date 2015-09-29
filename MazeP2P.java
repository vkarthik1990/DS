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
	MazeController MazeObj=new MazeController();
	
	public MazeInterface getMazeStub() {
		return MazeP2P.P2PObj.getMazeStubsafe();
	}

	public void setMazeStub(MazeInterface mazeStub) {
		MazeStub = mazeStub;
	}

	
	
	public void InitializeMaze(PlayerInfo Playerobj) throws UnknownHostException, NotBoundException, RemoteException, AlreadyBoundException
	{
		//PlayerInfo Playerobj=new PlayerInfo();
		this.getPlayerName(Playerobj);
		int port=2015;
		String HostIP="";
		//Playerobj.setPlayerPort(port);
		Registry MazeRegistry,P2PRegistry;
		try{
			
			MazeRegistry =LocateRegistry.getRegistry(HostIP,port);
			MazeStub=(MazeInterface) MazeRegistry.lookup("MazeP2P");
			
			//From second client onwards
			try{
				System.setProperty("java.rmi.server.hostname", "");
				while(!CheckPortAvailability(port)) port++;
				MazeP2PImpl PeerUpdate=new MazeP2PImpl();
				PeerStub = (MazeP2PInterface) UnicastRemoteObject.exportObject(PeerUpdate, 0);	
				//Creating a Peer to peer rmi
				P2PRegistry=LocateRegistry.createRegistry(port);
				P2PRegistry.bind("Peer2peer", PeerStub);
				Playerobj.setPlayerPort(port);
				System.out.println("[DEBUG]: PEER TO PEER RMI CREATED"+P2PRegistry.toString());
			}catch (AlreadyBoundException ex) {
				System.out.println("[Exception]: AlreadyBoundException");
			} catch(RemoteException ex) {
				System.out.println("[Exception]: RemoteException");
			}
		}catch(RemoteException e) //If RMI is not created then create the RMI
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
		
//Once RMI is set Join the game and run the Player threads and check for gameStatus....
		try {
			Playerobj.setPlayerPort(port);
			//System.out.println(Playerobj.getPlayerName()+"______________________"+port);
			Playerobj=MazeStub.joinGame(Playerobj);
			if(!("").equals(Playerobj.getPlayerName())){
				MazeP2P.Beanobj=MazeStub.getMazeBean(Playerobj);
			
				MazeP2P.Beanobj.DisplayMazeGrid(MazeP2P.Beanobj.getMazeGrid());
				while(!(MazeP2P.Beanobj.getGameStartTime()<System.currentTimeMillis())){
					//20 Seconds wait till game start...
				}
				MazeP2P.Beanobj.setJoinFlag(false);
				System.out.println("*********20 SECONDS OVER GAMESTARTS !***************");
				
				playerAlive paobj=new playerAlive(Playerobj);
				
				MazeP2P.MazeGUIobj=new MazeGUI(Playerobj);
				
				Thread gameStatusThread=new Thread(new gameStatusThread(Playerobj,MazeGUIobj)); 
				gameStatusThread.start();
				
				
				//Thread to check node state
				Thread pt = new Thread(new playerThreads(Playerobj));
				pt.start();
							
				MazeP2P.Beanobj=MazeStub.getMazeBean(Playerobj);
				
				Playerobj=MazeP2P.Beanobj.getPlayerList().get(Playerobj.getPlayerID());
						
				MazeP2P.Beanobj.DisplayMazeGrid(MazeP2P.Beanobj.getMazeGrid());
				
				
				
			}
			
			
			
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	}
	

	public void getPlayerName(PlayerInfo Playerobj){
		MazeP2P.Beanobj=new MazeBean();
		
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
		
	public MazeInterface getMazeStubsafe(){
		Registry MazeRegistry=null;
		try{
			//System.out.println("Stub obtained from Primary Port number "+MazeP2P.Beanobj.getPrimaryPortNum());
			MazeRegistry=LocateRegistry.getRegistry(MazeP2P.Beanobj.getPrimaryIPaddress(),MazeP2P.Beanobj.getPrimaryPortNum());
			MazeStub=(MazeInterface) MazeRegistry.lookup("MazeP2P");
		}catch(RemoteException | NotBoundException ex){
			try {
				//System.out.println("[EXCEPTION]: Cannot connect with primary Server. Connecting with secondary server");
				//System.out.println("Stub obtained from Secondary Port number "+MazeP2P.Beanobj.getSecondaryPortNum());
				MazeRegistry=LocateRegistry.getRegistry(MazeP2P.Beanobj.getSecondaryIPaddress(),MazeP2P.Beanobj.getSecondaryPortNum());
				MazeStub=(MazeInterface) MazeRegistry.lookup("MazeP2P");
			} catch (RemoteException | NotBoundException e) {
				// TODO Auto-generated catch block
				System.out.println("[EXCEPTION]: MazeP2P RMI not found.. trying to connect secondary Port..."+MazeP2P.Beanobj.getSecondaryPortNum());
			}
		}	
		return MazeStub;
	}
		

	public static void main(String[] args) throws UnknownHostException, RemoteException, NotBoundException, AlreadyBoundException {
		P2PObj=new MazeP2P();
		PlayerInfo Playerobj=new PlayerInfo();
		Val =new Validate();
		P2PObj.InitializeMaze(Playerobj);
		
	}

}
