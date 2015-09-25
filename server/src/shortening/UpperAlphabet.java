package shortening;

import java.util.HashMap;

public class UpperAlphabet extends Alphabet {

    public UpperAlphabet(){
        super.alphabet = new HashMap<String, String>();
        initAlphabet(super.alphabet);
    }

    @Override
    protected void initAlphabet(HashMap<String, String> map) {
        map.put("A","B");
        map.put("B","C");
        map.put("C","D");
        map.put("D","E");
        map.put("E","F");
        map.put("F","G");
        map.put("G","H");
        map.put("H","I");
        map.put("I","J");
        map.put("J","K");
        map.put("K","L");
        map.put("L","M");
        map.put("M","N");
        map.put("N","O");
        map.put("O","P");
        map.put("P","Q");
        map.put("Q","R");
        map.put("R","S");
        map.put("S","T");
        map.put("T","U");
        map.put("U","V");
        map.put("V","W");
        map.put("W","X");
        map.put("X","Y");
        map.put("Y","Z");
        map.put("Z","A");
    }
}
