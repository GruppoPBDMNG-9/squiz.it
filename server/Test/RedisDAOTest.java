import dao.RedisDAO;
import junit.framework.TestCase;
import utility.Pair;
import utility.StatisticRecord;
import utility.StatisticsIndex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Created by Donato on 19/09/2015.
 */
public class RedisDAOTest extends TestCase {

    private RedisDAO rd;
    /*
    array di utenti che vengono memorizzati nel database per effettuare i test
     */
    private String[] users= {"donato91","peppo91","corrado91","davide23","marco65","zuby"};

    /*
    array di utenti che NON vengono inseriti nel database
    tale array serve per testare il metodo checkUsernameAvailability
     */
    private String [] notUsers= {"carlo12","donato","crrado91","dav23","antitifosotto","caparezza","liga78"
             ,"gigi2","tifosotto","pantera78","da23vide","zuby4ever","pepGuard","anacona12",
              "martial47","Pogba",""};

    /*
    array di password associate agli utenti memorizzati nel database per effettare i test
     */
    private String [] passwords = {"0000","9587","a85x","w8lke","wqr45","47opiy"};

    /*
    array di long url inseriti nel database
     */
    private String [] longURL ={"www.google.it","www.facebook.it","www.diretta.it","www.sportmediaset.it","www.youtube.com",
    "web.whatsapp.com","www.bibbia.net","www.rtl.it/home/","www.mtv.it"};

    /*
    array di short url inseriti nel database,
    il corrispondente longURL è nella
    stessa posizione nell'array dei long

     */
    private String[] shortURL= {"squiz1","squiz2","squiz3","squiz4","squiz5","squiz6","squiz7","squiz8","squiz9"};

    /*
    array di date di creazione di shortURL,
    il corrispondente short lo si trova
    nella stessa posizione nell'array degli short
     */
    private String[] dataCreation={"15/02/2015","31/03/2015","01/04/2015","08/042015","09/05/2015","23/06/2015",
                     "24/06/2015","01/07/2015","10/08/2015"};
    /*
    array di short url NON inseriti nel database
    servono per testare il corretto funzionamento
    del metodo availableUrl
     */
    private String []notShortURL={"squiz10","squiz11","squiz12","squiz13","squiz14","squiz15","squz1",
                    "suizz2","nonEsisto","quiz1","squi25","squi5"};

    /*
    linkedList di hashMap che serve per
    memorizzare i risultati attesi
    per quanto riguarda i click
    suddivisi per continente
     */
    private LinkedList<HashMap<String,Integer>> listClicksContinent = new LinkedList<HashMap<String, Integer>>();

    /*
    tale struttura serve per memorizzare
    i risultatti attesi per quanto riguarda
    il metodo loadUserStat su ciascun utente
     */
    private LinkedList<HashMap<String,Object>> listGlobalUsersStat = new LinkedList<HashMap<String, Object>>();

    /*
    tale struttura serve per memorizzare i risultati
    attesi per quanto riguarda i click suddivisi
    per nazioni appartenenti ad un continente specifico
     */
    private LinkedList<LinkedList<HashMap<String,Integer>>> listClicksCountry = new LinkedList<LinkedList<HashMap<String,Integer>>> ();

    /*
      tale struttura serve per memorizzare i risultati
      attesi per quanto riguarda il numero di click
      ricevuti dagli utenti suddivisi per mesi nell'arco di un
      anno
  */
    private LinkedList<LinkedList<Pair>> listDataStat = new LinkedList<LinkedList<Pair>>();


    private String[] continents = {"EUROPA","AMERICA","ASIA","AFRICA","OCEANIA"};



    /*
    metodo privato che serve
    per popolare il database
    con degli utenti e rispettive
    password per effettuare i test
     */
    private void addUsers() {
        for (int i=0; i<users.length; i++) {
            rd.saveNewUser(users[i],passwords[i]);
        }
    }

    /*
    metodo privato che serve per popolare il database
    con degli shortsURL con i rispettivi long
    e gli utenti che li hanno creati
     */
    private void addShortsURL(){
        /*
        aggiungo al db gli shorts creati
        dall'user donato91
         */
        for (int i=0; i<3; i++){
            rd.saveUrl(longURL[i],shortURL[i],"donato91",dataCreation[i]);
        }

        /*
        aggiungo al db gli shorts creati
        dall'user peppo91
         */
        for (int i=3; i<5; i++){
            rd.saveUrl(longURL[i],shortURL[i],"peppo91",dataCreation[i]);
        }

        //short aggiunto dall'user corrado91
        rd.saveUrl(longURL[5],shortURL[5],"corrado91",dataCreation[5]);

        //short aggiunto dall'user dall'user davide23
        rd.saveUrl(longURL[6],shortURL[6],"davide23",dataCreation[6]);

        //short aggiunti dall'user zuby
        for (int i=7; i<9; i++){
            rd.saveUrl(longURL[i], shortURL[i], "zuby", dataCreation[i]);
        }
    }


    /*
    metodo privato che serve per popolare
    il dabase on i click
     */
    private void addClick(){

        //aggiungo i click ricevuti dallo short link "squiz2"
        /*
        rd.addClick(shortURL[0],"EUROPA","ITALIA","17/02/2015");
        rd.addClick(shortURL[0],"EUROPA","ITALIA","25/02/2015");
        rd.addClick(shortURL[0],"EUROPA","ITALIA","26/02/2015");
        rd.addClick(shortURL[0],"EUROPA","ITALIA","17/03/2015");
        rd.addClick(shortURL[0],"EUROPA","ITALIA","19/03/2015");
        rd.addClick(shortURL[0],"EUROPA","GRECIA","25/03/2015");
        rd.addClick(shortURL[0],"EUROPA","SPAGNA","26/03/2015");
        rd.addClick(shortURL[0],"EUROPA","SPAGNA","28/03/2015");
        rd.addClick(shortURL[0],"EUROPA","GERMANIA","29/03/2015");
        rd.addClick(shortURL[0],"EUROPA","FRANCIA","29/03/2015");
        rd.addClick(shortURL[0],"EUROPA","GERMANIA","30/03/2015");
        rd.addClick(shortURL[0],"ASIA","GIAPPONE","03/04/2015");
        rd.addClick(shortURL[0],"ASIA","GIAPPONE","05/04/2015");
        rd.addClick(shortURL[0],"AMERICA","USA","08/04/2015");
        rd.addClick(shortURL[0],"AMERICA","USA","26/04/2015");
        rd.addClick(shortURL[0],"AMERICA","BRASILE","26/04/2015");
        rd.addClick(shortURL[0],"AMERICA","USA","28/04/2015");
        rd.addClick(shortURL[0],"AMERICA","ARGENTINA","17/05/2015");
        rd.addClick(shortURL[0],"AMERICA","USA","19/05/2015");
        rd.addClick(shortURL[0],"AMERICA","BRASILE","21/05/2015");
        rd.addClick(shortURL[0],"AFRICA","EGITTO","21/05/2015");
        rd.addClick(shortURL[0],"AFRICA","TUNISIA","27/05/2015");
        rd.addClick(shortURL[0],"AFRICA","MAROCCO","28/05/2015");
        rd.addClick(shortURL[0],"ASIA","CINA","30/05/2015");
        rd.addClick(shortURL[0],"ASIA","SINGAPORE","30/05/2015");

        //aggiungo i clicl ricevuti dallo short link "squiz2"
        rd.addClick(shortURL[1],"EUROPA","BELGIO","01/06/2015");
        rd.addClick(shortURL[1],"EUROPA","INGHILTERRA","12/06/2015");
        rd.addClick(shortURL[1],"EUROPA","INGHILTERRA","25/06/2015");
        rd.addClick(shortURL[1],"EUROPA","OLANDA","01/07/2015");
        rd.addClick(shortURL[1],"EUROPA","BELGIO","25/07/2015");
        rd.addClick(shortURL[1],"AMERICA","CANADA","01/08/2015");
        rd.addClick(shortURL[1],"ASIA","SINGAPORE","14/08/2015");
        rd.addClick(shortURL[1],"OCEANIA","AUSTRALIA","09/09/2015");
        rd.addClick(shortURL[1],"OCEANIA","AUSTRALIA","15/09/2015");
        rd.addClick(shortURL[1],"OCEANIA","AUSTRALIA","01/10/2015");
        rd.addClick(shortURL[1],"OCEANIA","NUOVA ZELANDA","08/10/2015");

        //aggiungo i click ricevuti dallo short lin "squiz4"
        rd.addClick(shortURL[3],"EUROPA","IRLANDA","10/04/2015");
        rd.addClick(shortURL[3],"EUROPA","IRLANDA","10/05/2015");
        rd.addClick(shortURL[3],"EUROPA","IRLANDA","21/05/2015");
        rd.addClick(shortURL[3],"EUROPA","ITALIA","25/05/2015");
        rd.addClick(shortURL[3],"EUROPA","ITALIA","26/05/2015");
        rd.addClick(shortURL[3],"EUROPA","SPAGNA","13/07/2015");
        rd.addClick(shortURL[3],"EUROPA","GRECIA","21/07/2015");
        rd.addClick(shortURL[3],"EUROPA","SPAGNA","12/08/2015");
        rd.addClick(shortURL[3],"EUROPA","FRANCIA","24/08/2015");
        rd.addClick(shortURL[3],"AMERICA","MESSICO","01/09/2015");
        rd.addClick(shortURL[3],"AMERICA","MESSICO","10/09/2015");
        rd.addClick(shortURL[3],"AMERICA","ARGENTINA","12/09/2015");
        rd.addClick(shortURL[3],"ASIA","GIAPPONE","14/09/2015");
        rd.addClick(shortURL[3],"ASIA","GIAPPONE","16/09/2015");
        rd.addClick(shortURL[3],"ASIA","GIAPPONE","01/10/2015");
        rd.addClick(shortURL[3],"AFRICA","EGITTO","01/11/2015");
        rd.addClick(shortURL[3],"AFRICA","SUD AFRICA","01/12/2015");
        rd.addClick(shortURL[3],"AFRICA","SUD AFRICA","08/12/2015");

        //aggiungo i click ricevuti dallo short link "squiz6"
        rd.addClick(shortURL[5],"EUROPA","ITALIA","30/06/2015");
        rd.addClick(shortURL[5],"EUROPA","ITALIA","08/10/2015");

        //aggiungo i click ricevuti dallo short link "squiz8"
        rd.addClick(shortURL[7],"AMERICA","USA","05/07/2015");
        rd.addClick(shortURL[7],"AMERICA","USA","10/10/2015");
        rd.addClick(shortURL[7],"AMERICA","CANADA","11/10/2015");
        rd.addClick(shortURL[7],"AMERICA","BRASILE","05/10/2015");
        rd.addClick(shortURL[7],"AMERICA","USA","05/11/2015");
        rd.addClick(shortURL[7],"AMERICA","USA","08/11/2015");
        rd.addClick(shortURL[7],"ASIA","GIAPPONE","01/12/2015");
        rd.addClick(shortURL[7],"AFRICA","EGITTO","01/12/2015");
        rd.addClick(shortURL[7],"AFRICA","MAROCCO","15/12/2015");
        rd.addClick(shortURL[7],"AFRICA","TUNISIA","22/12/2015");


        //aggiungo i click ricevuti dallo short link "squiz8"
        rd.addClick(shortURL[8],"EUROPA","ITALIA","02/05/2015");
        rd.addClick(shortURL[8],"EUROPA","ITALIA","05/05/2015");
        rd.addClick(shortURL[8],"EUROPA","ITALIA","07/05/2015");
        rd.addClick(shortURL[8],"EUROPA","ITALIA","02/07/2015");
        rd.addClick(shortURL[8],"EUROPA","ITALIA","14/07/2015");
        rd.addClick(shortURL[8],"EUROPA","ITALIA","02/08/2015");
        rd.addClick(shortURL[8],"EUROPA","ITALIA","02/10/2015");
        rd.addClick(shortURL[8],"EUROPA","ITALIA","02/12/2015");
*/
    }


    /*
    metodo privato che serve
    per memorizzare i risultati attesi
    per il testo del metodo getContinentClick
     */
    private void loadListContinentClick(){
        HashMap<String,Integer> hmap= new HashMap<String,Integer>();
        hmap.put("EUROPA",11);
        hmap.put("ASIA",4);
        hmap.put("AMERICA",7);
        hmap.put("AFRICA", 3);
        listClicksContinent.add(hmap);


        hmap= new HashMap<String,Integer>();
        hmap.put("EUROPA", 5);
        hmap.put("AMERICA", 1);
        hmap.put("ASIA",1);
        hmap.put("OCEANIA", 4);
        listClicksContinent.add(hmap);



        hmap= new HashMap<String,Integer>();
        listClicksContinent.add(hmap);


        hmap= new HashMap<String,Integer>();
        hmap.put("EUROPA", 9);
        hmap.put("AMERICA", 3);
        hmap.put("AFRICA", 3);
        hmap.put("ASIA", 3);
        listClicksContinent.add(hmap);


        hmap= new HashMap<String,Integer>();
        listClicksContinent.add(hmap);


        hmap= new HashMap<String,Integer>();
        hmap.put("EUROPA", 2);
        listClicksContinent.add(hmap);


        hmap= new HashMap<String,Integer>();
        listClicksContinent.add(hmap);


        hmap= new HashMap<String,Integer>();
        hmap.put("AMERICA",6);
        hmap.put("ASIA", 1);
        hmap.put("AFRICA",3);
        listClicksContinent.add(hmap);


        hmap= new HashMap<String,Integer>();
        hmap.put("EUROPA", 8);
        listClicksContinent.add(hmap);
    }

    private void loadListClicksCountry(){
        LinkedList<HashMap<String,Integer>> listClick = new  LinkedList<HashMap<String,Integer>>();
        HashMap<String,Integer> hmap= new HashMap<String,Integer>();
        hmap.put("ITALIA",5);
        hmap.put("GRECIA", 1);
        hmap.put("SPAGNA",2);
        hmap.put("GERMANIA", 2);
        hmap.put("FRANCIA", 1);
        listClick.add(hmap);
        hmap= new HashMap<String,Integer>();
        hmap.put("USA", 4);
        hmap.put("BRASILE", 2);
        hmap.put("ARGENTINA", 1);
        listClick.add(hmap);
        hmap= new HashMap<String,Integer>();
        hmap.put("GIAPPONE", 2);
        hmap.put("CINA", 1);
        hmap.put("SINGAPORE", 1);
        listClick.add(hmap);
        hmap= new HashMap<String,Integer>();
        hmap.put("EGITTO", 1);
        hmap.put("MAROCCO", 1);
        hmap.put("TUNISIA", 1);
        listClick.add(hmap);
        hmap= new HashMap<String,Integer>();
        listClick.add(hmap);
        listClicksCountry.add(listClick); //in posizione uno viene memorizzata la linkedList di hashMap riguardante l'utente in posizione 2


        listClick = new  LinkedList<HashMap<String,Integer>>();
        hmap= new HashMap<String,Integer>();
        hmap.put("BELGIO", 2);
        hmap.put("INGHILTERRA", 2);
        hmap.put("OLANDA", 1);
        listClick.add(hmap);
        hmap= new HashMap<String,Integer>();
        hmap.put("CANADA", 1);
        listClick.add(hmap);
        hmap= new HashMap<String,Integer>();
        hmap.put("SINGAPORE", 1);
        listClick.add(hmap);
        hmap= new HashMap<String,Integer>();
        listClick.add(hmap);
        hmap= new HashMap<String,Integer>();
        hmap.put("AUSTRALIA", 3);
        hmap.put("NUOVA ZELANDA", 1);
        listClick.add(hmap);
        listClicksCountry.add(listClick); //in posizione 2 viene memorizzata la linkedList di hashMap riguardante l'utente in posizione due


        listClick = new  LinkedList<HashMap<String,Integer>>();
        for (int i=0; i<5; i++){
            hmap= new HashMap<String,Integer>();
            listClick.add(hmap);
        }
        listClicksCountry.add(listClick); //in posizione 3 viene memorizzata la linkedList di hashMap riguardante l'utente in posizione 3


        listClick = new  LinkedList<HashMap<String,Integer>>();
        hmap= new HashMap<String,Integer>();
        hmap.put("IRLANDA", 3);
        hmap.put("ITALIA", 2);
        hmap.put("SPAGNA", 2);
        hmap.put("FRANCIA", 1);
        hmap.put("GRECIA", 1);
        listClick.add(hmap);
        hmap= new HashMap<String,Integer>();
        hmap.put("MESSICO", 2);
        hmap.put("ARGENTINA", 1);
        listClick.add(hmap);
        hmap= new HashMap<String,Integer>();
        hmap.put("GIAPPONE", 3);
        listClick.add(hmap);
        hmap= new HashMap<String,Integer>();
        hmap.put("EGITTO", 1);
        hmap.put("SUD AFRICA", 2);
        listClick.add(hmap);
        hmap= new HashMap<String,Integer>();
        listClick.add(hmap);
        listClicksCountry.add(listClick); //in posizione 4 viene memorizzata la linkedList di hashMap riguardante l'utente in posizione 4


        listClick = new  LinkedList<HashMap<String,Integer>>();
        for (int i=0; i<5; i++){
            hmap= new HashMap<String,Integer>();
            listClick.add(hmap);
        }
        listClicksCountry.add(listClick); //in posizione 5 viene memorizzata la linkedList di hashMap riguardante l'utente in posizione 5

        listClick = new  LinkedList<HashMap<String,Integer>>();
        hmap= new HashMap<String,Integer>();
        hmap.put("ITALIA", 2);
        listClick.add(hmap);
        for (int i=0; i<4; i++){
            hmap= new HashMap<String,Integer>();
            listClick.add(hmap);
        }
        listClicksCountry.add(listClick); //in posizione 6 viene memorizzata la linkedList di hashMap riguardante l'utente in posizione 6


        listClick = new  LinkedList<HashMap<String,Integer>>();
        for (int i=0; i<5; i++){
            hmap= new HashMap<String,Integer>();
            listClick.add(hmap);
        }
        listClicksCountry.add(listClick); //in posizione 7 viene memorizzata la linkedList di hashMap riguardante l'utente in posizione 7


        listClick = new  LinkedList<HashMap<String,Integer>>();
        hmap= new HashMap<String,Integer>();
        listClick.add(hmap);
        hmap= new HashMap<String,Integer>();
        hmap.put("USA", 4);
        hmap.put("CANADA",1);
        hmap.put("BRASILE",1);
        listClick.add(hmap);
        hmap= new HashMap<String,Integer>();
        hmap.put("GIAPPONE", 1);
        listClick.add(hmap);
        hmap= new HashMap<String,Integer>();
        hmap.put("EGITTO", 1);
        hmap.put("MAROCCO", 1);
        hmap.put("TUNISIA", 1);
        listClick.add(hmap);
        hmap= new HashMap<String,Integer>();
        listClick.add(hmap);
        listClicksCountry.add(listClick); //in posizione 8 viene memorizzata la linkedList di hashMap riguardante l'utente in posizione 8


        listClick = new  LinkedList<HashMap<String,Integer>>();
        hmap= new HashMap<String,Integer>();
        hmap.put("ITALIA", 8);
        listClick.add(hmap);
        for (int i=0; i<4; i++){
            hmap= new HashMap<String,Integer>();
            listClick.add(hmap);
        }
        listClicksCountry.add(listClick); //in posizione 9 viene memorizzata la linkedList di hashMap riguardante l'utente in posizione 9

    }

    private void loadDataClickStat(){
        LinkedList<Pair> listClicks= new LinkedList<Pair>();
        Pair p = new Pair("Gennaio-2015",0);
        listClicks.add(p);
        p = new Pair("Febbraio-2015",3);
        listClicks.add(p);
        p = new Pair("Marzo-2015",8);
        listClicks.add(p);
        p = new Pair("Aprile-2015",6);
        listClicks.add(p);
        p = new Pair("Maggio-2015",8);
        listClicks.add(p);
        p = new Pair("Giugno-2015",3);
        listClicks.add(p);
        p = new Pair("Luglio-2015",2);
        listClicks.add(p);
        p = new Pair("Agosto-2015",2);
        listClicks.add(p);
        p = new Pair("Settembre-2015",2);
        listClicks.add(p);
        p = new Pair("Ottobre-2015",2);
        listClicks.add(p);
        p = new Pair("Novembre-2015",0);
        listClicks.add(p);
        p = new Pair("Dicembre-2015",0);
        listClicks.add(p);
        p = new Pair("Gennaio-2016",0);
        listClicks.add(p);
        listDataStat.add(listClicks);


        listClicks= new LinkedList<Pair>();
        p = new Pair("Gennaio-2015",0);
        listClicks.add(p);
        p = new Pair("Febbraio-2015",0);
        listClicks.add(p);
        p = new Pair("Marzo-2015",0);
        listClicks.add(p);
        p = new Pair("Aprile-2015",1);
        listClicks.add(p);
        p = new Pair("Maggio-2015",4);
        listClicks.add(p);
        p = new Pair("Giugno-2015",0);
        listClicks.add(p);
        p = new Pair("Luglio-2015",2);
        listClicks.add(p);
        p = new Pair("Agosto-2015",2);
        listClicks.add(p);
        p = new Pair("Settembre-2015",5);
        listClicks.add(p);
        p = new Pair("Ottobre-2015",1);
        listClicks.add(p);
        p = new Pair("Novembre-2015",1);
        listClicks.add(p);
        p = new Pair("Dicembre-2015",2);
        listClicks.add(p);
        p = new Pair("Gennaio-2016",0);
        listClicks.add(p);
        listDataStat.add(listClicks);


        listClicks= new LinkedList<Pair>();
        p = new Pair("Gennaio-2015",0);
        listClicks.add(p);
        p = new Pair("Febbraio-2015",0);
        listClicks.add(p);
        p = new Pair("Marzo-2015",0);
        listClicks.add(p);
        p = new Pair("Aprile-2015",0);
        listClicks.add(p);
        p = new Pair("Maggio-2015",0);
        listClicks.add(p);
        p = new Pair("Giugno-2015",1);
        listClicks.add(p);
        p = new Pair("Luglio-2015",0);
        listClicks.add(p);
        p = new Pair("Agosto-2015",0);
        listClicks.add(p);
        p = new Pair("Settembre-2015",0);
        listClicks.add(p);
        p = new Pair("Ottobre-2015",1);
        listClicks.add(p);
        p = new Pair("Novembre-2015",0);
        listClicks.add(p);
        p = new Pair("Dicembre-2015",0);
        listClicks.add(p);
        p = new Pair("Gennaio-2016",0);
        listClicks.add(p);
        listDataStat.add(listClicks);

        for (int i=0; i<2; i++){
            listClicks= new LinkedList<Pair>();
            p = new Pair("Gennaio-2015",0);
            listClicks.add(p);
            p = new Pair("Febbraio-2015",0);
            listClicks.add(p);
            p = new Pair("Marzo-2015",0);
            listClicks.add(p);
            p = new Pair("Aprile-2015",0);
            listClicks.add(p);
            p = new Pair("Maggio-2015",0);
            listClicks.add(p);
            p = new Pair("Giugno-2015",0);
            listClicks.add(p);
            p = new Pair("Luglio-2015",0);
            listClicks.add(p);
            p = new Pair("Agosto-2015",0);
            listClicks.add(p);
            p = new Pair("Settembre-2015",0);
            listClicks.add(p);
            p = new Pair("Ottobre-2015",0);
            listClicks.add(p);
            p = new Pair("Novembre-2015",0);
            listClicks.add(p);
            p = new Pair("Dicembre-2015",0);
            listClicks.add(p);
            p = new Pair("Gennaio-2016",0);
            listClicks.add(p);
            listDataStat.add(listClicks);
        }


        listClicks= new LinkedList<Pair>();
        p = new Pair("Gennaio-2015",0);
        listClicks.add(p);
        p = new Pair("Febbraio-2015",0);
        listClicks.add(p);
        p = new Pair("Marzo-2015",0);
        listClicks.add(p);
        p = new Pair("Aprile-2015",0);
        listClicks.add(p);
        p = new Pair("Maggio-2015",3);
        listClicks.add(p);
        p = new Pair("Giugno-2015",0);
        listClicks.add(p);
        p = new Pair("Luglio-2015",3);
        listClicks.add(p);
        p = new Pair("Agosto-2015",1);
        listClicks.add(p);
        p = new Pair("Settembre-2015",0);
        listClicks.add(p);
        p = new Pair("Ottobre-2015",4);
        listClicks.add(p);
        p = new Pair("Novembre-2015",2);
        listClicks.add(p);
        p = new Pair("Dicembre-2015",5);
        listClicks.add(p);
        p = new Pair("Gennaio-2016",0);
        listClicks.add(p);
        listDataStat.add(listClicks);
    }


    private void loadListGlobalUsersStat(){
        HashMap<String,Object> stat = new HashMap<String,Object>();
        stat.put("totalShorteners",3);
        stat.put("totalClick", 36);
        LinkedList<StatisticRecord> listRecord= new LinkedList<StatisticRecord>();
        //statistiche per short Url "squiz3"
        ArrayList<String> statistics= new ArrayList<String>();
        statistics.add(StatisticsIndex.DATA,dataCreation[2]);
        statistics.add(StatisticsIndex.SHORT_URL,shortURL[2]);
        statistics.add(StatisticsIndex.LONG_URL, longURL[2]);
        statistics.add(StatisticsIndex.CLICK, "0");
        StatisticRecord record = new StatisticRecord(statistics);
        listRecord.add(record);
        //statistiche per short url "squiz2"
        statistics= new ArrayList<String>();
        statistics.add(StatisticsIndex.DATA,dataCreation[1]);
        statistics.add(StatisticsIndex.SHORT_URL,shortURL[1]);
        statistics.add(StatisticsIndex.LONG_URL, longURL[1]);
        statistics.add(StatisticsIndex.CLICK, "11");
        record = new StatisticRecord(statistics);
        listRecord.add(record);
        //statistiche per short url "squiz1"
        statistics= new ArrayList<String>();
        statistics.add(StatisticsIndex.DATA,dataCreation[0]);
        statistics.add(StatisticsIndex.SHORT_URL,shortURL[0]);
        statistics.add(StatisticsIndex.LONG_URL, longURL[0]);
        statistics.add(StatisticsIndex.CLICK, "25");
        record = new StatisticRecord(statistics);
        listRecord.add(record);
        stat.put("records", listRecord);
        listGlobalUsersStat.add(stat); // aggiungo in posizione uno le statistiche dell'utente 1


        stat = new HashMap<String,Object>();
        stat.put("totalShorteners", 2);
        stat.put("totalClick", 18);
        listRecord= new LinkedList<StatisticRecord>();
        //statistiche per short Url "squiz5"
        statistics= new ArrayList<String>();
        statistics.add(StatisticsIndex.DATA, dataCreation[4]);
        statistics.add(StatisticsIndex.SHORT_URL,shortURL[4]);
        statistics.add(StatisticsIndex.LONG_URL, longURL[4]);
        statistics.add(StatisticsIndex.CLICK, "0");
        record = new StatisticRecord(statistics);
        listRecord.add(record);
        //statistiche per short Url "squiz4"
        statistics= new ArrayList<String>();
        statistics.add(StatisticsIndex.DATA, dataCreation[3]);
        statistics.add(StatisticsIndex.SHORT_URL,shortURL[3]);
        statistics.add(StatisticsIndex.LONG_URL, longURL[3]);
        statistics.add(StatisticsIndex.CLICK, "18");
        record = new StatisticRecord(statistics);
        listRecord.add(record);
        stat.put("records", listRecord);
        listGlobalUsersStat.add(stat); // aggiungo in posizione 2 le statistiche dell'utente 2


        stat = new HashMap<String,Object>();
        stat.put("totalShorteners", 1);
        stat.put("totalClick", 2);
        listRecord= new LinkedList<StatisticRecord>();
        //statistiche per short Url "squiz6"
        statistics= new ArrayList<String>();
        statistics.add(StatisticsIndex.DATA, dataCreation[5]);
        statistics.add(StatisticsIndex.SHORT_URL,shortURL[5]);
        statistics.add(StatisticsIndex.LONG_URL, longURL[5]);
        statistics.add(StatisticsIndex.CLICK, "2");
        record = new StatisticRecord(statistics);
        listRecord.add(record);
        stat.put("records", listRecord);
        listGlobalUsersStat.add(stat); // aggiungo in posizione 3 le statistiche dell'utente 3

        stat = new HashMap<String,Object>();
        stat.put("totalShorteners", 1);
        stat.put("totalClick", 0);
        listRecord= new LinkedList<StatisticRecord>();
        //statistiche per short Url "squiz7"
        statistics= new ArrayList<String>();
        statistics.add(StatisticsIndex.DATA, dataCreation[6]);
        statistics.add(StatisticsIndex.SHORT_URL,shortURL[6]);
        statistics.add(StatisticsIndex.LONG_URL, longURL[6]);
        statistics.add(StatisticsIndex.CLICK, "0");
        record = new StatisticRecord(statistics);
        listRecord.add(record);
        stat.put("records", listRecord);
        listGlobalUsersStat.add(stat); // aggiungo in posizione 4 le statistiche dell'utente 4


        stat = new HashMap<String,Object>();
        stat.put("totalShorteners", 0);
        stat.put("totalClick", 0);
        listRecord= new LinkedList<StatisticRecord>();
        stat.put("records", listRecord);
        listGlobalUsersStat.add(stat); // aggiungo in posizione 5 le statistiche dell'utente 5



        stat = new HashMap<String,Object>();
        stat.put("totalShorteners", 2);
        stat.put("totalClick", 18);
        listRecord= new LinkedList<StatisticRecord>();
        //statistiche per short Url "squiz9"
        statistics= new ArrayList<String>();
        statistics.add(StatisticsIndex.DATA, dataCreation[8]);
        statistics.add(StatisticsIndex.SHORT_URL,shortURL[8]);
        statistics.add(StatisticsIndex.LONG_URL, longURL[8]);
        statistics.add(StatisticsIndex.CLICK, "8");
        record = new StatisticRecord(statistics);
        listRecord.add(record);
        statistics= new ArrayList<String>();
        // statistiche per short url "squiz8"
        statistics.add(StatisticsIndex.DATA, dataCreation[7]);
        statistics.add(StatisticsIndex.SHORT_URL,shortURL[7]);
        statistics.add(StatisticsIndex.LONG_URL, longURL[7]);
        statistics.add(StatisticsIndex.CLICK, "10");
        record = new StatisticRecord(statistics);
        listRecord.add(record);
        stat.put("records", listRecord);
        listGlobalUsersStat.add(stat); // aggiungo in posizione 6 le statistiche dell'utente 6
    }


    public void setUp() throws Exception {
        super.setUp();
        rd= RedisDAO.getInstance();

    }

    public void tearDown() throws Exception {
        rd=null;
    }


    public void testLogin() throws Exception {
       // addUsers();
        boolean result;
        for (int i=0; i<users.length; i++){
            result= rd.login(users[i],passwords[i]);
            assertTrue(result==true);
        }
    }


    public void testCheckUsernameAvailability()  {

        boolean result;

        for (int i=0; i<users.length; i++){
            result= rd.checkUsernameAvailability(users[i]);
            assertTrue(result==false);
        }

        for (int i=0; i<notUsers.length; i++){
            result= rd.checkUsernameAvailability(notUsers[i]);

              assertTrue(result==true);

        }
    }


    public void testAvailableUrl(){
        //addShortsURL();
        boolean result;
        for (int i=0; i<shortURL.length; i ++) {
            result= rd.availableUrl(shortURL[i]);
            assertTrue(result==false);
        }

        for (int i=0; i<notShortURL.length; i ++) {
            result= rd.availableUrl(notShortURL[i]);
            assertTrue(result==true);
        }
    }


    public void testFindLongUrl(){
        String result;
        for (int i=0; i<shortURL.length; i ++) {
            result= rd.findLongUrl(shortURL[i]);
            assertTrue(result.equals(longURL[i]));
        }

        for (int i=0; i<notShortURL.length; i ++) {
            result= rd.findLongUrl(notShortURL[i]);
            assertTrue(result.equals(""));
        }
    }


    public void testGetContinentClick(){
        addClick(); //memorizzo nel database i clicks
        loadListContinentClick();  //valorizzo la struttura dati che contiene i risultati attesi

        /*
        effettuo un ciclo su ciascun short URL memorizzato nel database
         */
        for (int i=0; i<shortURL.length; i++){

            /*
            recupero l'hashMap contenente le statistiche memorizzate
            nel database dell'url short in esame
             */
            HashMap<String,Integer> hmap=rd.getContinentClick(shortURL[i]);

            /*
            recuper l'hashMap contente le statistiche attese
            del'url short in esame
             */
            HashMap<String,Integer> hmapResult=listClicksContinent.get(i);

            /*
            verifico che tutte le chiavi ottenute
            siano contenute nell'HashMap atteso
            e che i valori associati corrispondano
             */
            for (String s: hmap.keySet()){
                int value=hmap.get(s);
                int result=hmapResult.get(s);
                assertTrue(value==result);
            }

            /*
            questa volta verifico che tutte le chiavi
            attese siano contenute nell'hashMap
            contenute nel database e che i valori
            associati corrispondano
             */
            for (String s: hmapResult.keySet()){
                int value=hmap.get(s);
                int result=hmapResult.get(s);
                assertTrue(value==result);
            }
        }

        /*
        ora verifico che shortURL
        senza click (nel caso specifico non sono
        proprio presenti nel db)
        restituiscano un hashMap di lunghezza = 0
         */
        for (int i=0; i<notShortURL.length; i++){
            HashMap<String,Integer> hmap= rd.getContinentClick(notShortURL[i]);
            assertTrue(hmap.size()==0);
        }

    }


    public void testGetCountryClick(){
        loadListClicksCountry();


        /*
            ciclo su ciascun utente salvato nel database
         */
        for (int i=0; i<shortURL.length; i++){
            /*
            ciclo su ciascun continente
             */
            for (int j=0; j<continents.length; j++){

                 /*
                    recupero l'hashMap contenente le statistiche memorizzate
                    nel database dell'url short in esame in base allo
                    specifico continente
                 */
                HashMap<String,Integer> hmap=rd.getCountryClick(shortURL[i],continents[j]);

                 /*
                     recuper l'hashMap contente le statistiche attese
                     del'url short in esame in base allo specifico continente
                */
                LinkedList<HashMap<String,Integer>> listClicks= listClicksCountry.get(i);
                HashMap<String,Integer> hmapResult=listClicks.get(j);

                /*
                     verifico che tutte le chiavi ottenute
                     siano contenute nell'HashMap atteso
                     e che i valori associati corrispondano
                */
                for (String s: hmap.keySet()){
                    int value=hmap.get(s);
                    int result=hmapResult.get(s);
                    assertTrue(value==result);
                }

                /*
                     questa volta verifico che tutte le chiavi
                     attese siano contenute nell'hashMap
                     contenute nel database e che i valori
                     associati corrispondano
             */
                for (String s: hmapResult.keySet()){
                    int value=hmap.get(s);
                    int result=hmapResult.get(s);
                    assertTrue(value==result);
                }
            }
        }
    }

    public void testGetLastStat(){
        loadDataClickStat();
        final String dataRif="01/01/2016";
        /*
        ciclo su ciascun utente memorizzato nel database
         */
        for (int i=0; i<users.length; i++) {
            /*
            recupero la struttura attesa per lo specifico utente
             */
            LinkedList<Pair> listAttesa=listDataStat.get(i);

            /*
            recupero le statistiche memorizzate nel database
             */
            LinkedList<Pair> listOttenuta=rd.getLastStat(users[i],dataRif);

            assertTrue(listAttesa.size()==listOttenuta.size()); // verifico subito se hanno la stessa lunghezza

            for (int j=0; j<listAttesa.size(); j++){
                Pair pAttesso=listAttesa.get(j);
                Pair pOttenuto= listOttenuta.get(j);
                assertTrue(pAttesso.getMonth().equals(pOttenuto.getMonth()) && pAttesso.getClick()==pOttenuto.getClick());
            }
        }
    }


    public void testLoadUserStat(){
        loadListGlobalUsersStat();

        /*
        ciclo su ciascun utente salvato nel database
         */
        for (int i=0; i<users.length; i++){

            /*
            recuper la struttura dati attesa per tale utente
             */
            HashMap<String, Object> hMapAtteso=listGlobalUsersStat.get(i);


            /*
            recuper la struttura dati che contiene le statitistiche
            per tale utente realmente memorizzati nel database
             */
            HashMap<String, Object> hMapOttenuto=rd.loadUserStat(users[i]);

            assertTrue(hMapAtteso.get("totalShorteners")==hMapOttenuto.get("totalShorteners"));
            assertTrue(hMapAtteso.get("totalClick")==hMapOttenuto.get("totalClick"));

            LinkedList<StatisticRecord> listRecordAttesi= (LinkedList<StatisticRecord>) hMapAtteso.get("records");
            LinkedList<StatisticRecord> listRecordOttenuti= (LinkedList<StatisticRecord>) hMapOttenuto.get("records");

            assertTrue(listRecordAttesi.size()==listRecordOttenuti.size());

            for (int j=0; j<listRecordAttesi.size(); j++){
                StatisticRecord recordAtteso=listRecordAttesi.get(j);
                StatisticRecord recordOttenuto=listRecordOttenuti.get(j);

                assertTrue(recordAtteso.getClick().equals(recordOttenuto.getClick()));
                assertTrue(recordAtteso.getLongUrl().equals(recordOttenuto.getLongUrl()));
                assertTrue(recordAtteso.getData().equals(recordOttenuto.getData()));
                assertTrue(recordAtteso.getShortUrl().equals(recordOttenuto.getShortUrl()));
            }
        }
    }
}