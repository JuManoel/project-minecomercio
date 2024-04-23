package edu.prog2.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.prog2.helpers.Utils;

public class Compra extends CompraVenta{
    private Provedor provedor;
    public Compra() throws Exception {
      this(Utils.getRandomKey(5), new Provedor(), new Vendedor(), LocalDateTime.parse("2024-01-01T00:00"), new ArrayList<Detalle>());
    }
  
    public Compra(String id) throws Exception {
      this();
      setId(id);
    }
  
    public Compra(String id, Provedor provedor, Vendedor vendedor, LocalDateTime fecha, ArrayList<Detalle> detalles) throws Exception {
      // Use mutadores para asignar los valores recibidos
      super(id,vendedor,fecha,detalles);
      setProvedor(provedor);
      
  
    }
  
    public Compra(Provedor cliente, Vendedor vendedor, LocalDateTime fecha, ArrayList<Detalle> detalles) throws Exception {
      // use el constructor parametrizado
      super(vendedor,fecha);
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
      this.provedor=new Provedor(json.getJSONObject("provedor"));
    }
  
    public Provedor getProvedor() {
        return provedor;
    }
    public void setProvedor(Provedor provedor) {
        this.provedor = provedor;
    }
  
    @Override
    public String toString() {
      String str=super.toString();
      str+=String.format(
        "Provedor: \n%s\n"+
        "Vendedor: \n%s\n"
      , getProvedor().toString(),getVendedor().toString());
      return str;
    }
  
    @Override
    public JSONObject toJSONObject() {
      return new JSONObject(this);
    }
}
