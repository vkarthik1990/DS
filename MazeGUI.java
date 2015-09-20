import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MazeGUI extends JFrame implements KeyListener {
	MazeBean BeanObj;
	PlayerInfo PlayerObj;
	Graphics g;
	JFrame Jframeobj;
	JPanel JPanelObj;
	JPanel panelInfo;
	JLabel Tressure = new JLabel("Treassure Collected:");
	JLabel RespFromServer = new JLabel("\n Server Response:");
	
	Random randomize = new Random();
	private int N, M;
	private int[][] coordinates;
	private Map<Integer, String> MazeGrid=MazeP2P.Beanobj.getMazeGrid();
	public Map<Integer, JLabel> JLabelMap;
    private int sizeOfLabel = 0;
	public MazeGUI(PlayerInfo Playerobj) {
		PlayerObj=Playerobj;
		JLabelMap = new HashMap<Integer, JLabel>();
		MazeBean Beanobj=MazeP2P.Beanobj;
		
		Jframeobj = new JFrame();
		panelInfo = new JPanel();
		panelInfo.setSize(700, 50);
		JPanelObj = new JPanel(new GridLayout(Beanobj.getGridSize(), Beanobj.getGridSize()));
		Jframeobj.setTitle("[ "+Playerobj.getPlayerName()+"] Maze Game");
		sizeOfLabel = 800/Beanobj.getGridSize();
		Jframeobj.setVisible(true);
		Jframeobj.pack();
		Jframeobj.setSize(800, 800);
		Jframeobj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		N = Beanobj.getGridSize();
		M = Beanobj.getTreasure();
		Jframeobj.addKeyListener(this);
		initPanelComponents();
		Jframeobj.add(panelInfo);
		Jframeobj.add(JPanelObj);
	}

	public void paint(Graphics g) {
		super.paint(g);

	}
	
	private void initPanelComponents() {
		createMazeGridFrame(N, M);
	}

	public void DisplayMazeGridinBoard(Map<Integer, String> mazeGrid,
			Map<Integer, JLabel> JLabelMap, JFrame Jframeobj) {

		JPanelObj.setFocusable(true);
		JPanelObj.removeAll();
		if(MazeP2P.Beanobj.getPlayerList().containsKey(PlayerObj.getPlayerID())){
			PlayerObj=MazeP2P.Beanobj.getPlayerList().get(PlayerObj.getPlayerID());
			Tressure.setText("Treassure Collected: "+PlayerObj.getCollectedTreasure());
			RespFromServer.setText("Server Response:"+PlayerObj.getResponseFromServer());
		}
		
		ArrayList GridValues = new ArrayList(mazeGrid.values());
		int count=0;
		for(int i=0;i<MazeP2P.Beanobj.getGridSize();i++){
			for(int j=0;j<MazeP2P.Beanobj.getGridSize();j++){
				JLabelMap.get(coordinates[i][j]).setText((String) GridValues.get(count));
				count++;
			}
		}
		
		

		for (int label: JLabelMap.keySet()) {
			JPanelObj.add(JLabelMap.get(label));
		}
		
		Jframeobj.repaint();
		JPanelObj.repaint();
		panelInfo.repaint();

	}

	public void createMazeGridFrame(int N, int M) {

		int randRow, randColumn, counter = 0;
		coordinates = new int[N][N];
		
		InitializeGrid();
	
		DisplayMazeGridinBoard(MazeGrid, JLabelMap, Jframeobj);
	}
	public void InitializeGrid(){
		
		//Intialize Info Labels
		setLayout(new BorderLayout());
		//JLabel Tressure = new JLabel("Treassure Collected:");
		//JLabel RespFromServer = new JLabel("Server Response:");
		Tressure.setSize(700, 50);
		RespFromServer.setSize(700, 50);
		panelInfo.add(Tressure);
		panelInfo.add(RespFromServer);
		
		add(panelInfo, BorderLayout.NORTH);
        add(JPanelObj, BorderLayout.CENTER);
        
		// Initialize the Mazegrid
				int temp = 0;
				for (int i = 0; i < N; i++) {
					for (int j = 0; j < N; j++) {
						coordinates[i][j] = temp;
						JLabel jlbl= new JLabel("0");
						jlbl.setSize(sizeOfLabel, sizeOfLabel);
						jlbl.setVisible(true);
						JLabelMap.put(temp, jlbl);
						temp++;
					}
					System.out.println();
				}
	}
	

	public void DisplayMazeGrid(Map<Integer, String> mazeGrid) {

		Iterator iterator = mazeGrid.keySet().iterator();

		while (iterator.hasNext()) {
			int key = (int) iterator.next();
			String value = mazeGrid.get(key).toString();
			// System.out.println(key + " " + value);
			if (key % (MazeP2P.Beanobj.getGridSize()) == 0) {
				System.out.println("");
			}
			System.out.print(value + "\t");
		}

	}



	public ArrayList getRowcolumns(int position) {
		ArrayList PositionCoordinates = new ArrayList();

		for (int i = 0; i < BeanObj.getGridSize(); i++) {
			for (int j = 0; j < BeanObj.getGridSize(); j++) {
				if (coordinates[i][j] == position) {
					PositionCoordinates.add(i);
					PositionCoordinates.add(j);

				}
			}
		}
		return PositionCoordinates;
	}


	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public synchronized void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

		int code = e.getKeyCode();
		switch (code) {
		case KeyEvent.VK_UP:
		case KeyEvent.VK_W:
			try {
				MazeP2P.Beanobj=MazeP2P.MazeStub.move(PlayerObj, "w", N);
				System.out.println("\n["+ PlayerObj.getPlayerName()+ "]   has  Moved UP");
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			break;
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_S:
			try {
				MazeP2P.Beanobj=MazeP2P.MazeStub.move(PlayerObj, "s", N);
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println("\n ["+ PlayerObj.getPlayerName()+ "]   has  Moved DOWN");
			break;
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_A:
			try {
				MazeP2P.Beanobj=MazeP2P.MazeStub.move(PlayerObj, "a", N);
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println("\n ["+ PlayerObj.getPlayerName()+ "]   has  Moved LEFT");
			break;
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_D:
			try {
				MazeP2P.Beanobj=MazeP2P.MazeStub.move(PlayerObj, "d", N);
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println("\n ["+ PlayerObj.getPlayerName()+ "]   has  Moved RIGHT");
			break;
		case KeyEvent.VK_N:
			try {
				MazeP2P.Beanobj=MazeP2P.MazeStub.move(PlayerObj, "n", N);
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println("\n ["+ PlayerObj.getPlayerName()+ "]   has  done a NO MOVE and Game state is synchronised with server");
			break;
		case KeyEvent.VK_Q:
			try {
				MazeP2P.Beanobj=MazeP2P.MazeStub.quitGame(PlayerObj);
				Jframeobj.dispose();
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//System.out.println(MazeP2P.Beanobj.getResponseFromServer());
			break;
		default:
			System.out.println("\n ["+ PlayerObj.getPlayerName()+ "]   : INVALID MOVE");
		}
		DisplayMazeGrid(MazeP2P.Beanobj.getMazeGrid());
		DisplayMazeGridinBoard(MazeP2P.Beanobj.getMazeGrid(),JLabelMap, Jframeobj);
		//Jframeobj.repaint();

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}
