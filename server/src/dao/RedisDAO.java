package dao;

import redis.clients.jedis.Jedis;
import utility.CalendarUtility;
import utility.StatisticRecord;

<<<<<<< HEAD
import redis.clients.jedis.Jedis;

=======
>>>>>>> 8e3a763b71d7501897e9da251f6c749e08db1df7
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

public class RedisDAO extends DAO {
    private static final String host = "127.0.0.1";
    private static final int port = 6379;
    private static Jedis jedis;
    public static final RedisDAO redis = new RedisDAO();


    public RedisDAO() {
        jedis = this.openConnection(host, port);
    }

    public static void main(String[] args) {
        ArrayList<HashMap<String, HashMap<String, Object>>> lprova = new ArrayList<HashMap<String, HashMap<String, Object>>>();
        HashMap<String, HashMap<String, Object>> map1 = new HashMap<String, HashMap<String, Object>>();
        HashMap<String, Object> map2 = new HashMap<String, Object>();
        HashMap<String, Object> click = new HashMap<String, Object>();
        HashMap<String, Object> inclick1 = new HashMap<String, Object>();
        HashMap<String, Object> inclick2 = new HashMap<String, Object>();


        inclick1.put("Inclick11", "Inclick11");
        inclick1.put("Inclick12", "Inclick12");
        inclick2.put("Inclick21", "Inclick21");
        inclick2.put("Inclick22", "Inclick22");
        click.put("Click1", inclick1);
        click.put("Click2", inclick2);

        System.out.println(click.toString());

        HashMap<String, Object> users = new HashMap<String, Object>();
        users.put("Nome", "Corrado");
        users.put("Cognome", "Giancaspro");
        users.put("Clicks", click);

        HashMap<String, String> fusers = new HashMap<String, String>();
        for (Object o : users.entrySet()) {
            Entry<String, Object> coppia = (Entry<String, Object>) o;
            fusers.put(coppia.getKey(), coppia.getValue().toString());
        }

        jedis.hmset("Utente 1", fusers);


    }

    public boolean availableUrl(String url) {
        /*
        Controlla nella supercolonna degli shortening se la customizzazione scelta è disponibile
         */
        return true;

    }

    /*
    Salva un nuovo url shortening. Se è associato ad un username lo associa ad esso, altrmenti finisce fra gli anonimi.
    Le cose da salvare sono quindi:
    - short url
    - long url
    - data in cui è stato effettuato lo shortening
    - username (eventualmente null)
     */
    public void saveUrl(String longUrl, String shortUrl, String username) {
        final String UNDEFINED_USER = "---"; //se l'url va messo fra quelli anonimi arriva questo username
        final String data = new CalendarUtility().getCurrentData();

        if (username.equals(UNDEFINED_USER)) {
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
    public boolean login(String username, String password) {
        return true;
    }

    public HashMap<String, Object> loadUserStat(String username) {
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
        HashMap<String, Object> result = new HashMap<String, Object>();

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
        for (StatisticRecord record : list) {
            totalClicks += Integer.parseInt(record.getClick());
        }

        result.put("totalShorteners", totalShortening);
        result.put("totalClick", totalClicks);
        result.put("records", list);

        return result;
    }

    /*
    Used in SINGUP phase, and check the username availability
     */
    public boolean checkUsernameAvailability(String username) {
        return true;
    }

    /*
    Register a new user on out platform. Welcome on board!
     */
    public void saveNewUser(String username, String password) {

    }

    /*
    Si cerca nel db se esiste uno short url associato a quello specificato

    Se non esiste, ritornare direttamente la variabile come stringa vuota SENZA SOLLEVARE ECCEZIONI.
    Se non esiste lo shortUrl corrispondente lo capisco dal fatto che la stringa ritornata è vuota, quindi lo gestisco direttamente nel server,
    questo metodo deve ritornare o una stringa piena (se esiste lo shortUrl) o una stringa vuota, e basta. Vi amo.
     */
    public String findLongUrl(String shortUrl) {
        String longUrl = ""; //variabile da ritornare
        longUrl = "http://google.it"; //fare query al db, se esiste la corrispondenza assegnarla. Ho messo google come prova
        return longUrl; //ritornare la variabile
    }

   /*
   Prendere i parametri e metterli nelle statistiche
    */
    public void addClick(String shortUrl, String continent, String country, String data){

    }

    /*
    Richiede tutti i click suddivisi per continenti associati ad uno shortUrl
     */
<<<<<<< HEAD
    public void addClick(String shortUrl, String country, String data) {
=======
    public HashMap<String, Integer> getContinentClick(String shortUrl){
        HashMap<String, Integer> continentResult = new HashMap<String, Integer>();
>>>>>>> 8e3a763b71d7501897e9da251f6c749e08db1df7

        //inizializzare e ritornare questo hashmap. per ora metto un hashmap di prova
        continentResult.put("Europe",20);
        continentResult.put("Asia", 10);

        return continentResult;
    }

    /*
    Richiede tutti i click suddivisi per nazione di uno specifico continente associati ad uno specifico short url
     */
    public HashMap<String, Integer> getCountryClick(String shortUrl, String continent){
        HashMap<String, Integer> countriesResult = new HashMap<String, Integer>();

        //stessa cosa dei continenti, questa è giusto una prova
        if(continent.equalsIgnoreCase("Europe")){
            countriesResult.put("Italy",13);
            countriesResult.put("Germany", 7);
        } else {
            countriesResult.put("Japan", 10);
        }

<<<<<<< HEAD
    In questo modo abbiamo un'unica struttura dati in cui abbiamo tutto innestato, e con una sola query ci portiamo tutto su.
     */
    public LinkedList<Object> getUrlStat(String shortUrl) {
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
=======
        return countriesResult;
>>>>>>> 8e3a763b71d7501897e9da251f6c749e08db1df7
    }

    /*
    Get all stats data from an username's shorteners
     */
    public LinkedList<LinkedList<String>> getData(String username) {
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
