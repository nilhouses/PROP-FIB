import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import dominio.CjtUsuarios;
import dominio.Estanteria;
import dominio.Producto;
import dominio.Usuario;


/*En este fichero vamos a testear el correcto funcionamiento de la clase CjtUsuarios
*  mockeando la clase Usuario en algunos casos
*/

public class TestCjtUsuarios {
    CjtUsuarios instancia;
    ByteArrayOutputStream output;

    @Test
    public void TestLogin_y_signUp() {
        instancia = new CjtUsuarios();
        output = new java.io.ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        
        /*
        * TEST 1: Comprovamos se muestra mensaje de error al hacer login de usuario no registrado
        */
        instancia.login("nil.casas.duatis","1234");
        String expected = "El usuario nil.casas.duatis no existe";
        assertEquals("LOGIN FALLIDO (USUARIO INEXISTENTE)", expected.trim(), output.toString().trim());
        output.reset();
        /*
        * TEST 2: Comprovamos que al crear una cuenta que no existe se añade correctamente la cuenta y se ejecuta un login automatico
        */
        instancia.signUp(new Usuario("nil.casas.duatis","1234"));
        expected = "Se ha creado una cuenta para el usuario nil.casas.duatis y se ha iniciado sesion";
        assertEquals("SIGN UP CORRECTO", expected.trim(), output.toString().trim());
        System.setOut(System.out);
    }

    
    @Test
    public void CambioDeCuenta() {
        instancia = new CjtUsuarios();
        output = new java.io.ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        
        //Creamos cuenta
        instancia.signUp(new Usuario("nil.casas.duatis","1234"));
        String expected = "Se ha creado una cuenta para el usuario nil.casas.duatis y se ha iniciado sesion";
        assertEquals("SIGN UP CORRECTO", expected.trim(), output.toString().trim());
        output.reset();
        
        /*
        * TEST 3: Comprovamos que al estar logeado en una cuenta hay hacer logout para dar de alta otra cuenta
        */
        instancia.signUp(new Usuario("andreu.corden","543"));
        expected = "Sesion iniciada como nil.casas.duatis, cierrela y vuelvalo a intentar";
        assertEquals("SIGN UP FALLIDO", expected.trim(), output.toString().trim());
        output.reset();
        //Al salir de nil se puede dar de alta andreu
        instancia.logout();
        expected = "Cerrando sesion del usuario nil.casas.duatis";
        assertEquals("Cerrar sesion", expected.trim(), output.toString().trim());
        output.reset();
        //alta andreu
        instancia.signUp(new Usuario("andreu.corden","543"));
        expected = "Se ha creado una cuenta para el usuario andreu.corden y se ha iniciado sesion";
        assertEquals("SIGN UP CORRECTO 2", expected.trim(), output.toString().trim());
        output.reset();
    
        System.setOut(System.out);
    }

    @Test
    public void passwordErroneo() {
        instancia = new CjtUsuarios();
        output = new java.io.ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        //Creamos cuenta
        instancia.signUp(new Usuario("nil.casas.duatis","1234"));
        String expected = "Se ha creado una cuenta para el usuario nil.casas.duatis y se ha iniciado sesion";
        assertEquals("SIGN UP CORRECTO", expected.trim(), output.toString().trim());
        instancia.logout();
        output.reset();
        
        /*
        * TEST 4: Comprovamos que no se pueda hacer login con una contraseña incorecta
        */
        instancia.login("nil.casas.duatis","123");
        expected = "Incorrect Password";
        assertEquals("LOGIN FALLIDO (CONTRASENA INCORRECTA)", expected.trim(), output.toString().trim());
        instancia.logout();
        output.reset();

        System.setOut(System.out);
    }
    @Test
    public void usernameUnico() {
        instancia = new CjtUsuarios();
        output = new java.io.ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        //Creamos cuenta
        instancia.signUp(new Usuario("nil.casas.duatis","1234"));
        String expected = "Se ha creado una cuenta para el usuario nil.casas.duatis y se ha iniciado sesion";
        assertEquals("SIGN UP CORRECTO", expected.trim(), output.toString().trim());
        instancia.logout();
        output.reset();
    
        /*
        * TEST 5: Comprovamos que no se puedan dar de alta 2 cuentas con el mismo username
        */
        instancia.signUp(new Usuario("nil.casas.duatis","patata"));
        expected = "El usuario nil.casas.duatis ya existe";
        assertEquals("SIGN UP FALLIDO", expected.trim(), output.toString().trim());
    
        System.setOut(System.out);
    }
    // No hemos hecho tests de las otras clases ya que representan funciones de la clase Controladora del dominio, 
    // y solo transmiten los datos a esta capa immediatamente inferior
//TESTS CON MOCKS/////////////////////////////////////////////////////////////////////////////////////////////////////

    @Mock
    Usuario MockNil;
    Usuario MockAndreu;
    Usuario MockDavid;

    @Before
    public void setUp() {
        //Inicializamos los mocks
        MockitoAnnotations.initMocks(this);
        //Le asigno un comportamiento
        MockNil = mock(Usuario.class);
        when(MockNil.getUsername()).thenReturn("nil.casas.duatis");
        when(MockNil.getPassword()).thenReturn("pwd1");
        
        MockAndreu = mock(Usuario.class);
        when(MockAndreu.getUsername()).thenReturn("david.vergel");
        when(MockAndreu.getPassword()).thenReturn("pwd2");

        MockDavid = mock(Usuario.class);
        when(MockDavid.getUsername()).thenReturn("andreu.corden");
        when(MockDavid.getPassword()).thenReturn("pwd3");
    }

    @Test
    public void TestGestionUsuarios() {
        // Insantciamos el gestor de usuarios
        instancia = new CjtUsuarios();
        //Creamos Usuario MockNil
        assertTrue(instancia.signUp(MockNil));
        assertTrue(instancia.loggedIn()); //Automáticamente se loggea al hacer el sign up
        assertFalse(instancia.signUp(MockNil)); //Error -> Sesion iniciada
        //Salimos de Nil
        assertTrue(instancia.logout());
        assertFalse(instancia.signUp(MockNil)); //Error -> Usuario ya existente
        assertFalse(instancia.loggedIn());
        //Creamos Usuario MockAndreu
        assertTrue(instancia.signUp(MockAndreu));
        assertTrue(instancia.loggedIn()); //Automáticamente se loggea al hacer el sign up
        assertTrue(instancia.logout());//salimos del usuario
        assertFalse(instancia.loggedIn());
        //Creamos Usuario MockDavid
        assertTrue(instancia.signUp(MockDavid));
        assertTrue(instancia.loggedIn());
    }

}
