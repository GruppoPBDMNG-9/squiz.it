package dao;

import utility.StatisticRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class CassandraDAO extends DAO {

    public boolean availableUrl(String url){
        /*
        Controlla nella supercolonna degli shortening se la customizzazione scelta � disponibile
         */
        return true;

    }

    public void saveUrl(String longUrl, String shortUrl, String username){
        /*
        Se l'username � nullo significa che lo shortening � stato effettuato da un utente anonimo, altrimenti va salvato lo shortening
        associato all'username che lo ha effettuato.
         */

        //Una stampa di prova, si pu� cancellare
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
        //Due shortening di prova
        ArrayList<String> args1 = new ArrayList<String>();
        args1.add("30/08/2015");
        args1.add("short1");
        args1.add("long1");
        args1.add("250");
        args1.add("Germany");
        StatisticRecord record1 = new StatisticRecord(args1);

        ArrayList<String> args2 = new ArrayList<String>();
        args2.add("30/08/2015");
        args2.add("short2");
        args2.add("long2");
        args2.add("136");
        args2.add("England");
        StatisticRecord record2 = new StatisticRecord(args1);

        //Lista di statistiche
        LinkedList<StatisticRecord> list = new LinkedList<StatisticRecord>();
        list.add(record1);
        list.add(record2);

        //Map di esempio
        HashMap<String, Object> result  = new HashMap<String, Object>();
        result.put("totalShorteners", "10");
        result.put("totalClick" , "250");
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
