package dominio.excepciones;

public class MismoProductoException extends Exception {
    public MismoProductoException(String mensaje) {
        super(mensaje);
    }
}