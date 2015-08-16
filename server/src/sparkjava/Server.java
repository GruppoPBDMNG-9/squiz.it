package sparkjava;

import static spark.Spark.*;

import dao.CassandraDAO;
import org.json.JSONObject;
import shortening.Shortener;

public class Server {

    public static void main(String[] args) {

        /*
        Random generate operation
         */
        get("/shortening", (request, response) -> {
            JSONObject responseData = new JSONObject();

            try {
                String longUrl = request.queryParams(Args.LONG_URL);
                String shortUrl = new Shortener().generate();

                CassandraDAO dao = new CassandraDAO();
                dao.saveUrl(shortUrl);
                responseData.put(Args.SHORT_URL, shortUrl);

            } catch (Exception e) {
                System.out.println("exception = [" + e.getMessage() + "]");
            }

            System.out.println("responseData = [" + responseData.toString() + "]");
            System.out.println("done");
            return responseData.toString();
        });

        /*
        Login
         */
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

        /*
        Costumized generate operation ONLY FOR MEMBERS!
         */
        get("/customShortening", (request, response) -> {
            JSONObject responseData = new JSONObject();
            return "";
        });

        /*
        Singup
         */
        get("/singup", (request, response) -> {
           return "";
        });

        /*
          We need this just to allow Cross-domain resource sharing.
          More info: https://en.wikipedia.org/wiki/Cross-origin_resource_sharing

          WARNING: It seems useless, doesn't work! I can't use POST.


        options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });
        */

    }
}