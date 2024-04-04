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
    this();
    setId(id);
  }

  public Venta(String id, Cliente cliente, Vendedor vendedor, LocalDateTime fecha) {
    // Use mutadores para asignar los valores recibidos
    setCliente(cliente);
    setFechaHora(fecha);
    setId(id);
    setVendedor(vendedor);

  }

  public Venta(Cliente cliente, Vendedor vendedor, LocalDateTime fecha) {
    // use el constructor parametrizado
    this();
    setCliente(cliente);
    setFechaHora(fecha);
    setVendedor(vendedor);
  }

  public Venta(Venta v) {
    setCliente(v.getCliente());
    setFechaHora(v.getFechaHora());
    setId(v.getId());
    setVendedor(v.getVendedor());
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
    return this.vendedor;
  }

  public void setVendedor(Vendedor vendedor) {
    this.vendedor=new Vendedor(vendedor);
  }

  @Override
  public String toString() {
    String str=super.toString();
    str+=String.format(
      "Cliente: \n%s\n"+
      "Vendedor: \n%s\n"
    , getCliente().toString(),getVendedor().toString());
    return str;
  }

  @Override
  public JSONObject toJSONObject() {
    return new JSONObject(this);
  }
}
