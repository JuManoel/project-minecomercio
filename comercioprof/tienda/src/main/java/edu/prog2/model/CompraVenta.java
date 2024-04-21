package edu.prog2.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class CompraVenta extends Transaccion{
    private ArrayList<Detalle> detalles;
    private Vendedor vendedor;

    public CompraVenta() {
        super();
        detalles=new ArrayList<>();
    }

    public CompraVenta(String id, Vendedor vendedor, LocalDateTime fechaHora) throws Exception {
        super(id,fechaHora);
        this.setVendedor(vendedor);
        setDetalles(detalles);
    }


    public CompraVenta(String id, Vendedor vendedor, LocalDateTime fechaHora, ArrayList<Detalle> detalles) throws Exception {
        super(id,fechaHora);
        this.setVendedor(vendedor);
        setDetalles(detalles);
    }

    public CompraVenta(Vendedor vendedor, LocalDateTime localDateTime) throws Exception {
        super();
        setVendedor(vendedor);
        setFechaHora(localDateTime);
        detalles=new ArrayList<>();
    }

    public CompraVenta(JSONObject json) throws JSONException, Exception {
        setId(json.getString("id"));
        setVendedor(new Vendedor(json.getString("vendedor")));
        setFechaHora(LocalDateTime.parse(json.getString("fechaHora")));
        detalles=new ArrayList<>();
        JSONArray jsonArray= json.optJSONArray("detalle");
        for (int i = 0; i < jsonArray.length(); i++) {
            Detalle detalle=new Detalle();
            detalle.setCantidad(jsonArray.getJSONObject(i).getInt("cantidad"));
            detalle.setProducto(new Producto(jsonArray.getJSONObject(i).getString("producto")));
            this.detalles.add(detalle);
        }
        
    }

    public ArrayList<Detalle> getDetalles() {
        return detalles;
    }
    public JSONObject getDetalles(JSONObject json){
        JSONArray jsonArray=new JSONArray(this.detalles);
        json.put("detalle", jsonArray);
        return json;
    }

    public void setDetalles(ArrayList<Detalle> detalles) {
        for (Detalle detalle : detalles) {
            this.detalles.add(detalle);
        }
    }

    public void setVendedor(Vendedor vendedor) throws Exception {
        this.vendedor = new Vendedor(vendedor);
    }
    public Vendedor getVendedor() {
        return vendedor;
    }

    public double getTotal(){
        double total=0;
        for (Detalle detalle : detalles) {
            total+=detalle.getSubTotal();
        }
        return total;
    }
    @Override
    public JSONObject toJSONObject() {
        JSONObject json=super.toJSONObject();
        json.put("vendedor", this.vendedor.getId());
        String detalles="[";
        for (Detalle detalle : this.detalles) {
            detalles+=detalle.toJSONObject().toString();
        }
        detalles+="]";
        json.put("detalle", detalles);
        return json;
    }
    @Override
    public String toString() {
        String str=this.toJSONObject().toString();
        return str;
    }
}
