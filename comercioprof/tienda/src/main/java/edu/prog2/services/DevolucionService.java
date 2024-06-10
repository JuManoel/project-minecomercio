package edu.prog2.services;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import edu.prog2.helpers.Utils;
import edu.prog2.model.*;

public class DevolucionService extends TransaccionService {

    private Class<? extends CompraVenta> cv;
    private String tipo;

    public DevolucionService(Class<? extends CompraVenta> cv) {
        this.cv = cv;
        if (cv.getSimpleName().equals(Venta.class.getSimpleName())) {
            tipo = "venta";
        } else if (cv.getSimpleName().equals(Compra.class.getSimpleName())) {
            tipo = "compra";
        } else {
            throw new IllegalArgumentException("No existe ese tipo de devolucion");
        }
        this.fileName = Utils.PATH + tipo + ".json";
        if (Utils.fileExists(fileName)) {
            // load();
        } else {
            list = new ArrayList<>();
        }

    }

    @Override
    public JSONObject add(String strJson) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'add'");
    }

    @Override
    public JSONObject get(int index) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }

    @Override
    public JSONObject get(String id) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }

    @Override
    public Transaccion getItem(String id) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getItem'");
    }

    @Override
    public JSONObject getAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }

    @Override
    public List load() throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'load'");
    }

    @Override
    public JSONObject update(String id, String strJson) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void refreshAll() throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'refreshAll'");
    }

    @Override
    public JSONObject remove(String id) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }

    @Override
    public Class getDataType() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDataType'");
    }

}