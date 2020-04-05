
public class OpponentDisconnectedException extends Exception
{
	public OpponentDisconnectedException(String playerName)
	{
		super(playerName);
	}
	
	public OpponentDisconnectedException()
	{
	}
}
