import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;

import java.beans.Transient;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import dominio.Pair;

public class TestPair {
    
    @Test
    public void TestCreadoraYGettersPair() {
        Pair<?, ?> p = new Pair<>();
        assertEquals("Null si no inicializamos First", null, p.getFirst());
        assertEquals("Null si no inicializamos Second", null, p.getSecond());

        p = new Pair<>(0, 1);
        assertEquals("0 si inicializamos First a 0", 0, p.getFirst());
        assertEquals("1 si inicializamos Second a 1", 1, p.getSecond());
    }

    @Test
    public void TestSettersPair() {
        Pair<Integer, Integer> p = new Pair<>();
        assertEquals("0 si modificamos First a 0", Integer.valueOf(0), p.setFirst(0));
        assertEquals("1 si modificamos Second a 1", Integer.valueOf(1), p.setSecond(1));
    }

    @Test
    public void TestPrintPair() {
        ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        Pair<Integer, Integer> p = new Pair<>(0, 1);
        p.printPair();
        String exp = "(0,1)";
        assertEquals("Output correcto para (0,1)", exp.trim(), out.toString().trim());
        
        out.reset();
        p = new Pair<>();
        p.printPair();
        exp = "( , )";
        assertEquals("Output correcto para ( , )", exp.trim(), out.toString().trim());

    }

    @Test
    public void TestEquals() {
        Pair<Integer, Float> p1 = new Pair<>(0, 0.1f);
        Pair<Integer, Float> p2 = new Pair<>(0, 0.1f);
        assertTrue("Comprueba que dos Pairs sean iguales", p1.equals(p2));

        p2 = new Pair<>(0, 0.2f);
        assertFalse("Comprueba que dos Pairs sean distintos", p1.equals(p2));

        p2 = null;
        assertFalse("Comprueba que un Pair es distinto a Null", p1.equals(p2));

        Integer i = 1;
        assertFalse("Comprueba que un Pair es diferente a un Integer", p1.equals(i));
    }

    @Test 
    public void TestHashCode() {
        Pair<Integer, String> p1 = new Pair<>(0, "A");
        Pair<Integer, String> p2 = new Pair<>(0, "A");
        assertEquals("Comprueba el mismo hashCode para distintos Pairs con el mismo", p1.hashCode(), p2.hashCode());

        p2 = new Pair<>(1, "B");
        assertNotEquals("Comprueba diferente hashCode para distintos Pairs con distinto valor", p1.hashCode(), p2.hashCode());
    }
}   