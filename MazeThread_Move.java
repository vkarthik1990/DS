import java.rmi.RemoteException;
import java.util.Scanner;


public class MazeThread_Move implements Runnable {
	
	MazeP2P MazeP2P;
	private PlayerInfo PlayerObj;
	String Direction="";
	public MazeThread_Move(PlayerInfo PlayerObj){
		this.PlayerObj=PlayerObj;
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Scanner ScannerObj = new Scanner(System.in);
		while (true){
			System.out.println("["+PlayerObj.getPlayerName()+"] Enter the Direction to be moved: ");
			Direction=ScannerObj.next();
			
			try {
				MazeP2P.getMazeStub().move(PlayerObj, Direction);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
	}

}
