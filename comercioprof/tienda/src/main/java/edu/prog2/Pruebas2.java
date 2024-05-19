package edu.prog2;

import edu.prog2.helpers.Keyboard;
import edu.prog2.model.Cliente;
import edu.prog2.model.Detalle;
import edu.prog2.model.Persona;
import edu.prog2.model.Producto;
import edu.prog2.model.Provedor;
import edu.prog2.model.TipoProducto;
import edu.prog2.model.Vendedor;
import edu.prog2.services.PersonaService;
import edu.prog2.services.ProductoService;
import edu.prog2.services.CompraVentaService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

public final class Pruebas2 {

  /**
   * Esta es una demostración de cómo es posible trasferir
   * toda la lógica de los CRUD a clases xService.
   */

  static ProductoService productos;
  static PersonaService clientes;
  static PersonaService vendedores;
  static PersonaService proveedores;
  static CompraVentaService ventas;
  static CompraVentaService compras;

  public static void main(String[] args) throws Exception {
    inicializar();

    do {
      try {
        switch (leerOpcion()) {
          case 1 -> agregarProducto();
          case 2 -> actualizarProducto();
          case 3 -> listarProductos();
          case 4 -> agregarClientes();
          case 5 -> agregarProveedores();
          case 6 -> agregarVendedores();
          case 7 -> agregarVentas();
          case 8 -> agregarCompras();
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
    productos = new ProductoService();
    clientes = new PersonaService(Cliente.class);
    vendedores = new PersonaService(Vendedor.class);
    proveedores = new PersonaService(Provedor.class);
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
        // .put("valorBase", Keyboard.readDouble("Valor base: "))
        // .put("valorVenta", Keyboard.readDouble("Valor venta: "))
        // .put("iva", Keyboard.readDouble("IVA: "))
        // .put("disponible", Keyboard.readInt("Disponibles: "))
        .put("vencimiento", Keyboard.readDate("Vence (AAAA-MM-DD): ").toString());

    json = productos.update(id, json.toString());
    System.out.printf("El producto %s fue actualizado con los siguientes datos: %n", id);
    System.out.println(json);
  }

  private static void listarProductos() throws JSONException, Exception {
    List<Producto> list = productos.load();
    for (Producto producto : list) {
      System.out.println(producto);
    }
  }

  private static void agregarClientes() throws Exception {
    JSONObject json = new JSONObject().put("nombre", Keyboard.readString("Nombre: "))
        .put("correo", Keyboard.readString("Correo: ")).put("telefono", Keyboard.readString("Teléfono: "))
        .put("password", "123").put("credito", Keyboard.readBoolean("Credito (S/N): "));

    clientes.add(json.toString());
    System.out.println("Cliente agregado a la lista ./data/Cliente.json\n");
  }

  private static void agregarVendedores() throws Exception {
    JSONObject json = new JSONObject().put("nombre", Keyboard.readString("Nombre: "))
        .put("correo", Keyboard.readString("Correo: ")).put("telefono", Keyboard.readString("Teléfono: "))
        .put("password", "123").put("administrador", Keyboard.readBoolean("Administrador (S/N): "));

    vendedores.add(json.toString());
    System.out.println("Vendedor agregado a la lista ./data/Vendedor.json\n");
  }

  private static void agregarProveedores() throws Exception {
    JSONObject json = new JSONObject().put("nombre", Keyboard.readString("Nombre: "))
        .put("correo", Keyboard.readString("Correo: ")).put("telefono", Keyboard.readString("Teléfono: "))
        .put("password", "123").put("intermediario", Keyboard.readBoolean("Proveedor (S/N): "));

    proveedores.add(json.toString());
    System.out.println("Proveedor agregado a la lista ./data/Proveedor.json\n");
  }

  private static void agregarVentas() throws Exception {
    List<Persona> listC = clientes.load();
    List<Persona> listV = vendedores.load();
    List<Producto> listP = productos.load();
    Cliente cliente;
    Vendedor vendedor;
    Producto producto;
    ArrayList<Detalle> detalle = new ArrayList<>();
    int auxFor = 0;
    int cantidad = 0;
    LocalDate fecha;
    ventas = new CompraVentaService(Cliente.class);
    for (int i = 0; i < listC.size(); i++) {
      System.out.println("------" + i + "-----");
      System.out.println(listC.get(i));
    }
    cliente = (Cliente) listC.get(Keyboard.readInt("Selecione el Cliente (Index) "));
    for (int i = 0; i < listV.size(); i++) {
      System.out.println("------" + i + "-----");
      System.out.println(listV.get(i));
    }
    vendedor = (Vendedor) listV.get(Keyboard.readInt("Selecione el Vendedor (Index) "));
    ventas = new CompraVentaService(Cliente.class);
    auxFor = Keyboard.readInt("Cuantos Productos vas a Compra? ");
    for (int i = 0; i < auxFor; i++) {
      for (int j = 0; j < listP.size(); j++) {
        System.out.println("------" + j + "-----");
        System.out.println(listP.get(j));
      }
      producto = listP.get(Keyboard.readInt("Selecione El Index "));
      cantidad = Keyboard.readInt("Cuantos vas a Comprar? ");
      if (cantidad > producto.getDisponible()) {
        throw new Exception("Quieres comprar mas de lo q puedes ");
      }
      detalle.add(new Detalle(producto, cantidad));
      // producto.setDisponible(producto.getDisponible()-cantidad);
      productos.update(producto.getId(), producto.toJSONObject().toString());
      listP = productos.load();

    }
    fecha = Keyboard.readDate("Comprarste en (AAAA-MM-DD): ");
    String arrayJson = "";
    for (int i = 0; i < detalle.size(); i++) {
      if (i == detalle.size() - 1) {
        arrayJson += detalle.get(i).toJSONObject().toString();
      } else {
        arrayJson += detalle.get(i).toJSONObject().toString() + ",";
      }
    }
    String venta = """
        {
            "fechaHora": "%s",
            "cliente": "%s",
            "vendedor": "%s",
            "detalle": [
              %s
              ]
        }
        """;
    String jsonVenta = String.format(venta, fecha.atStartOfDay().toString(), cliente.getId(), vendedor.getId(),
        arrayJson);
    JSONObject json = new JSONObject(jsonVenta);
    System.out.println(json);
    json = ventas.add(json.toString());
    System.out.println("Venta agregada a la lista ./data/Venta.json:\n");
    System.out.println(json.toString(2));
  }

  private static void agregarCompras() throws Exception {
    // *** por supuesto los datos de prueba deben estar disponibles ***
    // imagino q vendedor y provedor sean solo los IDs
    List<Persona> listPro = proveedores.load();
    List<Persona> listV = vendedores.load();
    List<Producto> listP = productos.load();
    Provedor provedor;
    Vendedor vendedor;
    Producto producto;
    ArrayList<Detalle> detalle = new ArrayList<>();
    int auxFor = 0;
    int cantidad = 0;
    LocalDate fecha;
    int index = 0;// caso yo adicione productos nuevos, voy cojer ellos en el final de mi json
    for (int i = 0; i < listPro.size(); i++) {
      System.out.println("------" + i + "-----");
      System.out.println(listPro.get(i));
    }
    provedor = (Provedor) listPro.get(Keyboard.readInt("Selecione el Provedor (Index) "));
    for (int i = 0; i < listV.size(); i++) {
      System.out.println("------" + i + "-----");
      System.out.println(listV.get(i));
    }
    vendedor = (Vendedor) listV.get(Keyboard.readInt("Selecione el Vendedor (Index) "));
    compras = new CompraVentaService(Provedor.class);
    auxFor = Keyboard.readInt("Cuantos Productos vas a Compra: ");
    for (int i = 0; i < auxFor; i++) {
      if (Keyboard.readBoolean("Va a adicionar un nuevo producto?")) {
        agregarProducto();
        Producto p = new Producto(productos.get(productos.load().size() - 1));
        int tot = p.getDisponible();
        p.setDisponible(0);
        productos.update(p.getId(), p.toJSONObject().toString());
        detalle.add(new Detalle(p, tot));
        index++;
      } else if (Keyboard.readBoolean("Va a Llenar el estoque de un producto?")) {
        for (int j = 0; j < listP.size() - index; j++) {
          System.out.println("------" + j + "-----");
          System.out.println(listP.get(j));
        }
        producto = listP.get(Keyboard.readInt("Selecione El Index "));
        cantidad = Keyboard.readInt(1, Integer.MAX_VALUE, "Cuantos vas a Comprar? ");
        detalle.add(new Detalle(producto, cantidad));
        listP = productos.load();
      } else {
        throw new Exception("No sabe que hacer?");
      }

    }
    fecha = Keyboard.readDate("Comprarste en (AAAA-MM-DD): ");
    String arrayJson = "";
    for (int i = 0; i < detalle.size(); i++) {
      if (i == detalle.size() - 1) {
        arrayJson += detalle.get(i).toJSONObject().toString();
      } else {
        arrayJson += detalle.get(i).toJSONObject().toString() + ",";
      }
    }
    String venta = """
        {
            "fechaHora": "%s",
            "provedor": "%s",
            "vendedor": "%s",
            "detalle": [
              %s
              ]
        }
        """;
    String jsonVenta = String.format(venta, fecha.atStartOfDay().toString(), provedor.getId(), vendedor.getId(),
        arrayJson);
    JSONObject json = new JSONObject(jsonVenta);
    json = compras.add(json.toString());
    System.out.println("compra agregada a la lista ./data/Compra.json:\n");
    System.out.println(json.toString(2));
  }

  static int leerOpcion() throws Exception {
    String opciones = """
        Men\u00fa de opciones:
         1 - Agregar un producto
         2 - Actualizar un producto
         3 - Listar productos
         4 - Agregar clientes
         5 - Agregar proveedores
         6 - Agregar vendedores
         7 - Agregar venta
         8 - Agregar compras

        Elija una opci\u00f3n "0" para salir >\u00A0""";

    int opcion = Keyboard.readInt(opciones);
    System.out.println();
    return opcion;
  }
}
