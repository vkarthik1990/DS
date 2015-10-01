import java.rmi.RemoteException;


public class MazeP2PImpl implements MazeP2PInterface{

	@Override
	public synchronized void UpdateMazeState(MazeBean MazeBeanObj) throws RemoteException {
		// TODO Auto-generated method stub
		MazeP2P.Beanobj=MazeBeanObj;
		System.out.println(">>>> Synchronized Maze State with Primary Server");
	}

	@Override
	public synchronized void UpdateServerInfo(MazeBean LatestBeanObj) throws RemoteException {
		// TODO Auto-generated method stub
		MazeP2P.Beanobj.setSecondaryPortNum(LatestBeanObj.getSecondaryPortNum());
		MazeP2P.Beanobj.setPrimaryPortNum(LatestBeanObj.getPrimaryPortNum());
		System.out.println(">>>> Primary and secondary ports are broadcasted to peers.\nPrimary Player Port is ["+LatestBeanObj.getPrimaryPortNum()+"] \nSecondary player Port is : ["+LatestBeanObj.getSecondaryPortNum()+"]");
	}

}
