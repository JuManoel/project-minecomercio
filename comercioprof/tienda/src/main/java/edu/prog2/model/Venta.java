package edu.prog2.model;

import edu.prog2.helpers.Utils;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public final class Venta extends CompraVenta {

  private Cliente cliente;

  public Venta() throws Exception {
    this(Utils.getRandomKey(5), new Cliente(), new Vendedor(), LocalDateTime.parse("2024-01-01T00:00"), new ArrayList<Detalle>());
  }

  public Venta(String id) throws Exception {
    this();
    setId(id);
  }

  public Venta(String id, Cliente cliente, Vendedor vendedor, LocalDateTime fecha, ArrayList<Detalle> detalles) throws Exception {
    // Use mutadores para asignar los valores recibidos
    super(id,vendedor,fecha,detalles);
    

  }

  public Venta(Cliente cliente, Vendedor vendedor, LocalDateTime fecha, ArrayList<Detalle> detalles) throws Exception {
    // use el constructor parametrizado
    super(vendedor,fecha);
    setCliente(cliente);
    setDetalles(detalles);
  }

  public Venta(Venta v) throws Exception {
    setCliente(v.getCliente());
    setFechaHora(v.getFechaHora());
    setId(v.getId());
    setVendedor(v.getVendedor());
  }

  public Venta(JSONObject json) throws JSONException, Exception {
    super(json);
    this.cliente=new Cliente(json.getJSONObject("cliente"));
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
