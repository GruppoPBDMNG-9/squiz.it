package georecord;

import java.util.LinkedList;

public abstract class GeoRecord {
    protected String name;
    protected int clicks;
    protected LinkedList<Object> list;

    public GeoRecord(String name, int clicks, LinkedList<Object> list){
        this.name = name;
        this.clicks = clicks;
        this.list = list;
    }

    public String getName(){return this.name;}
    public int getClicks(){return this.clicks;}
    public LinkedList<Object> getList(){return this.list;}

}
