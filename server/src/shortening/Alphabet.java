package shortening;

import java.util.HashMap;

public abstract class Alphabet {
    protected HashMap<String, String> alphabet;

    protected abstract void initAlphabet(HashMap<String, String> map);

    public String getNext(String s){
        return this.alphabet.get(s);
    }
}
