import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
import javax.swing.JLabel;
import javax.swing.JPanel;


public class MazeGUI extends Frame{
	MazeBean BeanObj;
	PlayerInfo PlayerObj;
	Graphics g;
	JFrame Jframeobj=new JFrame();
	JPanel JPanelObj=new JPanel(new GridLayout(5,5));
	
	Random randomize = new Random();
	public int N,M;
	public int[][] coordinates;
	public Map<Integer,String> MazeGrid;
	CustomKeyListener ckObj=new CustomKeyListener();
	

	public  MazeGUI(PlayerInfo Playerobj,MazeBean Beanobj) {

		Jframeobj.setTitle("Maze Game");
		Jframeobj.setSize(500, 500);
		Jframeobj.setVisible(true);
		Jframeobj.setFocusable(true);
		Jframeobj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		
		BeanObj=Beanobj;
		PlayerObj=Playerobj;
		Playerobj.setPlayerID(GeneratePlayerID());
		N=Beanobj.getGridSize();
		M=Beanobj.getTreasure();
		//JFrame MazeFrame=new JFrame("MazeFrame");
		createMazebox(N,M);
		Jframeobj.addKeyListener(ckObj); 

	}
	
	public class ActionListener extends KeyAdapter{
			
	}
	
	public void paint(Graphics g){
		super.paint(g);
		
	}
	public void DisplayMazeGridinBoard(Map<Integer, String> mazeGrid,JFrame Jframeobj) {
			
			JPanelObj.setFocusable(true);
			JPanelObj.setSize(10, 10);
			JPanelObj.removeAll();
		Iterator iterator = mazeGrid.keySet().iterator();
		  
		while (iterator.hasNext()) {
		   int key = (int) iterator.next();
		   String value = mazeGrid.get(key).toString();			  
		   //System.out.println(key + " " + value);
		   if(key%(BeanObj.getGridSize())==0)
		   {
			   JPanelObj.add(new JLabel(" "));
			   //System.out.println("");
		   }
		 
		   JPanelObj.add(new JLabel(value));
		   		//System.out.print(value+"\t");
		}
		
		JPanelObj.repaint();
		Jframeobj.add(JPanelObj);
		//JPanelObj.repaint();

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
		else {
			MazeGrid.put(coordinates[randRow][randColumn], PlayerObj.getPlayerName());
			PlayerObj.collectedTreasure++;
			PlayerObj.setPlayerPosition(coordinates[randRow][randColumn]);
		}
		DisplayMazeGrid(MazeGrid);
		//PlayerMoves(PlayerObj,"w",N);		
		DisplayMazeGrid(MazeGrid);
		DisplayMazeGridinBoard(MazeGrid,Jframeobj);
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
		   if(key%(BeanObj.getGridSize())==0)
		   {
			   System.out.println("");
		   }
		   		System.out.print(value+"\t");
		}

	}

	/*Player Direction controls are placed here*/
	public void PlayerMoves(PlayerInfo PlayerObj,String direction,int GridSize) {
		ArrayList playercoordinates=getRowcolumns(PlayerObj.getPlayerPosition());
		Validate val=new Validate();
		int currentRow=(int) playercoordinates.get(0);
		int currentColumn=(int) playercoordinates.get(1);
		int newRow=currentRow;
		int newColumn=currentColumn;
		int CollectedTreassure=PlayerObj.getCollectedTreasure();

			
		if(direction.equalsIgnoreCase("w") && (currentRow-1) !=-1) //Top
			{
				newRow=currentRow-1;
				if(MazeGrid.get(coordinates[newRow][newColumn]).equals("0")|| MazeGrid.get(coordinates[newRow][newColumn]).equals("X"))
				{
					if(MazeGrid.get(coordinates[newRow][newColumn]).equals("X")) 
						{
						PlayerObj.setCollectedTreasure(CollectedTreassure+1);
						BeanObj.setTreasure(--M);
						}
				MazeGrid.put(coordinates[currentRow][currentColumn], "0");
				MazeGrid.put(coordinates[newRow][newColumn], PlayerObj.getPlayerName());
				PlayerObj.setPlayerPosition(coordinates[newRow][newColumn]);
				System.out.println("Collected treasure "+ PlayerObj.collectedTreasure);
				DisplayMazeGrid(MazeGrid);
				DisplayMazeGridinBoard(MazeGrid, Jframeobj);
				
				}
				else {
					newRow=currentRow;
					System.out.println("Move Unsuccessfull!  Player ["+PlayerObj.getPlayerName()+ "]   Cannot move Up");
				}
			}
		else if(direction.equalsIgnoreCase("a") && (currentColumn-1)!=-1) //Left
		{
			newColumn=currentColumn-1;
			if(MazeGrid.get(coordinates[newRow][newColumn]).equals("0") || MazeGrid.get(coordinates[newRow][newColumn]).equals("X"))
			{
				if(MazeGrid.get(coordinates[newRow][newColumn]).equals("X")){
					PlayerObj.setCollectedTreasure(CollectedTreassure+1);
					BeanObj.setTreasure(--M);
				}
			MazeGrid.put(coordinates[currentRow][currentColumn], "0");
			MazeGrid.put(coordinates[newRow][newColumn], PlayerObj.getPlayerName());
			PlayerObj.setPlayerPosition(coordinates[newRow][newColumn]);
			System.out.println("Collected treasure "+ PlayerObj.collectedTreasure);
			DisplayMazeGrid(MazeGrid);
			DisplayMazeGridinBoard(MazeGrid, Jframeobj);
			}
			else {
				newColumn=currentColumn;
				System.out.println("Move Unsuccessfull!    Player ["+PlayerObj.getPlayerName()+ "]    Cannot move Left");
			}
		}
		else if(direction.equalsIgnoreCase("s") && currentRow+1<GridSize) //down
		{
			newRow=currentRow+1;
			if(MazeGrid.get(coordinates[newRow][newColumn]).equals("0")|| MazeGrid.get(coordinates[newRow][newColumn]).equals("X"))
			{
				if(MazeGrid.get(coordinates[newRow][newColumn]).equals("X")){
					PlayerObj.setCollectedTreasure(CollectedTreassure+1);
					BeanObj.setTreasure(--M);
				}
			MazeGrid.put(coordinates[currentRow][currentColumn], "0");
			MazeGrid.put(coordinates[newRow][newColumn], PlayerObj.getPlayerName());
			PlayerObj.setPlayerPosition(coordinates[newRow][newColumn]);
			System.out.println("Collected treasure "+ PlayerObj.collectedTreasure);
			DisplayMazeGrid(MazeGrid);
			DisplayMazeGridinBoard(MazeGrid, Jframeobj);
			}
			else {
				newRow=currentRow;
				System.out.println("Move Unsuccessfull!    Player ["+PlayerObj.getPlayerName()+ "]    Cannot move Down");
			}
		}
		else if(direction.equalsIgnoreCase("d") && (currentColumn+1)<GridSize)//right
		{
			newColumn=currentColumn+1;
			if(MazeGrid.get(coordinates[newRow][newColumn]).equals("0") || MazeGrid.get(coordinates[newRow][newColumn]).equals("X"))
			{
				if(MazeGrid.get(coordinates[newRow][newColumn]).equals("X")) {
					PlayerObj.setCollectedTreasure(CollectedTreassure+1);
					BeanObj.setTreasure(--M);
				}
			MazeGrid.put(coordinates[currentRow][currentColumn], "0");
			MazeGrid.put(coordinates[newRow][newColumn], PlayerObj.getPlayerName());
			PlayerObj.setPlayerPosition(coordinates[newRow][newColumn]);
			System.out.println("Collected treasure "+ PlayerObj.collectedTreasure);
			
			DisplayMazeGrid(MazeGrid);
			DisplayMazeGridinBoard(MazeGrid, Jframeobj);
			}
			else {
				newColumn=currentColumn;
				System.out.println("Move Unsuccessfull!   ["+PlayerObj.getPlayerName()+ "]     Cannot move Right");
			}
		}
		else{System.out.println("Move Unsuccessful !   ["+PlayerObj.getPlayerName()+ "]     is at the GRID EDGES");}
		
		val.isTreasureOver(M);
	
		
}
	
	public ArrayList getRowcolumns(int position){
		ArrayList PositionCoordinates=new ArrayList();
		
		for(int i=0;i<BeanObj.getGridSize();i++){
			for(int j=0;j<BeanObj.getGridSize();j++){
				if(coordinates[i][j]==position){
					PositionCoordinates.add(i);
					PositionCoordinates.add(j);
					
				}
			}
		}
		return PositionCoordinates;
		
	}
	class CustomKeyListener extends KeyAdapter{
		public void actionPerformed(ActionEvent e){
			repaint();
		}
	public synchronized void keyReleased(KeyEvent e) {  
            int code = e.getKeyCode();  
			switch(code)
			{
				case KeyEvent.VK_UP:
				case KeyEvent.VK_W:
					PlayerMoves(PlayerObj,"w",N);
					System.out.println("Up");
				break;
				case KeyEvent.VK_DOWN:
				case KeyEvent.VK_S:
					System.out.println("Down");
					PlayerMoves(PlayerObj,"s",N);
				break;
				case KeyEvent.VK_LEFT:
				case KeyEvent.VK_A:
					PlayerMoves(PlayerObj,"a",N);
					System.out.println("Left");
				break;
				case KeyEvent.VK_RIGHT:
				case KeyEvent.VK_D:
					PlayerMoves(PlayerObj,"d",N);
					System.out.println("Right");
				break;
				case KeyEvent.VK_N:
					System.out.println("No Move");
				break;
				default:
					System.out.println("Invalid Move");				
			}
			
	}
	}

          

	public static void main(String[] args){
		
		
	}





}


