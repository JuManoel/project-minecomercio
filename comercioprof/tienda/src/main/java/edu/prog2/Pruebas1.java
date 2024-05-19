package edu.prog2;

import edu.prog2.helpers.Keyboard;
import edu.prog2.helpers.Utils;
import edu.prog2.model.Producto;
import edu.prog2.model.TipoProducto;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;
import org.json.JSONObject;

public final class Pruebas1 {

  static ArrayList<Producto> productos;

  public static void main(String[] args) throws Exception {
    inicializar();

    do {
      try {
        switch (leerOpcion()) {
          case 1 -> probarIgualdadProductos1();
          case 2 -> probarIgualdadProductos2();
          case 3 -> probarIgualdadProductos3();
          case 4 -> probarIgualdadProductos4();
          case 0 -> {
            System.exit(0);
            break;
          }
          default -> System.out.println("Opción inválida");
        }
      } catch (Exception e) {
        // enviar la traza de error a la salida estándar
        e.printStackTrace(System.out);
      }
    } while (true);
  }

  static void inicializar() throws Exception {
    // esencial para estandarizar el formato monetario con separador de punto
    // decimal, no con coma
    Locale.setDefault(Locale.of("es_CO"));
    inicializarProductos();
    // inicializar clientes
    // inicializar proveedores
    // etc., etc., ...
    System.out.println("Inicializados los datos de prueba");
  }

  private static void inicializarProductos() throws Exception {
    productos = new ArrayList<>();
    productos.add(new Producto("Yogurt fresa vaso x 120 ml", TipoProducto.LACTEOS, 2000, 2300, 19, 100,
        LocalDate.parse("2023-06-19")));

    productos.add(new Producto("Yogurt melocotón vaso x 120 ml", TipoProducto.LACTEOS, 2000, 2300, 19, 100,
        LocalDate.parse("2023-06-19")));

    // crear un JSON de producto con todos los datos menos con el ID
    JSONObject jsonObj = new JSONObject("""
        {
            "descripcion": "Yogurt melocotón vaso x 120 ml",
            "tipo": "LACTEOS",
            "valorBase": 2000,
            "valorVenta": 2300,
            "iva": 19,
            "disponible": 100,
            "vencimiento": "2023-06-19"
        }
        """);
    // Agregar el ID al JSONObject
    jsonObj.put("id", Utils.getRandomKey(5));

    // agregar una instancia Java de Producto, con base en un JSONObject
    productos.add(new Producto(jsonObj));
  }

  private static void probarIgualdadProductos1() {
    Producto p0 = productos.get(0);
    Producto px = productos.get(0);
    System.out.printf("Son iguales p0 y px? > %B%n", p0.equals(px));
  }

  @SuppressWarnings("")
  private static void probarIgualdadProductos2() {
    String producto = "Yogurt fresa vaso x 120 ml";
    System.out.printf("Son iguales p0 y px? > %B%n", productos.get(0).equals(producto));
  }

  private static void probarIgualdadProductos3() {
    Producto p1 = productos.get(0);
    Producto p2 = productos.get(1);
    System.out.println(p1);
    System.out.println(p2);
    System.out.printf("Son iguales p1 y p2? > %B%n", p1.equals(p2));
  }

  private static void probarIgualdadProductos4() {
    Producto p1 = productos.get(1);
    Producto p2 = productos.get(2);
    System.out.println(p1);
    System.out.println(p2);
    System.out.printf("Son iguales p1 y p2? > %B%n", p1.equals(p2));
  }

  static int leerOpcion() {
    String opciones = """
        Men\u00fa de opciones:
         1 - Probar referencias iguales (línea 173)
         2 - Probar diferentes tipos (línea 181)
         3 - Probar diferentes tipos (línea 187)
         4 - Probar diferentes tipos (líneas 187 y 189)

        Elija una opci\u00f3n "0" para salir >\u00A0""";

    int opcion = Keyboard.readInt(opciones);
    System.out.println();
    return opcion;
  }
}
