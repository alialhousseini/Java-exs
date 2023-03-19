package it.polito.po.test;

import java.util.List;

import ticketing.Commento;
import ticketing.Ticket;
import ticketing.Tracker;
import ticketing.Utente;
import junit.framework.TestCase;

public class TestR4_Commenti extends TestCase {

    Tracker t;
    String cp1;
    String cp2;
    String cp3;
    static final String nick1 = "jsm";
    static final String nick2 = "giove";
    static final String nick3 = "gv";
    static final String nick4 = "maro";

    private static void aspetta() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            // ignore exception
        }
    }

    public void setUp() throws Exception {
        t = new Tracker("http://www.polito.it/track");
        
        t.nuovoUtente(nick1, "John Smith", "john@smith.com", "secret");
        t.nuovoUtente(nick2, "Giovanni Verdi", "giova@green.it", "facile");
        t.nuovoUtente(nick3, "Giuseppe Verdi", "verdi@green.it", "aida");
        t.nuovoUtente(nick4, "Mario Rossi", "rossi@gov.it", "reds");
        cp1 = t.nuovoProdotto("Bug tracking system", "Sistema di gestione dei ");
        cp2 = t.nuovoProdotto("Web Portal", "company main web portal");
        cp3 = t.nuovoProdotto("My Cloud", "Enterprise wide cloud system");
        t.nuovoTicket(cp1, nick1, "No English version");
        aspetta();
        t.nuovoTicket(cp2, nick1, "Broken link in home page");
        aspetta();
        t.nuovoTicket(cp2, nick2, "Titolo errato");
    }

    public void testComment(){
        final String testo = " dovrebbe essere in grassetto";
        Ticket tk = t.getTickets().get(0);
        
        long before = System.currentTimeMillis();
        aspetta();
        Commento c = tk.nuovoCommento(nick3, testo);
        aspetta();
        long after = System.currentTimeMillis();
        
        Utente u = t.getUtente(nick3);
        long ts = c.getTimestamp();
        assertNotNull("Non c'e' nessun commento",c);
        
        assertEquals(testo, c.getTesto());
        assertEquals(u,c.getAutore());
        assertEquals(tk,c.getTicket());
        assertTrue("Il timestamp non sembra corretto",before < ts && ts < after);
    }
    
    public void testCommenti(){
        Ticket tk = t.getTickets().get(0);
        
        tk.nuovoCommento(nick2, "sulla pagina del personale");
        aspetta();
        tk.nuovoCommento(nick1, "ed anche su quella dei progetti");
        aspetta();
        tk.nuovoCommento(nick2, "Dovrebbe essere in maiuscolo e grassetto");
        
        List<Commento> commenti = tk.getCommenti();
        
        assertNotNull("Non ci sono commenti",commenti);
        assertEquals("Dovrebbero esserci 3 commenti",3,commenti.size());
        
    }

    public void testCommentiOrdinati(){
        Ticket tk = t.getTickets().get(0);
        
        tk.nuovoCommento(nick2, "sulla pagina del personale");
        aspetta();
        tk.nuovoCommento(nick1, "ed anche su quella dei progetti");
        aspetta();
        tk.nuovoCommento(nick2, "Dovrebbe essere in maiuscolo e grassetto");
        
        List<Commento> commenti = tk.getCommenti();
        
        assertTrue("Ordine non corretto",commenti.get(0).getTimestamp() >= commenti.get(1).getTimestamp() );
        assertTrue("Ordine non corretto",commenti.get(1).getTimestamp() >= commenti.get(2).getTimestamp() );

    }
}
