import java.util.Scanner;


public class Validate {
	Scanner sc = new Scanner(System.in);
	public boolean isvalidGridSize(String GridSizeString){
		
		if(GridSizeString.matches("[0-9]{1,5}")) return true;
		//if(N>1) return true;
		else return false;
	}
	public boolean isvalidTreasure(int N,String NoTreasuresString){
		int M=0;
		if(NoTreasuresString.matches("[0-9]{1,5}")) 
		{
			M=Integer.parseInt(NoTreasuresString);
		}
		if((N*N>=M) && (M!=0)) return true;
		else return false;
	}
	public boolean isvalidName(String PlayerName) {
		if(PlayerName.matches( "[a-zA-Z]*" )) return true;
		return false;
	}
	
	public void isTreasureOver(int M) {
		if(M == 0){
		System.out.println("Game Over!!");
		}
		//All Thread close operations should be done here
	}
	
}