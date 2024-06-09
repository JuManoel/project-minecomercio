package edu.prog2.model;

import org.json.JSONException;
import org.json.JSONObject;

public class DetalleCompra extends Detalle{

    public DetalleCompra() {
        super();
    }

    public DetalleCompra(Producto producto, int cantidad) throws Exception {
        super(producto, cantidad);
    }

    public DetalleCompra(Detalle detalle) throws Exception {
        super(detalle);
    }

    public DetalleCompra(JSONObject json) throws JSONException, Exception {
        super(json);
    }


    @Override
    public double getSubTotal() {
        return Math.round(super.getSubTotal()*this.getProducto().getValorBase());
    }
}
