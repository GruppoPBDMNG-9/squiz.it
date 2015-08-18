package utility;

public class FormatStringChecker {
    private String text;

    private static final int NULL = 0;
    private static final int ZERO = 48;
    private static final int NINE = 57;
    private static final int A_UPPER = 65;
    private static final int Z_UPPER = 90;
    private static final int A_LOWER = 97;
    private static final int Z_LOWER = 122;

    public FormatStringChecker(String text){
        this.text = text;
    }

    public boolean check(){
        if(this.text.hashCode() == NULL)
            return false;

        this.text = removeStartAndEndIfEmpty(this.text);

        for(char c : this.text.toCharArray()){
            int cCode = (Character.toString(c)).hashCode();
            if(!(cCode >= ZERO && cCode <= NINE))
                if(!(cCode >= A_UPPER && cCode <= Z_UPPER))
                    if(!(cCode >= A_LOWER && cCode <= Z_LOWER))
                        return false;
        }

        return true;
    }

    private static String removeStartAndEndIfEmpty(String text){
        while(text.startsWith(" ")){
            text = text.substring(1);
        }

        while(text.endsWith(" ")){
            text = text.substring(0,text.length()-1);
        }

        return text;
    }

    //USING ONLY FOR TESTING
    public static void main(String[] args){
        String s = "  ciao     ";
        System.out.println(new FormatStringChecker(s).check());
    }

}
