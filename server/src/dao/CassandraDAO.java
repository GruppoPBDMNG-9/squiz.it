package dao;

import java.util.List;

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

}
