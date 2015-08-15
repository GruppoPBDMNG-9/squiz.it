package sparkjava;

class Utility {
    /*
     * Pensiamo ad un algoritmo che, consultando l'ultimo link generato, sia in grado di capire quali sono i link ancora disponibili,
     * in modo tale da non fare tentativi generando stringhe random che potrebbero già essere occupate e guadagnando così in efficienza.
     */
    public static String shortening(String longUrl){
        return longUrl + "short";
    }
}
