import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Matchmaking implements Runnable
	{
		private Queue<DaneUczestnika> qGraczy;
		private ArrayList<Gra> alGier;
		LinkedList<DaneUczestnika> lBezcelowych;
		
		public Matchmaking(Queue<DaneUczestnika> tempQGraczy, ArrayList<Gra> tempALGier, LinkedList<DaneUczestnika> tempLBezcelowych )
		{
			qGraczy = tempQGraczy;
			alGier = tempALGier;
			lBezcelowych = tempLBezcelowych;
		}
		
		private class PrzeprowadzGre implements Runnable
		{
			private Gra nowaGra;
			private DaneUczestnika gracz1;
			private DaneUczestnika gracz2;
			LinkedList<DaneUczestnika> lBezcelowych;


			private PrzeprowadzGre(DaneUczestnika tempGracz1, DaneUczestnika tempGracz2, ArrayList<Gra> alGier, LinkedList<DaneUczestnika> tempLBezcelowych )
			{
				gracz1 = tempGracz1;
				gracz2 = tempGracz2;
				lBezcelowych = tempLBezcelowych;
			}
			
			public void run()
			{
				nowaGra = new Gra(gracz1, gracz2, lBezcelowych);
				alGier.add(nowaGra);
				nowaGra.runGra();
			}
		
		}
		
		public void run()
		{
			while(true)
			{
				synchronized (qGraczy)
				{
					if(qGraczy.peek() != null)
					{
						DaneUczestnika pierwszyGracz = qGraczy.poll();
						if(qGraczy.peek() != null)
						{
							Thread nowaGra = new Thread(new PrzeprowadzGre(pierwszyGracz, qGraczy.poll(), alGier, lBezcelowych));
							nowaGra.start();
						}
						else
							qGraczy.add(pierwszyGracz);
					}
				}
				
				try 
				{
					Thread.sleep(100);
				} 
				catch (InterruptedException e) 
				{
					break;
				}
			}
		}
	}
