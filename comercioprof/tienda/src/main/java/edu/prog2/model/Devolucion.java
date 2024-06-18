package edu.prog2.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class Devolucion extends Transaccion {
    // aca tengo mi pecado, la parte de devolucion incompleta
    // las devoluciones no tienen vendedores, clientes y provedores.
    // ya que ellos ya "existen" dentro de compraVenta
    protected ArrayList<Detalle> detalles;
    protected CompraVenta compraVenta;

    public Devolucion() {
        // por defecto
        super();
    }

    public Devolucion(String id, LocalDateTime fechaHora, ArrayList<Detalle> detalles, CompraVenta compraVentas) {
        // constructor con id, fecha, detalle y la compraVenta que referencia
        super(id, fechaHora);
        this.setDetalles(detalles);
        this.setCompraVenta(compraVentas);
    }

    public Devolucion(Devolucion devolucion) {
        // copia
        super((Transaccion) devolucion);
        this.detalles = devolucion.getDetalles();
        this.compraVenta = devolucion.getCompraVenta();
    }

    public Devolucion(String id) {
        // solo id
        super(id);
    }

    public Devolucion(JSONObject json) throws JSONException, Exception {
        // json
        super(json);
        JSONArray jsonArray = json.optJSONArray("detalles");
        // pelo facto de detalles ser una lista, debemos percorrer el jsonArray, y cojer
        // detale por detalle
        for (int i = 0; i < jsonArray.length(); i++) {
            Detalle detalle = new Detalle();
            if (jsonArray.getJSONObject(i).get("producto").getClass().equals(String.class)) {
                detalle.setProducto(new Producto(jsonArray.getJSONObject(i).getString("producto")));
            } else {
                detalle.setProducto(new Producto(jsonArray.getJSONObject(i).getJSONObject("producto")));
            }
            detalle.setCantidad(jsonArray.getJSONObject(i).getInt("cantidad"));
            this.detalles.add(detalle);
        }
    }

    // acesores y modificadores
    public ArrayList<Detalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(ArrayList<Detalle> detalle) {
        this.detalles = new ArrayList<>();
        for (int i = 0; i < detalle.size(); i++) {
            this.detalles.add(detalle.get(i));
        }
    }

    public CompraVenta getCompraVenta() {
        return compraVenta;
    }

    public void setCompraVenta(CompraVenta compraVenta) {
        this.compraVenta = compraVenta;
    }

}
