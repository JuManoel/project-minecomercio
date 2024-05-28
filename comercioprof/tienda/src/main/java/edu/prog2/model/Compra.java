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
    if (json.get("provedor").getClass().equals(String.class)) {
      this.setProvedor(new Provedor(json.getString("provedor")));
    } else {
      this.setProvedor(new Provedor(json.getJSONObject("provedor")));
    }
  }

  @Override
  public void setVendedor(Vendedor vendedor) throws Exception {
      if(vendedor.getAdmin()){
        super.setVendedor(vendedor);
      }else{
        throw new IllegalArgumentException("EL vendedor tiene que ser adiministrador");
      }
  }

  public Provedor getProvedor() {
    return provedor;
  }

  public void setProvedor(Provedor provedor) throws Exception {
    this.provedor = new Provedor(provedor);
  }

  @Override
  public void setDetalles(ArrayList<Detalle> detalles) {
      super.setDetalles(detalles);
      for (Detalle detalle : this.detalles) {
        detalle.setSubTotal(detalle.getProducto().getValorBase());
      }
  }

  @Override
  public String toString() {
    JSONObject json = this.toJSONObject();
    json.put("vendedor", this.getVendedor().getId());
    return json.toString();
  }

  @Override
  public JSONObject toJSONObject() {
    JSONObject json = super.toJSONObject();
    json.put("provedor", this.provedor.getId());
    return json;
  }
}
