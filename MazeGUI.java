import java.awt.Graphics;
import java.awt.List;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javax.swing.JFrame;


public class MazeGUI {
	MazeBean Beanobj=new MazeBean();
	PlayerInfo PlayerObj=new PlayerInfo();
	Graphics g;
	
	Random randomize = new Random();
	public int N,M;
	public int[][] coordinates;
	public Map<Integer,String> MazeGrid;
	
	

	public  MazeGUI(){
		Beanobj=new MazeBean();
		PlayerObj=new PlayerInfo();
		PlayerObj.setPlayerID(GeneratePlayerID());
		N=Beanobj.getGridSize();
		M=Beanobj.getTreasure();
		//JFrame MazeFrame=new JFrame("MazeFrame");
		createMazebox(N,M);
	}
	
	public void createMazebox(int N,int M){
		MazeGrid=new HashMap<Integer,String>();
		int randRow, randColumn, counter = 0;
		coordinates=new int[N][N];
		
		//Initialize the Mazegrid
		int temp=0;
		for(int i=0;i<N;i++){
			for(int j=0;j<N;j++){
				coordinates[i][j]=temp;
				MazeGrid.put(temp, "0");
				temp++;
			}
			System.out.println();
		}
		
	
		//Intialize the Random Tressure Positions
		while(counter < M){
			  randRow = randomize.nextInt(N);
			  randColumn = randomize.nextInt(N);			
			if(MazeGrid.get(coordinates[randRow][randColumn]) != "X"){
				MazeGrid.put(coordinates[randRow][randColumn], "X");
				++counter;
			}			
		}
		
		//Intialize the Player Positions
		  randRow = randomize.nextInt(N);
		  randColumn = randomize.nextInt(N);			
		if(MazeGrid.get(coordinates[randRow][randColumn]) != "X"){
			MazeGrid.put(coordinates[randRow][randColumn], PlayerObj.getPlayerName());
			PlayerObj.setPlayerPosition(coordinates[randRow][randColumn]);
			++counter;
		}
		DisplayMazeGrid(MazeGrid);
		PlayerMoves(PlayerObj,"w");		
		DisplayMazeGrid(MazeGrid);
	}
	
	
	//Generate Player ID
	private int GeneratePlayerID() {
		int PlayerId = randomize.nextInt(4000 - 3000 ) + 3000;
		return PlayerId;		
	}

	private void DisplayMazeGrid(Map<Integer, String> mazeGrid) {

		Iterator iterator = mazeGrid.keySet().iterator();
		  
		while (iterator.hasNext()) {
		   int key = (int) iterator.next();
		   String value = mazeGrid.get(key).toString();			  
		   //System.out.println(key + " " + value);
		   if(key%(N)==0)
		   {
			   System.out.println("");
		   }
		   		System.out.print(value+"\t");
		}

	}

	public void PlayerMoves(PlayerInfo PlayerObj,String direction) {
		ArrayList playercoordinates=getRowcolumns(PlayerObj.getPlayerPosition());
		int currentRow=(int) playercoordinates.get(0);
		int currentColumn=(int) playercoordinates.get(1);
		int newRow=currentRow;
		int newColumn=currentColumn;
			
		if(direction.equalsIgnoreCase("w"))
			{
				newRow=currentRow-1;
				MazeGrid.put(coordinates[currentRow][newColumn], "0");
				MazeGrid.put(coordinates[newRow][newColumn], PlayerObj.getPlayerName());
			}
			
		
		}
	
	public ArrayList getRowcolumns(int position){
		ArrayList PositionCoordinates=new ArrayList();
		
		for(int i=0;i<N;i++){
			for(int j=0;j<N;j++){
				if(coordinates[i][j]==position){
					PositionCoordinates.add(i);
					PositionCoordinates.add(j);
					
				}
			}
		}
		return PositionCoordinates;
		
	}
	
	public static void main(String[] args){
		
		
	}


}
