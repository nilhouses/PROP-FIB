package dominio;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

//Clase que controla los diferentes valores de un producto.
public class Producto implements Serializable{
    //El identificador unico numerico de un producto.
    private int productid;

    //El nombre de un producto.
    private String NomProducte;

    //La marca de un producto.
    private String MarcaProducte;

    // Constructor sin argumentos para la correcta deserializacion de la clase
    public Producto() {
    }

    //Creadora de la clase producto con los tres parametros para formato JSON.
    @JsonCreator
    public Producto(@JsonProperty("id") int productid,
                    @JsonProperty("nom") String NomProducte,
                    @JsonProperty("marca") String MarcaProducte) {
        this.productid = productid;
        this.NomProducte = NomProducte;
        this.MarcaProducte = MarcaProducte;
    }

    //Creadora de la clase con solo nombre y marca.
    public Producto (String NP, String MP)
    {
        productid = -1;
        NomProducte = NP;
        MarcaProducte = MP;
    }

    //Funcion que añade un valor al identificador de producto en caso que se haya creado sin el.
    public void setId(int id) {
        productid = id;
    }

    //Funcion que modifica el nombre del producto.
    public void setNombre(String nombre) {
        NomProducte = nombre;
    }

    //Funcion que modifica la marca del producto.
    public void setMarca(String marca) {
        MarcaProducte = marca;
    }

    //Devuelve el identificador del producto.
    public int getid()
    {
        return productid;
    }

    //Devuelve el nombre del producto.
    public String getNom()
    {
        return NomProducte;
    }

    //Devuelve la marca del producto.
    public String getMarca()
    {
        return MarcaProducte;
    }

    //Funcion que imprime el nombre y la marca del producto.
    public String printProducte()
    {
       return NomProducte + " " + MarcaProducte;
    }
}