package utility;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;

/**
 * Classe per rilevare l'indirizzo IP e ottenere informazioni quali continente, nazione e citt�
 */
public class IPFinder {
    InetAddress ipAddress;
    CityResponse response;


    public IPFinder(){
        String path = System.getProperty("user.dir") + "/geoip-db/GeoLite2-City.mmdb";
        File database = new File(path);

        try {
            DatabaseReader reader = new DatabaseReader.Builder(database).build();
            ipAddress = InetAddress.getByName(getIpAddress());
            response = reader.city(ipAddress);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeoIp2Exception e) {
            e.printStackTrace();
        }


    }

    public String getCity(){
        return String.valueOf(response.getCity());
    }

    public String getCountry(){
        return String.valueOf(response.getCountry());
    }

    public String getContinent(){
        return String.valueOf(response.getContinent());
    }

    private String getIpAddress()
    {
        URL myIP;
        try {
            myIP = new URL("http://api.externalip.net/ip/");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(myIP.openStream())
            );
            return in.readLine();
        } catch (Exception e)
        {
            try
            {
                myIP = new URL("http://myip.dnsomatic.com/");

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(myIP.openStream())
                );
                return in.readLine();
            } catch (Exception e1)
            {
                try {
                    myIP = new URL("http://icanhazip.com/");

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(myIP.openStream())
                    );
                    return in.readLine();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }

        return null;
    }
}