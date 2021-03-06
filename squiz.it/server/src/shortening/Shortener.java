package shortening;

public class Shortener {
    private static String tmpChar1;
    private static String tmpChar2;
    private static String tmpChar3;
    private static String tmpChar4;
    private static short tmpNum1;
    private static short tmpNum2;

    private static String char1 = "a";
    private static String char2 = "a";
    private static String char3 = "a";
    private static String char4 = "a";
    private static short num1 = 0;
    private static short num2 = 0;

    private static Alphabet alphabet;

    public Shortener(){
        alphabet = new LowerAlphabet();
    }

    public Shortener(String lastUrl){
        alphabet = new LowerAlphabet();

        this.char1 = Character.toString(lastUrl.charAt(0));
        this.char2 = Character.toString(lastUrl.charAt(1));
        this.num1 = (short) (Integer.parseInt(Character.toString(lastUrl.charAt(2))));
        this.num2 = (short) (Integer.parseInt(Character.toString(lastUrl.charAt(3))));
        this.char3 = Character.toString(lastUrl.charAt(4));
        this.char4 = Character.toString(lastUrl.charAt(5));
    }

    /**
     * Generate a random short link
     * @return shortUrl
     */
    public String randomGenerate(){
        setUndo();

        if(!checkNum())
            switchChar();
        String shortUrl = (char1 + char2 + num1 + num2 + char3 + char4);

        return shortUrl;
    }

    /*
    If an user make a shortening operation, we generate a random short url.
    If he doesn't like auto-generated url, it can customize it by himself, and
    then we don't use the auto-generated url, but we will use it for the
    next shortening request.
     */
    public void undo(){
        char1 = tmpChar1;
        char2 = tmpChar2;
        char3 = tmpChar3;
        char4 = tmpChar4;
        num1 = tmpNum1;
        num2 = tmpNum2;
    }

    /*
    Set params for undo operation
     */
    private void setUndo(){
        tmpChar1 = char1;
        tmpChar2 = char2;
        tmpChar3 = char3;
        tmpChar4 = char4;
        tmpNum1 = num1;
        tmpNum2 = num2;
    }

    /**
     * Incrementa il contatore che corrisponde alla parte numerica dello short link.
     * Ritorna vero se il contatore non � ancora arrivato a 99. Ritorna falso altrimenti.
     * @return result
     */
    private static boolean checkNum(){
        boolean result = false;

        if(num2 == 9){
            if(num1 == 9){
                num1 = 0;
                num2 = 0;
                result = false;
            } else {
                num1++;
                num2 = 0;
                result = true;
            }
        } else {
            num2++;
            result = true;
        }

        return result;
    }

    /**
     * Fa scorrere le lettere del suffisso. Se il suffisso � arrivato ad assumere la forma "zz" si far� scorrere il prefisso.
     * Se anche prefisso e suffisso hanno entrambi la forma "zz" si passa ad utilizzare l'alfabeto delle lettere maiuscole,
     * resettando prefisso e suffisso nella forma "AA".
     */
    private static void switchChar(){
        if(!checkSuffix()){
            if(!checkPrefix()){
                switchAlphabet();
            }
        }
    }

    /**
     * Genera un nuovo suffisso.
     * Ritorna FALSE se il suffisso � arrivato alla forma "zz". TRUE altrimenti.
     * @return
     */
    private static boolean checkSuffix(){
        boolean result = false;

        if(char4.equals("z")){
            if(char3.equals("z")){
                char4 = "a";
                char3 = "a";
                result = false;
            } else {
                char3 = alphabet.getNext(char3);
                char4 = "a";
                result = true;
            }
        } else {
            char4 = alphabet.getNext(char4);
            result = true;
        }

        return result;
    }

    /**
     * Genera un nuovo prefisso.
     * Ritorna FALSE se il prefisso � arrivato alla forma "zz". TRUE altrimenti.
     * @return
     */
    private static boolean checkPrefix(){
        boolean result = false;

        if(char2.equals("z")){
            if(char1.equals("z")){
                result = false;
            } else {
                char1 = alphabet.getNext(char1);
                char2 = "a";
                result = true;
            }
        } else {
            char2 = alphabet.getNext(char2);
            result = true;
        }

        return result;
    }

    /**
     * Switcha dall'alfabeto minuscole a quello delle lettere maiuscole, resettando prefisso suffisso e contatore.
     */
    private static void switchAlphabet(){
        alphabet = new UpperAlphabet();
        char1 = "A";
        char2 = "A";
        char3 = "A";
        char4 = "A";
        num1 = 0;
        num2 = 0;
    }

    //TESTING
    public static void main(String[] args){
        Shortener s = new Shortener();

        for(int i = 0; i < 20; i++){
            String shortUrl = s.randomGenerate();
            System.out.println(shortUrl);
        }
    }
}
