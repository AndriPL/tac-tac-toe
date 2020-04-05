import java.io.IOException;
import java.util.Scanner;

public class Obserwator extends Uczestnik
{
	public Obserwator(String tempPlayerName, StrumienieIO tempStrumienieIO)
	{
		playerName = tempPlayerName;
		strumienieIO = tempStrumienieIO;
	}
	
	public int Graj()
	{	
		plansza = null;
		int taskCode;
		
		do {
			taskCode = 0;
			
			do {
				try {
				taskCode = strumienieIO.oIS.readInt(); //TODO - dodać EOFException
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("Nie udalo sie odebrac taskCode");
				}
			}while(taskCode == 0);
			
			if(taskCode == 1 || taskCode == 2) //taskCode = 1 oznacza ruch danego gracza | taskCode = 2 ¿¹danie ponownego podania koordynatów
			{
					
			}
			
			
			if(taskCode == 4) //taskCode = 4 oznacza komunikat o nadawaniu planszy
			{
				do {
					try {
						
						plansza = (Plansza) strumienieIO.oIS.readUnshared();
						
					} catch (ClassNotFoundException | IOException e) 
					{
						e.printStackTrace();
						System.out.println("Nie udalo sie odebrac obiektu plansza");
					}
				}while(plansza == null);
				System.out.println(plansza);
			}		

			if(taskCode == -2)
			{
				String winner = null;
				do{
					try {
						winner = (String) strumienieIO.oIS.readObject();
					} 
					catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
						System.out.println("Nie udalo sie odebrac obiektu winner");
					}
				}while(winner == null);
				System.out.println("Wygra³ gracz " + winner + ".");
			}
			
			if(taskCode == -3)
			{
				System.out.println("Remis!");
			}
			
		}while(taskCode >= 0);
		return 0;
	}
}
