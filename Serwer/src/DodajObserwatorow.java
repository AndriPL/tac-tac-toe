import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class DodajObserwatorow implements Runnable
{
	private ArrayList<Gra> alGier = new ArrayList<Gra>();
	private static Queue<DaneUczestnika> qObserwatorow = new LinkedList<DaneUczestnika>();
	
	private int zapytaj(DaneUczestnika obserwator)
	{
		ObjectOutputStream oOS = obserwator.strumienieIO.oOS;
		try {
			oOS.writeInt(6); //taskCode == 6 - zapytaj obserwatora o nr gry
			oOS.flush();
			obserwator.lastTaskCode = 6;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Nie udalo sie wyslac kodu zadnia w funkcji coChceszRobic.");
			return -1;
		}
		return 0;
	}
	
	private int odbierz(DaneUczestnika obserwator)
	{
		int wybor = 0;
		try 
			{
				obserwator.socket.setSoTimeout(5);
				wybor = (int)obserwator.strumienieIO.oIS.readObject();
			} 
		catch (SocketTimeoutException e) {}
		catch (IOException | ClassNotFoundException e) 
		{
			e.printStackTrace();
			System.out.println("Nie udalo sie odebrac coChceRobic w funkcji coChceszRobic.");
			return -1;
		}
		return wybor;
	}
	
	public void run()
	{
		DaneUczestnika obserwator = null;
		int nrGry;
		
		while(true)
		{
			synchronized(qObserwatorow)
			{
				if(qObserwatorow.peek() != null)
					obserwator = qObserwatorow.poll();
				else 
					obserwator = null;
			}
			if(obserwator != null)
			{
				synchronized(obserwator)
				{
					if(obserwator.lastTaskCode != 6)
					{
						if(zapytaj(obserwator) != -1)
							qObserwatorow.add(obserwator);
					}
					else 
					{
						nrGry = odbierz(obserwator);
						
						if(nrGry == -1);
						else if(nrGry == 0)
						{
							synchronized(alGier)
							{
								int wylosowanyNrGry = (int)(alGier.size() * (double) Math.random()) + 1;
								alGier.get(wylosowanyNrGry).dodajObserwatora(obserwator);
							}
						}
						else if(nrGry > 0)
						{
							synchronized(alGier)
							{
								if(nrGry <= alGier.size())
									alGier.get(nrGry).dodajObserwatora(obserwator);
								else
								{
									obserwator.lastTaskCode = 0;
									qObserwatorow.add(obserwator);
								}
							}
						}
						else 
						{
							obserwator.lastTaskCode = 0;
							qObserwatorow.add(obserwator);
						}
					}
				}
			}
		}
		
	}

}
