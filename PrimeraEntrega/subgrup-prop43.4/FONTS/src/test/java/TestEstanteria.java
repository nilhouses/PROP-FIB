import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.gson.internal.PreJava9DateFormatProvider;

import dominio.Estanteria;
import dominio.Producto;



/*En este fichero vamos a testear el correcto funcionamiento de la clase Estanteria
*  mockeando la clase Producto en algunos casos
*/


public class TestEstanteria {
    
    Estanteria e;

    @Before
    public void inicializaEstanteria() {
        e = new Estanteria();
    }

//TESTS SIN MOCKS/////////////////////////////////////////////////////////////////////////////////////////////////////
    //(1)// Alta Producto y Baja Producto
    @Test
    public void TestAltaProducto() {
        /*
        * TEST 1: Comprobamos que añadir un producto a la estanteria tiene un id valido (distinto de -1)
        *         Comprobamos también que este id sea 1, ya que al no haber ningún id anterior en esa 
        *         Estanteria se usara 1 como id del primer producto insertado.
        */
        int id = e.altaProducto(new Producto("Patatas","Lays"));
        assertNotEquals(-1,id);
        assertEquals(1,id);
        /*
        * TEST 2: Comprobamos que añadir un producto que ya esta en la estanteria devuelve -1
        */
        int id2 = e.altaProducto(new Producto("Patatas","Lays"));
        assertEquals(-1,id2);
        /*
        * TEST 3: Usamos la funcion getter para comprovar que en el alta del producto haya habido una 
        *         correcta asignacion del nombre y marca del producto.
        */
        Producto p = e.getProducto(id);
        assertEquals("Patatas", p.getNom());
        assertEquals("Lays",p.getMarca());
    }

    @Test
    public void  TestEliminaProducto() {
        /*
        * TEST 1: Comprobamos que eliminar un producto existente en la estanteria devuelve el id de ese 
        *         producto.
        */
        e.altaProducto(new Producto("Patatas","Lays"));
        int id = e.eliminaProducto(new Producto("Patatas", "Lays"));
        assertNotEquals(-1,id);
        assertEquals(1,id);
        /*
        * TEST 2: Comprobamos que eliminar un producto que inexistente devuelve -1.
        */
        int removedId = e.eliminaProducto(new Producto("Patatas", "Lays"));
        assertEquals(-1,removedId);
        assertNull(e.getProducto(id));
    }

    //(2)// Getters
    @Test 
    public void TestgetIdProductos() {
        int id1 = e.altaProducto(new Producto("Patatas","Lays"));    //id1 = 1
        int id2 = e.altaProducto(new Producto("Patatas","Ruffles")); //id2 = 2
        int id3 = e.altaProducto(new Producto("Patatas","Pringles"));//id3 = 3
        int id4 = e.altaProducto(new Producto("Ketchup", "Heinz"));   //id4 = 4
        /*
        * TEST 1: Comprobamos que se obtiene los ids de los productos correctamente.
        */
        ArrayList<Integer> ordenDeseado = new ArrayList<>(Arrays.asList(1,2, 3, 4));
        ArrayList<Integer> ordenInterno = e.getIdProductos();
        assertTrue(ordenDeseado.equals(ordenInterno));
        /*
        * TEST 2: Comprobamos que al eliminar un producto se devuelve la lista anterior sin su id.
        */
        int id = e.eliminaProducto(new Producto("Patatas","Ruffles"));
        ordenDeseado = new ArrayList<>(Arrays.asList(1, 3, 4));
        ordenInterno = e.getIdProductos();
        assertTrue(ordenDeseado.equals(ordenInterno));
    }
  
    @Test 
    public void TestgetProductoPorID() {
        /*
        * TEST 1: Comprobamos que se se pueda acceder a un producto por su nombre y marca.
        */
        int id1 = e.altaProducto(new Producto("Patatas","Lays"));
        Producto p = e.getProducto(id1);
        assertEquals(id1, p.getid()); 
        assertEquals("Patatas",p.getNom());
        assertEquals("Lays",p.getMarca());
        /*
        * TEST 2: Comprobamos que se devuelva null al preguntar por un id que no existe.
        */
        assertNull(e.getProducto(4));
        /*
        * TEST 3: Comprobamos también, que se devuelva null al preguntar por un id que existió
        *         en un momento determinado en la estanteria pero ya no existe.
        */
        int id = e.eliminaProducto(new Producto("Patatas", "Lays"));
        assertNull(e.getProducto(1));
    }
    
    @Test 
    public void TestgetProductoPorNombreMarca() {
        /*
        * TEST 1: Comprobamos que el producto devuelto por la consultora con nombre y marca devuelva 
        *         un producto con mismo nombre, marca y id. Comprovamos que el id del producto sea el 
        *         que se la asigna en su alta en la estanteria.
        */
        int id1 = e.altaProducto(new Producto("Patatas","Lays"));
        Producto p = e.getProducto(new Producto("Patatas","Lays"));
        assertEquals(id1, p.getid());
        assertEquals("Patatas",p.getNom());
        assertEquals("Lays",p.getMarca());
        /*
        * TEST 2: Comprobamos que se devuelva null al consultar si existe un producto que no esta en el sistema
        */
        assertNull(e.getProducto(new Producto("Patatas","Rancheras")));
        /*
        * TEST 3: Comprobamos también, que se devuelva null al preguntar por un producto que existió
        *         en un momento determinado en la estanteria pero ya no existe.
        */
        int id = e.eliminaProducto(new Producto("Patatas", "Lays"));
        assertNotEquals(-1,id);
        assertNull(e.getProducto(new Producto("Patatas","Lays")));
    }
    

    @Test
    public void TestgetIdProducto() {
        /*
        * TEST 1: Comprobamos que la funcion devuelva un id valido.
        */
        int id  = e.altaProducto(new Producto("Patatas","Lays")); 
        assertNotEquals(-1, e.getIdProducto(new Producto("Patatas", "Lays")));
        assertEquals(1, e.getIdProducto(new Producto("Patatas", "Lays")));
        /*
        * TEST 2: Comprobamos que la asignación de ids en estanteria sea incremental.
        */
        int id2  = e.altaProducto(new Producto("Patatas","Ruffles")); 
        assertEquals(2, e.getIdProducto(new Producto("Patatas", "Ruffles")));
        /*
        * TEST 3: Comprobamos que se devuelva -1 al preguntar por un producto que no esta en la estanteria.
        */
        assertEquals(-1, e.getIdProducto(new Producto("Patatas", "Cheetos")));
    }

    //(3)// Setters
    @Test
    public void TestaplicaOrden() {
        /*
         * Funcion usada para asignar el orden resultante de aplicar el algoritmo (Kruskal o Backtracking)
         * 
         * Esta funcionalidad acepta otro orden alternativo si y solo si tiene los mismos elementos que devuelve 
         *  getIdProductos, ya que devuelve el orden actual de los productos representado por el arraylist de sus ids
        */
        int id1 = e.altaProducto(new Producto("Patatas","Lays"));    //id1 = 1
        int id2 = e.altaProducto(new Producto("Patatas","Ruffles")); //id2 = 2
        int id3 = e.altaProducto(new Producto("Patatas","Pringles"));//id3 = 3
        int id4 = e.altaProducto(new Producto("Ketchup","Heinz"));   //id4 = 4
        // Vemos cual es el orden inicial de id de productos
        ArrayList<Integer> ordenDeseado = new ArrayList<>(Arrays.asList(1,2,3,4));
        ArrayList<Integer> ordenInterno = e.getIdProductos();
        assertTrue(ordenDeseado.equals(ordenInterno));
        /*
        * TEST 1: Asignamos a estanteria los mismos productos en distinto orden y vemos que se aplica correctamente 
        *         y la función devuelve True
        */
        ordenDeseado = new ArrayList<>(Arrays.asList( 4,3,2,1));
        assertEquals(true, e.aplicaOrden(ordenDeseado));
        ordenInterno = e.getIdProductos();
        assertTrue(ordenDeseado.equals(ordenInterno));
        /*
        * TEST 2: Vemos que asignar un orden comprueba que todos los identificadores a asignar sean de productos almacenados
        *         actualmente en la estanteria, en caso incorrecto tiene que devolver false y mantener el orden inicial
        */
        ArrayList<Integer> ordenErroneo = new ArrayList<>(Arrays.asList( 5,3,2,1));
        assertEquals(false, e.aplicaOrden(ordenErroneo));
        assertTrue(ordenDeseado.equals(ordenInterno));
        /*
        * TEST 3: Vemos que un orden con productos repetidos también es erróneo
        */
        ordenErroneo = new ArrayList<>(Arrays.asList( 4,4,2,1));
        assertEquals(false, e.aplicaOrden(ordenErroneo));
        assertTrue(ordenDeseado.equals(ordenInterno));

    }

    @Test
    public void TestVaciarEstanteria() {
        /*
        * TEST 1: Añadimos algunos productos a la estanteria y comprobamos que tras
        *         vaciarla esta quede vacia. 
        */
        e.altaProducto(new Producto("Zumo", "Don Simon"));
        e.altaProducto(new Producto("Pizza", "Buitoni"));
        e.vaciarEstanteria();

        assertTrue(e.getIdProductos().isEmpty());
        assertEquals(0, e.getProductos().size());
    }

    //(4)// Funciones de Entrada y Salida (I/O)
    @Test
    public void TestPrint() {
        ByteArrayOutputStream output = new java.io.ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        /*
        * TEST 1: Comprovamos que se imprima una estanteria vacia como deseamos
        */
        e.print();
        String expected = "[  ]";
        assertEquals("Mismo output para Estanteria vacia", expected.trim(), output.toString().trim());
        //preparamos el siguiente test
        output.reset();
        /*
        * TEST 2: Comprovamos que se imprima una estanteria llena como deseamos
        */
        e.altaProducto(new Producto("Patatas","Lays"));
        e.altaProducto(new Producto("Patatas","Ruffles"));
        e.altaProducto(new Producto("Patatas","Pringles"));
        e.print();
        expected ="[ Patatas Lays | Patatas Ruffles | Patatas Pringles ]\n";
        assertEquals("Mismo output para Estanteria llena", expected.trim(), output.toString().trim());
        System.out.println("Expected: " + expected.trim());
        System.out.println("Actual: " + output.toString().trim());
        System.setOut(System.out);
    }
//TESTS CON MOCKS/////////////////////////////////////////////////////////////////////////////////////////////////////

    @Mock
    Producto MockGalletasCuetara;
    Producto MockLechePascual;
    Producto MockPanBimbo;

    @Before
    public void setUp() {
        //Inicializamos los Mocks
        MockitoAnnotations.initMocks(this);
        //Le asignamos un comportamiento
        MockGalletasCuetara = mock(Producto.class);
        when(MockGalletasCuetara.getNom()).thenReturn("Galletas");
        when(MockGalletasCuetara.getMarca()).thenReturn("Cuetara");
        
        MockLechePascual = mock(Producto.class);
        when(MockLechePascual.getNom()).thenReturn("Leche");
        when(MockLechePascual.getMarca()).thenReturn("Pascual");

        MockPanBimbo = mock(Producto.class);
        when(MockPanBimbo.getNom()).thenReturn("Pan");
        when(MockPanBimbo.getMarca()).thenReturn("Bimbo");
    }

    @Test
    public void TestGetProducto() {

        // Creamos la estantería y agregamos los mocks
        e.altaProducto(MockGalletasCuetara);
        e.altaProducto(MockLechePascual);
        e.altaProducto(MockPanBimbo);

        // Recuperamos los Mocks
        Producto p1 = e.getProducto(new Producto("Galletas", "Cuetara"));
        Producto p2 = e.getProducto(new Producto("Leche", "Pascual"));
        Producto p3 = e.getProducto(new Producto("Pan", "Bimbo"));

        // Verificamos que los productos devueltos coinciden
        assertNotNull(p1);
        assertEquals("Galletas", p1.getNom());
        assertEquals("Cuetara", p1.getMarca());

        assertNotNull(p3);
        assertEquals("Leche", p2.getNom());
        assertEquals("Pascual", p2.getMarca());

        assertNotNull(p3);
        assertEquals("Pan", p3.getNom());
        assertEquals("Bimbo", p3.getMarca());
    }
}
