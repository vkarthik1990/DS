import java.rmi.RemoteException;

import javax.swing.JFrame;


public class gameStatusThread implements Runnable{
	PlayerInfo playerObj;
	MazeGUI MazeGUIObj;
	boolean gameStatus=false;
	boolean running=true;
	MazeBean Beanobj;
	public gameStatusThread(PlayerInfo Playerobj,MazeGUI MazeGUIObj){
		this.playerObj=Playerobj;
		this.MazeGUIObj=MazeGUIObj;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
	
	playerObj=MazeP2P.Beanobj.getPlayerList().get(playerObj.getPlayerID());
	//System.out.println("*********GAME STATUS THREAD**********"+"running"+running+"  ACTIVE: "+playerObj.isActive()+ "gameStatus   :"+gameStatus);
	while(running && playerObj.isActive()){
		if(!gameStatus){
			try {
				Beanobj=MazeP2P.P2PObj.getMazeStubsafe().getMazeBean(playerObj);
				playerObj=MazeP2P.Beanobj.getPlayerList().get(playerObj.getPlayerID());
				//System.out.println("GameStatus Thread runnning" +Beanobj.getTreasureCount());
				if(Beanobj.getTreasureCount()==0){
					Beanobj.setGameover(true);
					MazeP2P.Beanobj.setGameover(true);
					MazeGUIObj.Jframeobj.removeKeyListener(MazeGUIObj);
					MazeGUIObj.DisplayMazeGridinBoard(Beanobj.getMazeGrid(),MazeGUIObj.JLabelMap,MazeGUIObj.Jframeobj);
					if(playerObj.isPrimary()){
					MazeP2P.Beanobj=MazeP2P.MazeStub.computeWinner();
					System.out.println("***********************GAME OVER**************************");
					}
					MazeP2P.Beanobj=MazeP2P.P2PObj.getMazeStubsafe().getMazeBean(playerObj);
					if(playerObj.isWinner()){ System.out.println("winner :  "+playerObj.getPlayerName());
						playerObj.setResponseFromServer("you win");
						playerObj.getPlayerName();
					}
					else {
						playerObj.setResponseFromServer("you Lost");
					}
					MazeP2P.Beanobj.getPlayerList().put(playerObj.getPlayerID(), playerObj);
					gameStatus=true;
					MazeGUIObj.DisplayMazeGridinBoard(Beanobj.getMazeGrid(),MazeGUIObj.JLabelMap,MazeGUIObj.Jframeobj);
					
				}else{
					Thread.sleep(2000);
				}
			} catch (RemoteException | InterruptedException e) {
				// TODO Auto-generated catch block
				//running=false;
				//e.printStackTrace();
			}catch(NullPointerException e){
				//running=false;
				//System.out.println("");
			}
		}else running=false;
	}
	//System.out.println("No of treassures collected by ["+playerObj.getPlayerName()+"]   is  " +playerObj.getCollectedTreasure());
	}

	
}
