package servidor;

import java.io.*;
import java.net.*;

public class ServidorSocket {
    public static void main(String[] args) {
        int puerto = 12345;
        try (ServerSocket servidor = new ServerSocket(puerto)) {
            System.out.println("Servidor iniciado. Esperando conexiones...");
            while (true) {
                Socket cliente = servidor.accept();
                System.out.println("Cliente conectado: " + cliente.getInetAddress());

                BufferedReader in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                PrintWriter out = new PrintWriter(cliente.getOutputStream(), true);

                // Estado del nombre de usuario generado para cada cliente
                String nombreUsuario = null;

                // Menú inicial
                out.println("Bienvenido al Servidor de Usuarios.");
                out.println("Menú:");
                out.println("1 - Generar nombre de usuario");
                out.println("2 - Generar dirección de correo electrónico");
                out.println("0 - Salir");
                out.println("Ingrese una opción:");

                boolean seguir = true;
                while (seguir) {
                    String opcion = null;

                    // Bucle hasta opción válida
                    while (true) {
                        opcion = in.readLine();
                        if (opcion == null) {
                            seguir = false;
                            break;
                        }
                        if (opcion.equals("1") || opcion.equals("2") || opcion.equals("0")) {
                            break;
                        } else {
                            out.println("Opción inválida. Por favor, ingrese 1, 2 o 0:");
                            out.println("Ingrese una opción:");
                        }
                    }
                    if (!seguir) break;

                    switch (opcion) {
                        case "1":
                            // Generar usuario a partir de nombre completo
                            String usuarioValido = null;
                            while (usuarioValido == null) {
                                out.println("Ingrese su nombre y apellido :");
                                String nombreCompleto = in.readLine();

                                usuarioValido = generarNombreUsuarioDesdeNombreCompleto(nombreCompleto);

                                if (usuarioValido == null) {
                                    out.println("Nombre inválido. Debe tener entre 5 y 20 letras, al menos una vocal y una consonante, y sin números ni caracteres especiales. Intente de nuevo:");
                                } else {
                                    out.println("Nombre de usuario generado: " + usuarioValido);
                                }
                            }
                            nombreUsuario = usuarioValido; // Solo se guarda acá el nombre válido
                            out.println("Ingrese una opción:");
                            break;

                        case "2":
                            if (nombreUsuario == null) {
                                out.println("Primero debe generar su nombre de usuario con la opción 1.");
                                out.println("Ingrese una opción:");
                            } else {
                                String[] dominios = {"@gmail.com", "@hotmail.com"};
                                String dominio = dominios[(int) (Math.random() * dominios.length)];
                                String correo = nombreUsuario.toLowerCase() + dominio;

                                out.println("Dirección de correo generada: " + correo);
                                out.println("Ingrese una opción:");
                            }
                            break;

                        case "0":
                            out.println("Hasta luego!");
                            seguir = false;
                            break;
                    }
                }

                in.close();
                out.close();
                cliente.close();
                System.out.println("Cliente desconectado.");
            }

        } catch (IOException e) {
            System.out.println("Error en el servidor: " + e.getMessage());
        }
    }

    // Procesa nombre y apellido, genera el usuario según la consigna
    public static String generarNombreUsuarioDesdeNombreCompleto(String nombreCompleto) {
        if (nombreCompleto == null) return null;
        nombreCompleto = nombreCompleto.trim();
        nombreCompleto = nombreCompleto.replaceAll("\\s+", " ");
        String[] partes = nombreCompleto.split(" ");
        if (partes.length < 2) return null;

        String nombre = partes[0].trim();
        String apellido = partes[partes.length - 1].trim(); // TOMAR ÚLTIMA PALABRA COMO APELLIDO

        // Validar que ambas partes sean solo letras
        if (!nombre.matches("^[a-zA-Z]+$") || !apellido.matches("^[a-zA-Z]+$")) {
            return null;
        }

        String usuario = nombre + apellido;

        if (usuario.length() < 5 || usuario.length() > 20) return null;
        if (!tieneVocalYConsonante(usuario)) return null;

        return usuario;
    }

    // Chequea que el nombre de usuario tenga al menos una vocal y una consonante
    public static boolean tieneVocalYConsonante(String usuario) {
        String vocales = "aeiouAEIOU";
        String consonantes = "bcdfghjklmnpqrstvwxyzBCDFGHJKLMNPQRSTVWXYZ";
        boolean tieneVocal = false, tieneConsonante = false;
        for (char c : usuario.toCharArray()) {
            if (vocales.indexOf(c) >= 0) tieneVocal = true;
            if (consonantes.indexOf(c) >= 0) tieneConsonante = true;
        }
        return tieneVocal && tieneConsonante;
    }
}
