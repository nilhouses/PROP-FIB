package dominio.excepciones;

public class EstanteriaVaciaException extends Exception {
    public EstanteriaVaciaException(String mensaje) {
        super(mensaje);
    }
}