package persistencia;

import dominio.ControladoraDominio;
import dominio.ControladoraUsuario;
import dominio.Usuario;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;

/*
    Esta clase se encarga de mantener la informacion del sistema almacenada en el formato:
        src/main/usuarios/
        ├── [usuario_1].json
        ├── [usuario_2].json
            ...
        ├── [usuario_n].json
    donde cada fichero de usuario almacena:
        - username del usuario 
        - contraseña del usuario
        - Estanteria de productos (productos, ordenProductos, contadorId)
        - Similitudes de productos (jsonHmap)
*/

public class ControladoraPersistencia implements Serializable {

    private static String BASE_PATH = "src/main/usuarios/";             // Path donde almacenaremos todos los ficheros relevantes para la persistencia del proyecto
    private ObjectMapper mapper;                                        // Instancia del lector y escritor usado para tratar los ficheros .json

    // Funcion creadora
    public ControladoraPersistencia() {
        this.mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    // Funcion que se encarga de cargar todos los usuarios desde sus archivos
    public HashMap<String, ControladoraUsuario> cargarUsuarios() throws IOException {

        File directorio = new File(BASE_PATH);
        HashMap<String, ControladoraUsuario> usuarios = new HashMap<>();
    
        if (!directorio.exists() || !directorio.isDirectory()) {// Si no existe el directorio, devolvemos un HashMap vacío
            return usuarios; 
        }
    
        File[] ficheros = directorio.listFiles();
    
        if (ficheros != null) {
            for (File f : ficheros) {
                if (f.isFile() && f.getName().endsWith(".json")) { //Eliminamos los usuarios del directorio
                    // Informacion mostrada por el terminal
                    System.out.println("Cargando usuario desde: " + f.getName());  
                    try {
                        ControladoraUsuario ctrlUsuario = cargarControladoraUsuario(f.getName().replace(".json", ""));
                        if (ctrlUsuario != null) {
                            usuarios.put(ctrlUsuario.getUsername(), ctrlUsuario);
                        } else {
                            System.out.println("No se pudo cargar el usuario: " + f.getName());  // Mensaje de depuración
                        }
                    } catch (IOException e) {
                        System.out.println("Error al cargar el archivo: " + f.getName());
                    }
                }
            }
        }        
        return usuarios;
    }
    


    // Funcion para escribir en ficheros todos los usuarios actuales
    public void guardarUsuarios(HashMap<String, ControladoraUsuario> usuarios) throws IOException {
        for (String username : usuarios.keySet()) {
            ControladoraUsuario ctrlUsuario = usuarios.get(username);
            guardarControladoraUsuario(username, ctrlUsuario);
        }
    }

    // Funcion para leer un usuario en concreto
    public ControladoraUsuario cargarControladoraUsuario(String username) throws IOException {
        String filePath = BASE_PATH + username + ".json";
        File archivo = new File(filePath);

        if (archivo.exists() && archivo.length() > 0) {
            // Informacion mostrada por el terminal
            System.out.println("Cargando archivo: " + filePath);
            System.out.println("Contenido del archivo: ");
            BufferedReader br = new BufferedReader(new FileReader(archivo));
            String linea;
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }
            br.close();
            ControladoraUsuario ctrlUsuario = mapper.readValue(archivo, ControladoraUsuario.class);
            System.out.println("Usuario cargado: " + ctrlUsuario.getUsername());
            return ctrlUsuario;
        }
        return null;
    }

    //Funcion para leer un usuario desde un fichero.json en concreto
    public ControladoraUsuario cargarUsuarioDesdeArchivo(String filePath) throws Exception {
        File archivo = new File(filePath);
    
        if (archivo.exists() && archivo.length() > 0) {

            if (!archivo.getName().endsWith(".json")) {
                throw new Exception("El archivo no tiene un formato válido, tiene que ser un .json");
            }
            System.out.println("Cargando archivo: " + filePath);
    
            try {
                // Mostramos el contenido del fichero
                BufferedReader br = new BufferedReader(new FileReader(archivo));
                String linea;
                while ((linea = br.readLine()) != null) {
                    System.out.println(linea);  
                }
                br.close();
    
                // Leemos el fichero
                ObjectMapper mapper = new ObjectMapper();
                ControladoraUsuario controladora = null;
                try {
                    controladora = mapper.readValue(archivo, ControladoraUsuario.class);
                } catch (IOException e) {
                    throw new Exception("Error al deserializar el fichero JSON: " + e.getMessage());
                }
    
                if (controladora != null) {
                    String usernameDelFichero = archivo.getName().replace(".json", "");
                    if (!controladora.getUsername().equals(usernameDelFichero)) {
                        throw new Exception("El nombre de usuario dentro del fichero no coincide con el nombre del fichero.");
                    }
                    
                    try {
                        Path destino = Path.of(BASE_PATH + usernameDelFichero + ".json");
                        Files.copy(archivo.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("Archivo copiado a: " + destino.toString());
                    } catch (IOException e) {
                        throw new Exception("Error al copiar el archivo: " + e.getMessage());
                    }
          
                    System.out.println("Usuario cargado: " + controladora.getUsername());
                }
                return controladora;
    
            } catch (IOException e) {
                throw new Exception("Error al leer el fichero: " + e.getMessage());
            }
        } else throw new Exception("El fichero no existe o está vacío: " + filePath);
        
    }
       

    // Funcion usada para escribir en el fichero .json la ControladoraUsuario de un usuario
    public void guardarControladoraUsuario(String username, ControladoraUsuario ctrlUsuario) throws IOException {
        String fitxerPath = BASE_PATH + username + ".json";
        File fitxer = new File(fitxerPath);

        mapper.writeValue(fitxer, ctrlUsuario);
    }

    // Funcion para eliminar todos los ficheros de usuario (restablecer el sistema)
    public void borrarInfo() {
        File directorio = new File(BASE_PATH);

        if (!directorio.exists() || !directorio.isDirectory()) return;

        File[] ficheros = directorio.listFiles();

        if (ficheros != null) {
            for (File f : ficheros) {
                if (f.isFile() && f.getName().endsWith(".json")) 
                    if (f.delete()) System.out.println("Fichero eliminado: " + f.getName());
            }
        }
    }
}
