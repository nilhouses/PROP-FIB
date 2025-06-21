import static org.junit.Assert.assertEquals;

import dominio.Producto;
import org.junit.Test;

public class TestProducto {
    @Test
    public void TestMakeProducto() {
        Producto p = new Producto(1,"Patates", "Lays");
        assertEquals(1,p.getid());
        assertEquals("Patates",p.getNom());
        assertEquals("Lays",p.getMarca());
    }
}
