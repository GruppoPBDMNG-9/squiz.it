package shortening;

import java.util.HashMap;

public abstract class Alphabet {
    protected HashMap<String, String> alphabet;

    public String getNext(String s){
        return this.alphabet.get(s);
    }
}
