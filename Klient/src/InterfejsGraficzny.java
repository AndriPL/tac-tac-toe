import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

public class InterfejsGraficzny implements Runnable
{
	private int staryNrPanelu;
	private int nowyNrPanelu;
	private Okno oknoGry;
	private Uczestnik uczestnik;
	
	public void run()
	{
		oknoGry = new Okno();
		
		while(true)
		{
			if(nowyNrPanelu != staryNrPanelu)
			{
				oknoGry.getContentPane().removeAll();
				oknoGry.wyswietlPanel(nowyNrPanelu);
				oknoGry.setVisible(true);
				staryNrPanelu = nowyNrPanelu;
			}
			else
			{
				try 
				{
					Thread.sleep(100);
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
					System.out.println("Nie udalo sie uspic watku w Klient w petli coDalej:(");
				}
			}
				
		}
	}
	
	public void setPanel(int n) // 1 - Panel Startowy, 2 - Panel Wyboru, 3 - Panel Gry
	{
		nowyNrPanelu = n;
	}
	
	public void insertCoordinates(int x, int y)
	{
		oknoGry.insertCoordinates(x, y);
	}
	
	public void setUczestnik(Uczestnik tempUczestnik)
	{
		uczestnik = tempUczestnik;
	}
	
	public void enablePanelGry()
	{
		oknoGry.enablePanelGry();
	}
	
	public void setCommunicate(String komunikat)
	{
		oknoGry.setCommunicate(komunikat);
		oknoGry.setVisible(true);
		
	}
	
	public void zakonczMecz()
	{
		oknoGry.zakonczMecz();
	}
	
	public class Okno extends JFrame
	{
//		JPanel panelKomunikatow = new JPanel();
		private JLabel komunikat;
		private PanelGracza panelGracza;
//		private PanelObserwatora panelObserwatora;
		private PanelWyboru panelWyboru;
		private PanelWyboruSymbolu panelWyboruSymbolu;
		private PanelStartowy panelStartowy;
		private PanelBleduConnectingFailed panelBleduConnectingFailed;
		private String symbolGracza;
		private String symbolPrzeciwnika;
		
		public Okno()
		{
			super("Kołko i krzyżyk: SUPER HIPER ULTRA BOOM! BANG! MULTIPLAYER Edition");
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setSize(525, 400);
			setLocation(100, 100);
			setVisible(true);
			
			komunikat = new JLabel();
			komunikat.setAlignmentX(CENTER_ALIGNMENT);
			komunikat.setBorder(BorderFactory.createEmptyBorder(50, 25, 50, 25));
			
			nowyNrPanelu = 1;
			staryNrPanelu = 0;
		}
		
		public void wyswietlPanel(int nrPanelu)
		{
			switch(nowyNrPanelu)
			{
			case 1:
				getContentPane().add(panelStartowy = new PanelStartowy());
				break;
			case 2:
				getContentPane().add(panelWyboru = new PanelWyboru());
				break;
			case 3:
				getContentPane().add(panelWyboruSymbolu = new PanelWyboruSymbolu());
				break;
			case 4:
				getContentPane().add(panelGracza = new PanelGracza());
				break;
//			case 5:
//				getContentPane().add(panelObserwatora = new PanelObserwatora());
//				break;
			case 6:
				getContentPane().add(panelBleduConnectingFailed = new PanelBleduConnectingFailed());
				break;
			default:
				break; //Nie wiem czy co tu dac. Czy to w ogóle może się tu zepsuć?
			}
		}
	
		public void insertCoordinates(int x, int y)
		{
			panelGracza.insertCoordinates(x, y);
		}
		
		public void enablePanelGry()
		{
			while(panelGracza == null)
			{
				try 
				{
					Thread.sleep(100);
				} catch (InterruptedException e) 
				{
					e.printStackTrace();
					System.out.println("Nie udalo sie uspic watku w Klient :(");
				}
			}
			panelGracza.enablePanelGry();
			setVisible(true);
		}
	
		public void setCommunicate(String tempKomunikat)
		{
			komunikat.setText(tempKomunikat);
			setVisible(true);
		}
		
		public void zakonczMecz()
		{
			panelGracza.zakonczMecz();
		}
		
		public class PanelStartowy extends JPanel implements ActionListener
		{
			JTextField polePodajNick;
			
			public PanelStartowy()
			{
				
				super();
				this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
				
				
				Box box = new Box(BoxLayout.Y_AXIS);
				JLabel tekst1 = new JLabel("Witaj w grze");
				JLabel tekst2 = new JLabel("\"Kołko i krzyżyk: SUPER HIPER ULTRA BOOM! BANG! MULTIPLAYER Edition\"");
				tekst1.setAlignmentX(CENTER_ALIGNMENT);
				tekst2.setAlignmentX(CENTER_ALIGNMENT);
//				tekst2.setFont(new Font("", Font.BOLD , 16));
				box.add(tekst1);
				box.add(tekst2);
				box.setBorder(BorderFactory.createEmptyBorder(50, 25, 50, 25));
				
				add(box);

				JPanel panelPodajNick = new JPanel(new FlowLayout());
				panelPodajNick.setBorder(BorderFactory.createEmptyBorder(50, 25, 50, 25));
				JLabel tekst3 = new JLabel("Podaj swój nick:");
				panelPodajNick.add(tekst3);
				
				polePodajNick = new JTextField("", 25);
//				polePodajNick.setBackground(Color.);
			 	polePodajNick.setHorizontalAlignment(JTextField.CENTER);
			 	polePodajNick.setBorder(BorderFactory.createLineBorder(Color.black, 1));
			 	polePodajNick.addActionListener(this);
			 	panelPodajNick.add(polePodajNick);
			 	
			 	add(panelPodajNick);
			}
			public void actionPerformed(ActionEvent zdarzenie)
			{
				String s = polePodajNick.getText();
				Klient.setPlayerName(s);
				
			}
		}
		
		public class PanelWyboru extends JPanel
		{
			
			public PanelWyboru()
			{
				super(new BorderLayout());
				
				JLabel tekst = new JLabel("Wybierz co chcesz robić");
				tekst.setHorizontalAlignment(JLabel.CENTER);
				tekst.setBorder(BorderFactory.createEmptyBorder(50, 25, 50, 25));
				
				add(BorderLayout.NORTH, tekst);
				
				JPanel panelPrzyciskow = new JPanel(new FlowLayout());
				panelPrzyciskow.setBorder(BorderFactory.createEmptyBorder(50, 25, 50, 25));
				
				JButton przyciskGraj = new JButton("Zagraj w grę");
				przyciskGraj.addActionListener(new GrajListener());
				panelPrzyciskow.add(przyciskGraj);

//				JButton przyciskObserwuj = new JButton("Obserwuj grę");
//				przyciskObserwuj.addActionListener(new ObserwujListener());
//				panelPrzyciskow.add(przyciskObserwuj);
				
				JButton przyciskZakoncz = new JButton("Zakończ grę");
				przyciskZakoncz.addActionListener(new ZakonczListener());
				panelPrzyciskow.add(przyciskZakoncz);
				
				add(BorderLayout.CENTER, panelPrzyciskow);
			}
			
			public class GrajListener implements ActionListener
			{
				public void actionPerformed(ActionEvent z)
				{
					Klient.setCoDalej(1);
					setPanel(3);
				}
			}
			
			public class ObserwujListener implements ActionListener
			{
				public void actionPerformed(ActionEvent z)
				{
					Klient.setCoDalej(2);
					setPanel(5);
				}
			}
			
			public class ZakonczListener implements ActionListener
			{
				public void actionPerformed(ActionEvent z)
				{
					Klient.setCoDalej(-1);
					//TODO - zakończ dzialanie
				}
			}
		}
		
		public class PanelWyboruSymbolu extends JPanel
		{
			
			public PanelWyboruSymbolu()
			{
				super(new BorderLayout());
				
				JLabel tekst = new JLabel("Wybierz swój symbol");
				tekst.setHorizontalAlignment(JLabel.CENTER);
				tekst.setBorder(BorderFactory.createEmptyBorder(50, 25, 50, 25));
				
				add(BorderLayout.NORTH, tekst);
				
				JPanel panelPrzyciskow = new JPanel(new FlowLayout());
				panelPrzyciskow.setBorder(BorderFactory.createEmptyBorder(50, 25, 50, 25));
				
				JButton przyciskX = new JButton("x");
				przyciskX.addActionListener(new XListener());
				panelPrzyciskow.add(przyciskX);

				JButton przyciskO = new JButton("o");
				przyciskO.addActionListener(new OListener());
				panelPrzyciskow.add(przyciskO);
								
				add(BorderLayout.CENTER, panelPrzyciskow);
			}
			
			public class XListener implements ActionListener
			{
				public void actionPerformed(ActionEvent z)
				{
					symbolGracza = "x";
					symbolPrzeciwnika = "o";
					setPanel(4);
					setCommunicate("Trwa wyszukiwanie przeciwnika");
					Klient.dalej();
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						e.printStackTrace();
						System.out.println("Nie udalo sie uspic watku w Klient :(");
					}
				}
			}
			
			public class OListener implements ActionListener
			{
				public void actionPerformed(ActionEvent z)
				{
					symbolGracza = "o";
					symbolPrzeciwnika = "x";
					setPanel(4);
					setCommunicate("Trwa wyszukiwanie przeciwnika");
					Klient.dalej();
					try 
					{
						Thread.sleep(300);
					} 
					catch (InterruptedException e) 
					{
						e.printStackTrace();
						System.out.println("Nie udalo sie uspic watku w IntefejsGraficzny :(");
					}
					
				}
			}
		}
		
		public class PanelGracza extends JPanel
		{
			private ArrayList<JButton> alPrzyciskow;
			private Gracz gracz;
						
			public PanelGracza()
			{
				super(new BorderLayout());
				
				add(BorderLayout.NORTH, komunikat);
				
				JPanel panelPrzyciskow = new JPanel(new GridLayout(3, 3));
				alPrzyciskow = new ArrayList<JButton>(9);
				for(int i = 0; i < 9; i++)
				{
					JButton przycisk = new JButton();
					przycisk.setPreferredSize(new Dimension(20, 20));
					przycisk.addActionListener(new PrzyciskListener());
					przycisk.setEnabled(false);
					
					alPrzyciskow.add(przycisk);
					panelPrzyciskow.add(przycisk);
				}
				panelPrzyciskow.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
				
				add(BorderLayout.CENTER, panelPrzyciskow);
				
				gracz = (Gracz) uczestnik;
			}
			
			public void disablePanelGry()
			{
				for(int i = 0; i < alPrzyciskow.size(); i++)
					alPrzyciskow.get(i).setEnabled(false);
				setVisible(true);

			}
			
			public void enablePanelGry()
			{
				for(int i = 0; i < alPrzyciskow.size(); i++)
				{
					JButton przycisk = alPrzyciskow.get(i);
					if(przycisk.getText().equals(""))
						przycisk.setEnabled(true);
				}
			}
			
			public void insertCoordinates(int x, int y)
			{
				if(!alPrzyciskow.get(3 * y + x).getText().equals(symbolGracza))
				{
					alPrzyciskow.get(3 * y + x).setEnabled(false);
					alPrzyciskow.get(3 * y + x).setText(symbolPrzeciwnika);
					setVisible(true);
				}
				
			}
			
			public class PrzyciskListener implements ActionListener
			{
				public void actionPerformed(ActionEvent z)
				{
					int index = alPrzyciskow.indexOf((JButton) z.getSource());
					alPrzyciskow.get(index).setEnabled(false);
					alPrzyciskow.get(index).setText(symbolGracza);
					disablePanelGry();
					if(gracz == null)
					{
						System.out.println("Gracz = null");
						gracz = (Gracz) uczestnik;
					}
					gracz.setCoordinates(index % 3, index/3);
				}
			}
			
			public void zakonczMecz()
			{
				JButton przejdzDalej = new JButton("Przejdź dalej");
				przejdzDalej.addActionListener(new PrzyciskPrzejdzDalej());
				getContentPane().add(BorderLayout.SOUTH, przejdzDalej);
				oknoGry.setVisible(true);
			}
			
			public class PrzyciskPrzejdzDalej implements ActionListener
			{
				public void actionPerformed(ActionEvent z)
				{
					gracz = null;
					uczestnik = null;
					Klient.dalej();
				}
			}	
		}
		
//		public class PanelObserwatora extends JPanel
//		{
//			private ArrayList<JButton> alPrzyciskow;
//			private Gracz gracz = (Gracz) uczestnik;
//						
//			public PanelObserwatora()
//			{
//				
//				super(new BorderLayout());
//				
//				add(BorderLayout.NORTH, komunikat);
//				
//				JPanel panelPrzyciskow = new JPanel(new GridLayout(3, 3));
//				alPrzyciskow = new ArrayList<JButton>(9);
//				for(int i = 0; i < 9; i++)
//				{
//					JButton przycisk = new JButton();
//					przycisk.setPreferredSize(new Dimension(20, 20));
//					przycisk.setEnabled(false);
//					
//					alPrzyciskow.add(przycisk);
//					panelPrzyciskow.add(przycisk);
//				}
//				panelPrzyciskow.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
//				
//				add(BorderLayout.CENTER, panelPrzyciskow);
//			}
//			
//			public void disablePanelGry()
//			{
//				for(int i = 0; i < alPrzyciskow.size(); i++)
//					alPrzyciskow.get(i).setEnabled(false);
//				setVisible(true);
//			}
//			
//			public void insertCoordinates(int x, int y)
//			{
//				if(!alPrzyciskow.get(3 * y + x).getText().equals(symbolGracza))
//				{
//					alPrzyciskow.get(3 * y + x).setEnabled(false);
//					alPrzyciskow.get(3 * y + x).setText(symbolPrzeciwnika);
//					setVisible(true);
//				}
//				
//			}
//			
//			public void zakonczMecz()
//			{
//				JButton przejdzDalej = new JButton("Przejdź dalej");
//				przejdzDalej.addActionListener(new PrzyciskPrzejdzDalej());
//				add(BorderLayout.SOUTH, przejdzDalej);
//				setVisible(true);
//			}
//			
//			public class PrzyciskPrzejdzDalej implements ActionListener
//			{
//				public void actionPerformed(ActionEvent z)
//				{
//					setPanel(2);
//					Klient.dalej();
//				}
//			}	
//		}
		
		public abstract class PanelBledu extends JPanel
		{
			protected JPanel panelPrzyciskow;
			protected JButton przycisk;
			
			public PanelBledu()
			{
				super(new BorderLayout());
				
				komunikat.setText("");
				komunikat.setHorizontalAlignment(JLabel.CENTER);
				komunikat.setBorder(BorderFactory.createEmptyBorder(50, 25, 50, 25));
				komunikat.setFont(new Font("",Font.BOLD, 16));
				
				add(BorderLayout.CENTER, komunikat);
				
				panelPrzyciskow = new JPanel(new FlowLayout());
				panelPrzyciskow.setBorder(BorderFactory.createEmptyBorder(50, 25, 50, 25));
				
				przycisk = new JButton("");
				panelPrzyciskow.add(przycisk);				
				add(BorderLayout.SOUTH, panelPrzyciskow);
			}
			
			public abstract class PrzyciskListener implements ActionListener
			{
				public abstract void actionPerformed(ActionEvent z);
				
			}
		}
			
		public  class PanelBleduConnectingFailed extends PanelBledu
		{
			
			public PanelBleduConnectingFailed()
			{
				super();
				panelPrzyciskow.setVisible(false);
				komunikat.setText("Błąd połaczenia z serwerem.");
				setVisible(true);
			}
		}
			
		public  class PanelBleduOpponentDisconnected extends PanelBledu
		{
			
			public PanelBleduOpponentDisconnected()
			{
				super();
				komunikat.setText("Przeciwnik wyszedł z gry.");
				przycisk.setText("Naciśnij, aby kontynuować.");
				przycisk.addActionListener(new PrzyciskListener());
				setVisible(true);
			}
			
			public class PrzyciskListener implements ActionListener
			{
				public void actionPerformed(ActionEvent z)
				{
					przycisk.setEnabled(false);
					przycisk.setVisible(false);
					komunikat.setText("Czekaj");
					setVisible(true);
					Klient.dalej();
				}
			}
		}
		
//		public class PanelUstawienSerwera extends JPanel implements ActionListener
//		{
//			JTextField polePodajIP;
//			JTextField polePodajPort;
//
//			public PanelUstawienSerwera()
//			{
//				
//				super();
//				this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//				
//				
//				Box box = new Box(BoxLayout.Y_AXIS);
//				JLabel tekst1 = new JLabel("Ustawienia serwera");
//				tekst1.setAlignmentX(CENTER_ALIGNMENT);
//				box.add(tekst1);
//				box.setBorder(BorderFactory.createEmptyBorder(50, 25, 50, 25));
//				
//				add(box);
//
//				JPanel panelPodajIP = new JPanel(new FlowLayout());
//				panelPodajIP.setBorder(BorderFactory.createEmptyBorder(50, 25, 50, 25));
//				JLabel tekst3 = new JLabel("Podaj adres IP serwera:");
//				panelPodajIP.add(tekst3);
//				
//				polePodajIP = new JTextField(Klient.getNrIP, 25);
////				polePodajNick.setBackground(Color.);
//			 	polePodajIP.setHorizontalAlignment(JTextField.CENTER);
//			 	polePodajIP.setBorder(BorderFactory.createLineBorder(Color.black, 1));
//			 	panelPodajIP.add(polePodajIP);
//			 	
//			 	add(panelPodajIP);
//			 	
//				JPanel panelPodajPort = new JPanel(new FlowLayout());
//				panelPodajPort.setBorder(BorderFactory.createEmptyBorder(50, 25, 50, 25));
//				JLabel tekst4 = new JLabel("Podaj numer portu:");
//				panelPodajPort.add(tekst4);
//				
//				polePodajPort = new JTextField("", 25);
////				polePodajNick.setBackground(Color.);
//				polePodajPort.setHorizontalAlignment(JTextField.CENTER);
//				polePodajPort.setBorder(BorderFactory.createLineBorder(Color.black, 1));
//			 	panelPodajPort.add(polePodajPort);
//			 	
//			 	add(panelPodajPort);
//			 	
//			 	JPanel panelPrzyciskow;
//			 	JButton przyciskZapisz = new JButton("Zapisz zmiany");
//			 	JButton przyciskAnuluj = new JButton("Anuluj");
//
//			}
//			
//			public class ZapiszZmianyListener implements ActionListener
//			{
//				public void actionPerformed(ActionEvent zdarzenie)
//				{
//					String s = polePodajIP.getText();
//					Klient.setNrIP(s);
//				}
//			}
//				
//		}

	}
}
