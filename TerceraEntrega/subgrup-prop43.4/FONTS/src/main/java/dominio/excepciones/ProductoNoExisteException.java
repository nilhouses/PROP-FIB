package dominio.excepciones;

public class ProductoNoExisteException extends Exception{
    public ProductoNoExisteException(String mensaje) {
        super(mensaje);
    }
}
