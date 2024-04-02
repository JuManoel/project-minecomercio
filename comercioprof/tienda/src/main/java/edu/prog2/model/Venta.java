package edu.prog2.model;

import edu.prog2.helpers.Utils;
import java.time.LocalDateTime;
import org.json.JSONObject;

public final class Venta extends Transaccion {

  private Cliente cliente;
  private Vendedor vendedor;

  public Venta() {
    this(Utils.getRandomKey(5), new Cliente(), new Vendedor(), LocalDateTime.parse("2024-01-01T00:00"));
  }

  public Venta(String id) {
    // use el constructor por defecto y luego asigne el ID
  }

  public Venta(String id, Cliente cliente, Vendedor vendedor, LocalDateTime fecha) {
    // Use mutadores para asignar los valores recibidos
  }

  public Venta(Cliente cliente, Vendedor vendedor, LocalDateTime fecha) {
    // use el constructor parametrizado
  }

  public Venta(Venta v) {
    // ...
  }

  public Venta(JSONObject json) {
    // ...
  }

  public Cliente getCliente() {
    return cliente;
  }

  public void setCliente(Cliente cliente) {
    if (cliente == null) {
      throw new NullPointerException("El cliente de una venta no puede ser nulo");
    }
    this.cliente = cliente;
  }

  public Vendedor getVendedor() {
    throw new UnsupportedOperationException("Falta implementar 'getVendedor()'");
  }

  public void setVendedor(Vendedor vendedor) {
    // similar a setCliente()
  }

  @Override
  public String toString() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public JSONObject toJSONObject() {
    return new JSONObject(this);
  }
}
