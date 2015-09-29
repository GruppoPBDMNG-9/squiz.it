package utility;


public class FormatStringChecker {
    private String text;

    private static final short NULL_CODE = 0;
    private static final short ZERO_CODE = 48;
    private static final short NINE_CODE = 57;
    private static final short A_UPPER_CODE = 65;
    private static final short Z_UPPER_CODE = 90;
    private static final short A_LOWER_CODE = 97;
    private static final short Z_LOWER_CODE = 122;

    public FormatStringChecker(String text){
        this.text = text;
    }

    public boolean check(){
        if(this.text.hashCode() == NULL_CODE)
            return false;

        this.text = removeStartAndEndIfEmpty(this.text);

        for(char c : this.text.toCharArray()){
            int cCode = (Character.toString(c)).hashCode();
            if(!(cCode >= ZERO_CODE && cCode <= NINE_CODE))
                if(!(cCode >= A_UPPER_CODE && cCode <= Z_UPPER_CODE))
                    if(!(cCode >= A_LOWER_CODE && cCode <= Z_LOWER_CODE))
                        return false;
        }

        return true;
    }

    public static String removeStartAndEndIfEmpty(String text){

        while(text.startsWith(" ")){
            text = text.substring(1);
        }

        while(text.endsWith(" ")){
            text = text.substring(0,text.length()-1);
        }


        return text;
    }

}
