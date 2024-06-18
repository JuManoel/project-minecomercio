package edu.prog2.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class CompraVenta extends Transaccion {
    protected ArrayList<Detalle> detalles;
    protected Vendedor vendedor;

    // la clase CompraVenta es el padre de las classes de compra y venta.
    // ella existe debido la cantidad de atributos que ambas compartem
    public CompraVenta(String id) {

        // solo id
        super();
        setId(id);
    }

    public CompraVenta() {

        // por defecto
        super();
        detalles = new ArrayList<>();
    }

    public CompraVenta(String id, Vendedor vendedor, LocalDateTime fechaHora) throws Exception {
        // sin detalle
        super(id, fechaHora);
        this.setVendedor(vendedor);
        detalles = new ArrayList<>();

    }

    public CompraVenta(String id, Vendedor vendedor, LocalDateTime fechaHora, ArrayList<Detalle> detalles)
            throws Exception {
        // solo if, fecha, vendedor y detalles
        super(id, fechaHora);
        this.setVendedor(vendedor);
        setDetalles(detalles);
    }

    public CompraVenta(Vendedor vendedor, LocalDateTime localDateTime) throws Exception {
        // solo vendedor y fecha
        this();
        setVendedor(vendedor);
        setFechaHora(localDateTime);
        detalles = new ArrayList<>();
    }

    public CompraVenta(JSONObject json) throws JSONException, Exception {
        // constructor con json
        setId(json.getString("id"));
        if (json.get("vendedor").getClass().equals(String.class)) {
            setVendedor(new Vendedor(json.getString("vendedor")));
        } else {
            setVendedor(new Vendedor(json.getJSONObject("vendedor")));
        }
        setFechaHora(LocalDateTime.parse(json.getString("fechaHora")));
        detalles = new ArrayList<>();
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

    // acessores y modificadores
    public ArrayList<Detalle> getDetalles() {
        return detalles;
    }

    public JSONObject getDetalles(JSONObject json) {
        JSONArray jsonArray = new JSONArray(this.detalles);
        json.put("detalle", jsonArray);
        return json;
    }

    public void setDetalles(ArrayList<Detalle> detalles) {
        this.detalles = new ArrayList<>();
        for (int i = 0; i < detalles.size(); i++) {
            this.detalles.add(detalles.get(i));
        }
    }

    public void setVendedor(Vendedor vendedor) throws Exception {
        this.vendedor = new Vendedor(vendedor);
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    // metodo getTotal, el percorre los detalles y suma todos los subtotales
    public double getTotal() {
        double total = 0;
        for (int i = 0; i < this.detalles.size(); i++) {
            total += detalles.get(i).getSubTotal();
        }
        return total;
    }

    @Override // tojson
    public JSONObject toJSONObject() {
        JSONObject json = super.toJSONObject();
        json.put("vendedor", this.vendedor.getId());
        json.put("detalles", detalles);
        return json;
    }

    @Override // to string
    public String toString() {
        String str = this.toJSONObject().toString();
        return str;
    }
}
