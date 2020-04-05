import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.LinkedList;

public class Gra 
{
	private Plansza  oPlansza = new Plansza();
	ArrayList<DaneUczestnika> daneGraczy = new ArrayList<DaneUczestnika>();
	ArrayList<DaneUczestnika> daneObserwatorow = new ArrayList<DaneUczestnika>();
	LinkedList<DaneUczestnika> lBezcelowych;
	
	
	public Gra(DaneUczestnika daneUczestnika, DaneUczestnika daneUczestnika2, LinkedList<DaneUczestnika> tempLBezcelowych)
	{
		lBezcelowych = tempLBezcelowych;
		
		double liczba = (double) Math.random();
		DaneUczestnika gracz = new DaneUczestnika();
		if(liczba < 0.5)
		{
			gracz = daneUczestnika;
			daneGraczy.add(gracz);
			gracz = new DaneUczestnika();
			gracz = daneUczestnika2;
			daneGraczy.add(gracz);
		}
		else
		{
			gracz = daneUczestnika2;
			daneGraczy.add(gracz);
			gracz = new DaneUczestnika();
			gracz = daneUczestnika;
			daneGraczy.add(gracz);
		} 
	}

	public void dodajObserwatora(DaneUczestnika obserwator)
	{
		daneObserwatorow.add(obserwator);
	}
	
	public void broadcastCoordinates(Coordinates coordinates) throws BrokenConnectionException
	{
		for(int i=0; i<2; i++)
		{
			try 
			{
				ObjectOutputStream oOS = daneGraczy.get(i).strumienieIO.oOS;
				oOS.writeInt(4);
				oOS.flush();
				oOS.reset();
				oOS.writeUnshared(coordinates);
			} 
			catch (IOException e) 
			{
				throw new BrokenConnectionException("Nie udalo sie wyslac kodu zadnia do gracza " + daneGraczy.get(i).playerName + " w funkcji broadcastCoordinates.", e, daneGraczy.get(i));
			}
		}
		
		int rozmiarObserwatorow = daneObserwatorow.size();
		for(int i = 0; i < rozmiarObserwatorow; i++)
		{
			try 
			{
				ObjectOutputStream oOS = daneGraczy.get(i).strumienieIO.oOS;
				oOS.writeInt(4);
				oOS.flush();
				oOS.reset();
				oOS.writeUnshared(coordinates);
			}
			catch (IOException e) 
			{
				System.out.println("Nie udalo sie wyslac kodu zadnia lub coordinates do obserwatora " + daneGraczy.get(i).playerName + " w funkcji broadcastPlansza.");
				e.printStackTrace();
				disconnectPlayer(daneObserwatorow.get(i));
				daneObserwatorow.remove(i);
				i--;
				rozmiarObserwatorow--;
			}
		}
	}
	
	public int ktoWygral()
	{
		for(int  x= 0;  x <= 2 ; x++)
		{
			int y = 1;
//			System.out.print("\n" + x + "kolumna: ");
			while(y<=2 && oPlansza.getPlansza(x, y-1) == oPlansza.getPlansza(x, y))
			{
//				System.out.print(oPlansza.getPlansza(x, y-1) + " ");
				if(y==2 && oPlansza.getPlansza(x, y) != 0)
					return oPlansza.getPlansza(x, y);
				y++;
			}
		}
		for(int y=0; y<=2; y++)
		{
			int x=1;
//			System.out.print("\n" + y + "kolumna: ");
			while(x<=2 && oPlansza.getPlansza(x-1, y) == oPlansza.getPlansza(x, y))
			{
//				System.out.print(oPlansza.getPlansza(x-1, y) + " ");
				if(x==2 && oPlansza.getPlansza(x, y) != 0)
					return oPlansza.getPlansza(x, y);
				x++;
			}
		}
//		System.out.print("\n" + "skos malejacy: ");
		for(int x=1, y=1; x<=2 && y<=2 && oPlansza.getPlansza(x-1, y-1) == oPlansza.getPlansza(x, y); x++, y++)
		{
//			System.out.print(oPlansza.getPlansza(x-1, y-1) + " ");
			if(x==2 && y==2 && oPlansza.getPlansza(x, y) != 0)
				return oPlansza.getPlansza(x, y);
		}
//		System.out.print("\n" + "skos rosnacy: ");
		for(int x=1, y=1; x>=0 && y<=2 && oPlansza.getPlansza(x+1, y-1) == oPlansza.getPlansza(x, y); x--, y++)
		{
//			System.out.print(oPlansza.getPlansza(x+1, y-1) + " ");
			if(x==0 && y==2 && oPlansza.getPlansza(x, y) != 0)
				return oPlansza.getPlansza(x, y);
		}
		return 0;	
	}
	
	private void disconnectPlayer(DaneUczestnika uczestnik)
	{
		try 
		{
			uczestnik.strumienieIO.close();
		} 
		catch(MyException e1)
		{
			System.out.println("W klasie Gra w metodzie disconnectPlayer podczas zamykania strumienieIO wystapil wyjatek typu MyException");
			System.out.println("Nie wiem co z tym zrobiæ");
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
			System.out.println("W klasie Gra w metodzie disconnectPlayer podczas zamykania socket wystapil wyjatek typuIOException");
			System.out.println("Nie wiem co z tym zrobiæ");
			System.out.println(e1.getMessage());
			e1.printStackTrace();
		}
	}
	
	private void zakonczGre()
	{
		for(int i = 0; i < daneGraczy.size(); i++)
		{
			try 
			{
				daneGraczy.get(i).strumienieIO.oOS.writeInt(-444);
				daneGraczy.get(i).strumienieIO.oOS.flush();
//				daneGraczy.get(i).strumienieIO.oOS.writeObject(playerName);
//				daneGraczy.get(i).strumienieIO.oOS.flush();
			
				lBezcelowych.addLast(daneGraczy.get(i));
			} 
			catch (IOException e) {}
		}
		
		int rozmiarObserwatorow = daneObserwatorow.size();
		for(int i = 0; i < rozmiarObserwatorow; i++)
		{
			try 
			{
				DaneUczestnika uczestnik = daneObserwatorow.get(i);
				uczestnik.strumienieIO.oOS.writeInt(-444);
				uczestnik.strumienieIO.oOS.flush();
//				uczestnik.strumienieIO.oOS.writeObject(playerName);
//				uczestnik.strumienieIO.oOS.flush();
			
				lBezcelowych.addLast(uczestnik);
			} 
			catch (IOException e) 
			{
				System.out.println("Nie udalo sie wyslac kodu zadnia lub coordinates do obserwatora " + daneGraczy.get(i).playerName + " w funkcji broadcastPlansza.");
				e.printStackTrace();
				disconnectPlayer(daneObserwatorow.get(i));
				daneObserwatorow.remove(i);
				i--;
				rozmiarObserwatorow--;
			}
		}
	}
	
	public boolean runGra()
	{
		Coordinates coordinates = null;
		int ktoWygral = 0;
		int ileRuchow = 0;
		int czyjRuch = 0;
		
		System.out.println("Zaczynamy gre.");
		
		try
		{
			for(int i=0; i<2; i++)
			{
				try 
				{
					daneGraczy.get(i).strumienieIO.oOS.writeInt(101);
					daneGraczy.get(i).strumienieIO.oOS.flush();
				} 
				catch (IOException e) 
				{
					throw new BrokenConnectionException("Nie udalo sie wyslac kodu rozpoczêcia gry w klasie Gra w funkcji runGra do gracza" + daneGraczy.get(i).playerName + ".", e, daneGraczy.get(i));
				}
			}
		
			do {
				ileRuchow++;
				coordinates = null;
				
				try 
				{
					daneGraczy.get(czyjRuch).strumienieIO.oOS.writeInt(1);
					daneGraczy.get(czyjRuch).strumienieIO.oOS.flush();
//					System.out.println(oPlansza);
				} 
				catch (IOException e) 
				{
					throw new BrokenConnectionException("Nie udalo sie wyslac kodu zadnia w klasie Gra w funkcji runGra do gracza" + daneGraczy.get(czyjRuch).playerName + ".", e, daneGraczy.get(czyjRuch));
				}
				
				try 
				{
					daneGraczy.get(czyjRuch).socket.setSoTimeout(600000);
					coordinates = (Coordinates) daneGraczy.get(czyjRuch).strumienieIO.oIS.readObject();
				} 
				catch (SocketTimeoutException e) 
				{
					throw new BrokenConnectionException("Czas oczekiwania na coordinates w klasie Gra w funkcji runGra od gracza" + daneGraczy.get(czyjRuch).playerName + ".", e, daneGraczy.get(czyjRuch));
				}
				catch (ClassNotFoundException e) 
				{
					throw new ClassNotFoundException("To nie powinno siê zdarzyæ: \n ClassNotFoundException podczas odbierania coordinates w klasie Gra w funkcji runGra od gracza" + daneGraczy.get(czyjRuch).playerName + ".", e);
				}
				catch (IOException e) 
				{
					throw new BrokenConnectionException("Nie uda³o sie odebraæ coordinates w klasie Gra w funkcji runGra od gracza" + daneGraczy.get(czyjRuch).playerName + " z powodu IOException.", e, daneGraczy.get(czyjRuch));
				}

				if(oPlansza.isLegal(coordinates) == false)
	//			while(oPlansza.isLegal(coordinates) == false)
				{
					try 
					{
						daneGraczy.get(czyjRuch).strumienieIO.oOS.writeInt(2);
						daneGraczy.get(czyjRuch).strumienieIO.oOS.flush();
						System.out.println(oPlansza);
					} 
					catch (IOException e) 
					{
						throw new BrokenConnectionException("Nie uda³o sie kodu zadania 2 w klasie Gra w funkcji runGra do gracza" + daneGraczy.get(czyjRuch).playerName + " z powodu IOException.", e, daneGraczy.get(czyjRuch));
					}
	//				do {
	//					try {
	//						coordinates = (Coordinates)daneGraczy.get(czyjRuch).strumienieIO.oIS.readObject();
	//						System.out.println(coordinates.getX() + " " + coordinates.getY());
	//					} 
	//					catch (SocketTimeoutException e) {}	
	//					catch (ClassNotFoundException | IOException e) {
	//						e.printStackTrace();
	//						System.out.println("Nie udalo sie odebrac coordinates w funkcji runGra.");
	//					}
	//				}while(coordinates == null);
				}
				
				oPlansza.set(coordinates, czyjRuch+1);
				System.out.println(oPlansza);
				broadcastCoordinates(coordinates);
				
				if(ileRuchow >= 4)
				{
					ktoWygral = ktoWygral();
					System.out.println(ktoWygral);
				}
				
				czyjRuch = (czyjRuch * (-1)) + 1;
				System.out.println(ileRuchow);
			}while(ktoWygral == 0 && ileRuchow < 9);
			
			for(int i=0; i<2; i++)
			{
				if(ktoWygral != 0)
				{
					if(i+1 == ktoWygral)
					{
						try 
						{
							daneGraczy.get(i).strumienieIO.oOS.writeInt(-1);
							daneGraczy.get(i).strumienieIO.oOS.flush();
							lBezcelowych.addLast(daneGraczy.get(i));
						} 
						catch (IOException e) 
						{
							throw new BrokenConnectionException("Nie udalo sie wyslac kodu zadnia podczas informowania zwyciezcy" + daneGraczy.get(i).playerName +".", e, daneGraczy.get(i));
						}
					}
					else
					{
						try 
						{
							daneGraczy.get(i).strumienieIO.oOS.writeInt(-2);
							daneGraczy.get(i).strumienieIO.oOS.flush();
							daneGraczy.get(i).strumienieIO.oOS.writeUnshared(daneGraczy.get(i).playerName);
							daneGraczy.get(i).strumienieIO.oOS.flush();
							lBezcelowych.addLast(daneGraczy.get(i));
						} 
						catch (IOException e) 
						{
							throw new BrokenConnectionException("\"Nie udalo sie wyslac kodu zadnia podczas informowania przegranych" + daneGraczy.get(i).playerName +".", e, daneGraczy.get(i));
						}
					}
				}
				else
				{
					try 
					{
						daneGraczy.get(i).strumienieIO.oOS.writeInt(-3);
						daneGraczy.get(i).strumienieIO.oOS.flush();
						lBezcelowych.addLast(daneGraczy.get(i));
					} 
					catch (IOException e) 
					{
						throw new BrokenConnectionException("Nie udalo sie wyslac kodu zadnia podczas informowania o remisie gracza" + daneGraczy.get(i).playerName +".", e, daneGraczy.get(i));
					}	
				}
			}
	
			int rozmiarObserwatorow = daneObserwatorow.size();
			for(int i = 0; i < rozmiarObserwatorow; i++)
			{
				try 
				{
					daneObserwatorow.get(i).strumienieIO.oOS.writeInt(-2);
					daneObserwatorow.get(i).strumienieIO.oOS.flush();
					daneObserwatorow.get(i).strumienieIO.oOS.writeUnshared(daneGraczy.get(i).playerName);
					daneObserwatorow.get(i).strumienieIO.oOS.flush();
					lBezcelowych.addLast(daneObserwatorow.get(i));
				} 
				catch (IOException e) 
				{
					throw new BrokenConnectionException("Nie udalo sie wyslac kodu zadnia podczas informowania o zakoñczeniu gry obserwatora" + daneObserwatorow.get(i).playerName +".", e, daneObserwatorow.get(i));
				}
			}
			
			System.out.println("Gra zakonczona.");
			return true;
		}
		catch(BrokenConnectionException e)
		{
			System.out.println("W klasie Gra w metodzie runGra wystapil wyjatek typu BrokenConnectionException.");
			System.out.println(e.getMessage());
			disconnectPlayer(e.getUczestnik());
			daneGraczy.remove(e.getUczestnik());
			zakonczGre();
			return false;
		}
		catch(Exception e)
		{
			System.out.println("W klasie Gra w metodzie runGra wystapil wyjatek typu Exception.");
			System.out.println(e.getMessage());
			e.printStackTrace();
			zakonczGre();
			return false;
		}
		
	}			
}
//public void broadcastPlansza()
//{
//	for(int i=0; i<2; i++)
//	{
//		try {
//			daneGraczy.get(i).strumienieIO.oOS.writeInt(4);
//			daneGraczy.get(i).strumienieIO.oOS.flush();
//		} catch (IOException e) {
//			e.printStackTrace();
//			System.out.println("Nie udalo sie wyslac kodu zadnia w funkcji broadcastPlansza.");
//		}
//		try {
//			daneGraczy.get(i).strumienieIO.oOS.reset();
//			daneGraczy.get(i).strumienieIO.oOS.writeUnshared(oPlansza);
//		} catch (IOException e) {
//			e.printStackTrace();
//			System.out.println("Nie udalo sie wyslac planszy w funkcji broadcastPlansza.");
//		}
//	}
//	synchronized(daneObserwatorow)
//	{
//		int rozmiar = daneObserwatorow.size();
//		
//		for(int i=0; i<rozmiar; i++)
//		{
//			try {
//				daneObserwatorow.get(i).strumienieIO.oOS.writeInt(4);
//				daneObserwatorow.get(i).strumienieIO.oOS.flush();
//			}
////			catch ( e) { //TODO - wywalanie obserwatora gdy nie mozna sie z nim polaczyc
////				e.printStackTrace();
////				System.out.println("Nie udalo sie wyslac kodu zadnia w funkcji broadcastPlansza.");
////			}
//			catch (IOException e) {
//				e.printStackTrace();
//				System.out.println("Nie udalo sie wyslac kodu zadnia w funkcji broadcastPlansza.");
//			}
//			
//			try {
//				daneObserwatorow.get(i).strumienieIO.oOS.reset();
//				daneObserwatorow.get(i).strumienieIO.oOS.writeUnshared(oPlansza);
//			} catch (IOException e) {
//				e.printStackTrace();
//				System.out.println("Nie udalo sie wyslac planszy Obserwatorowi w funkcji broadcastPlansza.");
//			}
//		}
//	}	
//}

