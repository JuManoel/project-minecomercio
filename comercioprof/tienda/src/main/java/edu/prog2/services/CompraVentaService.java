package edu.prog2.services;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import edu.prog2.helpers.Utils;
import edu.prog2.model.*;


public class CompraVentaService extends TransaccionService{

    private Class<? extends Persona> receptor;
    private Class<? extends Persona> vendedor;
    private PersonaService serV;
    private PersonaService serR;
    private final String fileName;
    private final Class<? extends CompraVenta> clase;
    private String compraVenta;

    public CompraVentaService(Class<? extends Persona> receptor, Class<? extends Persona> vendedor) throws Exception {
        this.vendedor=vendedor;
        this.receptor=receptor;
        this.productoService=new ProductoService();
        serV=new PersonaService(Vendedor.class);
        if(this.receptor.equals(Cliente.class)){
            compraVenta="venta";
            this.clase=Venta.class;
            serR=new PersonaService(Cliente.class);
        }else if(this.receptor.equals(Provedor.class)){
            compraVenta="compra";
            this.clase=Compra.class;
            serR=new PersonaService(Provedor.class);
        }else{
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
        CompraVenta cv=creatCompraVenta(strJson);
        list.add(cv);
        Utils.writeJSON(list, fileName);
        return new JSONObject().put("message", "ok").put("data", cv.toJSONObject());
    }


    @Override
    public JSONObject get(String id) throws Exception {
        CompraVenta compraVenta = this.clase.getConstructor(String.class).newInstance(id);
        int i = list.indexOf(compraVenta);
        return i > -1 ? get(i) : null;
    }

    @Override
    public CompraVenta getItem(String id) throws Exception {
        JSONObject json= get(id);
        CompraVenta cv=creatCompraVenta(json.toString());
        return cv;
    }

    @Override
    public JSONObject getAll() {
        try {
            JSONArray data = new JSONArray(Utils.readText(fileName));
            return new JSONObject().put("message", "ok").put("data", data);
          } catch (IOException | JSONException e) {
            Utils.printStackTrace(e);
            return Utils.keyValueToJson("message", "Sin acceso a datos de productos", "error", e.getMessage());
          }
    }

    @Override
    public List<Transaccion> load() throws Exception {
        this.productoService=new ProductoService();
        list = new ArrayList<>();
        String data = Utils.readText(fileName);
        JSONArray jsonArr = new JSONArray(data);
        CompraVenta cv;
        for (int i = 0; i < jsonArr.length(); i++) {
            JSONObject jsonObj = jsonArr.getJSONObject(i);
            if(compraVenta.equals("compra")){
                String id=jsonObj.getString("id");
                Vendedor v=new Vendedor(serV.get(jsonObj.getString("vendedor")));
                Provedor pro = new Provedor(serR.get(jsonObj.getString("provedor")));
                LocalDateTime fecha = LocalDateTime.parse(jsonObj.getString("fechaHora"));
                JSONArray de=jsonObj.getJSONArray("detalle");
                ArrayList<Detalle> d=new ArrayList<>();
                for (int j = 0; j < de.length(); j++) {
                    JSONObject jsonObject=de.getJSONObject(j);
                    Producto p=productoService.getItem(jsonObject.getString("producto"));
                    d.add(new Detalle(p,jsonObject.getInt("cantidad")));
                }

                cv=new Compra(id,pro,v,fecha,d);
            }else{
                String id=jsonObj.getString("id");
                Vendedor v=new Vendedor(jsonObj.getJSONObject("vendedor"));
                Cliente c = new Cliente(jsonObj.getJSONObject("cliente"));
                LocalDateTime fecha = LocalDateTime.parse(jsonObj.getString("fechaHora"));
                JSONArray de=jsonObj.getJSONArray("detalles");
                ArrayList<Detalle> d=new ArrayList<>();
                for (int j = 0; j < de.length(); j++) {
                    JSONObject jsonObject=de.getJSONObject(j);
                    Producto p=productoService.getItem(jsonObject.getJSONObject("producto").getString("id"));
                    d.add(new Detalle(p,jsonObject.getInt("cantidad")));
                }
                cv=new Venta(id,c,v,fecha,d);

            }
            list.add(cv);
        }

        return list;
    }
    
    @Override
    public JSONObject update(String id, String strJson) throws Exception {
        CompraVenta cv = creatCompraVenta(strJson);
        int i = list.indexOf(cv);
        list.set(i, cv);
        // actualizar el archivo
        Utils.writeJSON(list, fileName);
        // devolver la instancia con los cambios realizados
        return new JSONObject().put("message", "ok").put("data", cv.toJSONObject());
    }
    private CompraVenta creatCompraVenta(String strJson) throws Exception {
        CompraVenta cv;
        JSONObject json = new JSONObject(strJson);
        json.put("id", Utils.getRandomKey(5));
        ArrayList<Detalle> detalles=new ArrayList<>();
        JSONArray jsonArray=json.getJSONArray("detalle");
        Producto p;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject=jsonArray.getJSONObject(i);
            p=this.productoService.getItem(jsonObject.getString("producto"));
            detalles.add(new Detalle(p,jsonObject.getInt("cantidad")));
            
        }
        if(compraVenta.equals("venta")){
            cv=new Venta(new Cliente(this.serR.get(json.getString("cliente"))),new Vendedor(serV.get(json.getString("vendedor"))),LocalDateTime.parse(json.getString("fechaHora")),detalles);            
        }else if(compraVenta.equals("compra")){
            cv=new Compra(new Provedor(this.serR.get(json.getString("provedor"))),new Vendedor(serV.get(json.getString("vendedor"))),LocalDateTime.parse(json.getString("fechaHora")),detalles);
        }else{
            throw new Exception("No existe ese tipo de transacion");
        }
        for (int i = 0; i < cv.getDetalles().size(); i++) {
            Producto pro = cv.getDetalles().get(i).getProducto();
            productoService.update(pro.getId(), pro.toJSONObject().toString());
            if(compraVenta.equals("compra")){
                pro.setDisponible(pro.getDisponible()+cv.getDetalles().get(i).getCantidad());
            }else{
                pro.setDisponible(pro.getDisponible()-cv.getDetalles().get(i).getCantidad());
            }
            productoService.update(pro.getId(), pro.toJSONObject().toString());
        }
        cv.setId(json.getString("id"));
        return cv;
    }

    @Override
    public void refreshAll() throws Exception {
        list = new ArrayList<>();
        load();
    }

    @Override
    public JSONObject remove(String id) throws Exception {
        CompraVenta compraVenta = getItem(id);
        if(this.list.remove(compraVenta)){
          Utils.writeJSON(list, fileName);
        // devolver la instancia con los cambios realizados
          return new JSONObject().put("message", "ok").put("data", compraVenta.toJSONObject());
        }
        throw new Exception("No se pudo remover la compraVenta con el ID:"+id);
    }

    @Override
    public Class<CompraVenta> getDataType() {
        return (Class<CompraVenta>) this.clase;
    }
    
}
