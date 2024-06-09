package edu.prog2.model;

import java.time.LocalDateTime;

import org.json.JSONException;
import org.json.JSONObject;

public class Baja extends Transaccion {
    private Producto producto;
    private TipoBaja tipoBaja;
    private int cantidad;

    public Baja() {
        super();
    }


    public Baja(String id) {
        super(id);
    }


    public Baja(String id, LocalDateTime fechaHora) {
        super(id, fechaHora);
    }


    public Baja(String id, LocalDateTime fechaHora, Producto producto, TipoBaja tipoBaja, int cantidad) {
        super(id, fechaHora);
        this.producto = producto;
        this.tipoBaja = tipoBaja;
        this.cantidad = cantidad;
    }

    public Baja(JSONObject json) throws JSONException, Exception {
        super(json);
        if(json.get("producto").getClass().equals(JSONObject.class)){
            this.producto = new Producto(json.getJSONObject("producto"));
        }else{
            this.producto = new Producto(json.getString("producto"));
        }
        this.tipoBaja = TipoBaja.getEnum(json.getString("tipoBaja"));
        this.cantidad = json.getInt("cantidad");
    }

    public Baja(String id, Producto producto, TipoBaja tipoBaja, int cantidad) {
        super(id);
        this.producto = producto;
        this.tipoBaja = tipoBaja;
        this.cantidad = cantidad;
    }

    public Baja(Baja baja) {
        super((Transaccion) baja);
        this.producto = baja.getProducto();
        this.tipoBaja = baja.getTipoBaja();
        this.cantidad = baja.getCantidad();
    }

    public Baja(Producto producto, TipoBaja tipoBaja) {
        this.producto = producto;
        this.tipoBaja = tipoBaja;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public TipoBaja getTipoBaja() {
        return tipoBaja;
    }

    public void setTipoBaja(TipoBaja tipoBaja) {
        this.tipoBaja = tipoBaja;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject json = super.toJSONObject();
        json.put("producto", this.getProducto().getId());
        json.put("cantidad", this.cantidad);
        json.put("tipoBaja", this.cantidad);
        return json;
    }

}
