import java.io.IOException;

public class BrokenConnectionException extends IOException
{
	public BrokenConnectionException(String message, Exception cause)
	{
		super(message, cause);
	}
}
