package edu.prog2.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import edu.prog2.helpers.Utils;

public class Compra extends CompraVenta {
  private Provedor provedor;

  public Compra() throws Exception {
    // constructor por defecto
    // antes tenia una compreencion mala sobre como funcionaba los archivos json
    // creia que no importaba muchos los atributos que no eran id.
    // entonces tenia esse constructor que serveria para que al menos pudiare genrar
    // todo de una
    // y despues cambiar los id. pero veo que no es asi
    this.id = (Utils.getRandomKey(5));
    this.provedor = new Provedor();
    this.detalles = new ArrayList<>();
    this.vendedor = new Vendedor();
    this.fechaHora = LocalDateTime.now();
  }

  public Compra(String id) throws Exception {
    // constructor con solo id
    this();
    setId(id);
  }

  public Compra(String id, Provedor provedor, Vendedor vendedor, LocalDateTime fechaHora, ArrayList<Detalle> detalles)
      throws Exception {
    // Use mutadores para asignar los valores recibidos
    // constructor con todo
    super(id, vendedor, fechaHora, detalles);
    setProvedor(provedor);

  }

  public Compra(Provedor provedor, Vendedor vendedor, LocalDateTime fechaHora, ArrayList<Detalle> detalles)
      throws Exception {
    // use el constructor parametrizado
    // sin el id
    super(vendedor, fechaHora);
    setProvedor(provedor);
    setDetalles(detalles);
  }

  public Compra(Compra v) throws Exception {
    // constructor copia
    setProvedor(v.getProvedor());
    setFechaHora(v.getFechaHora());
    setId(v.getId());
    setVendedor(v.getVendedor());
  }

  public Compra(JSONObject json) throws JSONException, Exception {
    // con json
    super(json);
    if (json.get("provedor").getClass().equals(String.class)) {
      this.setProvedor(new Provedor(json.getString("provedor")));
    } else {
      this.setProvedor(new Provedor(json.getJSONObject("provedor")));
    }
  }

  // acesores y modificadores.
  // seguindo las reglas donde el vendedor tiene que tener permiso de admin para
  // poder realizar la compra
  @Override
  public void setVendedor(Vendedor vendedor) throws Exception {
    if (vendedor.getAdmin()) {
      super.setVendedor(vendedor);
    } else {
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
  }

  @Override // to string
  public String toString() {
    JSONObject json = this.toJSONObject();
    json.put("vendedor", this.getVendedor().getId());
    return json.toString();
  }

  @Override // to json
  public JSONObject toJSONObject() {
    JSONObject json = super.toJSONObject();
    json.put("provedor", this.provedor.getId());
    return json;
  }
}
