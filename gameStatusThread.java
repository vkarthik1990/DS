import java.rmi.RemoteException;

import javax.swing.JFrame;


public class gameStatusThread implements Runnable{
	PlayerInfo playerObj;
	MazeGUI MazeGUIObj;
	boolean gameStatus=false;
	MazeBean Beanobj;
	public gameStatusThread(PlayerInfo Playerobj,MazeGUI MazeGUIObj){
		this.playerObj=Playerobj;
		this.MazeGUIObj=MazeGUIObj;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	while(!gameStatus){
			
			try {
				
				Beanobj=MazeP2P.MazeStub.getMazeBean(playerObj);
				playerObj=MazeP2P.Beanobj.getPlayerList().get(playerObj.getPlayerID());
				System.out.println("GameStatus Thread runnning" +Beanobj.getTreasureCount());
				if(Beanobj.getTreasureCount()==0){
					MazeGUIObj.Jframeobj.removeKeyListener(MazeGUIObj);
					MazeGUIObj.DisplayMazeGridinBoard(Beanobj.getMazeGrid(),MazeGUIObj.JLabelMap,MazeGUIObj.Jframeobj);
					Beanobj=MazeP2P.MazeStub.computeWinner();
					System.out.println("GAME OVER");

					
					if(playerObj.isWinner()){
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
					Thread.sleep(1000);
				}
			} catch (RemoteException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		
	}
	System.out.println("No of treassures collected by ["+playerObj.getPlayerName()+"]   is  " +playerObj.getCollectedTreasure());
	}

	
}
