package dominio;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Usuario implements Serializable{
    private String username;
    private String password;

    // Constructor vacio necesario para la deserializacion de JSON
    public Usuario() {

    }
    // Creadora ahora un formato accesible para JSON 
    @JsonCreator
    public Usuario(@JsonProperty("username") String username, @JsonProperty("password") String password) {
        this.username = username;
        this.password = password;
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