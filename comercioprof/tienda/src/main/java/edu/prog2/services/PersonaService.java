package edu.prog2.services;

import edu.prog2.helpers.Utils;
import edu.prog2.model.Cliente;
import edu.prog2.model.Persona;
import edu.prog2.model.Provedor;
import edu.prog2.model.Vendedor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PersonaService implements Service<Persona> {

  private List<Persona> list;
  private final String fileName;
  private final Class<? extends Persona> clase;

  public PersonaService(Class<? extends Persona> clase) throws Exception {
    this.clase = clase;
    fileName = Utils.PATH + clase.getSimpleName() + ".json";
    System.out.println(clase.getSimpleName());
    if (Utils.fileExists(fileName)) {
      load();
    } else {
      list = new ArrayList<>();
    }
  }

  @Override
  public JSONObject add(String strJson) throws Exception {
    JSONObject json = new JSONObject(strJson);

    json.put("id", Utils.getRandomKey(5));
    Persona p = clase.getConstructor(JSONObject.class).newInstance(json);

    if (list.contains(p)) {
      throw new ArrayStoreException(String.format("El %s con ID %s ya existe", clase.getSimpleName(), p.getId()));
    }

    if (list.add(p)) {
      Utils.writeJSON(list, fileName);
    }
    return new JSONObject().put("message", "ok").put("data", p.toJSONObject());
  }

  @Override
  public JSONObject get(int index) {
    return list.get(index).toJSONObject();
  }

  @Override
  public JSONObject get(String id) {
    int i;
    if(clase.getSimpleName().equals("Cliente")){
      i = list.indexOf(new Cliente(id));

    }else if(clase.getSimpleName().equals("Provedor")){
      i = list.indexOf(new Provedor(id));

    }else if(clase.getSimpleName().equals("Vendedor")){
      i = list.indexOf(new Vendedor(id));

    }else{
      throw new UnsupportedOperationException("No existe ese tipo de Persona");
    }
    return i > -1 ? get(i) : null;
  }

  @Override
  public Persona getItem(String id) {
    JSONObject json= get(id);
    if(clase.getSimpleName().equals("Cliente")){
      return new Cliente(json);

    }
    if(clase.getSimpleName().equals("Provedor")){
      return new Provedor(json);

    }
    if(clase.getSimpleName().equals("Vendedor")){
      return new Vendedor(json);
    }
    throw new UnsupportedOperationException("No existe ese tipo de Persona");
    
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
  public final List<Persona> load() throws Exception {
    list = new ArrayList<>();

    String data = Utils.readText(fileName);
    JSONArray jsonArr = new JSONArray(data);

    for (int i = 0; i < jsonArr.length(); i++) {
      JSONObject jsonObj = jsonArr.getJSONObject(i);
      if(clase.getSimpleName().equals("Cliente")){
        list.add(new Cliente(jsonObj));
  
      }else if(clase.getSimpleName().equals("Provedor")){
        list.add(new Provedor(jsonObj));
  
      }else if(clase.getSimpleName().equals("Vendedor")){
        list.add(new Vendedor(jsonObj));
  
      }else{
        throw new UnsupportedOperationException("No existe ese tipo de Persona");
      }
      
    }

    return list;
  }

  @Override
  public JSONObject update(String id, String json) throws Exception {
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
}
