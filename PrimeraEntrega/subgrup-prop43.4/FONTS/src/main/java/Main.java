import dominio.CjtUsuarios;
import dominio.Usuario;
import dominio.Producto;
import dominio.Pair;

import java.util.HashMap;
import java.util.Locale;//Floats
import java.util.Map;
import java.util.Iterator;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        CjtUsuarios users = new CjtUsuarios();
        Scanner scanner = new Scanner(System.in);
        boolean read = true;

        System.out.println("\nCASOS DE USO A EJECUTAR\n");

        String texto = "";
        while(read){
            users.printUsuarioActual();
            imprimeCasosdeUso();
            texto = scanner.nextLine();
            
            //Casos de uso
            if(texto.equals("1") || texto.equals("Ordenar Estanteria"))  {
                if (users.loggedIn()) {
                    boolean lecturaCorrecta = false;
                    Integer num = -1;
                    while (!lecturaCorrecta) {
                        System.out.println("Inserte 1 para ordenarla mediante Backtracking");
                        System.out.println("Inserte 2 para ordenarla mediante Kruskal");
                        System.out.println("Inserte 3 para ordenarla mediante HillClimbing");
                        String strInt = scanner.nextLine();
                        num = Integer.parseInt(strInt);
                        if (num != 1 && num != 2 && num != 3) lecturaCorrecta = false;
                        else lecturaCorrecta = true;
                    }
                    users.ordenarEstanteria(num);
                }
                else System.out.println("Se debe iniciar sesion");
            }
            else if (texto.equals("2") || texto.equals("Alta Producto")) {
                if (users.loggedIn()) {
                    System.out.println("Primero inserte el nombre del producto: ");
                    String nombreP1 = scanner.nextLine();
                    System.out.println("Ahora inserte la marca del producto: ");
                    String marcaP1 = scanner.nextLine();
                    //crear producto
                    Producto p = new Producto(nombreP1, marcaP1);
                    Integer prod1ID = users.anadirProductoEstanteria(p);
                    if (prod1ID != -1) {
                        HashMap<Integer, Producto> prods = users.getProds();
                        if (prods.size() > 1) {
                            Iterator<Map.Entry<Integer, Producto>> it = prods.entrySet().iterator();
                            while (it.hasNext()) {
                                HashMap.Entry<Integer, Producto> e = it.next();

                                Integer prod2ID = e.getKey();

                                Producto prod2 = e.getValue();
                                String nombreP2 = prod2.getNom();
                                String marcaP2 = prod2.getMarca();

                                if (prod2ID != prod1ID) {
                                    Float sim = -1.0f;
                                    while (sim.equals(-1.0f)) {
                                        scanner.useLocale(Locale.US);//lectura floats
                                        System.out.println("Proporcione un grado de similitud entre 0.0 y 1.0 para " + nombreP1 + " " + marcaP1 + " y " + nombreP2 + " " + marcaP2);
                                        String strSim = scanner.nextLine();
                                        sim = Float.parseFloat(strSim);
                                        if (0.0f <= sim && sim <= 1.0f) {
                                            users.anadirSimilitudProducto(prod1ID, prod2ID, sim);
                                            System.out.println("El nuevo grado de similitud entre " + nombreP1 + " " + marcaP1 + " y " + nombreP2 + " " + marcaP2 + " es "+sim);
                                        }
                                        else {
                                            System.out.println("El grado tiene que estar entre 0.0 y 1.0");
                                            sim = -1.0f;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                else System.out.println("Se debe iniciar sesion");
            }
            else if (texto.equals("3") || texto.equals("Eliminar Producto")) {
                if (users.loggedIn()) {
                    System.out.println("Inserte el nombre del producto a eliminar: ");
                    String nombre = scanner.nextLine();
                    System.out.println("Ahora inserte la marca del producto a eliminar: ");
                    String marca = scanner.nextLine();
                    Producto p = new Producto(nombre, marca);
                    users.eliminarProductoEstanteria(p);
                }
                else System.out.println("Se debe iniciar sesion");
            }
            else if (texto.equals("4") || texto.equals("Modificar un grado de similitud")) {
                if (users.loggedIn()) {
                    System.out.println("Primero inserte el nombre del primer producto:");
                    String nombreP1 = scanner.nextLine();
                    System.out.println("Ahora inserte su marca:");
                    String marcaP1 = scanner.nextLine();
                    
                    Producto p1 = new Producto(nombreP1, marcaP1); 
                    
                    System.out.println("Ahora inserte el nombre  del segundo producto:");
                    String nombreP2 = scanner.nextLine();
                    System.out.println("Ahora inserte su marca:");
                    String marcaP2 = scanner.nextLine();  

                    Producto p2 = new Producto(nombreP2, marcaP2);

                    Pair<Boolean, Boolean> p = users.validarProductos(p1,p2);
            
                    if (p.equals(new Pair<>(true, false))) System.out.println("El producto " + nombreP2 + " " + marcaP2 + " no existe en la estantería");
                    else if (p.equals(new Pair<>(false, true))) System.out.println("El producto " + nombreP1 + " " + marcaP1 + " no existe en la estantería");
                    else if (p.equals(new Pair<>(false, false)))System.out.println("Ninguno de los dos productos existe en la estanteria");
                    else {
                        Float sim = -1.0f;
                        while (sim.equals(-1.0f)) {
                            scanner.useLocale(Locale.US);//lectura floats
                            System.out.println("Proporcione un grado de similitud entre 0.0 y 1.0 para " + nombreP1 + " " + marcaP1 + " y " + nombreP2 + " " + marcaP2);
                            String strSim = scanner.nextLine();
                            sim = Float.parseFloat(strSim);
                            if (0.0f <= sim && sim <= 1.0f) {
                                users.modificarSimilitudProductos(p1, p2, sim);
                                System.out.println("La similitud entre " + nombreP1 + " " + marcaP1 + " y " + nombreP2 + " " + marcaP2 + " se ha modificado correctamente a " + sim + ".");
                            }
                            else sim = -1.0f;
                        }
                    }
                }
                else System.out.println("Se debe iniciar sesion");
            }
            else if (texto.equals("5") || texto.equals("Modificar grados de similitud de un producto")) {

                if (users.loggedIn()) {
                    System.out.println("Primero inserte el nombre producto:");
                    String nombreP1 = scanner.nextLine();
                    System.out.println("Ahora inserte la marca del producto:");
                    String marcaP1 = scanner.nextLine();
                    Producto p1 = new Producto(nombreP1, marcaP1);
                    Integer prod1ID = users.existeProducto(p1);
                    
                    if (prod1ID != -1) {
                        HashMap<Integer, Producto> prods = users.getProds();
                        if (prods.size() > 1) {
                            Iterator<Map.Entry<Integer, Producto>> it = prods.entrySet().iterator();
                            while (it.hasNext()) {
                                HashMap.Entry<Integer, Producto> e = it.next();
                
                                Integer prod2ID = e.getKey();
                
                                Producto p2 = e.getValue();
                                String nombreP2 = p2.getNom();
                                String marcaP2 = p2.getMarca();

                                if (prod2ID != prod1ID) {
                                    Float sim = -1.0f;
                                    while (sim.equals(-1.0f)) {
                                        scanner.useLocale(Locale.US);//lectura floats
                                        System.out.println("Proporcione un grado de similitud entre 0.0 y 1.0 para " + nombreP1 + " " + marcaP1 + " y " + nombreP2 + " " + marcaP2);
                                        String strSim = scanner.nextLine();
                                        sim = Float.parseFloat(strSim);
                                        if (0.0f <= sim && sim <= 1.0f) {
                                            users.modificarSimilitudProductos(p1,p2, sim);
                                            System.out.println("La similitud entre " + nombreP1 + " " + marcaP1 + " y " + nombreP2 + " " + marcaP2 + " se ha modificado correctamente a " + sim + ".");
                                        }
                                        else sim = -1.0f;
                                    }
                                }
                            }
                        }
                    }
                    else {
                        System.out.println("El producto introducido no esta en la estanteria"); 
                    }
                }
                else System.out.println("Se debe iniciar sesion");
            }
            else if (texto.equals("6") || texto.equals("Intercambia posicion productos")) {
                
                if (users.loggedIn()) {
                    System.out.println("Primero inserte el nombre del primer producto:");
                    String nombreP1 = scanner.nextLine();
                    System.out.println("Ahora inserte su marca:");
                    String marcaP1 = scanner.nextLine();
                    Producto p1 = new Producto(nombreP1, marcaP1);

                    System.out.println("Ahora inserte el nombre  del segundo producto:");
                    String nombreP2 = scanner.nextLine();
                    System.out.println("Ahora inserte su marca:");
                    String marcaP2 = scanner.nextLine();
                    Producto p2 = new Producto(nombreP2, marcaP2);

                    if (users.intercambiarProductos(p1,p2)) {
                        System.out.println("Se han intercambiado los productos " + nombreP1 + " " + marcaP1 + " y " + nombreP2 + " " + marcaP2);
                    }
                    //los errores los lanza la controladora
                }
                else System.out.println("Se debe iniciar sesion");
            }
            else if (texto.equals("7") || texto.equals("Consulta grado de similitud productos")) {
                
                if (users.loggedIn()) {
                    System.out.println("Primero inserte el nombre del primer producto:");
                    String nombreP1 = scanner.nextLine();
                    System.out.println("Ahora inserte su marca:");
                    String marcaP1 = scanner.nextLine();
                    Producto p1 = new Producto(nombreP1, marcaP1);

                    System.out.println("Ahora inserte el nombre  del segundo producto:");
                    String nombreP2 = scanner.nextLine();
                    System.out.println("Ahora inserte su marca:");
                    String marcaP2 = scanner.nextLine();
                    Producto p2 = new Producto(nombreP2, marcaP2);

                    Float sim = users.getSimilitudProductos(p1, p2);
                    System.out.println("La similitud entre " + nombreP1 + " " + marcaP1 + " y " + nombreP2 + " " + marcaP2 + " es: " + sim);
                }
                else System.out.println("Se debe iniciar sesion");
            }
            else if (texto.equals("8") || texto.equals("Consulta grados de similitud  de un producto")) {
                
                if (users.loggedIn()) {
                    System.out.println("Inserte el nombre del producto:");
                    String nombre = scanner.nextLine();
                    System.out.println("Ahora inserte su marca:");
                    String marca = scanner.nextLine();
                    Producto p = new Producto(nombre, marca);

                    users.getSimilitudesProducto(p);
                }
                else System.out.println("Se debe iniciar sesion");
            }
            else if (texto.equals("9") || texto.equals("Consulta productos")) {
                
                if (users.loggedIn()) {
                    users.print(); //la controladora printea la estanteria si esta loggeada
                }
                else System.out.println("Se debe iniciar sesion");    
            }
            else if (texto.equals("10") || texto.equals("Existe producto")) {
                
                if (users.loggedIn()) {
                    System.out.println("Inserte el nombre del producto:");
                    String nombreP1 = scanner.nextLine();
                    System.out.println("Inserte la marca del producto:");
                    String marcaP1 = scanner.nextLine();
                    Producto p = new Producto(nombreP1, marcaP1);


                    Integer prod1ID = users.existeProducto(p    );

                    if (prod1ID != -1){
                        System.out.println("El producto introducido esta en la estanteria"); 
                    }else {
                        System.out.println("El producto introducido no esta en la estanteria"); 
                    }
                }
                else System.out.println("Se debe iniciar sesion");  
            }
            else if (texto.equals("11") || texto.equals("Signup")){//OPCIONAL
                System.out.println("usuario: ");
                String username = scanner.nextLine();
                System.out.println("password: ");
                String pswd = scanner.nextLine();
                Usuario usr = new Usuario(username, pswd);
                users.signUp(usr);
            }
            else if (texto.equals("12") || texto.equals("Login")){//OPCIONAL
                System.out.println("usuario: ");
                String username = scanner.nextLine();
                System.out.println("password: ");
                String pswd = scanner.nextLine();
                users.login(username,pswd);
            }
            else if (texto.equals("13") || texto.equals("Logout")){//OPCIONAL
                users.logout();
            }
            else if (texto.equals("14") || texto.equals("Vaciar Estanteria") ) {//OPCIONAL
                if (users.loggedIn()) {
                    users.vaciarEstanteria();
                }
                else System.out.println("Se debe iniciar sesion"); 
            }
            else if (texto.equals("15") || texto.equals("Salir") || texto.equals("Exit")) {
                read = false;
                System.out.println("Saliendo del sistema");
            }
            else {
                System.out.println("Formato incorrecto, introduzca un el nombre o entero que represente el caso de uso a ejecutar");
            }
        }
        scanner.close();
    }

    private static void imprimeCasosdeUso() {
        System.out.println("|   1) Ordenar Estanteria                             |");
        System.out.println("|   2) Alta Producto                                  |");
        System.out.println("|   3) Eliminar Producto                              |");
        System.out.println("|   4) Modificar un grado de similitud                |");
        System.out.println("|   5) Modificar grados de similitud de un producto   |");
        System.out.println("|   6) Intercambia posicion productos                 |");
        System.out.println("|   7) Consulta grado de similitud productos          |");
        System.out.println("|   8) Consulta grados de similitud  de un producto   |");
        System.out.println("|   9) Consulta productos                             |");
        System.out.println("|   10) Existe producto                               |");
        //CASOS DE USO OPCIONALES
        System.out.println("|   11) Signup                                        |");
        System.out.println("|   12) Login                                         |");
        System.out.println("|   13) Logout                                        |");
        System.out.println("|   14) Vaciar Estanteria                             |");
        System.out.println("|   15) Salir                                         |");
        System.out.println("|_____________________________________________________|");
        System.out.println();
    }
}
