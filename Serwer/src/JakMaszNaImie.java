import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.LinkedList;

public class JakMaszNaImie implements Runnable
{
	private LinkedList<DaneUczestnika> lBezimiennych = new LinkedList<DaneUczestnika>();
	private LinkedList<DaneUczestnika> lBezcelowych = new LinkedList<DaneUczestnika>();
	
	public JakMaszNaImie(LinkedList <DaneUczestnika> tempLBezimiennych, LinkedList <DaneUczestnika> tempLBezcelowych)
	{
		lBezimiennych = tempLBezimiennych;
		lBezcelowych = tempLBezcelowych;
	}
	
	protected void askForName(DaneUczestnika uczestnik) throws BrokenConnectionException
	{
		try 
		{
			uczestnik.strumienieIO.oOS.writeInt(3);
			uczestnik.strumienieIO.oOS.flush();
			uczestnik.lastTaskCode = 3;
		} 
		catch (IOException e) 
		{
			throw new BrokenConnectionException("Nie udalo sie wyslac kodu zadnia w funkcji askForName.", e, uczestnik);
		}
	}

	protected String readName(DaneUczestnika uczestnik) throws BrokenConnectionException
	{
		String playerName = null;
		
		try 
		{	
			uczestnik.socket.setSoTimeout(5);
			playerName = (String) uczestnik.strumienieIO.oIS.readObject();
		} 
		catch (SocketTimeoutException e) 
		{
			//Spokojnie to tylko po to, ¿eby program nie czeka³ za d³ugo na odpowiedz i móg³ obs³u¿yæ pozosta³ych klientow.
		}
		catch (ClassNotFoundException | IOException e) 
		{
			throw new BrokenConnectionException("Nie udalo sie odebrac playerName w funkcji askForName.", e, uczestnik);
		}
		
		return playerName;
	}
	
	public void run()
	{
		DaneUczestnika uczestnik;
		String playerName = null;
		
		while(true)
		{
			uczestnik = null;
//			System.out.println("Jestem w while w CoChceszRobic.");
			synchronized(lBezimiennych)
			{
				if(lBezimiennych.peek() != null)
					uczestnik = lBezimiennych.poll();
			}
			if(uczestnik != null)
			{
				synchronized(uczestnik)
				{
					try 
					{
						if(uczestnik.lastTaskCode != 3)
						{
							askForName(uczestnik);
							System.out.println("ZapytaÅ‚em o imie w klasie JakMaszNaImie.");
							lBezimiennych.addLast(uczestnik);
						}
						else 
						{
							playerName = readName(uczestnik);
							
							if(playerName != null)
							{
								System.out.println(playerName);
								uczestnik.playerName = playerName;
								lBezcelowych.addLast(uczestnik);
							}
							else
								lBezimiennych.addLast(uczestnik);
						}
					}
					catch(BrokenConnectionException e)
					{
						System.out.println("W klasie JakMaszNaImie w metodzie run wystapil wyjatek typu BrokenConnectionException.");
						System.out.println(e.getMessage());
						
						try 
						{
							uczestnik.strumienieIO.close();
						} 
						catch(MyException e1)
						{
							System.out.println("W klasie JakMaszNaImie w metodzie run podczas zamykania strumienieIO wystapil wyjatek typu MyException");
							System.out.println(e1.getMessage());
							e1.printStackTrace();
						}
						
						try 
						{
							uczestnik.socket.close();
							System.out.println("Pomyœlnie roz³¹czono uczestnika");
							uczestnik = null;
						} 
						catch(IOException e1)
						{
							System.out.println("W klasie JakMaszNaImie w metodzie run podczas zamykania socket wystapil wyjatek typuIOException");
							System.out.println(e1.getMessage());
							e1.printStackTrace();
						}
					}
					catch(Exception  e)
					{
						System.out.println("W klasie JakMaszNaImie w metodzie run wystapil wyjatek typu Exception");
						System.out.println("Nie wiem co z tym zrobiæ");
						e.printStackTrace();
					}
//					
				}
			}
			else
			{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
					System.out.println("Nie udalo sie uspic watku w Matchmaking :(");
				}
			}
			
		}
	}
}
