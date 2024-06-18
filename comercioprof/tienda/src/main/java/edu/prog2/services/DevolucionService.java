package edu.prog2.services;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.prog2.helpers.Utils;
import edu.prog2.model.*;

public class DevolucionService extends TransaccionService {
    //ese codigo segue la misma logica de CompraVenta, pero incompleto
    private Class<? extends CompraVenta> cv;
    private String tipo;

    public DevolucionService(Class<? extends CompraVenta> cv) throws Exception {
        this.cv = cv;
        if (cv.getSimpleName().equals(Venta.class.getSimpleName())) {
            this.clase = DevolucionVenta.class;
            tipo = "Venta";
        } else if (cv.getSimpleName().equals(Compra.class.getSimpleName())) {
            this.clase = DevolucionCompra.class;
            tipo = "Compra";
        } else {
            throw new IllegalArgumentException("No existe ese tipo de devolucion");
        }
        this.fileName = Utils.PATH +"Devolucion"+tipo + ".json";
        if (Utils.fileExists(fileName)) {
            load();
        } else {
            list = new ArrayList<>();
        }

    }
//metodo malo, no logre terminalo
    // @Override
    // public JSONObject add(String strJson) throws Exception {
        
    // }

    // public Devolucion creatDevolucion(String strJson) throws Exception{
    //     JSONObject json = new JSONObject(strJson);
    //     Devolucion dev;
    //     CompraVentaService cvs;
    //     CompraVenta cv; 
    //     if(tipo.equals("Venta"))
    //         cvs = new CompraVentaService(Cliente.class);
    //     else
    //         cvs = new CompraVentaService(Provedor.class);
    //     cv = cvs.getItem(json.getString(tipo.toLowerCase()));
        
    // }

    @Override
    public JSONObject get(String id) throws Exception {
        Devolucion dev = (Devolucion) this.clase.getConstructor(String.class).newInstance(id);
        int i = list.indexOf(dev);
        return i > -1 ? get(i) : null;
    }

    @Override
    public Devolucion getItem(String id) throws Exception {
        Devolucion dev = (Devolucion) super.getItem(id);
        
        if (this.tipo.equals("Venta")) {
            return (DevolucionVenta) dev;
        }
        return (DevolucionCompra) dev;
    }

    @Override
    public List<Transaccion> load() throws Exception {
        this.productoService = new ProductoService();
        list = new ArrayList<>();
        String data = Utils.readText(fileName);
        JSONArray jsonArr = new JSONArray(data);
        Devolucion cv;
        for (int i = 0; i < jsonArr.length(); i++) {
            JSONObject jsonObj = jsonArr.getJSONObject(i);
            //cv = this.clase.getConstructor(JSONObject.class).newInstance(jsonObj);
            if(this.tipo.toLowerCase().equals("Compra")){
                cv = new DevolucionCompra(jsonObj);
            } else {
                cv = new DevolucionVenta(jsonObj);


            }
            list.add(cv);
        }
        /* */
        return list;
    }

    @Override
    public Class getDataType() {
        return this.clase;
    }

}
