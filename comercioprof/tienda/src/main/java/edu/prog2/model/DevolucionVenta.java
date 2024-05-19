package edu.prog2.model;

import java.time.LocalDateTime;

import org.json.JSONException;
import org.json.JSONObject;

public class DevolucionVenta extends Devolucion {
    private Venta venta;

    public DevolucionVenta(String id, LocalDateTime fechaHora, int cantidad, Producto producto, Venta venta) {
        super(id, fechaHora, cantidad, producto);
        this.venta = venta;
    }

    public DevolucionVenta(DevolucionVenta devolucion) {
        super((Devolucion) devolucion);
        this.producto = devolucion.getProducto();
    }

    public DevolucionVenta(JSONObject json) throws JSONException, Exception {
        super(json);
        this.venta = new Venta(json.getJSONObject("venta"));
    }

    public DevolucionVenta(String id) {
        super(id);
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

}
