package dao;

import redis.clients.jedis.Jedis;



public  class RedisConnection {
    private static Jedis redisConnection=new Jedis("127.0.0.1", 6379);

    public static Jedis getIstance(){
        return redisConnection;
    }

}