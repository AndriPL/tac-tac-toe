import java.io.Serializable;

public class Plansza implements Serializable
{
	private int tPlansza[][] = new int [3][3];
	
	public String toString() 
	{
		String sPlansza = new String();
		for(int y = 0; y <= 2; y++)
		{
			for(int x = 0; x <= 2; x++)
			{
				sPlansza += tPlansza[x][y];
				sPlansza+="\t";
			}
			sPlansza += "\n";
		}
		return sPlansza;
	}
	
	public int getPlansza(int x, int y) 
	{
		return tPlansza[x][y];
	}
	
	public void set(int x, int y, int value)
	{
		tPlansza[x][y] = value;
	}
	
	public void set(Coordinates coordinates, int value)
	{
		tPlansza[coordinates.getX()][coordinates.getY()] = value;
	}
	
	public boolean isLegal(Coordinates coordinates)
	{
		if(tPlansza[coordinates.getX()][coordinates.getY()] == 0)
			return true;
		return false;
	}
	
	public Plansza ()
	{
	}
	
//	public Plansza (Plansza p)
//	{
//		tPlansza = p.tPlansza;
//	}
}
