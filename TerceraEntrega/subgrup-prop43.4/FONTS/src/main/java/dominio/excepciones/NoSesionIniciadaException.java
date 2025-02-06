package dominio.excepciones;

public class NoSesionIniciadaException extends Exception{
    public NoSesionIniciadaException(String mensaje) {
        super(mensaje);
    }
}