
public abstract class Uczestnik 
{
	protected StrumienieIO strumienieIO;
	protected String playerName;
	protected Plansza plansza;
	
	protected abstract int Graj() throws OpponentDisconnectedException, BrokenConnectionException, Exception;
}
