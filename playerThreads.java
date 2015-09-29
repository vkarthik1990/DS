import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map.Entry;




public class playerThreads implements Runnable{
	PlayerInfo playerObj;
	
	
	public playerThreads(PlayerInfo playerObj){
		this.playerObj=playerObj;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		//playerObj=MazeP2P.Beanobj.getPlayerList().get(playerObj.getPlayerID());
		boolean primary=false;
		boolean secondary=false;
		while(true){

		playerObj=MazeP2P.Beanobj.getPlayerList().get(playerObj.getPlayerID());
		//System.out.println(playerObj.getPlayerName()+"Player Thread running and it's primary status :"+playerObj.isPrimary()+"and secondary status: "+playerObj.isSecondary());
		if(playerObj.isPrimary()){ if(primary)continue;
			//Primary server thread runs here
			//System.out.println("Threads for Primary server Started");
			Thread isPlayerAlive = new Thread(new peerActiveness());	
			isPlayerAlive.start();
			primary=true;
			System.out.println("["+playerObj.getPlayerName()+"]Selected as Primary server");
		}
		//System.out.println("playerObj.isSecondary()     : "+playerObj.isSecondary());
		if(playerObj.isSecondary()){ if(secondary)continue;
			try {
				MazeP2P.Beanobj=MazeP2P.P2PObj.getMazeStubsafe().getMazeBean(playerObj);
				bindSecondarytoRMI(MazeP2P.Beanobj);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Threads for Secondary server Started");
			Thread checkPrimStatus = new Thread(new checkPrimaryStatus(playerObj));
			checkPrimStatus.start();
			
			Thread BackupState = new Thread(new SyncwithPrimary(playerObj));
			BackupState.start();
			
			
			secondary=true;
			System.out.println("["+playerObj.getPlayerName()+"]Selected as Secondary server");
		}
		
		try {
			Thread.sleep(500);
			//checkplayerknowledge(MazeP2P.Beanobj);
			MazeP2P.Beanobj=MazeP2P.P2PObj.getMazeStubsafe().getMazeBean(playerObj);
			playerObj=MazeP2P.Beanobj.getPlayerList().get(playerObj.getPlayerID());
			//checkplayerknowledge(MazeP2P.Beanobj);
		} catch (InterruptedException | RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	}

	private void checkplayerknowledge(MazeBean Beanobj) {
		for(Entry<Integer, PlayerInfo> player :Beanobj.getPlayerList().entrySet()){
			PlayerInfo tempPlayer=player.getValue();
			if(tempPlayer.isPrimary()){
				System.out.println("Primary is"+tempPlayer.getPlayerName());
			}else if(tempPlayer.isSecondary())System.out.println("Secondary is"+tempPlayer.getPlayerName());
		}
	}
	
	private void bindSecondarytoRMI(MazeBean Beanobj) {
		System.out.println("Binding the New Secondary to RMI");
		// TODO Auto-generated method stub
		Registry MazeRegistry=null;
		MazeController MazeObj=new MazeController();
		try{
			System.setProperty("java.rmi.server.hostname","");
			MazeInterface MazeStub = (MazeInterface) UnicastRemoteObject.exportObject(MazeObj, 0);	
			System.out.println("Secondary port number"+Beanobj.getSecondaryPortNum());
			MazeRegistry=LocateRegistry.getRegistry(Beanobj.getSecondaryIPaddress(),Beanobj.getSecondaryPortNum());
			MazeRegistry.bind("MazeP2P", MazeStub);
			//MazeP2P.Beanobj=MazeP2P.P2PObj.getMazeStubsafe().getMazeBean(playerObj);
			
		}catch (AlreadyBoundException ex) {
			System.out.println("[Exception]: AlreadyBoundException while binding secondary server to RMI");
		} catch(RemoteException ex) {
			System.out.println("[Exception]: RemoteException while binding secondary server to RMI");
		}
		
		
	}
	
	 public class peerActiveness implements Runnable{
			@Override
			public void run() {
				while(!MazeP2P.Beanobj.isGameover()){
					try {
						if(MazeP2P.Beanobj.isJoinFlag()){Thread.sleep(15000); continue;}
						MazeP2P.Beanobj=MazeP2P.P2PObj.getMazeStubsafe().getMazeBean(playerObj);
						for(Entry<Integer, PlayerInfo> P: MazeP2P.Beanobj.getPlayerList().entrySet()) {
							PlayerInfo player=P.getValue();
							//player.setActive(false);
							if((System.currentTimeMillis()-player.getLastActiveTime())>7000){
								if(player.isSecondary()){
									System.out.println("["+player.getPlayerName()+"] Secondary Server has crashed or quited");
									MazeP2P.P2PObj.getMazeStubsafe().quitGame(player);
									MazeP2P.Beanobj.selectSecondaryServer();
								}
								else{
									System.out.println("["+player.getPlayerName()+"]  player has crashed or quited");
									MazeP2P.P2PObj.getMazeStubsafe().quitGame(player);
								}
										
							}
						}
						Thread.sleep(3000);
					} catch (RemoteException | InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
				}	
			} 
		 }
	 
	 public class SyncwithPrimary implements Runnable{
		 PlayerInfo Po;
		 public SyncwithPrimary(PlayerInfo Po){
				this.Po=Po;
			}
		 
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(!Po.isPrimary() && !MazeP2P.Beanobj.isGameover()){
				try {
					MazeP2P.Beanobj=MazeP2P.P2PObj.getMazeStubsafe().getMazeBean(Po);
					Po=MazeP2P.P2PObj.getMazeStubsafe().getMazeBean(Po).getPlayerList().get(Po.getPlayerID());
					Thread.sleep(500);
				} catch (RemoteException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		 
	 }
	
}
