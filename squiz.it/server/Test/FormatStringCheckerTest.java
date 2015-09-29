import junit.framework.TestCase;
import utility.FormatStringChecker;

/**
 * Created by Donato on 23/09/2015.
 */
public class FormatStringCheckerTest extends TestCase {

    /*
    array di stringhe
    contiene SOLO parole accettate, con esse il metodo check deve restituire true
     */
    private String[] acceptedWords = {"marco23","danot21","w1donato","25555","soloParole","ancora14","87ligalizzato",
    "forzaJuveSempre","vasconvolto89","corr87do","ambra","saetta78","ineterista4ever","744558686","violetta","peppoG",
    "a4w8x57w5","Alex12","ALEX12","Xv58", " donato"};


    /*
    array di stringhe
    contiene SOLO parole non accettate, con esse il metodo check deve restituire false
     */
    private String[] wordsNotAccepted ={"","forza juve sempre", "forza juve","forza_juve","forza-juve","forza.juve",
    "forza;juve", "ciao mondo", "donato!", "donato£", "donato%", "donato&", "donato|", "donato/", "donato(",
            "donato)", "donato=", "donato'", "donato^","donato[","donato]","donato{","donato}","donato*",
            "donato+","donatoç","donato@","donato°","donato#","donato§","donato>","donato<","donato:", "cioè", "fù",
             "falò", "perché", "Wxò", "DONATO23!","+donato","don!ato"};


    /*
    array di stringhe
    sulle quali verrà testato il metodo removeStartAndEndIfEmpty
    per verificare se il suo comportamento è quello aspettato
    e cioà eliminare SOLO gli eventuali spazi vuoti che possono
    presentarsi all'inizio e alla fine di una stringa
     */
    private String [] testStringEmptyStartEnd = {"prova"," donato", " esame ", " esame     ", " es ame ", "      test",
                      "     ancora    ", "forza inter", " m are ", "spiagg ia ","canz one"};



    /*
        array di stringhe
        esse corrispondono a cosa ci si aspetta di ottnere
        dopo l'esecuzione del metodo removeStartAndEndIfEmpty
    */
    private String [] testStringEmptyStartEndExexpected = {"prova","donato", "esame", "esame", "es ame", "test",
            "ancora", "forza inter", "m are","spiagg ia", "canz one"};


    public void setUp() throws Exception {
        super.setUp();

    }

    public void tearDown() throws Exception {

    }

    /*
    si esegue prima il metodo per ogni stringa
    dell'array acceptedWords verificando che esso
    resistuisca SEMPRE true
    e dopo si esegue il metodo per ogni stringa
    dell'array wordsNotAccepted verificando che
    esso in questo caso restituisca SEMPRE false
     */
    public void testCheck() throws Exception {
            for (int i=0; i<acceptedWords.length; i++){
                FormatStringChecker fsc= new FormatStringChecker(acceptedWords[i]);
                assertTrue(fsc.check());
            }

            for (int i=0; i<wordsNotAccepted.length; i++){
                 FormatStringChecker fsc= new FormatStringChecker(wordsNotAccepted[i]);
                 assertTrue(!fsc.check());
            }
    }

    /*
    si esegue il metodo removeStartAndEndIfEmpty su ogni stringa contenuta
    nell'array testStringEmptyStartEnd e si verifica che ciò che si ottiene
    corrisponde a quello che ci si aspetta che è contenuto
     nell'array testStringEmptyStartEndExexpected
     */
    public void testRemoveStartAndEndIfEmpty(){
        for (int i=0; i<testStringEmptyStartEnd.length; i++){
            String result=FormatStringChecker.removeStartAndEndIfEmpty(testStringEmptyStartEnd[i]);
            assertTrue(result.equals(testStringEmptyStartEndExexpected[i]));
        }
    }



}