import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Serwer 
{
	
	private static ServerSocket serverSocket;
	private static Queue<DaneUczestnika> qGraczy = new LinkedList<DaneUczestnika>();
	private static Queue<DaneUczestnika> qObserwatorow = new LinkedList<DaneUczestnika>();
	private static ArrayList<Gra> alGier = new ArrayList<Gra>();
	private static LinkedList<DaneUczestnika> lBezimiennych = new LinkedList<DaneUczestnika>();
	private static LinkedList<DaneUczestnika> lBezcelowych = new LinkedList<DaneUczestnika>();
	
	public static void uruchomSerwer(int portNr)
	{
		try
		{
			serverSocket = new ServerSocket(portNr);
		} 
		catch (IOException e) 
		{
			System.out.println("Nie udalo sie stworzyc ServerSocket :(");
			e.printStackTrace();
			return;
		}

		Thread jakMaszNaImie = new Thread(new JakMaszNaImie(lBezimiennych, lBezcelowych));
		jakMaszNaImie.start();
		
		Thread coChceszRobic = new Thread(new CoChceszRobic (lBezcelowych, qGraczy, qObserwatorow));
		coChceszRobic.start();

		Thread matchmaking = new Thread(new Matchmaking (qGraczy, alGier, lBezcelowych));
		matchmaking.start();
		
		while(true)
		{
			Socket socket;
			try 
			{
				socket = serverSocket.accept();
				System.out.println("1");
				if(socket != null)
				{
					System.out.println("2");
					DaneUczestnika nowyUczestnik = new DaneUczestnika(socket, new StrumienieIO(socket));
					System.out.println("3");
					lBezimiennych.add(nowyUczestnik);
					System.out.println("Dodalem uczestnika.");
				}
				else
				{
					System.out.println("Nie dodalem uczestnika");
				}
			}
			catch(Exception e)
			{
				System.out.println("Nie udalo sie nawiazac polaczenia z klientem o numerze portu " + portNr + " :(");
				e.printStackTrace();
			}
			
			if(matchmaking.isInterrupted() == true)
			{
				matchmaking = new Thread(new Matchmaking (qGraczy, alGier, lBezcelowych));
				matchmaking.start();
			}
			if(coChceszRobic.isInterrupted() == true)
			{
				coChceszRobic = new Thread(new CoChceszRobic (lBezcelowych, qGraczy, qObserwatorow));
				coChceszRobic.start();
			}
			if(jakMaszNaImie.isInterrupted() == true)
			{
				jakMaszNaImie = new Thread(new JakMaszNaImie (lBezimiennych, lBezcelowych));
				jakMaszNaImie.start();
			}
		}
	}
			
	public static void main(String[] args)
	{
		System.out.println("Serwer rozpoczyna prace \\o/");
		uruchomSerwer(6000);
		System.out.println("Serwer skoñczy³ pracê na dziœ.");
	}
	
	
}


