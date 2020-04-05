import java.io.Serializable;

public class Coordinates implements Serializable
{
	protected int x;
	protected int y;
	protected boolean isNew;
	
	public int getX()
	{	
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public void setX(int tempX)
	{
		x = tempX;
	}
	
	public void setY(int tempY)
	{
		y = tempY;
	}
	
	public void set(int tempX, int tempY)
	{
		x = tempX;
		y = tempY;
	}
	
	public void set(int tempX, int tempY, boolean tempIsNew)
	{
		x = tempX;
		y = tempY;
		isNew = tempIsNew;
	}
	
	public Coordinates()
	{
	}
	
	public Coordinates(int tempX, int tempY)
	{
		x = tempX;
		y = tempY;
	}
	
	public boolean isCorrect()
	{
		if(x < 0 || x > 2 || y < 0 || y > 2)
			return false;
		return true;
	}
	
	public boolean getIsNew()
	{
		return isNew;
	}
	
	public void setIsNew(boolean tempIsNew)
	{
		isNew = tempIsNew;
	}
}
