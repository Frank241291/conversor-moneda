package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.InputMismatchException;
import java.util.Scanner;
import org.json.JSONObject;

public class ConversorMonedas {
    private static final String API_KEY = "a38ed880577e8c1d854387fd";
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";

    public ConversorMonedas() {
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String mensajeBienvenida = "Bienvenido al Conversor de Monedas";
        System.out.println(mensajeBienvenida);

        while(true) {
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
                int opcion = scanner.nextInt();
                if (opcion >= 1 && opcion <= 8) {
                    System.out.print("Ingresa el valor que deseas convertir: ");

                    double cantidad;
                    try {
                        cantidad = scanner.nextDouble();
                    } catch (InputMismatchException var13) {
                        System.out.println("Error: La cantidad ingresada no es válida. Por favor, ingresa un número.");
                        scanner.next();
                        continue;
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
                            continue;
                    }

                    try {
                        double rate = obtenerTasaCambio(fromCurrency, toCurrency);
                        double resultado = cantidad * rate;
                        System.out.println("" + cantidad + " " + fromCurrency + " corresponde a " + resultado + " " + toCurrency + ".");
                    } catch (Exception var12) {
                        System.out.println("Error al obtener la tasa de cambio. Por favor, intenta nuevamente más tarde.");
                    }
                } else {
                    if (opcion == 9) {
                        break;
                    }

                    System.out.println("Opción no válida. Por favor, intenta nuevamente.");
                }
            } catch (InputMismatchException var14) {
                System.out.println("Error: Opción no válida. Por favor, ingresa un número entre 1 y 9.");
                scanner.next();
            }
        }

        System.out.println("Gracias por visitar el conversor de monedas. ¡Adiós!");
        scanner.close();
    }

    private static double obtenerTasaCambio(String fromCurrency, String toCurrency) throws Exception {
        String urlStr = "https://v6.exchangerate-api.com/v6/a38ed880577e8c1d854387fd/pair/" + fromCurrency + "/" + toCurrency;
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");
        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("HTTP error code : " + responseCode);
        } else {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer response = new StringBuffer();

            String inputLine;
            while((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
            JSONObject jsonResponse = new JSONObject(response.toString());
            return jsonResponse.getDouble("conversion_rate");
        }
    }
}