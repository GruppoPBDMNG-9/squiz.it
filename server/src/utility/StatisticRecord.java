package utility;

import java.util.ArrayList;

public class StatisticRecord {
    private ArrayList<String> args;

    public StatisticRecord(ArrayList<String> args){
        this.args = args;

    }

    public String getData(){
        return this.args.get(StatisticsIndex.DATA);
    }

    public String getShortLink(){
        return this.args.get(StatisticsIndex.SHORT_URL);
    }

    public String getLongUrl(){
        return this.args.get(StatisticsIndex.LONG_URL);
    }

    public String getClick(){
        return this.args.get(StatisticsIndex.CLICK);
    }

    public String getPopularCountry(){
        return this.args.get(StatisticsIndex.POPULAR_COUNTRY);
    }
}

