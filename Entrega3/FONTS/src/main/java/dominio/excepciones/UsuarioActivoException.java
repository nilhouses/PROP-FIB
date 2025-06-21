package dominio.excepciones;

public class UsuarioActivoException extends Exception {
    public UsuarioActivoException(String mensaje) {
        super(mensaje);
    }
}