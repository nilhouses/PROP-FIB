package dominio;

//Clase que controla los diferentes valores de un producto.
public class Producto {
    //El identificador unico numerico de un producto.
    private int productid;

    //El nombre de un producto.
    private String NomProducte;

    //La marca de un producto.
    private String MarcaProducte;

    //Creadora de la clase producto con los tres parametros.
    public Producto (int ID, String NP, String MP)
    {
        productid = ID;
        NomProducte = NP;
        MarcaProducte = MP;
    }

    //Creadora de la clase con solo nombre y marca.
    public Producto (String NP, String MP)
    {
        productid = -1;
        NomProducte = NP;
        MarcaProducte = MP;
    }

    //Fucnion que añade un valor al identificador de producto en caso que se haya creado sin el.
    public void setId(int id) {
        productid = id;
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
    public void printProducte()
    {
        System.out.print(NomProducte + " " + MarcaProducte);
    }
}