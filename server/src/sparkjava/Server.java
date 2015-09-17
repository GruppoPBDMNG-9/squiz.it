package sparkjava;

import dao.RedisDAO;
import org.json.JSONArray;
import org.json.JSONObject;
import shortening.Shortener;
import spark.Request;
import spark.Response;
import utility.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import static spark.Spark.get;
import static spark.Spark.options;

public class Server {
    static String lastUrl;

    public static void main(String[] args) {

        //Random short path generator
        get("/generateShortening", (request, response) -> {
            JSONObject json = new JSONObject();

            String shortUrl = new Shortener().randomGenerate();
            json.put(Args.SHORT_URL, shortUrl);

            //Set lastUrl. We use it in /saveUrl for checking operation
            lastUrl = shortUrl;

            setResponseHeader(request, response);
            return json;
        });

        //Save shortened url
        get("/saveUrl", (request, response) -> {
            JSONObject json = new JSONObject();

            //Get args
            String username = request.queryParams(Args.USERNAME);
            String longUrl = request.queryParams(Args.LONG_URL);
            String url = request.queryParams(Args.URL);

            //Check if user has customized his url. If not, we can save it. Otherwise, we have to check the availability of the choosen url.
            String[] splittedUrl = url.split("/");
            String choosenUrl = splittedUrl[splittedUrl.length - 1];

            if(choosenUrl.equals(lastUrl)){
                new RedisDAO().saveUrl(longUrl, url, username);

                json.put(Args.RESULT, Args.OKAY);
                json.put(Args.URL_SAVED, Args.URL_SAVED_MSG);
                json.put(Args.URL, url);
            } else {
                //Url auto-generated is available again
                new Shortener().undo();

                RedisDAO dao = new RedisDAO();
                if(dao.availableUrl(url)) {
                    dao.saveUrl(longUrl, url, username);
                    json.put(Args.RESULT, Args.OKAY);
                    json.put(Args.URL_SAVED, Args.URL_SAVED_MSG);
                    json.put(Args.URL, url);
                } else {
                    json.put(Args.RESULT, Args.ERROR);
                    json.put(Args.URL_SAVED, Args.URL_ALREADY_TAKEN);
                }
            }

            setResponseHeader(request, response);
            return json;
        });

        /*
        Singup
         */
        get("/singup", (request, response) -> {
            JSONObject json = new JSONObject();

            String username = request.queryParams(Args.USERNAME);
            String password = request.queryParams(Args.PASSWORD);
            FormatStringChecker fscUsername = new FormatStringChecker(username);
            FormatStringChecker fscPassword = new FormatStringChecker(password);

            if(fscUsername.check() && fscPassword.check()){
                RedisDAO dao = new RedisDAO();
                if(dao.checkUsernameAvailability(username)){
                    dao.saveNewUser(username, password);
                    json.put(Args.SINGUP_RESULT_MSG, Args.SINGUP_OK);
                } else {
                    json.put(Args.SINGUP_RESULT_MSG, Args.SINGUP_ERROR);
                }
            } else {
                json.put(Args.SINGUP_RESULT_MSG, Args.FORMAT_ERROR);
            }

            setResponseHeader(request, response);
            return json;
        });

        /*
        LOGIN
         */
        get("/login", (request, response) -> {
            JSONObject json = new JSONObject();

            try {
                String result = "";
                String username = request.queryParams(Args.USERNAME);
                String password = request.queryParams((Args.PASSWORD));

                //Check username and password. Only letters and numbers are allowed
                FormatStringChecker fsc;
                fsc = new FormatStringChecker(username);
                boolean validUsername = fsc.check();
                fsc = new FormatStringChecker(password);
                boolean validPassword = fsc.check();

                if(!validUsername || !validPassword){
                    result = Args.ERROR;
                } else {
                    RedisDAO dao = new RedisDAO();
                    if(dao.login(username, password)){
                        result = Args.OKAY;
                        json.put(Args.USERNAME, username);
                    } else {
                        result = Args.ERROR;
                    }
                }

                json.put(Args.RESULT, result);
            } catch (Exception e) {
                e.printStackTrace();
            }

            setResponseHeader(request, response);
            System.out.println(json.toString());
            return json;
        });

        /*
        Load statistics once authenticated
         */
        get("/loadShortening", (request, response) -> {
            JSONObject json = new JSONObject();
            String username = request.queryParams(Args.USERNAME);
            HashMap<String, Object> map = new RedisDAO().loadUserStat(username);
            LinkedList<StatisticRecord> recordList = (LinkedList<StatisticRecord>) map.get(Args.STAT_RECORDS);

            JSONObject records = new JSONObject();
            int i = 0;
            for(StatisticRecord record : recordList){
                JSONObject jsonRecord = new JSONObject();
                jsonRecord.put(Args.STAT_DATA, record.getData());
                jsonRecord.put(Args.STAT_LONG_URL, record.getLongUrl());
                jsonRecord.put(Args.STAT_SHORT_URL, record.getShortUrl());
                jsonRecord.put(Args.STAT_CLICK, record.getClick());

                records.put((""+(i++)) , jsonRecord);
            }
            json.put(Args.STAT_TOTAL_SHORTENERS, map.get(Args.STAT_TOTAL_SHORTENERS));
            json.put(Args.STAT_TOTAL_CLICK, map.get(Args.STAT_TOTAL_CLICK));
            json.put(Args.STAT_RECORDS, map.get(Args.STAT_RECORDS));

            setResponseHeader(request, response);
            return json;
        });

          /*
        Getter per i click di uno shortUrl suddivisi per continente
         */
        get("/showContinentClick", (request, response) -> {
            JSONObject json = new JSONObject();
            String shortUrl = request.queryParams(Args.SHORT_URL);

            JSONArray continentList = new JSONArray();
            try {
                HashMap<String, Integer> continents = new RedisDAO().getContinentClick(shortUrl);
                for(Entry<String, Integer> continent : continents.entrySet()){
                    JSONObject nextContinent = new JSONObject();
                    nextContinent.put(Args.NAME, continent.getKey());
                    nextContinent.put(Args.CLICK, continent.getValue());
                    continentList.put(nextContinent);
                }
            } catch (RuntimeException e) {
                System.out.println("exception in showUrlStat server");
            }

            setResponseHeader(request, response);
            return continentList;
        });

          /*
        Getter per i click di uno shortUrl suddivisi per nazioni di uno specifico continente
         */
        get("/showCountryClick", (request, response) -> {
            JSONObject json = new JSONObject();
            String shortUrl = request.queryParams(Args.SHORT_URL);
            String continent = request.queryParams(Args.CONTINENT);

            JSONArray countryList = new JSONArray();
            try {
                HashMap<String, Integer> countries = new RedisDAO().getCountryClick(shortUrl, continent);
                for(Entry<String, Integer> country : countries.entrySet()){
                    JSONObject nextCountry = new JSONObject();
                    nextCountry.put(Args.NAME, country.getKey());
                    nextCountry.put(Args.CLICK, country.getValue());
                    countryList.put(nextCountry);
                }
            } catch (RuntimeException e) {
                System.out.println("exception in showUrlStat server");
            }

            json.put(Args.COUNTRIES_LIST, countryList);
            setResponseHeader(request, response);
            return json;
        });

        /*
        Estrae le statistiche dell'ultimo periodo (da decidere quanto lungo)
         */
        get("/getLastStat", (request, response) -> {
            JSONArray json = new JSONArray();
            String username = request.queryParams(Args.USERNAME);

            try {
                LinkedList<Pair> list = new RedisDAO().getLastStat(username);
                for(Pair pair : list){
                    JSONObject nextMonth = new JSONObject();
                    nextMonth.put(Args.NAME, pair.getMonth());
                    nextMonth.put(Args.CLICK, pair.getClick());
                    json.put(nextMonth);
                }

            }  catch (RuntimeException e){
                e.printStackTrace();
            }

            setResponseHeader(request, response);
            return json;
        });

        //Click!
        get("/*", (request, response) -> {
            RedisDAO dao = new RedisDAO();
            String shortUrl = (request.pathInfo()).substring(1);
            String longUrl = dao.findLongUrl(shortUrl);

            if (longUrl.isEmpty()) {
                return Args.INVALID_SHORT_URL;
            } else {
                response.redirect(longUrl);
                try {
                    IPFinder ipFinder = new IPFinder();
                    String continent = ipFinder.getContinent();
                    String country = ipFinder.getCountry();
                    String data = new CalendarUtility().getCurrentData();
                    dao.addClick(shortUrl, continent, country, data);
                } catch (RuntimeException e) {
                    System.out.println("error = disallowed click update");
                }

            }

            setResponseHeader(request, response);
            return "";
        });

        //Some settings
        options("/*", (request, response) -> {
            setOptionRequestResponseHeader(request, response);
            return null;
        });

    }

    /*
          We need these two private methods because of Cross-domain resource sharing.
          More info: https://en.wikipedia.org/wiki/Cross-origin_resource_sharing

          WARNING: Every method will call setResponseHeader(). options() calls also setOptionRequestResponseHeader()
          */
    private static void setResponseHeader(Request req,Response res){
        String origin=req.headers("Origin");
        res.header("access-control-allow-origin", origin);
        res.header("content-type", "text/plain");
    }


    private static void setOptionRequestResponseHeader(Request req,Response res){
        String origin=req.headers("Origin");
        res.header("access-control-allow-origin", origin);
        res.header("access-control-allow-methods", "GET, OPTIONS");
        res.header("access-control-allow-headers", "content-type, accept");
        res.header("access-control-max-age", 10 + "");
        res.header("content-length", 0 + "");
    }

}