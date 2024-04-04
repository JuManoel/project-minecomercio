package edu.prog2;

import edu.prog2.helpers.Keyboard;
import edu.prog2.model.Cliente;
import edu.prog2.model.Producto;
import edu.prog2.model.TipoProducto;
import edu.prog2.model.Vendedor;
import edu.prog2.services.PersonaService;
import edu.prog2.services.ProductoService;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import org.json.JSONObject;

public final class Pruebas2 {

  /**
   * Esta es una demostración de cómo es posible trasferir
   * toda la lógica de los CRUD a clases xService.
   */

  static ProductoService productos;
  static PersonaService clientes;
  static PersonaService vendedores;

  public static void main(String[] args) throws Exception {
    inicializar();

    do {
      try {
        switch (leerOpcion()) {
          case 1 -> agregarProducto();
          case 2 -> actualizarProducto();
          case 3 -> listarProductos();
          case 4 -> agregarClientes();
          case 5 -> agregarVendedores();
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
    // esencial para estandarizar el formato monetario con separador de punto decimal, no con coma
    Locale.setDefault(Locale.of("es_CO"));
    productos = new ProductoService();
    clientes = new PersonaService(Cliente.class);
    vendedores = new PersonaService(Vendedor.class);

    // --------------------------------------
    // inicializar proveedores
    // etc., etc., ...
    System.out.println("Inicializadas las instancias xService");
  }

  private static void agregarProducto() throws Exception {
    JSONObject json = new JSONObject()
      .put("descripcion", Keyboard.readString("Descripción: "))
      .put("tipo", Keyboard.readEnum(TipoProducto.class, "Seleccione el tipo"))
      .put("valorBase", Keyboard.readDouble("Valor base: "))
      .put("valorVenta", Keyboard.readDouble("Valor venta: "))
      .put("iva", Keyboard.readDouble("IVA: "))
      .put("disponible", Keyboard.readInt("Disponibles: "))
      .put("vencimiento", Keyboard.readDate("Vence (AAAA-MM-DD): ").toString());

    productos.add(json.toString());
    System.out.println("Producto agregado a la lista ./data/Producto.json\n");
  }

  private static void actualizarProducto() throws Exception {
    // importante:
    // - se permite cambiar algunos valores, no necesariamente todos
    // - no se permite cambiar el ID de un producto
    String id = Keyboard.readString("ID del producto a actualizar: ");
    JSONObject json = new JSONObject()
      .put("descripcion", Keyboard.readString("Descripción: "))
      .put("valorBase", Keyboard.readDouble("Valor base: "))
      .put("valorVenta", Keyboard.readDouble("Valor venta: "))
      .put("iva", Keyboard.readDouble("IVA: "))
      .put("disponible", Keyboard.readInt("Disponibles: "))
      .put("vencimiento", Keyboard.readDate("Vence (AAAA-MM-DD): ").toString());

    json = productos.update(id, json.toString());
    System.out.printf("El producto %s fue actualizado con los siguientes datos: %n", id);
    System.out.println(json);
  }

  private static void listarProductos() throws IOException {
    List<Producto> list = productos.load();
    for (Producto producto : list) {
      System.out.println(producto);
    }
  }

  private static void agregarClientes() throws Exception {
    JSONObject json = new JSONObject().put("nombre", Keyboard.readString("Nombre: ")).put("correo", Keyboard.readString("Correo: ")).put("telefono", Keyboard.readString("Teléfono: ")).put("password", "123").put("credito", Keyboard.readBoolean("Credito (S/N): "));

    clientes.add(json.toString());
    System.out.println("Cliente agregado a la lista ./data/Cliente.json\n");
  }

  private static void agregarVendedores() throws Exception {
    JSONObject json = new JSONObject().put("nombre", Keyboard.readString("Nombre: ")).put("correo", Keyboard.readString("Correo: ")).put("telefono", Keyboard.readString("Teléfono: ")).put("password", "123").put("administrador", Keyboard.readBoolean("Administrador (S/N): "));

    vendedores.add(json.toString());
    System.out.println("Vendedor agregado a la lista ./data/Vendedor.json\n");
  }

  static int leerOpcion() {
    String opciones = """
            Men\u00fa de opciones:
             1 - Agregar un producto
             2 - Actualizar un producto
             3 - Listar productos
             4 - Agregar clientes
             5 - Agregar vendedores

            Elija una opci\u00f3n "0" para salir >\u00A0""";

    int opcion = Keyboard.readInt(opciones);
    System.out.println();
    return opcion;
  }
}
