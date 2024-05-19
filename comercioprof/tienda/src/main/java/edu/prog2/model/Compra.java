package edu.prog2.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import edu.prog2.helpers.Utils;

public class Compra extends CompraVenta {
  private Provedor provedor;

  public Compra() throws Exception {
    this.id = (Utils.getRandomKey(5));
    this.provedor = new Provedor();
    this.detalles = new ArrayList<>();
    this.vendedor = new Vendedor();
    this.fechaHora = LocalDateTime.now();
  }

  public Compra(String id) throws Exception {
    this();
    setId(id);
  }

  public Compra(String id, Provedor provedor, Vendedor vendedor, LocalDateTime fechaHora, ArrayList<Detalle> detalles)
      throws Exception {
    // Use mutadores para asignar los valores recibidos
    super(id, vendedor, fechaHora, detalles);
    setProvedor(provedor);

  }

  public Compra(Provedor provedor, Vendedor vendedor, LocalDateTime fechaHora, ArrayList<Detalle> detalles)
      throws Exception {
    // use el constructor parametrizado
    super(vendedor, fechaHora);
    setProvedor(provedor);
    setDetalles(detalles);
  }

  public Compra(Compra v) throws Exception {
    setProvedor(v.getProvedor());
    setFechaHora(v.getFechaHora());
    setId(v.getId());
    setVendedor(v.getVendedor());
  }

  public Compra(JSONObject json) throws JSONException, Exception {
    super(json);
    System.out.println(json);
    this.provedor = new Provedor(json.getString("provedor"));
  }

  public Provedor getProvedor() {
    return provedor;
  }

  public void setProvedor(Provedor provedor) {
    this.provedor = provedor;
  }

  @Override
  public String toString() {
    String str = super.toString();
    str += String.format(
        "Provedor: \n%s\n" +
            "Vendedor: \n%s\n",
        getProvedor().toString(), getVendedor().toString());
    return str;
  }

  @Override
  public JSONObject toJSONObject() {
    JSONObject json = super.toJSONObject();
    json.put("provedor", this.provedor.getId());
    return json;
  }
}
