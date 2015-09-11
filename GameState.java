import java.io.Serializable;


public class GameState implements Serializable {
	
	private MazeBean MazeState;

	public MazeBean getMazeState() {
		return MazeState;
	}

	public void setMazeState(MazeBean mazeState) {
		MazeState = mazeState;
	}

}
