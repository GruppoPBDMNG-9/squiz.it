package dao;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import utility.CalendarUtility;
import utility.Pair;
import utility.StatisticRecord;
import utility.StatisticsIndex;

import java.util.*;


public class RedisDAO  {

    private JedisPool jedisPool = new JedisPool(new JedisPoolConfig(),"localhost");

    private static final RedisDAO instance = null;

    private RedisDAO(){}

    public static RedisDAO getInstance(){
        if(instance == null)
            return new RedisDAO();
        else
            return instance;
    }


    public static void main(String[] args){
        RedisDAO d= getInstance();
        d.deleteDB();
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
    Il nome della chiave verrà completato
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
    il nome verrà completato con il concatenameto
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
    il nome verrà completato con il concatenameto
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
    prenderà il nome dello short URL a cui esso
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
    prenderà il nome dello short URL a cui esso
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
    prenderà il nome dello short URL a cui esso
    si riferisce
    */
    private final String recordUser="username";

    /*
        tale stringa memorizza il nome
        del campo che specifica
        se lo specifico short URL è stato auto generato
        dal sistema oppure è stato scelto manualmente
        dall'utente che lo ha creato.
        tale campo fa parte di una struttura dati record
        ogni record fa riferimento ad uno specifico
        short URL e nel database
        la chiave che identifica ogni record
        prenderà il nome dello short URL a cui esso
        si riferisce
     */
    private final String recordAutoGen="autogenarated";

    /*
    tale stringa memorizza il nome
    della chiave che indentifica la lista
    che memorizza nel database
    tutti gli shors-URL creati
    da uno specifico utente
    Il nome della chiave verrà completato
    con il concatenamento ad essa
    dello specifico utente
     */
    private final String userShorts="shorts-";

    /**
     Controlla se la customizzazione scelta è disponibile
     il metodo restituisce true se l'url short è disponibile
     false altrimenti
      */
    public boolean availableUrl(String url){
        boolean result;
        Jedis redis = jedisPool.getResource();

        /*
        con la seguente query recupero dal database la lista contenente
        tutti gli short URL creati
         */
        try {
            List<String> shortURL = redis.lrange(shortsURL, 0, redis.llen(shortsURL));
            if (shortURL.contains(url)) {
                result = false; //l'url è presente nella lista di quelli utilizzati quindi result=false
            } else {
                result = true; //l'url non è presente nella lista di quelli utilizzati quindi result=true
            }
        } finally {
            jedisPool.returnResource(redis);
        }


        return result;
    }

    /*
    Salva un nuovo url shortening. Se è associato ad un username lo associa ad esso, altrmenti finisce fra gli anonimi.
    Le cose da salvare sono quindi:
    - short url
    - long url
    - data in cui è stato effettuato lo shortening
    - username (eventualmente null)
     */
    public void saveUrl(String longUrl, String shortUrl, String username,String data, boolean autoGen){
        final String UNDEFINED_USER = "---"; //se l'url va messo fra quelli anonimi arriva questo username


        /*
        tale variabile memorizza il nome della lista
        in cui vengono salvati tutti gli short URL
        creati dall'utente con tale username
        alla prima parte uguale per tutte le liste
        viene concatenato lo specifico username
         */
        final String structureList= userShorts+username;


        Jedis redis = jedisPool.getResource();
        try{
            /*
            per prima cosa mi memorizzo l'url short nella lista degli url creati
            */
            redis.lpush(shortsURL, shortUrl);

            /*
            adesso memorizzo le informazioni associate all'URL short creato
            nel record ad esso appositamente associato
            */
            redis.hset(shortUrl, recordLong, longUrl);
            redis.hset(shortUrl, recordData, data);
            redis.hset(shortUrl, recordAutoGen, Boolean.toString(autoGen));

            if (username.equals(UNDEFINED_USER)) {
            /*
            lo short url è stato creato da un utente anonimo
             */
                redis.hset(shortUrl, recordUser, "UNDEFINED_USER");
            } else {
                redis.hset(shortUrl, recordUser, username);
            /*
             a questo punto per ultimare l'operazione
             aggiungo l'url short alla lista degli url
             creati dall'utente
         */
                redis.lpush(structureList, shortUrl);
            }
        } finally {
            jedisPool.returnResource(redis);
        }

    }

    /*
      Ritorna TRUE se l'username esiste && se la password coincide con l'username.
      Ritorna FALSE altrmenti
       */
    public boolean login(String username, String password){

        Jedis redis = jedisPool.getResource();
        HashMap<String,String> users;
        boolean result;
        /*
        con la seguente query recupero dal database
        l'hashmap degli utenti registrati
        per andare a verificare su essa
        se l'username esiste e la pasword
        coincide con quella associata a tale username
         */


        try{
            users= (HashMap<String,String>) redis.hgetAll(USERS);

            if (users.containsKey(username)) {
                String savedPassword=users.get(username);
                if (password.equals(savedPassword)) result= true;
                else result= false;
            } else {
                result= false;
            }
        } finally {
            jedisPool.returnResource(redis);
        }
        return result;
    }

    /*
       Questo metodo resistuisce la statistiche dell'utente avente l'username passato come argomento.
       Più nel dettaglio ritorna 3 oggetti:
       - shorteners totali
       - click totali, quindi sommando tutti click per ogni shortening salvato
       - Una lista di record. Ogni oggetto record contiene le informazioni su un singolo shortening effettuato
       */
    public HashMap<String, Object> loadUserStat(String username){


        //Map da restituire
        HashMap<String, Object> result  = new HashMap<String, Object>();

        /*
        lista conententi i record
        ogni record conterrà le statistiche di uno shortsURL
        la lista verrà aggointa all'hashMap da restituire
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
        Jedis redis = jedisPool.getResource();
        try{
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
        } finally {
            jedisPool.returnResource(redis);
        }

        return result;
    }

    /*
    verifica se un username è disponibile
    restituisce false se l'username è stato già utilizzato
    true altrimenti
     */
    public boolean checkUsernameAvailability(String username){
        HashMap<String,String> users;
        boolean result;
        Jedis redis = jedisPool.getResource();

        try {
            /*
            con la seguente query recupero dal database
            l'hashmap degli utenti registrati
            per andare a verificare su essa
            se l'username è stato già usato
            */
            users = (HashMap<String, String>) redis.hgetAll(USERS);

            if (users.containsKey(username)) {
                result= false;
            } else {
                result= true;
            }
        } finally {
            jedisPool.returnResource(redis);
        }
        return result;
    }

    /*
    Registra un nuovo utente alla piattaforma
      */
    public void saveNewUser(String username, String password){
        HashMap<String,String> users;



        Jedis redis = jedisPool.getResource();
        try{
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
        } finally {
            jedisPool.returnResource(redis);
        }

    }

    /*
    Restituisce il long URL associato allo
    short URL passato come parametro
    se lo short URL non è stato mai creato
    il metodo restituisce la stringa vuota
     */
    public String findLongUrl(String shortUrl){

        String longUrl;
        Jedis redis = jedisPool.getResource();
        try{
            /*
            con la seguente query recuper dal database
            esattamente il valore del campo longUrl
            associato al record avente come chiave
            proprio lo short url da ricercare
            */
            longUrl = redis.hget(shortUrl,recordLong);


            if (longUrl==null) longUrl="";


        } finally {
            jedisPool.returnResource(redis);
        }

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



        Jedis redis = jedisPool.getResource();
        try{
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
            verifico se il continente da cui proviene il clik è già presente nell'hashMap
             */
            if (mContinent.containsKey(continent)) {
                /*
                il continente è già presente
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
        } finally {
            jedisPool.returnResource(redis);
        }


    }

    /*
     Restituisce un hashMap<String,Integer> che associa
     il numero di click ricevuti dallo shortURL specifico
     divisi per continenti
      */
    public HashMap<String, Integer> getContinentClick(String shortUrl){
        HashMap<String, Integer> continentResult = new HashMap<String, Integer>();
        String mapContinent=hashMContinent+shortUrl;

        Jedis redis = jedisPool.getResource();
        try{
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
        } finally {
            jedisPool.returnResource(redis);
        }


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


        Jedis redis = jedisPool.getResource();
        try{
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
        } finally {
            jedisPool.returnResource(redis);
        }


        return countriesResult;
    }

    /*
    Restituisce il numero di click totali ottenuti dall'utente
    per i suoi shorts
    suddivisi per mesi nell'arco dell'ultimo anno (13 mesi compreso quello corrente)
     */
    public LinkedList<Pair> getLastStat(String username,String dataC){


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

        Jedis redis = jedisPool.getResource();
        try{
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
        } finally {
            jedisPool.returnResource(redis);
        }



        return result;
    }

    /*
    tale metodo è eseguito all'avvio del server
    serve per popolare il database con un utente
    e dei link.
     */
    public void populateDB(){

        /*
        verifico se l'username è disponibile
        se è disponibile vuol dire che nel
        database non sono presenti i dati di
        default quindi si procede
        ad inseririre tali dati
         */
        if (checkUsernameAvailability("fumarola")){

            //aggiungo l'utente
            saveNewUser("fumarola","pbdmng");

            //salvo dei shortURL
            String short1="http://localhost:4567/short1";
            String short2="http://localhost:4567/short2";

            saveUrl("http://pbdmng.datatoknowledge.it/",short1,"fumarola","23/09/2015",false);
            saveUrl("http://www.di.uniba.it/~malerba/",short2,"fumarola","23/09/2015",false);

            //salvo dei click
            addClick(short1,"EUROPA","Italia","24/09/2015");
            addClick(short1,"EUROPA","Italia","24/09/2015");
            addClick(short1,"EUROPA","Spagna","24/09/2015");

            addClick(short2,"EUROPA","Italia","24/09/2015");
        }
    }


    /*
    tale metodo serve per eliminare tutti i dati
    dal database
    esso viene utilizzato al momento solo nella classe test
    per andare a "pulire" il db dopo aver effettuato i test
     */
    public void deleteDB() {
        Jedis redis = jedisPool.getResource();

        try {

            /*
            con la seguente query
            recupero l'insieme delle chiavi
            memorizzate nel database
             */
            Set<String> keys = redis.keys("*");

            /*
            adesso itero sulla struttura dati
            e per ciascuna chiave eseguo il comando
            redis per elimanrla dal db
             */
            for (String key: keys){
                redis.del(key);
            }
        } finally {
            jedisPool.returnResource(redis);
        }
    }

    /*
    tale metodo restituisce l'ultimo
    short URL che è stato auto generato
    dal sistema.
    Se restituisce null vuol dire che
    il sistema non ha ancora auto generato
    alcun short URL
     */
    public String getLastShortURLAutogenerated(){
        String result=null;

        Jedis redis = jedisPool.getResource();

        try {


            /*
            con questa query mi faccio restituire la
            lista di tutti gli URL short creati e
            memorizzati nel database
             */
            List <String> listShortsURL= redis.lrange(shortsURL,0,redis.llen(shortsURL));

            /*
            ora ciclo su tutte gli shortURL appena ottenuti
            per verificare qual'è il primo autogenerato
             */
            int i=0;
            boolean urlFound=false;
            while (i<listShortsURL.size() &&  urlFound==false) {

                 /*
                        con tale query mi faccio restituire il valore
                        del campo autoGen del record che fa riferimento
                        allo specifico shortURL
                */
                String autoGen=redis.hget(listShortsURL.get(i),recordAutoGen);
                if (autoGen.equals("true")){
                    result=listShortsURL.get(i);
                    urlFound=true;
                }
                i++;
            }

/*
            for (String url: listShortsURL){



                String autoGen=redis.hget(url,recordAutoGen);

                if (autoGen.equals("true")){
                    result=url;
                    break;
                }

            }
            */

        } finally {
            jedisPool.returnResource(redis);
        }


        return result;
    }
}
