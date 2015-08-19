package sparkjava;

import static spark.Spark.*;

import dao.CassandraDAO;
import org.json.JSONObject;
import shortening.Shortener;
import spark.Request;
import spark.Response;
import utility.FormatStringChecker;

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

            String longUrl = request.queryParams(Args.LONG_URL);
            String url = request.queryParams(Args.URL);

            //Check if user has customized his url. If not, we can save it. Otherwise, we have to check the availability of the choosen url.
            String[] splittedUrl = url.split("/");
            String choosenUrl = splittedUrl[splittedUrl.length - 1];

            if(choosenUrl.equals(lastUrl)){
                new CassandraDAO().saveUrl(longUrl, url);

                json.put(Args.RESULT, Args.OKAY);
                json.put(Args.URL_SAVED, Args.URL_SAVED_MSG);
                json.put(Args.URL, url);
            } else {
                //Url auto-generated is available again
                new Shortener().undo();

                CassandraDAO dao = new CassandraDAO();
                if(dao.availableUrl(url)) {
                    dao.saveUrl(longUrl, url);
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
        Login

        get("/login", (request, response) -> {
            JSONObject responseData = new JSONObject();

            try {
                String email = request.queryParams(Args.EMAIL);
                String password = request.queryParams(Args.PASSWORD);
                System.out.println("email = [" + email + "]");
                System.out.println("password = [" + password + "]");

                CassandraDAO dao = new CassandraDAO();
                if(dao.login(email, password)){
                    //Apri sessione
                } else {
                    //Credenziali errate
                }

            }  catch (Exception e){
                System.out.println("exception = [" + e.getMessage() + "]");
            }


            return "";
        });
        */

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