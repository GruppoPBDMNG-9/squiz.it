package dao;

import georecord.ContinentRecord;
import georecord.CountryRecord;
import utility.CalendarUtility;
import utility.StatisticRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class RedisDAO extends DAO {

    public boolean availableUrl(String url){
        /*
        Controlla nella supercolonna degli shortening se la customizzazione scelta è disponibile
         */
        return true;

    }

    /*
    Salva un nuovo url shortening. Se è associato ad un username lo associa ad esso, altrmenti finisce fra gli anonmi.
    Le cose da salvare sono quindi:
    - short url
    - long url
    - data in cui è stato effettuato lo shortening
    - username (eventualmente null)
     */
    public void saveUrl(String longUrl, String shortUrl, String username){
        final String UNDEFINED_USER = "---"; //se l'url va messo fra quelli anonimi arriva questo username
        final String data = new CalendarUtility().getCurrentData();

        if(username.equals(UNDEFINED_USER)){
            //Inserisci l'url fra quelli anonimi
        } else {
            //Inserisci l'url associato all'utente
        }


        //Una stampa di prova per vedere se i dati giungono correttamente fin qui
        System.out.println("longUrl = [" + longUrl + "], shortUrl = [" + shortUrl + "], username = [" + username + "]");
    }

    /*
      Ritorna TRUE se l'username esiste && se la password coincide con l'username.
      Ritorna FALSE altrmenti
       */
    public boolean login(String username, String password){
        return true;
    }

    public HashMap<String, Object> loadUserStat(String username){
        /* PER DONATO E CORRADO
        Questo metodo resistuisce la statistiche dell'utente avente l'username passato come argomento.
        Più nel dettaglio ritorna 3 oggetti:
        - shorteners totali
        - click totali, quindi sommando tutti click per ogni shortening salvato
        - Una lista di record. Ogni oggetto record contiene le informazioni su un singolo shortening effettuato.

        La strategia è quella di creare solo la lista di shortening, e di calcolare in questo metodo shorteners totali e click totali,
        in modo da non avere informazioni ridondanti nel database. Create solo la lista di oggetti StatisticRecord al posto delle prove
        che ho fatto io. Total shorteners e click totali li ho già calcolati io.
        Seguono due shortening di prova per maggior chiarezza e utilizzati per restare il client

        Vi voglio bene :)
        */

        //Map di esempio
        HashMap<String, Object> result  = new HashMap<String, Object>();

        //Due shortening di prova
        ArrayList<String> args1 = new ArrayList<String>();
        args1.add("30/08/2015");
        args1.add("short1");
        args1.add("long1");
        args1.add("250");
        StatisticRecord record1 = new StatisticRecord(args1);

        ArrayList<String> args2 = new ArrayList<String>();
        args2.add("01/01/2000");
        args2.add("short2");
        args2.add("long2");
        args2.add("136");
        StatisticRecord record2 = new StatisticRecord(args2);

        //Lista di statistiche
        LinkedList<StatisticRecord> list = new LinkedList<StatisticRecord>();
        list.add(record1);
        list.add(record2);

        int totalShortening = list.size();
        int totalClicks = 0;
        for(StatisticRecord record : list){
            totalClicks += Integer.parseInt(record.getClick());
        }

        result.put("totalShorteners", totalShortening);
        result.put("totalClick" , totalClicks);
        result.put("records", list);

        return result;
    }

    /*
    Used in SINGUP phase, and check the username availability
     */
    public boolean checkUsernameAvailability(String username){
        return true;
    }

    /*
    Register a new user on out platform. Welcome on board!
     */
    public void saveNewUser(String username, String password){

    }

    /*
    Si cerca nel db se esiste uno short url associato a quello specificato

    Se non esiste, ritornare direttamente la variabile come stringa vuota SENZA SOLLEVARE ECCEZIONI.
    Se non esiste lo shortUrl corrispondente lo capisco dal fatto che la stringa ritornata è vuota, quindi lo gestisco direttamente nel server,
    questo metodo deve ritornare o una stringa piena (se esiste lo shortUrl) o una stringa vuota, e basta. Vi amo.
     */
    public String findLongUrl(String shortUrl){
        String longUrl = ""; //variabile da ritornare
        longUrl = "http://google.it"; //fare query al db, se esiste la corrispondenza assegnarla. Ho messo google come prova
        return longUrl; //ritornare la variabile
    }

    /*
    Il metodo viene chiamato dal server una volta che da uno shortUrl ci si è ricondotti al longUrl.
    Ogni click ha associato una liste di chiavi-valori, dove la chiave è una nazione e il valore indica il numero di click provenienti da tale nazione.
    Esempio:

    Link1
     {Francia: 5
      Italia: 5
      Giappone: 6
      Cina: 1
      }

      Bene, questo metodo aggiunge una nazione alla lista associata alla shortUrl (che sarà chiave primaria).
      Se la nazione è già presente incrementa il valore, se non esiste la aggiunge. Io vi do la nazione, voi la inserite.
      Con affetto.
     */
    public void addClick(String shortUrl, String country, String data){

    }

    /*
    Questo metodo restituisce una lista di luoghi geografici da cui è stato effettuato un click. Un elemento della lista è rappresentato da
    tre strutture dati derivate dallo stesso morfismo, con lo stesso comportamento che rappresentano però 3 entità differenti.
    I luoghi da cui viene effettuato un click vengono classificati in continenti, nazioni e città.

    L'elemento di ritorno è una lista.
    Ogni elemento della lista è una tripla (nomeContinente - numeroClick - Nazioni).
    Nazioni è una lista di nazioni. Ogni nazione è una tripla (nomeNazione - numeroClick - Città)
    Città a sua volta è una lista di città. Ogni città è una coppia (nomeCittà - numeroClick)

    In questo modo abbiamo un'unica struttura dati in cui abbiamo tutto innestato, e con una sola query ci portiamo tutto su.
     */
    public LinkedList<Object> getUrlStat(String shortUrl){
        LinkedList<Object> continents = new LinkedList<Object>();

        //START EUROPA
        LinkedList<Object> europenCountryList = new LinkedList<Object>();
        europenCountryList.add(new CountryRecord("Italy", 13));
        europenCountryList.add(new CountryRecord("France", 7));
        continents.add(new ContinentRecord("Europe", 20, europenCountryList));
        //END EUROPA

        //START ASIA
        LinkedList<Object> asianCountryList = new LinkedList<Object>();
        asianCountryList.add(new CountryRecord("Japan", 8));
        continents.add(new ContinentRecord("Asia", 8, asianCountryList));
        //END ASIA

        return continents;
    }

    /*
    Get all stats data from an username's shorteners
     */
    public LinkedList<LinkedList<String>> getData(String username){
        LinkedList<LinkedList<String>> resultList = new LinkedList<LinkedList<String>>();

        //oggetti per il testing del client
        LinkedList<String> url1 = new LinkedList<String>();
        url1.add("data1");
        url1.add("short1");
        url1.add("long1");
        url1.add("click1");
        url1.add("country1");

        LinkedList<String> url2 = new LinkedList<String>();
        url2.add("data2");
        url2.add("short2");
        url2.add("long2");
        url2.add("click2");
        url2.add("country2");

        LinkedList<String> url3 = new LinkedList<String>();
        url3.add("data3");
        url3.add("short3");
        url3.add("long3");
        url3.add("click3");
        url3.add("country3");

        resultList.add(url1);
        resultList.add(url2);
        resultList.add(url3);

        return resultList;
    }

}
