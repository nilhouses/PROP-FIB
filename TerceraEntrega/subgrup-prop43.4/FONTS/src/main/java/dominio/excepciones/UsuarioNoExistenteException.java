package dominio.excepciones;

public class UsuarioNoExistenteException extends Exception {
    public UsuarioNoExistenteException(String mensaje) {
        super(mensaje);
    }
}