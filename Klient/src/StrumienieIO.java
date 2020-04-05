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
			System.out.println("Tu jestem 1.");
			oIS = new ObjectInputStream(socket.getInputStream());
			System.out.println("Tu jestem 2.");
		} 
		catch (IOException e) 
		{
			throw new ConnectingFailedException("Nie udalo sie stworzyc strumienia wejsciowego :(", e);
		}
		try 
		{
			System.out.println("Tu jestem 3.");
			oOS = new ObjectOutputStream(socket.getOutputStream());
			System.out.println("Tu jestem 4.");
		} 
		catch (IOException e)
		{
			throw new ConnectingFailedException("Nie udalo sie stworzyc strumienia wyjsciowego :(", e);
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
