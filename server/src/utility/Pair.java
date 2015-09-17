package utility;

public class Pair {
    private String month;
    private int click;

    public Pair(String month, int click){
        this.month = month;
        this.click = click;
    }

    public String getMonth(){
        return this.month;
    }

    public int getClick(){
        return this.click;
    }

    public void setClick(int c) {
        this.click=c;
    }
}
