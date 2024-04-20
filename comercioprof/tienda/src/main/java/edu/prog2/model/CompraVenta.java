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
    }

    public CompraVenta(JSONObject json) throws JSONException, Exception {
        super(json);
        JSONArray jsonArray= json.optJSONArray("detalles");
        for (int i = 0; i < jsonArray.length(); i++) {
            Detalle detalle=new Detalle(jsonArray.getJSONObject(i));
            detalles.add(detalle);
        }

        setVendedor(new Vendedor(json.getJSONObject("vendedor")));
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


}
