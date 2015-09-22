package dao;


import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import utility.CalendarUtility;
import utility.Pair;
import utility.StatisticRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import redis.clients.jedis.Jedis;
import utility.StatisticsIndex;


public class RedisDAO  {

    private Jedis redis = new JedisPool(new JedisPoolConfig(),"localhost").getResource();

    private static final RedisDAO instance=new RedisDAO();
    private RedisDAO(){

    }
    public static RedisDAO getInstance(){
        return instance;
    }


    /*
    tale stringa memorizza il nome
    della chiave che identifica
    la lista nel database che memorizza
    tutti gli url short creati
     */
    private final String shortsURL="shortsURL";

    /*
    tale stringa memorizza il nome
    della chiave che identifica la
    struttura dati hashmap nel database che memorizza
    tutti gli utenti con le rispettive password
     */
    private final String USERS="users";


    /*
    tale stringa memorizza il nome
    della chiave che identifica
    la lista nel database
    che ha il compito di memorizzare
    le date dei click che riceve un singolo
    short URL.
    Il nome della chiave verr� completato
    con il concatenamento ad essa
    dello short URL
     */
    private final String listDa_Click="listDataClicks-";



    /*
    tale stringa memorizza il nome della chiave
    che identifica la struttura dati HashMap
    nel database che memorizza per uno specifico
    short URL il numero di clicks che esso riceve
    suddivisi per continenti.
    il nome verr� completato con il concatenameto
    dello specifico URL
     */
    private final String hashMContinent="mapContinent-";



    /*
    tale stringa memorizza il nome della chiave
    che identifica la struttura dati HashMap
    nel database che memorizza per uno specifico
    short URL il numero di clicks che esso riceve
    suddivisi per nazioni relative
    ad uno specifico continente.
    il nome verr� completato con il concatenameto
    dello specifico continente e dello specifico URL
     */
    private final String hashMCountry="mapCountry-";


    /*
    tale stringa memorizza il nome
    del campo che memorizza il long URL
    associato ad uno specifico short URL
    tale campo fa parte di una struttura dati record
    ogni record fa riferimento ad uno specifico
    short URL e nel database
    la chiave che identifica ogni record
    prender� il nome dello short URL a cui esso
    si riferisce
     */
    private final String recordLong="longURL";


    /*
    tale stringa memorizza il nome
    del campo che memorizza
    la data di creazione
    di uno specifico short URL
    tale campo fa parte di una struttura dati record
    ogni record fa riferimento ad uno specifico
    short URL e nel database
    la chiave che identifica ogni record
    prender� il nome dello short URL a cui esso
    si riferisce
    */
    private final String recordData="creationData";

    /*
    tale stringa memorizza il nome
    del campo che memorizza l'username
    dell'utente che ha creato
    il specifico short URL
    tale campo fa parte di una struttura dati record
    ogni record fa riferimento ad uno specifico
    short URL e nel database
    la chiave che identifica ogni record
    prender� il nome dello short URL a cui esso
    si riferisce
    */
    private final String recordUser="username";

    /*
    tale stringa memorizza il nome
    della chiave che indentifica la lista
    che memorizza nel database
    tutti gli shors-URL creati
    da uno specifico utente
    Il nome della chiave verr� completato
    con il concatenamento ad essa
    dello specifico utente
     */
    private final String userShorts="shorts-";



    public static void main(String[] args){

        RedisDAO rd= new RedisDAO();

        /*
        rd.saveNewUser("donato91","0000");
        rd.saveNewUser("peppo91","peppoGay");
        rd.saveNewUser("corrado91","marina");


        System.out.println("username donato91 disponibile= " + rd.checkUsernameAvailability("donato91"));
        System.out.println("username matteo disponibile= " + rd.checkUsernameAvailability("matteo"));
        System.out.println("username wanda disponibile= " + rd.checkUsernameAvailability("wanda"));
        System.out.println("username corrado91 disponibile= " + rd.checkUsernameAvailability("corrado91"));
        System.out.println("username peppo91 disponibile= " + rd.checkUsernameAvailability("peppo91"));


        System.out.println("login1= " + rd.login("donato91", "0000"));
        System.out.println("login2= " + rd.login("donato91", "1"));
        System.out.println("login3= "+rd.login("checco", "0000"));
        System.out.println("login4= "+rd.login("peppo91", "0000"));
        System.out.println("login5= "+rd.login("peppo91", "peppoGay"));



        rd.saveUrl("www.sportmediaset.mediaset.it", "short1", "donato91");
        rd.saveUrl("www.google.com", "short2", "donato91");
        rd.saveUrl("www.diretta.it", "short3", "peppo91");


        System.out.println(rd.findLongUrl("short1"));
        System.out.println(rd.findLongUrl("io"));
        System.out.println(rd.findLongUrl("tu"));
        System.out.println(rd.findLongUrl("egli"));
        System.out.println(rd.findLongUrl("noi"));
        System.out.println(rd.findLongUrl("short2"));
        System.out.println(rd.findLongUrl("short3"));



        rd.addClick("short2","europa","italia","15/09/2015");
        rd.addClick("short2","europa","italia","15/09/2015");
        rd.addClick("short2","europa","italia","15/09/2015");
        rd.addClick("short2","asia","giappone","15/09/2015");
        rd.addClick("short2","europa","italia","15/09/2015");
        rd.addClick("short2","europa","francia","15/09/2015");
        rd.addClick("short2","europa","italia","15/09/2015");
        rd.addClick("short2","asia","giappone","15/09/2015");
        rd.addClick("short2","europa","Germania","15/09/2015");
        rd.addClick("short2","europa","Germania","15/09/2015");
        rd.addClick("short2","asia","cina","15/09/2015");
        rd.addClick("short1","asia","cina","15/09/2015");


        HashMap<String,Integer> h= rd.getContinentClick("shorts2");
        for (String s: h.keySet()){
            System.out.println(s + " " + h.get(s));
        }

        HashMap<String,Integer> h2= rd.getCountryClick("shorts2", "europa");
        for (String s: h2.keySet()){
            System.out.println(s + " " + h2.get(s));
        }

        HashMap<String,Integer> h3= rd.getCountryClick("shorts2","asia");
        for (String s: h3.keySet()){
            System.out.println(s + " " + h3.get(s));
        }

        HashMap<String,Integer> h4= rd.getCountryClick("shorts2","america");
        for (String s: h4.keySet()){
            System.out.println(s + " " + h4.get(s));
        }


        HashMap<String,Object> s=rd.loadUserStat("donato91");
        System.out.println("num link: " + s.get("totalShorteners"));
        System.out.println("total click: " + s.get("totalClick"));
        */

        /*
        rd.saveNewUser("donato91", "0");
        rd.saveUrl("www.facebook.com", "short1", "donato91");
        rd.saveUrl("www.diretta.com","short2","donato91");
        rd.saveUrl("www.giovinazzoViva.com","short3","donato91");
        rd.saveUrl("www.google.com", "short4", "donato91");

        rd.addClick("short1","europa","italia","17/09/2015");
        rd.addClick("short1","europa","italia","17/09/2015");
        rd.addClick("short1","europa","italia","17/09/2015");
        rd.addClick("short1","europa","germania","17/09/2015");
        rd.addClick("short2","europa","italia","17/09/2015");
        rd.addClick("short2","europa","italia","17/09/2015");



        rd.addClick("short1","europa","italia","17/02/2014");
        rd.addClick("short1","europa","italia","17/03/2014");
        rd.addClick("short1","europa","italia","17/10/2014");
        rd.addClick("short1","europa","germania","17/12/2014");
        rd.addClick("short2","europa","italia","17/01/2014");
        rd.addClick("short2","europa","italia","17/09/2014");
        */
        LinkedList<Pair> listStat= rd.getLastStat("donato91","10/12/2015");
        for (Pair p: listStat ){
            System.out.println(p.getMonth() + " : " + p.getClick());
        }

    }


    /**
     Controlla se la customizzazione scelta � disponibile
     il metodo restituisce true se l'url short � disponibile
     false altrimenti
      */
    public boolean availableUrl(String url){

        boolean result;


      //  redis.connect();


        /*
        con la seguente query recupero dal database la lista contenente
        tutti gli short URL creati
         */
        List<String> shortURL = redis.lrange(shortsURL,0,redis.llen(shortsURL));
        if (shortURL.contains(url)){
            result=false; //l'url � presente nella lista di quelli utilizzati quindi result=false
        } else {
            result=true; //l'url non � presente nella lista di quelli utilizzati quindi result=true
        }

//        redis.close();

        return result;

    }

    /*
    Salva un nuovo url shortening. Se � associato ad un username lo associa ad esso, altrmenti finisce fra gli anonimi.
    Le cose da salvare sono quindi:
    - short url
    - long url
    - data in cui � stato effettuato lo shortening
    - username (eventualmente null)
     */
    public void saveUrl(String longUrl, String shortUrl, String username,String data){
        final String UNDEFINED_USER = "---"; //se l'url va messo fra quelli anonimi arriva questo username
        //final String data = new CalendarUtility().getCurrentData();

        /*
        tale variabile memorizza il nome della lista
        in cui vengono salvati tutti gli short URL
        creati dall'utente con tale username
        alla prima parte uguale per tutte le liste
        viene concatenato lo specifico username
         */
        final String structureList= userShorts+username;

        //redis.connect();

        /*
        per prima cosa mi memorizzo l'url short nella lista degli url creati
         */
        redis.lpush(shortsURL, shortUrl);

        /*
        adesso memorizzo le informazioni associate all'URL short creato
        nel record ad esso appositamente associato
         */
        redis.hset(shortUrl,recordLong,longUrl);
        redis.hset(shortUrl, recordData, data);

        if(username.equals(UNDEFINED_USER)){
            /*
            lo short url � stato creato da un utente anonimo
             */
            redis.hset(shortUrl,recordUser,"UNDEFINED_USER");
        } else {
            redis.hset(shortUrl,recordUser,username);
            /*
             a questo punto per ultimare l'operazione
             aggiungo l'url short alla lista degli url
             creati dall'utente
         */
            redis.lpush(structureList,shortUrl);
        }



//        redis.close();

        //Una stampa di prova per vedere se i dati giungono correttamente fin qui
       // System.out.println("longUrl = [" + longUrl + "], shortUrl = [" + shortUrl + "], username = [" + username + "]");
    }

    /*
      Ritorna TRUE se l'username esiste && se la password coincide con l'username.
      Ritorna FALSE altrmenti
       */
    public boolean login(String username, String password){

        HashMap<String,String> users;


        //redis.connect();
        /*
        con la seguente query recupero dal database
        l'hashmap degli utenti registrati
        per andare a verificare su essa
        se l'username esiste e la pasword
        coincide con quella associata a tale username
         */
        users= (HashMap<String,String>) redis.hgetAll(USERS);

    //    redis.close();

        if (users.containsKey(username)) {
            String savedPassword=users.get(username);
            if (password.equals(savedPassword)) return true;
            else return false;
        } else return false;


    }

    public HashMap<String, Object> loadUserStat(String username){
        /* PER DONATO E CORRADO
        Questo metodo resistuisce la statistiche dell'utente avente l'username passato come argomento.
        Pi� nel dettaglio ritorna 3 oggetti:
        - shorteners totali
        - click totali, quindi sommando tutti click per ogni shortening salvato
        - Una lista di record. Ogni oggetto record contiene le informazioni su un singolo shortening effettuato.

        La strategia � quella di creare solo la lista di shortening, e di calcolare in questo metodo shorteners totali e click totali,
        in modo da non avere informazioni ridondanti nel database. Create solo la lista di oggetti StatisticRecord al posto delle prove
        che ho fatto io. Total shorteners e click totali li ho gi� calcolati io.
        Seguono due shortening di prova per maggior chiarezza e utilizzati per restare il client

        Vi voglio bene :)
        */

        //Map da restituire
        HashMap<String, Object> result  = new HashMap<String, Object>();

        /*
        lista conententi i record
        ogni record conterr� le statistiche di uno shortsURL
        la lista verr� aggointa all'hashMap da restituire
         */
        LinkedList<StatisticRecord> statisticsList = new LinkedList<StatisticRecord>();

        //memorizza il numero totale di clicks ottenuti dall'utente
        int  totalClicks=0;

        /*
        tale variabile memorizza il nome della lista
        in cui vengono salvati tutti gli short URL
        creati dall'utente con tale username
        il nome viene completato
        concatendando alla prima parte uguale per
        tutte le liste, lo specifico username
         */
        final String structureList= userShorts+username;


       // redis.connect();

        /*
        con questa query recuper dal database la lista
        degli shorts url creati dallo specifico utente
         */
        List<String> shortsList=redis.lrange(structureList,0,redis.llen(structureList));

        /*
        ciclo sulla lista appena ottenuta per recuperare
        da ciascun short url  tutte le sue informazioni
         */
        for (String sURL:shortsList){

            /*
            recupero il long url
            la chiave su cui ricercare il record
            prende il nome dello short url
             */
            String longURL= redis.hget(sURL,recordLong);

            /*
            recupero la data di creazione
            la chiave su cui ricercare il record
            prende il nome dello short url
             */
            String data= redis.hget(sURL,recordData);

            /*
            recupero il numero di click
            totali ottenuti dallo short
            facendomi restituire la lunghezza
            della lista contenente le date
            in cui lo shortURl ha ricevuto click
             */
            String listClick=listDa_Click+sURL;
            String numClick=String.valueOf(redis.llen(listClick)).toString();

            /*
            aggiorno anche il numero di click totali
             */
            totalClicks=totalClicks+Integer.parseInt(numClick);

            /*
            incapsulo i dati in un arrayList
             */
            ArrayList<String> statistics= new ArrayList<String>();
            statistics.add(StatisticsIndex.DATA,data);
            statistics.add(StatisticsIndex.SHORT_URL,sURL);
            statistics.add(StatisticsIndex.LONG_URL,longURL);
            statistics.add(StatisticsIndex.CLICK,numClick);

            /*
            istanzio l'oogetto record di tipo SatisticsRecord
            per memorizzare l'arrayList
             */
            StatisticRecord record = new StatisticRecord(statistics);

            /*
            aggiungo alla lista finale da restituire nell'hashMap
            l'oggetto record appena istanziato
             */
            statisticsList.add(record);

        }


        result.put("totalShorteners", statisticsList.size());
        result.put("totalClick" , totalClicks);
        result.put("records", statisticsList);

        //redis.close();

        return result;
    }

    /*
    Used in SINGUP phase, and check the username availability
     */
    public boolean checkUsernameAvailability(String username){
        HashMap<String,String> users;


        //redis.connect();
        /*
        con la seguente query recupero dal database
        l'hashmap degli utenti registrati
        per andare a verificare su essa
        se l'username � stato gi� usato
         */
        users= (HashMap<String,String>) redis.hgetAll(USERS);

       // redis.close();

        if (users.containsKey(username)) return false;
        else return true;
    }

    /*
    Register a new user on out platform. Welcome on board!
     */
    public void saveNewUser(String username, String password){
        HashMap<String,String> users;


        //redis.connect();

        /*
        con la seguente query recupero dal database
        l'hashmap degli utenti registrati
        per andare ad aggiungervi il nuovo utente
         */

        users= (HashMap<String,String>) redis.hgetAll(USERS);
        users.put(username,password);

        /*
        con la seguente query inserisco nel database
        l'hashMap degli utenti aggiornato
        con il nuovo iscritto
         */
        redis.hmset(USERS,users);

        //redis.close();
    }


    /*
    Restituisce il long URL associato allo
    short URL passato come parametro
    se lo short URL non � stato mai creato
    il metodo restituisce la stringa vuota
     */
    public String findLongUrl(String shortUrl){

      //  redis.connect();

        /*
        con la seguente query recuper dal database
        esattamente il valore del campo longUrl
        associato al record avente come chiave
        proprio lo short url da ricercare
         */
        String longUrl = redis.hget(shortUrl,recordLong);

//        redis.close();

        if (longUrl==null) longUrl="";
        return longUrl;
    }

   /*
   il metodo ha il compito di memorizzare nel database
   tutte le informazione riguardanti il click
    */
    public void addClick(String shortUrl, String continent, String country, String data){
        /*
        tale variabile memorizza il nome della lista
        che contiene tutte le date dei click effettuati
        al short URL specifico
        la si ottiene concatenando alla prima parte
        uguale per tutti, il nome dello specifico
        short URL
         */
        String listClick=listDa_Click+shortUrl;

        /*
        tale variabile ha il compito di memorizzare il nome
        della chiave che identifica l'hashMap
        che memorizza il numero di click
        associati allo specifico shortURL
        suddivisi per continenti.
        la si ottiene concatenando alla prima parte
        uguale per tutti, il nome dello specifico
        short URL
         */
        String mapContinent=hashMContinent+shortUrl;

        /*
        tale variabile ha il compito di memorizzare il nome
        della chiave dell'hashMap
        che memorizza il numero di click
        associati allo specifico shortURL
        suddivisi per nazioni di uno specifico continenti
        la si ottiene concatenando alla prima parte
        uguale per tutti, il nome dello specifico
        continente e specifico short URL
         */
        String mapCountryContinent=hashMCountry+continent+"-"+shortUrl;


       // redis.connect();

        /*
        Aggiungo alla lista delle date dei click
        dello short url la nuova data passata
        come parametro
         */
        redis.lpush(listClick,data);

        /*
        recupero dal database l'hashMap dei
        click per continenti
         */
        HashMap<String,String> mContinent=(HashMap<String,String>)redis.hgetAll(mapContinent);
        /*
        verifico se il continente da cui proviene il clik � gi� presente nell'hashMap
         */
        if (mContinent.containsKey(continent)) {
            /*
            il continente � gi� presente
            quindi devo aggiornare il numero di click
             */
            int numClick=0;
            try{
                 numClick= Integer.parseInt(mContinent.get(continent));
            } catch (Exception e){
                System.out.println("errore di conversione, impossibile aggiornare il valore dei click sul continente "+ continent);
            }
            mContinent.remove(continent);
            numClick++;
            String click=String.valueOf(numClick).toString();
            mContinent.put(continent,click);
        } else {
            /*
            il continente non era ancora presente quindi mi basta
            semplicemente aggiungerlo come nuova chiave all'interno
            dell'hash map
             */
            mContinent.put(continent,"1");
        }
        /*
        a questo punto elimino dal database il vecchio hashMap per inserire quello aggiornato
         */
        redis.del(mapContinent);
        redis.hmset(mapContinent,mContinent);


        /*
        recupero dal database l'hashMap dei click
        suddivisi per nazioni di uno specifico continente
         */
        HashMap<String,String> mCountry= (HashMap<String,String>)redis.hgetAll(mapCountryContinent);

        /*
        utilizzo lo stesso metodo
        utilizzato in precedenza per i continenti
         */
        if (mCountry.containsKey(country)){
            int numClickCountry=0;
            try{
                numClickCountry=Integer.parseInt(mCountry.get(country));
            } catch (Exception e){
                System.out.println("errore di conversione, impossibile aggiornare il numero di click per lo stato "+country);
            }
            numClickCountry++;
            mCountry.remove(country);
            String nCCountry=String.valueOf(numClickCountry).toString();
            mCountry.put(country,nCCountry);
        } else {
            mCountry.put(country,"1");
        }

        redis.del(mapCountryContinent);
        redis.hmset(mapCountryContinent,mCountry);
       // redis.close();
    }

    /*
     Restituisce un hashMap<String,Integer> che associa
     il numero di click ricevuti dallo shortURL specifico
     divisi per continenti
      */
    public HashMap<String, Integer> getContinentClick(String shortUrl){
        HashMap<String, Integer> continentResult = new HashMap<String, Integer>();

        String mapContinent=hashMContinent+shortUrl;


        //redis.connect();

        /*
        con la seguente query recupero dal database
        l'hashMap contentente il numero di click
        ricevuti dallo shortURl suddivisi per continenti
         */
        HashMap<String,String> mCont= (HashMap<String,String>) redis.hgetAll(mapContinent);


        /*
        devo convertire il value dell'hashMap da string a int
         */
        for (String c: mCont.keySet()){
            continentResult.put(c,Integer.parseInt(mCont.get(c)));
        }

       // redis.close();
        return continentResult;
    }

    /*
     Restituisce un hashMap<String,Integer> che associa
     il numero di click ricevuti dallo shortURL specifico
     divisi per nazioni appartenenti al continente specificato
      */
    public HashMap<String, Integer> getCountryClick(String shortUrl, String continent){
        HashMap<String, Integer> countriesResult = new HashMap<String, Integer>();

        String mapCountryContinent=hashMCountry+continent+"-"+shortUrl;


       // redis.connect();

        /*
        con la seguente query recupero dal database
        l'hashMap contentente il numero di click
        ricevuti dallo shortURl suddivisi per nazioni
        appartenenti al continente specificato
         */
        HashMap<String,String> mCauntry = (HashMap<String,String>) redis.hgetAll(mapCountryContinent);


         /*
        devo convertire il value dell'hashMap da string a int
         */
        for (String c: mCauntry.keySet()){
            countriesResult.put(c,Integer.parseInt(mCauntry.get(c)));
        }

      //  redis.close();
        return countriesResult;
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

    /*
    Restituisce il numero di click totali ottenuti dall'utente
    per i suoi shorts
    suddivisi in
     */
    public LinkedList<Pair> getLastStat(String username,String dataC){


        //redis.connect();

        /*
        linkedlist da restituire
         */
        LinkedList<Pair> result = new LinkedList<Pair>();


        /*
        HashMap che associa il mese in formato numero
        al corrispondente in formato testo
         */
        HashMap<Integer,String> calendar= CalendarUtility.getCalendar();

       /*
        Slitto la data in modo da ottenere
        un array di tre elementi
        contenente giorno-mese-anno
         */
        String [] dataArray = dataC.split("/");

        /*
        mi memorizzo in fomrato int mese e anno
         */

        int currentMounth = Integer.parseInt(dataArray[1]);
        int currentYear= Integer.parseInt(dataArray[2]);



        /*
        Valorizzo la lista da restituire
         */
        for (int i=currentMounth; i<=12; i++){
            String year= String.valueOf(currentYear-1).toString();
            Pair pair = new Pair(calendar.get(i)+"-"+year,0);
            result.addLast(pair);
        }

        for (int i=1; i<=currentMounth; i++){
            Pair pair= new Pair(calendar.get(i)+"-"+dataArray[2],0 );
            result.addLast(pair);
        }

        /*
        con la seguente query recupero
        la lista degli short creati
        dall'utente
         */
        String query=userShorts+username;

        List<String> listURL = redis.lrange(query,0,redis.llen(query));

        /*
        a questo punto ciclo sulla lista
        per aggiornare la statistica
        elaborando ciascun short URL
         */

        for (String url:listURL){


            /*
            per il singolo short url
            recupero la sua corrispondente
            lista delle date dei click
             */
            String query2=listDa_Click+url;
            List<String> listD= redis.lrange(query2,0,redis.llen(query2));

            /*
            ciclo sulle date
             */
            for (String d: listD) {

                String dataAnalizeArray[]=d.split("/");
                int monthAnalize= Integer.parseInt(dataAnalizeArray[1]);
                String monthRicerca= calendar.get(monthAnalize)+"-"+dataAnalizeArray[2];


                for (Pair p: result){
                    if (p.getMonth().equals(monthRicerca)){
                        p.setClick(p.getClick()+1);
                        break;
                    }
                }
            }
        }

        /*
        Pair pair;
        pair = new Pair("gennaio", 10);
        result.add(pair);
        pair = new Pair("febbraio", 20);
        result.add(pair);
        pair = new Pair("marzo", 40);
        result.add(pair);
        pair = new Pair("aprile", 100);
        result.add(pair);
        pair = new Pair("maggio", 500);
        result.add(pair);
        pair = new Pair("giugno", 2000);
        result.add(pair);
        pair = new Pair("luglio", 5000);
        result.add(pair);
        pair = new Pair("agosto", 2300);
        result.add(pair);
        pair = new Pair("settembre", 1100);
        result.add(pair);
        pair = new Pair("ottobre", 850);
        result.add(pair);
        pair = new Pair("novembre", 1800);
        result.add(pair);
        pair = new Pair("dicembre", 1000);
        result.add(pair);
        */


       // redis.close();

        return result;
    }

}
