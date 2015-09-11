package dao;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/*
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
*/

public abstract class DAO {
    private Jedis connection;

    public void openConnection(String host, int port){
        Jedis jedis = new Jedis(host, port);
        connection=jedis;
    }

    public String getInstance(){
        return connection.ping();
    }

    public void CloseConnection(Jedis jedis){
        jedis.close();
    }

}