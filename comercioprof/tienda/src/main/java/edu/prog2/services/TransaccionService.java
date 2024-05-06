package edu.prog2.services;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import edu.prog2.helpers.Utils;
import edu.prog2.model.CompraVenta;
import edu.prog2.model.Transaccion;

public abstract class TransaccionService implements IService{
    protected ProductoService productoService;
    protected List<Transaccion> list;
    protected String fileName;
    @Override
    public JSONObject add(String strJson) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'add'");
    }

    public JSONObject get(int index) {
        return list.get(index).toJSONObject();
    }

    @Override
    public JSONObject get(String id) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }

    @Override
    public Object getItem(String id) throws Exception {
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
        list = new ArrayList<>();
        load();
    }

    @Override
    public JSONObject remove(String id) throws Exception {
        Transaccion transaccion = (Transaccion)getItem(id);
        if(this.list.remove(transaccion)){
          Utils.writeJSON(list, fileName);
        // devolver la instancia con los cambios realizados
          return new JSONObject().put("message", "ok").put("data", transaccion.toJSONObject());
        }
        throw new Exception("No se pudo remover la compraVenta con el ID:"+id);
    }

    @Override
    public Class getDataType() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDataType'");
    }
    
    
}
