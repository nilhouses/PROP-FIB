import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;

import static org.junit.Assert.assertFalse;
import org.junit.Test;

import dominio.Usuario;

/*
    En este fichero queremos testeamos el funcionamiento general de la clase Usuario
*/

public class TestUsuario {
    Usuario usuario;

    @Before
    public void setup() {
        usuario = new Usuario("nil", "pwd");
    }

    @Test
    public void testConstructor() {
    /*  TEST 1
        * En este test simplemente testeamos que la contructora gestione
        * bien los parametros proporcionados. 
    */
        assertEquals("nil", usuario.getUsername());
        assertEquals("pwd", usuario.getPassword());
    }

    @Test
    public void testEntraContrasenaCorrecta() {
    /*  TEST 2
        * En este test miramos que se acepte una contraseña correcta
    */
        assertTrue(usuario.entraContrasena("pwd"));
    }

    @Test
    public void testEntraContrasenaIncorrecta() {
    /*  TEST 3
        * En este test miramos que se denegue una contraseña incorrecta
    */
        assertFalse(usuario.entraContrasena("PWD"));
    }

    @Test
    public void testGetUsername() {
    /*  TEST 4
        * Test getter username
    */
        assertEquals("nil", usuario.getUsername());
    }
    @Test
    public void testGetPassword() {
    /*  TEST 5
        * Test getter password, esta funcion no la usamos nunca, pero nos interesa testearla
    */
        assertEquals("pwd", usuario.getPassword());
    }
}