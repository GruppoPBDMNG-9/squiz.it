package dao;

public class CassandraDAO extends DAO {

    public boolean availableUrl(String url){

        return true;

    }

    public void saveUrl(String longUrl, String newUrl){
        //probabilmente prenderà in input un arraylist, contenente i due url + username e password, settati a null se nessun ha effettuato l'accesso.
    }

    public boolean login(String email, String password){
        return true;
    }

}
