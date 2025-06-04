package servidor;

import java.io.*;
import java.net.*;

public class ClienteSocket {
    public static void main(String[] args) {
        String host = "localhost";
        int puerto = 12345;
        try (
            Socket socket = new Socket(host, puerto);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
        ) {
            String linea;
            // Menú inicial
            while ((linea = in.readLine()) != null) {
                System.out.println(linea);
                if (linea.contains("Ingrese una opción:")) break;
            }

            while (true) {
                String opcion = teclado.readLine();
                out.println(opcion);

                if (opcion.equals("1")) {
                    while (true) {
                        String msg = in.readLine();
                        System.out.println(msg);
                        if (msg.startsWith("Nombre de usuario generado:")) {
                            break;
                        }
                        // Solo permitir ingresar nombre DESPUÉS de recibir el prompt
                        if (msg.contains("Intente de nuevo:")) {
                            // Esperar a recibir el siguiente prompt del servidor
                            String prompt = in.readLine();
                            System.out.println(prompt);
                            String nombreCompleto = teclado.readLine();
                            out.println(nombreCompleto);
                        }
                        // Si pide el nombre y apellido, dejar ingresar
                        else if (msg.contains("nombre y apellido")) {
                            String nombreCompleto = teclado.readLine();
                            out.println(nombreCompleto);
                        }
                    }
                    // Espera el menú/prompt nuevamente
                    while ((linea = in.readLine()) != null) {
                        System.out.println(linea);
                        if (linea.contains("Ingrese una opción:") || linea.contains("Hasta luego")) break;
                    }
                    if (linea != null && linea.contains("Hasta luego")) break;

                } else if (opcion.equals("2")) {
                    // Lee la respuesta del servidor (correo generado o error)
                    while ((linea = in.readLine()) != null) {
                        System.out.println(linea);
                        if (linea.contains("Ingrese una opción:") || linea.contains("Hasta luego")) break;
                    }
                    if (linea != null && linea.contains("Hasta luego")) break;

                } else {
                    // Lee respuesta de error y vuelve al menú
                    while ((linea = in.readLine()) != null) {
                        System.out.println(linea);
                        if (linea.contains("Ingrese una opción:") || linea.contains("Hasta luego")) break;
                    }
                    if (linea != null && linea.contains("Hasta luego")) break;
                }
            }
            System.out.println("Cliente desconectado.");
        } catch (IOException e) {
            System.out.println("Error en el cliente: " + e.getMessage());
        }
    }
}
