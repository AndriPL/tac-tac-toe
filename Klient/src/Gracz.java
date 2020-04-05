import java.io.EOFException;
import java.io.IOException;
import java.util.Scanner;

public class Gracz extends Uczestnik
{
	private Coordinates coordinates;
	private InterfejsGraficzny gui;
	
	public Gracz(String tempPlayerName, StrumienieIO tempStrumienieIO, InterfejsGraficzny tempGUI)
	{
		playerName = tempPlayerName;
		strumienieIO = tempStrumienieIO;
		coordinates = new Coordinates();
		gui = tempGUI;
	}
	
	public void setCoordinates(int x, int y)
	{
		coordinates.set(x, y, true);
	}
	
//	public Coordinates getCoordinates()
//	{
//		coordinates.setIsNew(false);
//		return coordinates;
//	}
	
	public int Graj() throws OpponentDisconnectedException, BrokenConnectionException, Exception
	{
		gui.setUczestnik(this);
		System.out.println("Gra rozpoczeta!");
		int taskCode;
		
		do {
			taskCode = 0;
			
			do {
				try 
				{
					taskCode = strumienieIO.oIS.readInt();
				} 
				catch (IOException e) 
				{
					throw new BrokenConnectionException("Nie udalo sie odebrac taskCode", e);
				}
			}while(taskCode == 0);
			
			if(taskCode == 101)
			{
				gui.setCommunicate("Gra rozpoczęta! Czekaj na ruch przeciwnika.");
			}
			
			if(taskCode == -444)
			{
				throw new OpponentDisconnectedException();
			}
			
			if(taskCode == 1 || taskCode == 2) //taskCode = 1 oznacza ruch danego gracza | taskCode = 2 ¿¹danie ponownego podania koordynatów
				{
					if(taskCode == 1)
					{
						gui.setCommunicate("Podaj wspolrzedne pola, w które chcesz wstawic znak.");
						gui.enablePanelGry();
						System.out.println("Podaj wspolrzedne pola, w które chcesz wstawic znak.");
					}
					if(taskCode == 2)
						gui.setCommunicate("Podane przez ciebie wspolrzedne byly niepoprawne. Serwer zada ponownego podania koordynatow.");
					
					while(coordinates.getIsNew() == false)
					{
						try 
						{
							Thread.sleep(100);
						} 
						catch (InterruptedException e) 
						{
							throw new Exception("Nie udalo sie uspic watku w klasie Gracz w metodzie Graj:(", e);
						}
					}
					coordinates.setIsNew(false);
					
//					while(coordinates.isCorrect() == false)
//					{
//						System.out.println("Podane przez ciebie wspolrzedne sa niepoprawne.");
//						System.out.println("Podaj wspolrzedne ponownie.");
//						while(coordinates.getIsNew() == false)
//						{
//							try {
//								Thread.sleep(100);
//							} catch (InterruptedException e) {
//								e.printStackTrace();
//								System.out.println("Nie udalo sie uspic watku w Klient :(");
//							}
//						}
//						coordinates.setIsNew(false);
//					}
//					
					try 
					{
						System.out.println(coordinates.getX() + " " + coordinates.getY());
						strumienieIO.oOS.writeUnshared(coordinates);
						strumienieIO.oOS.flush();
					} 
					catch (IOException e) 
					{
						throw new BrokenConnectionException("Nie udalo sie  wyslac obiektu coordinates", e);
					}
				}
			
			if(taskCode == 4) //taskCode = 4 oznacza komunikat o nadawaniu planszy
			{
				try 
				{
					Coordinates serverCoordinates;
					serverCoordinates = (Coordinates) strumienieIO.oIS.readUnshared();
					gui.insertCoordinates(serverCoordinates.getX(), serverCoordinates.getY());
				} 
				catch (ClassNotFoundException e)
				{
					throw new Exception(e);
				}
				catch(IOException e) 
				{
					throw new BrokenConnectionException("Nie udalo sie odebrac obiektu serverCoordinates", e);
				}
			}
			
			if(taskCode == -2)
			{
				String winner = null;
				try 
				{
					winner = (String) strumienieIO.oIS.readObject();
				} 
				catch (ClassNotFoundException e)
				{
					throw new Exception(e);
				}
				catch(IOException e) 
				{
					throw new BrokenConnectionException("Nie udalo sie odebrac imienia zwyciezcy", e);
				}
				System.out.println("Przegrałeś!");
				System.out.println("Wygrał gracz " + winner + ".");
				gui.setCommunicate("Przegrałeś! Wygrał gracz " + winner + ".");
			}
			if(taskCode == -1)
			{
				System.out.println("Gratulacje! Wygrałeś.");
				gui.setCommunicate("Gratulacje! Wygrałeś.");
			}
			
			if(taskCode == -3)
			{
				System.out.println("Remis!");
				gui.setCommunicate("Remis!");
			}	
		}while(taskCode >= 0);
		gui.zakonczMecz();
		System.out.println("Koniec Gry");
		return 0;
	}
}