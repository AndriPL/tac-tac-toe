import java.io.IOException;

public class ConnectingFailedException extends IOException
{
	public ConnectingFailedException(String message, Exception cause)
	{
		super(message, cause);
	}
}
