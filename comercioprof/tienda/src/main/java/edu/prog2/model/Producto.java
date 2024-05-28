package edu.prog2.model;

import edu.prog2.helpers.Utils;
import java.time.LocalDate;

import org.json.JSONException;
import org.json.JSONObject;

public final class Producto implements Format {

  private String id;
  private String descripcion;
  private TipoProducto tipo;
  private double valorBase;
  private double valorVenta;
  private double iva;
  private int disponible;
  private LocalDate vencimiento;

  public Producto() throws Exception {
    this(Utils.getRandomKey(5), "Producto sin descripcion", TipoProducto.OTROS, 0, 0, 0, 0, LocalDate.now());
  }

  public Producto(String id) throws Exception { // nuevo constructor
    this();
    setId(id);
  }

  public Producto(String id, String descripcion, TipoProducto tipo, double valorBase, double valorVenta, double iva,
      int disponible, LocalDate vencimiento) throws Exception {
    if (valorBase > valorVenta) {
      throw new IllegalArgumentException("Valor base tiene que ser menor que valor venta");
    }
    setId(id);
    setDescripcion(descripcion);
    setTipo(tipo);
    setValorBase(valorBase);
    setValorVenta(valorVenta);
    setIva(iva);
    setDisponible(disponible);
    setVencimiento(vencimiento);
  }

  public Producto(String descripcion, TipoProducto tipo, double valorBase, double valorVenta, double iva,
      int disponible, LocalDate vencimiento) throws Exception {
    this(Utils.getRandomKey(5), descripcion, tipo, valorBase, valorVenta, iva, disponible, vencimiento);
  }

  public Producto(Producto p) throws Exception {
    this(p.id, p.descripcion, p.tipo, p.valorBase, p.valorVenta, p.iva, p.disponible, p.vencimiento);
  }

  public Producto(JSONObject json) throws JSONException, Exception { // hacer ensayos de Json a Java
    this(json.getString("id"), json.getString("descripcion"), TipoProducto.valueOf(json.optString("tipo")),
        json.getDouble("valorBase"), json.getDouble("valorVenta"), json.getDouble("iva"), json.getInt("disponible"),
        LocalDate.parse(json.getString("vencimiento"))); // transforma de string a localdatetime
  }

  public String getId() {
    return id;
  }

  public void setId(String id) throws Exception {
    if (id == null || id.isBlank()) {
      throw new NullPointerException("El ID de un producto no puede ser un valor nulo, vacío o en blanco");
    }
    this.id = id;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    descripcion = descripcion.trim(); // <-- IMPORTANTE
    if (descripcion == null || descripcion.isBlank()) {
      throw new NullPointerException("El descripcion de un producto no puede ser un valor nulo, vacío o en blanco");
    }
    this.descripcion = descripcion;
  }

  public TipoProducto getTipo() {
    return tipo;
  }

  public void setTipo(TipoProducto tipo) {
    if (tipo == null) {
      throw new NullPointerException("El tipo de un producto no puede ser un valor nulo");
    }
    this.tipo = tipo;
  }

  public double getValorBase() {
    return valorBase;
  }

  public void setValorBase(double valorBase) {
    if (valorBase < 0) {
      throw new IllegalArgumentException("Se espera un valor igual o superior 0.0");
    }
    this.valorBase = valorBase;
  }

  public double getValorVenta() {
    return valorVenta;
  }

  public void setValorVenta(double valorVenta) {
    if (valorVenta < 0) {
      throw new IllegalArgumentException("Se espera un valor igual o superior 0.0");
    }
    this.valorVenta = valorVenta;
  }

  public double getIva() {
    return iva;
  }

  public void setIva(double iva) {
    if (iva <= 40 && iva >= 0) {
      this.iva = iva;
    } else {
      throw new IllegalArgumentException("El iva generalmente no pasa del 40% y no es menor que 0%");
    }

  }

  public int getDisponible() {
    return disponible;
  }

  public void setDisponible(int disponible) {
    if (disponible < 0) {
      throw new IllegalArgumentException("Se espera un valor igual o superior a 0");
    }
    this.disponible = disponible;
  }

  public LocalDate getVencimiento() {
    return vencimiento;
  }

  public void setVencimiento(LocalDate vencimiento) {
    if (vencimiento.isBefore(LocalDate.parse("2022-01-01"))) {
      throw new IllegalArgumentException("Fecha de vencimiento inválida");
    }
    this.vencimiento = vencimiento;
  }

  public double getSubtotal() {
    return disponible * valorBase;
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    // las referencias this y obj apuntan a la misma instancia
    if (this == obj) {
      return true;
    }

    if (obj == null) {
      return false;
    }

    if (getClass() != obj.getClass()) {
      return false;
    }

    final Producto other = (Producto) obj;

    String message;
    if (!id.equals(other.id) && (descripcion.equalsIgnoreCase(other.descripcion))) {
      message = String.format("Error. Los productos con ID %s y %s tienen la descripción igual: \"%s\"", id, other.id,
          descripcion);
      throw new IllegalArgumentException(message);
    }

    return (id.equals(other.id) || (descripcion.equalsIgnoreCase(other.descripcion)));
  }

  @Override
  public String toString() {
    return String.format("%-6s%-30s%-20s%10.0f%5d%10.0f%12s\n", id, descripcion, tipo, valorBase, disponible,
        getSubtotal(), vencimiento);
  }

  @Override
  public JSONObject toJSONObject() {
    return new JSONObject(this);
  }
}
