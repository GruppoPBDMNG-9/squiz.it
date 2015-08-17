package sparkjava;

import static spark.Spark.*;

import dao.CassandraDAO;
import org.json.JSONObject;
import shortening.Shortener;
import spark.Request;
import spark.Response;

public class Server {

    public static void main(String[] args) {

        /*
        Shortening operation
        */
        get("/shortening",  (request, response) -> {
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
            System.out.println("done");

            System.out.println("responseData: " + responseData.toString());
            System.out.println("response: " + response.toString());

            setResponseHeader(request, response);
            return responseData;
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
          We need these two private method because of Cross-domain resource sharing.
          More info: https://en.wikipedia.org/wiki/Cross-origin_resource_sharing

          WARNING: Every method will call setResponseHeader(). options calls setOptionRequestResponseHeader()
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