package sparkjava;

import static spark.Spark.*;

import dao.CassandraDAO;
import org.json.JSONArray;
import org.json.JSONObject;
import shortening.Shortener;
import spark.Request;
import spark.Response;
import utility.FormatStringChecker;
import utility.StatisticRecord;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

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
                new CassandraDAO().saveUrl(longUrl, url, username);

                json.put(Args.RESULT, Args.OKAY);
                json.put(Args.URL_SAVED, Args.URL_SAVED_MSG);
                json.put(Args.URL, url);
            } else {
                //Url auto-generated is available again
                new Shortener().undo();

                CassandraDAO dao = new CassandraDAO();
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
                CassandraDAO dao = new CassandraDAO();
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
                    result = Args.SINGIN_ERROR;
                } else {
                    CassandraDAO dao = new CassandraDAO();
                    if(dao.login(username, password)){
                        result = Args.OKAY;
                    } else {
                        result = Args.LOGIN_ERROR;
                    }
                }

                json.put(Args.RESULT, result);
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("login = [" + request.pathInfo() + "]");

            setResponseHeader(request, response);
            return json;
        });

        /*
        Load statistics once authenticated
         */
        get("/loadShortening", (request, response) -> {
            JSONObject json = new JSONObject();
            String username = request.queryParams(Args.USERNAME);
            HashMap<String, Object> map = new CassandraDAO().loadStatistics(username);
            LinkedList<StatisticRecord> recordList = (LinkedList<StatisticRecord>) map.get(Args.STAT_RECORDS);

            JSONObject records = new JSONObject();
            int i = 0;
            for(StatisticRecord record : recordList){
                JSONObject jsonRecord = new JSONObject();
                jsonRecord.put(Args.STAT_DATA, record.getData());
                jsonRecord.put(Args.STAT_LONG_URL, record.getLongUrl());
                jsonRecord.put(Args.STAT_SHORT_URL, record.getShortUrl());
                jsonRecord.put(Args.STAT_CLICK, record.getClick());
                jsonRecord.put(Args.STAT_POPULAR_COUNTRY, record.getPopularCountry());

                records.put((""+(i++)) , jsonRecord);
            }
            json.put(Args.STAT_TOTAL_SHORTENERS, map.get(Args.STAT_TOTAL_SHORTENERS));
            json.put(Args.STAT_TOTAL_CLICK, map.get(Args.STAT_TOTAL_CLICK));
            json.put(Args.STAT_RECORDS, map.get(Args.STAT_RECORDS));

            setResponseHeader(request, response);
            return json;
        });

        get("/*", (request, response) -> {
            CassandraDAO dao = new CassandraDAO();

            String shortUrl = (request.pathInfo()).substring(1);
            String longUrl = dao.viweShortUrl(shortUrl);

            if(longUrl.isEmpty()){
                return "Invalid short Url";
            } else {
                response.redirect(longUrl);
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