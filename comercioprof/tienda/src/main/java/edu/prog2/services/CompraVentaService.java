package edu.prog2.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.prog2.helpers.Utils;
import edu.prog2.model.*;

public class CompraVentaService extends TransaccionService {

    private Class<? extends Persona> receptor;
    // private final Class<? extends CompraVenta> clase;
    private String compraVenta;

    public CompraVentaService(Class<? extends Persona> receptor) throws Exception {
        this.receptor = receptor;
        this.productoService = new ProductoService();
        if (this.receptor.equals(Cliente.class)) {
            compraVenta = "Venta";
            this.clase = Venta.class;
        } else if (this.receptor.equals(Provedor.class)) {
            compraVenta = "Compra";
            this.clase = Compra.class;
        } else {
            throw new Exception("Intentas crear una cosa que no existe");
        }
        fileName = Utils.PATH + compraVenta + ".json";
        if (Utils.fileExists(fileName)) {
            load();
        } else {
            list = new ArrayList<>();
        }
    }

    @Override
    public JSONObject add(String strJson) throws Exception {
        CompraVenta cv = creatCompraVenta(strJson);
        list.add(cv);
        Utils.writeJSON(list, fileName);
        return new JSONObject().put("message", "ok").put("data", cv.toJSONObject());
    }

    @Override
    public JSONObject get(String id) throws Exception {
        CompraVenta compraVenta = (CompraVenta) this.clase.getConstructor(String.class).newInstance(id);
        int i = list.indexOf(compraVenta);
        return i > -1 ? get(i) : null;
    }

    @Override
    public CompraVenta getItem(String id) throws Exception {
        CompraVenta cv = (CompraVenta) super.getItem(id);
        
        if (this.compraVenta.equals("Venta")) {
            return (Venta) cv;
        }
        return (Compra) cv;
    }

    @Override
    public List<Transaccion> load() throws Exception {
        this.productoService = new ProductoService();
        list = new ArrayList<>();
        String data = Utils.readText(fileName);
        JSONArray jsonArr = new JSONArray(data);
        CompraVenta cv;
        for (int i = 0; i < jsonArr.length(); i++) {
            JSONObject jsonObj = jsonArr.getJSONObject(i);
            if(this.compraVenta.toLowerCase().equals("compra")){
                cv = new Compra(jsonObj);
            } else {
                cv = new Venta(jsonObj);


            }
            list.add(cv);
        }
        /* */
        return list;
    }

    private CompraVenta creatCompraVenta(String strJson) throws Exception {
        int sumRest=0;
        Class<? extends Detalle> tipoD;
        if(this.compraVenta.equals("Venta")){
            sumRest=-1;
            tipoD =DetalleVenta.class;
        }else{
            sumRest=1;
            tipoD = DetalleCompra.class;
        }
        productoService = new ProductoService();// actualiza los productos
        CompraVenta cv;
        JSONObject json = new JSONObject(strJson);
        LocalDateTime ldt = LocalDateTime.parse(json.getString("fechaHora"));
        ArrayList<Detalle> deta = new ArrayList<>();
        Vendedor vendedor = new Vendedor((new PersonaService(Vendedor.class)).get(json.getString("vendedor")));
        Persona receptor = this.receptor.getConstructor(JSONObject.class)
        .newInstance((new PersonaService(this.receptor))
        .get(json.getString(this.receptor.getSimpleName().toLowerCase())));
        JSONArray array = json.getJSONArray("detalles");
        for (int i = 0; i < array.length(); i++) {
            Producto pro = new Producto(productoService.get(array.getJSONObject(i).getString("producto")));
            pro.setDisponible(pro.getDisponible()+(array.getJSONObject(i).getInt("cantidad")*sumRest));
            Detalle d = tipoD.getConstructor(Producto.class,int.class).newInstance(pro, array.getJSONObject(i).getInt("cantidad"));
            deta.add(d);
            productoService.update(pro.getId(), pro.toJSONObject().toString());
        }
        Object id;
        if(this.clase.equals(Venta.class)){
            id=1;
            for (Transaccion transaccion : list) {
                if(Integer.parseInt(transaccion.getId())>=(int)id){
                    id=Integer.parseInt(transaccion.getId())+1;
                }
            }
        }else{
            id = Utils.getRandomKey(5);
        }
        cv = (CompraVenta) this.clase.getConstructor(String.class,this.receptor,
        Vendedor.class,LocalDateTime.class,ArrayList.class).newInstance(id+"",receptor,vendedor,ldt,deta);
        return cv;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<CompraVenta> getDataType() {
        return (Class<CompraVenta>) this.clase;
    }

}
