package dao;

import utility.StatisticRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import sparkjava.Args;

public class CassandraDAO extends DAO {

    public boolean availableUrl(String url){
        /*
        Controlla nella supercolonna degli shortening se la customizzazione scelta è disponibile
         */
        return true;

    }

    public void saveUrl(String longUrl, String shortUrl, String username){
        final String UNDEFINED_USER = "---"; //se l'url va messo fra quelli anonimi arriva questo username
        if(username.equals(UNDEFINED_USER)){
            //Inserisci l'url fra quelli anonimi
        } else {
            //Inserisci l'url associato all'utente
        }


        //Una stampa di prova per vedere se i dati giungono correttamente fin qui
        System.out.println("longUrl = [" + longUrl + "], shortUrl = [" + shortUrl + "], username = [" + username + "]");
    }

    public boolean login(String username, String password){
        /*
        Ritorna TRUE se l'username esiste && se la password coincide con l'username.
        Ritorna FALSE altrmenti
         */
        return true;
    }

    public HashMap<String, Object> loadStatistics(String username){
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

        //Due shortening di prova
        ArrayList<String> args1 = new ArrayList<String>();
        args1.add("30/08/2015");
        args1.add("short1");
        args1.add("long1");
        args1.add("250");
        args1.add("Germany");
        StatisticRecord record1 = new StatisticRecord(args1);

        ArrayList<String> args2 = new ArrayList<String>();
        args2.add("01/01/2000");
        args2.add("short2");
        args2.add("long2");
        args2.add("136");
        args2.add("England");
        StatisticRecord record2 = new StatisticRecord(args2);

        //Lista di statistiche
        LinkedList<StatisticRecord> list = new LinkedList<StatisticRecord>();
        list.add(record1);
        list.add(record2);


        //Map di esempio
        HashMap<String, Object> result  = new HashMap<String, Object>();

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
    View short url.

    Si cerca nel db se esiste uno short url associato a quello specificato.
    Se esiste le cose da fare sono queste:
    - incrementare le visite +1
    - geolocalizzare l'ip che ha effettuato la visita
    - convertire lo shortUrl nel suo longUrl e associare il risultato alla variabile longUrl

    Se non esiste, ritornare direttamente la variabile come stringa vuota SENZA SOLLEVARE ECCEZIONI.
    Se non esiste lo shortUrl corrispondente lo capisco dal fatto che la stringa ritornata è vuota, quindi lo gestisco direttamente nel server,
    questo metodo deve ritornare o una stringa piena (se esiste lo shortUrl) o una stringa vuota, e basta. Vi amo.
     */
    public String viweShortUrl(String shortUrl){
        String longUrl = ""; //variabile da ritornare
        longUrl = "www.google.it"; //fare query al db, se esiste la corrispondenza assegnarla
        return longUrl; //ritornare la variabile
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
