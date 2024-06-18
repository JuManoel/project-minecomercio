package edu.prog2.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class DevolucionCompra extends Devolucion {
    private Compra compra;

    public DevolucionCompra(String id, LocalDateTime fechaHora, ArrayList<Detalle> detalles, Compra compra) {
        // constructor con todo
        super(id, fechaHora, detalles, compra);
    }

    public DevolucionCompra(DevolucionCompra devolucion) {
        // copia
        super((Devolucion) devolucion);
    }

    public DevolucionCompra(JSONObject json) throws JSONException, Exception {
        // json
        super(json);
        this.compra = new Compra(json.getJSONObject("compra"));
    }

    public DevolucionCompra(String id) {
        // solo id
        super(id);
    }

    public DevolucionCompra() {
        // por defecto
        super();
    }

    // acessores y modificadores
    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

}
