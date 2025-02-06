package dominio.excepciones;

public class NoExistenProductosException extends Exception{
    public NoExistenProductosException(String mensaje) {
        super(mensaje);
    }
}
