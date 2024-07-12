# Conversor de Monedas

Este proyecto es una aplicación de consola desarrollada en Java que permite realizar conversiones de monedas utilizando tasas de cambio en tiempo real obtenidas desde la API de ExchangeRate.

## Características

- Conversión entre diferentes monedas:
  - Dólar Norteamericano (USD) a Peso Argentino (ARS)
  - Dólar Norteamericano (USD) a Peso Chileno (CLP)
  - Dólar Norteamericano (USD) a Real Brasileño (BRL)
  - Dólar Norteamericano (USD) a Euro (EUR)
  - Peso Argentino (ARS) a Dólar Norteamericano (USD)
  - Peso Chileno (CLP) a Dólar Norteamericano (USD)
  - Real Brasileño (BRL) a Dólar Norteamericano (USD)
  - Euro (EUR) a Dólar Norteamericano (USD)

## Requisitos

- Java 17 o superior
- Dependencia `org.json` para manejar datos JSON

## Configuración del proyecto

### Dependencias

Si no usas un gestor de dependencias como Maven, descarga y añade la biblioteca `org.json` a tu proyecto. Puedes descargarla desde [aquí](https://github.com/stleary/JSON-java).

### Agregar la biblioteca `org.json` en IntelliJ IDEA

1. Descarga el archivo `json.jar` desde el repositorio oficial.
2. Crea una carpeta `lib` en el directorio raíz de tu proyecto y copia `json.jar` en esta carpeta.
3. En IntelliJ IDEA, haz clic derecho en `json.jar` dentro de la carpeta `lib` y selecciona "Add as Library".

## Uso

1. Clona el repositorio a tu máquina local:
    ```sh
    git clone https://github.com/tu_usuario/tu_repositorio.git
    ```
2. Abre el proyecto en IntelliJ IDEA.
3. Ejecuta la clase principal `ConversorMonedas` desde IntelliJ IDEA.

## Código de ejemplo

Aquí tienes un fragmento del código principal:

```java
package com.example.conversormonedas;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.InputMismatchException;
import java.util.Scanner;
import org.json.JSONObject;

public class ConversorMonedas {

    private static final String API_KEY = "TU_CLAVE_API";
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;
        double cantidad, resultado;
        String mensajeBienvenida = "Bienvenido al Conversor de Monedas";
        System.out.println(mensajeBienvenida);

        while (true) {
            System.out.println("\nPor favor, selecciona la opción de conversión:");
            System.out.println("1. Dólar Norteamericano a Peso Argentino");
            System.out.println("2. Dólar Norteamericano a Peso Chileno");
            System.out.println("3. Dólar Norteamericano a Real Brasileño");
            System.out.println("4. Dólar Norteamericano a Euro");
            System.out.println("5. Peso Argentino a Dólar Norteamericano");
            System.out.println("6. Peso Chileno a Dólar Norteamericano");
            System.out.println("7. Real Brasileño a Dólar Norteamericano");
            System.out.println("8. Euro a Dólar Norteamericano");
            System.out.println("9. Salir");
            System.out.print("Elige una opción: ");

            try {
                opcion = scanner.nextInt();

                if (opcion >= 1 && opcion <= 8) {
                    System.out.print("Ingresa el valor que deseas convertir: ");
                    try {
                        cantidad = scanner.nextDouble();
                    } catch (InputMismatchException e) {
                        System.out.println("Error: La cantidad ingresada no es válida. Por favor, ingresa un número.");
                        scanner.next(); // Limpiar la entrada incorrecta
                        continue; // Volver al menú
                    }

                    String fromCurrency = "";
                    String toCurrency = "";

                    switch (opcion) {
                        case 1:
                            fromCurrency = "USD";
                            toCurrency = "ARS";
                            break;
                        case 2:
                            fromCurrency = "USD";
                            toCurrency = "CLP";
                            break;
                        case 3:
                            fromCurrency = "USD";
                            toCurrency = "BRL";
                            break;
                        case 4:
                            fromCurrency = "USD";
                            toCurrency = "EUR";
                            break;
                        case 5:
                            fromCurrency = "ARS";
                            toCurrency = "USD";
                            break;
                        case 6:
                            fromCurrency = "CLP";
                            toCurrency = "USD";
                            break;
                        case 7:
                            fromCurrency = "BRL";
                            toCurrency = "USD";
                            break;
                        case 8:
                            fromCurrency = "EUR";
                            toCurrency = "USD";
                            break;
                        default:
                            System.out.println("Opción no válida. Por favor, intenta nuevamente.");
                            continue; // Volver al menú
                    }

                    try {
                        double rate = obtenerTasaCambio(fromCurrency, toCurrency);
                        resultado = cantidad * rate;
                        System.out.println(cantidad + " " + fromCurrency + " corresponde a " + resultado + " " + toCurrency + ".");
                    } catch (Exception e) {
                        System.out.println("Error al obtener la tasa de cambio. Por favor, intenta nuevamente más tarde.");
                    }

                } else if (opcion == 9) {
                    break;
                } else {
                    System.out.println("Opción no válida. Por favor, intenta nuevamente.");
                    continue; // Volver al menú
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Opción no válida. Por favor, ingresa un número entre 1 y 9.");
                scanner.next(); // Limpiar la entrada incorrecta
            }
        }

        System.out.println("Gracias por visitar el conversor de monedas. ¡Adiós!");
        scanner.close();
    }

    private static double obtenerTasaCambio(String fromCurrency, String toCurrency) throws Exception {
        String urlStr = BASE_URL + API_KEY + "/pair/" + fromCurrency + "/" + toCurrency;
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("HTTP error code : " + responseCode);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JSONObject jsonResponse = new JSONObject(response.toString());
        return jsonResponse.getDouble("conversion_rate");
    }
}