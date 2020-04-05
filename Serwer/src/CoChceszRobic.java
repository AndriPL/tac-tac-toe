import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.LinkedList;
import java.util.Queue;

public class CoChceszRobic implements Runnable
{
	private LinkedList<DaneUczestnika> lOczekujacychUczestnikow;
	private Queue<DaneUczestnika> qGraczy;
	private Queue<DaneUczestnika> qObserwatorow;
	
	public CoChceszRobic(LinkedList <DaneUczestnika> tempLOczekujacychUczestnikow, Queue<DaneUczestnika> tempQGraczy, Queue<DaneUczestnika> tempQObserwatorow)
	{
		lOczekujacychUczestnikow = tempLOczekujacychUczestnikow;
		qGraczy = tempQGraczy;
		qObserwatorow = tempQObserwatorow;
	}

	protected void zapytajCoChceRobic(DaneUczestnika uczestnik) throws BrokenConnectionException
	{
		StrumienieIO strumienIO = uczestnik.strumienieIO;
		try {
			strumienIO.oOS.writeInt(5);
			strumienIO.oOS.flush();
			uczestnik.lastTaskCode = 5;
		} 
		catch (IOException e) 
		{
			throw new BrokenConnectionException("Nie udalo sie wyslac kodu zadnia w funkcji coChceszRobic.", e, uczestnik);
		}	
	}
	
	protected int odbierzCoChceRobic(DaneUczestnika uczestnik) throws BrokenConnectionException //1 - gracz, 2 - obserwator, -1 koniec
	{
		int wybor = 0;
		try 
			{
				uczestnik.socket.setSoTimeout(5);
				wybor = (int)uczestnik.strumienieIO.oIS.readObject(); // Z jakiegoœ dziwnego powody nie dzia³a³o tu readInt() i odzczytywa³o dla wszystkich klientów tê sam¹ losow¹ liczbê.
				System.out.println("Wybor uczestnika " + uczestnik.playerName + " to " + wybor);
				uczestnik.lastTaskCode = 0;
			} 
		catch (SocketTimeoutException e) 
		{
			//Spokojnie to tylko po to, ¿eby program nie czeka³ za d³ugo na odpowiedz i móg³ obs³u¿yæ pozosta³ych klientow.
		}
		catch (IOException | ClassNotFoundException e) 
		{
			throw new BrokenConnectionException("Nie udalo sie odebrac coChceRobic w funkcji coChceszRobic.", e, uczestnik);
		}
		
		return wybor;
	}
	private void disconnectPlayer(DaneUczestnika uczestnik)
	{
		try 
		{
			uczestnik.strumienieIO.close();
		} 
		catch(MyException e1)
		{
			System.out.println("W klasie CoChceszRobic w metodzie run odczas zamykania stumienieIO wystapil wyjatek typu MyException");
			System.out.println("Nie wiem co z tym zrobiæ");
			System.out.println(e1.getMessage());
			e1.printStackTrace();
		}
		
		try 
		{
			uczestnik.socket.close();
			System.out.println("Pomyœlnie roz³¹czono uczestnika " + uczestnik.playerName);
			uczestnik = null;
		} 
		catch(IOException e1)
		{
			System.out.println("W klasie CoChceszRobic w metodzie run podczas zamykania socket wystapil wyjatek typuIOException");
			System.out.println("Nie wiem co z tym zrobiæ");
			System.out.println(e1.getMessage());
			e1.printStackTrace();
		}
	}
	
	public void run()
	{
		int wybor;
		DaneUczestnika uczestnik;
		
		while(true)
		{
			wybor = 0;
			uczestnik = null;
//			System.out.println("Jestem w while w CoChceszRobic.");
			synchronized(lOczekujacychUczestnikow)
			{
				if(lOczekujacychUczestnikow.peek() != null)
					uczestnik = lOczekujacychUczestnikow.poll();
			}
			if(uczestnik != null)
			{
				synchronized(uczestnik)
				{
//					System.out.println("Warunek if(uczestnik != null) speï¿½niony.");
					try 
					{
						if(uczestnik.lastTaskCode != 5)
						{
							zapytajCoChceRobic(uczestnik);
							lOczekujacychUczestnikow.addLast(uczestnik);
							System.out.println("Zapytalem gracza " + uczestnik.playerName + "coChceRobic");
						}
						else 
						{
	//						System.out.println("Warunek if(uczestnik.lastTaskCode != 5) nie speï¿½niony.");
							wybor = odbierzCoChceRobic(uczestnik);
							switch(wybor)
							{
								case 1:
									qGraczy.add(uczestnik);
									System.out.println("DodaÅ‚em gracza " + uczestnik.playerName);
									break;
								case 2:
									qObserwatorow.add(uczestnik);
									System.out.println("DodaÅ‚em obserwatora " + uczestnik.playerName);
									break;
								case -1:
									disconnectPlayer(uczestnik);
									break;
								default:
	//								System.out.println("Default");
									lOczekujacychUczestnikow.addLast(uczestnik);
									break;
							} 
								
						}
					}
					catch(BrokenConnectionException e)
					{
						System.out.println("W klasie CoChceszRobic w metodzie run wystapil wyjatek typu BrokenConnectionException.");
						System.out.println(e.getMessage());
						disconnectPlayer(uczestnik);
					}
					catch(Exception  e)
					{
						System.out.println("W klasie CoChceszRobic w metodzie run wystapil nieznany wyjatek typu Exception.");
						System.out.println("Nie wiem co z tym zrobiæ");
						e.printStackTrace();
					}
				}
			}
		}
	}
}
