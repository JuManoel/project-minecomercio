package edu.prog2.model;

import edu.prog2.helpers.Utils;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public final class Venta extends CompraVenta {

  private Cliente cliente;

  public Venta() throws Exception {
    // por defecto
    this(Utils.getRandomKey(5));
    this.cliente = new Cliente();
    this.detalles = new ArrayList<>();
    this.vendedor = new Vendedor();
    this.fechaHora = LocalDateTime.now();
  }

  public Venta(String id) throws Exception {
    // solo id
    setId(id);
  }

  public Venta(String id, Cliente cliente, Vendedor vendedor, LocalDateTime fechaHora, ArrayList<Detalle> detalles)
      throws Exception {
    // Use mutadores para asignar los valores recibidos
    // con todo
    super(id, vendedor, fechaHora, detalles);
    setCliente(cliente);
  }

  public Venta(Cliente cliente, Vendedor vendedor, LocalDateTime fechaHora, ArrayList<Detalle> detalles)
      throws Exception {
    // use el constructor parametrizado
    // sin el id
    super(vendedor, fechaHora);
    setCliente(cliente);
    setDetalles(detalles);
  }

  public Venta(Venta v) throws Exception {
    // copia
    setCliente(v.getCliente());
    setFechaHora(v.getFechaHora());
    setId(v.getId());
    setVendedor(v.getVendedor());
  }

  public Venta(JSONObject json) throws JSONException, Exception {
    // json
    super(json);
    if (json.get("cliente").getClass().equals(String.class)) {
      this.setCliente(new Cliente(json.getString("cliente")));
    } else {
      this.setCliente(new Cliente(json.getJSONObject("cliente")));
    }
  }

  // acessores y modificadores
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
  public void setDetalles(ArrayList<Detalle> detalles) {
    super.setDetalles(detalles);
  }

  @Override
  public String toString() {// to string
    JSONObject json = this.toJSONObject();
    json.put("vendedor", this.getVendedor().getId());
    return json.toString();
  }

  @Override
  public JSONObject toJSONObject() {// to json
    JSONObject json = super.toJSONObject();
    json.put("cliente", this.cliente.getId());
    return json;
  }
}
