package edu.prog2.model;

import java.time.LocalDateTime;

import org.json.JSONException;
import org.json.JSONObject;

public class DevolucionCompra extends Devolucion {
    private Compra compra;

    public DevolucionCompra(String id, LocalDateTime fechaHora, int cantidad, Producto producto, Compra compra) {
        super(id, fechaHora, cantidad, producto);
        this.compra = compra;
    }

    public DevolucionCompra(DevolucionCompra devolucion) {
        super((Devolucion) devolucion);
        this.producto = devolucion.getProducto();
    }

    public DevolucionCompra(JSONObject json) throws JSONException, Exception {
        super(json);
        this.compra = new Compra(json.getJSONObject("compra"));
    }

    public DevolucionCompra(String id) {
        super(id);
    }

    public DevolucionCompra() {
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

}
