package dominio;

public class Usuario {
    private String username;
    private String password;


    public Usuario(String usr, String psswd){
        this. username = usr;
        this.password = psswd;
    }

    public boolean entraContrasena(String psswd){
        if (psswd.equals(password)) return true;
        else return false;
    }

    public String getUsername(){
        return this.username;
    }
    
    public String getPassword(){
        return this.password;
    }
}