package edu.prog2.model;

import java.time.LocalDateTime;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class Devolucion extends Transaccion {
    protected int cantidad;
    protected Producto producto;

    public Devolucion() {
    }

    public Devolucion(String id, LocalDateTime fechaHora, int cantidad, Producto producto) {
        super(id, fechaHora);
        this.cantidad = cantidad;
        this.producto = producto;
    }

    public Devolucion(Devolucion devolucion) {
        super((Transaccion) devolucion);
        this.cantidad = devolucion.getCantidad();
        this.producto = devolucion.getProducto();
    }

    public Devolucion(String id) {
        super(id);
    }

    public Devolucion(JSONObject json) throws JSONException, Exception {
        super(json);
        this.cantidad = json.getInt("cantidad");
        this.producto = new Producto(json.getJSONObject("producto"));
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

}
