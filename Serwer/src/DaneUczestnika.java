import java.net.Socket;

public class DaneUczestnika {
	public Socket socket;
	public StrumienieIO strumienieIO;
	public String playerName;
	public Boolean isInGame;
	public int lastTaskCode;
	
	public DaneUczestnika()
	{
		isInGame = false;
		lastTaskCode = 0;
	}
	
	public DaneUczestnika(StrumienieIO tempStrumienieIO, String tempPlayerName)
	{
		strumienieIO = tempStrumienieIO;
		playerName = tempPlayerName;
		isInGame = false;
		lastTaskCode = 0;
	}
	
	public DaneUczestnika(Socket tempSocket, StrumienieIO tempStrumienieIO)
	{
		socket = tempSocket;
		strumienieIO = tempStrumienieIO;
		isInGame = false;
		lastTaskCode = 0;
	}
}
