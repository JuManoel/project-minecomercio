package edu.prog2.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class DevolucionVenta extends Devolucion {
    private Venta venta;

    public DevolucionVenta(String id, LocalDateTime fechaHora, ArrayList<Detalle> detalles, Venta venta) {
        // con todo
        super(id, fechaHora, detalles, venta);
        this.venta = venta;
    }

    public DevolucionVenta(DevolucionVenta devolucion) {
        // copia
        super((Devolucion) devolucion);
    }

    public DevolucionVenta(JSONObject json) throws JSONException, Exception {
        // json
        super(json);
        this.venta = new Venta(json.getJSONObject("venta"));
    }

    public DevolucionVenta(String id) {
        // solo id
        super(id);
    }

    // acessores y modificadores
    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

}
