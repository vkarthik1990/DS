import java.rmi.RemoteException;
import java.util.Map.Entry;


public class checkPrimaryStatus implements Runnable{
	PlayerInfo playerObj;
	MazeBean BeanObj;
	boolean running=true;
	
	public checkPrimaryStatus(PlayerInfo playerObj){
		this.playerObj=playerObj;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		//System.out.println("running: "+running+"playerObj.isActive()"+playerObj.isActive());
	while(playerObj.isActive() && running && !(MazeP2P.Beanobj.isGameover())){
		//System.out.println("running: "+running+"playerObj.isActive()"+playerObj.isActive()+"running the primary status check");
		try {
			BeanObj=MazeP2P.P2PObj.getMazeStubsafe().getMazeBean(playerObj);
			if(playerObj.isPrimary()) running=false;
			for(Entry<Integer, PlayerInfo> player :BeanObj.getPlayerList().entrySet()){
				PlayerInfo tempPlayer=player.getValue();
				if(tempPlayer.isPrimary()){
					//If Primary server active time is not updated for 5s assume as crashed.
					if((System.currentTimeMillis()-tempPlayer.getLastActiveTime())>5000){
						System.out.println(">>>> Primary Server has Crashed");
						//MazeP2P.P2PObj.getMazeStubsafe().quitGame(tempPlayer);
						for(Entry<Integer, PlayerInfo> p :BeanObj.getPlayerList().entrySet()){
							PlayerInfo secondary=p.getValue();
							if(secondary.isSecondary()){
								//playerObj.setPrimary(true);
								//playerObj.setSecondary(false);
								//System.out.println("playerobj: "+playerObj.getPlayerName()+secondary.getPlayerName());
								playerObj=MazeP2P.Beanobj.getPlayerList().get(secondary.getPlayerID());
								playerObj.setPrimary(true);
								playerObj.setSecondary(false);
								MazeP2P.Beanobj.getPlayerList().put(playerObj.getPlayerID(),playerObj);
								MazeP2P.Beanobj.getPlayerList().get(secondary.getPlayerID()).setPrimary(true);
								MazeP2P.Beanobj.setPrimaryPortNum(secondary.getPlayerPort());
								MazeP2P.Beanobj.getPlayerList().get(secondary.getPlayerID()).setSecondary(false);
								//MazeP2P.Beanobj.getPlayerList().get(secondary.getPlayerID()).Secondary=false;
								MazeP2P.Beanobj.selectSecondaryServer();
								System.out.println(">>>> ["+secondary.getPlayerName()+"] secondary server is made as primary server");
							}
						}MazeP2P.P2PObj.getMazeStubsafe().quitGame(tempPlayer);
					}
				}
				
				
			}
			if(BeanObj.isGameover() && !playerObj.isSecondary())running=false;
			Thread.sleep(1000);
		} catch (RemoteException | InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("Connection exception");
		}
	
		}
	}

}
