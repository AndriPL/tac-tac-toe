import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Klient 
{
	private static String playerName;
	private static StrumienieIO strumienieIO;
	private static Socket socket;
	private static int coDalej;
	private static InterfejsGraficzny gui;
	private static boolean czyDalej = false;
	private static String nrIP = "127.0.0.1";
	private static int nrPortu = 6000;
	
	
	protected static void connect(String adresIP, int nrPortu) throws ConnectingFailedException
	{
		try 
		{
			socket = new Socket(adresIP, nrPortu);
		} 
		catch (IOException e) 
		{
			throw new ConnectingFailedException("Nie udalo sie nawiazac polaczenia z serwerem o adresie " + adresIP + " i numerze portu " + nrPortu + " :(", e);
		}
	}
	
	private static void disconnect()
	{
		try 
		{
			strumienieIO.close();
		} 
		catch(MyException e1)
		{
			System.out.println("W klasie Klient w metodzie disconnect podczas zamykania strumienieIO wystapil wyjatek typu MyException");
			System.out.println("Nie wiem co z tym zrobić");
			System.out.println(e1.getMessage());
			e1.printStackTrace();
		}
		
		try 
		{
			socket.close();
			System.out.println("Pomyślnie rozłączono uczestnika");
		} 
		catch(IOException e1)
		{
			System.out.println("W klasie Klient w metodzie disconnect podczas zamykania socket wystapil wyjatek typuIOException");
			System.out.println("Nie wiem co z tym zrobić");
			System.out.println(e1.getMessage());
			e1.printStackTrace();
		}
	}

	protected static void stworzStrumienieIO() throws ConnectingFailedException, MyException
	{
			strumienieIO = new StrumienieIO(socket);
	}
	
	public static void sendName()
	{
		try {
			strumienieIO.oOS.writeObject(playerName);
			strumienieIO.oOS.flush();
			System.out.println("Imię wysłane.");
			} catch (IOException e) 
			{
				e.printStackTrace();
				System.out.println("Nie udalo sie wyslac obiektu playerName");
			}
	}
	
	public static void setPlayerName(String tempPlayerName)
	{
		playerName = tempPlayerName;
	}
	
	public static void setCoDalej(int tempCoDalej)
	{
		coDalej = tempCoDalej;
	}
	
	public static void setUczestnik(Uczestnik uczestnik)
	{
		gui.setUczestnik(uczestnik);
	}
	
	private static boolean czyDalej() throws InterruptedException
	{
		while(czyDalej == false)
		{
			try 
			{
				Thread.sleep(1000);
			} 
			catch (InterruptedException e) 
			{
				throw e;
			}
		}
		czyDalej = false;
		return true;
	}
	
	public static void dalej()
	{
		czyDalej = true;
	}

	public static void main (String [] args)
	{
		int taskCode;
		
		gui = new InterfejsGraficzny();
		Thread watekGUI = new Thread(gui);
		watekGUI.start();
		
		try
		{
			connect(nrIP, nrPortu);
				stworzStrumienieIO();
		}
		catch(ConnectingFailedException e)
		{
			try 
			{	gui.setPanel(6);
				czyDalej();
				return;
			} 
			catch (InterruptedException e1) 
			{
				System.out.println("Nie udalo sie uspic watku Klient :(");
				e1.printStackTrace();
				return;
			}
		}
		catch(MyException e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
			return;
		}
		
		
		taskCode = 0;
		do {
			try 
			{
				taskCode = strumienieIO.oIS.readInt();
			} 
			catch (IOException e) 
			{
				try 
				{	gui.setPanel(6);
					czyDalej();
					return;
				} 
				catch (InterruptedException e1) 
				{
					System.out.println("Nie udalo sie uspic watku Klient :(");
					e1.printStackTrace();
					return;
				}
			}
		}while(taskCode != 3);
		
		if(taskCode == 3)
		{
			while(playerName == null)
			{
				try 
				{
					Thread.sleep(100);
				} 
				catch (InterruptedException e) 
				{
					System.out.println("Nie udalo sie uspic watku Klient :(");
					e.printStackTrace();
					return;
				}
			}
			sendName();
		}
		
		
		try {
			do{
				coDalej = 0;
				gui.setPanel(2);
				taskCode = 0;
				
				do{
					try 
					{
						taskCode = strumienieIO.oIS.readInt();
					} 
					catch (IOException e) 
					{
						throw new BrokenConnectionException("Nie udalo sie odebrac taskCode", e);
					}
				}while(taskCode != 5);
				if(taskCode == 5)
				{
					
					do{
							try 
							{
								Thread.sleep(100);
							} 
							catch (InterruptedException e) 
							{
								System.out.println("Nie udalo sie uspic watku w Klient w petli coDalej:(");
								e.printStackTrace();
								return;
							}
						}while(coDalej != -1 && coDalej != 1 && coDalej != 2);
					
					try 
					{	gui.setPanel(3);
						czyDalej();
					} 
					catch (InterruptedException e1) 
					{
						System.out.println("Nie udalo sie uspic watku Klient :(");
						e1.printStackTrace();
						return;
					}
					
					try {
						strumienieIO.oOS.reset();
	//					strumienieIO.oOS.writeInt(coDalej);
						strumienieIO.oOS.writeObject(coDalej);
						strumienieIO.oOS.flush();
					} 
					catch (IOException e) 
					{
						throw new BrokenConnectionException("Nie udalo sie wyslac kodu z decyzja coChceszRobic", e);
					}
				}			
				try 
				{
					if(coDalej == 1)
					{
						Gracz player = new Gracz(playerName, strumienieIO, gui);
						gui.setUczestnik(player);
						player.Graj();
						try 
						{
							czyDalej();
						} 
						catch (InterruptedException e1) 
						{
							System.out.println("Nie udalo sie uspic watku Klient :(");
							e1.printStackTrace();
							return;
						}
					}
//					else if(coDalej == 2)
//					{
//						Obserwator obserwator = new Obserwator(playerName, strumienieIO);
//						gui.setUczestnik(obserwator);
//						obserwator.Graj();
//						coDalej = 0;
//						while(coDalej == 0)
//						{
//							try 
//							{
//								Thread.sleep(1000);
//							} 
//							catch (InterruptedException e1) 
//							{
//								e1.printStackTrace();
//								System.out.println("Nie udalo sie uspic watku w Klient :(");
//								return;
//							}
//						}
//					}
				}
				catch(BrokenConnectionException e)
				{
					try 
					{	gui.setPanel(6);
						czyDalej();
					} 
					catch (InterruptedException e1) 
					{
						System.out.println("Nie udalo sie uspic watku Klient :(");
						e1.printStackTrace();
						return;
					}
				}
				catch(OpponentDisconnectedException e)
				{
					try 
					{	gui.setPanel(7);
						czyDalej();
					} 
					catch (InterruptedException e1) 
					{
						System.out.println("Nie udalo sie uspic watku Klient :(");
						e1.printStackTrace();
						return;
					}
				}
				catch(Exception e)
				{
					System.out.println("To nie powinno się zdarzyc:);");
					e.printStackTrace();
					break;
				}
			}while(coDalej != -1);
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		disconnect();
		return;
	}
}
