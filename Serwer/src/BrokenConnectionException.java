import java.io.IOException;

public class BrokenConnectionException extends IOException
{
	private DaneUczestnika uczestnik;
	
	public BrokenConnectionException(String message, Exception cause, DaneUczestnika uczestnik)
	{
		super(message, cause);
		this.uczestnik = uczestnik;
	}
	
	public DaneUczestnika getUczestnik()
	{
		return uczestnik;
	}
}
