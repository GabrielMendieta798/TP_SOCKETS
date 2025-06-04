import socket

def main():
    host = "localhost"
    puerto = 12345

    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.connect((host, puerto))

        def leer_hasta_opcion():
            while True:
                data = s.recv(1024).decode()
                print(data, end='')
                if "Ingrese una opción:" in data or "Hasta luego" in data:
                    break

        leer_hasta_opcion()

        while True:
            opcion = input()
            s.sendall((opcion + "\n").encode())

            if opcion == "1":
                while True:
                    msg = s.recv(1024).decode()
                    print(msg, end='')
                    # Si el nombre fue generado, termina el ciclo
                    if msg.startswith("Nombre de usuario generado:"):
                        break
                    # Si da error, esperar el siguiente prompt antes de dejar ingresar
                    if "Intente de nuevo:" in msg:
                        prompt = s.recv(1024).decode()
                        print(prompt, end='')
                        nombre_completo = input()
                        s.sendall((nombre_completo + "\n").encode())
                    elif "nombre y apellido" in msg:
                        nombre_completo = input()
                        s.sendall((nombre_completo + "\n").encode())
                # Espera el menú/prompt nuevamente
                leer_hasta_opcion()

            elif opcion == "2":
                while True:
                    msg = s.recv(1024).decode()
                    print(msg, end='')
                    if "Ingrese una opción:" in msg or "Hasta luego" in msg:
                        break
                if "Hasta luego" in msg:
                    break

            else:
                while True:
                    msg = s.recv(1024).decode()
                    print(msg, end='')
                    if "Ingrese una opción:" in msg or "Hasta luego" in msg:
                        break
                if "Hasta luego" in msg:
                    break

        print("Cliente desconectado.")

if __name__ == "__main__":
    main()
