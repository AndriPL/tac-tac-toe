import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class StrumienieIO 
{
	public ObjectInputStream oIS;
	public ObjectOutputStream oOS;
	
	public StrumienieIO(Socket socket) throws ConnectingFailedException
	{
		try 
		{
			oOS = new ObjectOutputStream(socket.getOutputStream());
		} 
		catch (IOException e)
		{
			throw new ConnectingFailedException("Nie udalo sie stworzyc strumienia wyjsciowego :(", e);
		}
		try 
		{
			oIS = new ObjectInputStream(socket.getInputStream());
		} 
		catch (IOException e) 
		{
			throw new ConnectingFailedException("Nie udalo sie stworzyc strumienia wejsciowego :(", e);
		}
		
	}
	
	public void close() throws MyException
	{
		try 
		{
			oOS.close();
		} 
		catch (IOException e) 
		{
			throw new MyException("Blad podczas zamykania ObjectOutputstream", e);
		}
		try 
		{
			oIS.close();
		} 
		catch (IOException e) 
		{
			throw new MyException("Blad podczas zamykania ObjectInputstream", e);
		}
	}
}
