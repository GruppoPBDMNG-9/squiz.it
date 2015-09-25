package shortening;

import java.util.HashMap;

public class LowerAlphabet extends Alphabet{

    public LowerAlphabet(){
        super.alphabet = new HashMap<String, String>();
        initAlphabet(super.alphabet);
    }


    @Override
    protected void initAlphabet(HashMap<String, String> map) {
        map.put("a","b");
        map.put("b","c");
        map.put("c","d");
        map.put("d","e");
        map.put("e","f");
        map.put("f","g");
        map.put("g","h");
        map.put("h","i");
        map.put("i","j");
        map.put("j","k");
        map.put("k","l");
        map.put("l","m");
        map.put("m","n");
        map.put("n","o");
        map.put("o","p");
        map.put("p","q");
        map.put("q","r");
        map.put("r","s");
        map.put("s","t");
        map.put("t","u");
        map.put("u","v");
        map.put("v","w");
        map.put("w","x");
        map.put("x","y");
        map.put("y","z");
        map.put("z","a");
    }
}
