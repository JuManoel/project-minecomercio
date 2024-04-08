package edu.prog2.services;

import edu.prog2.helpers.Utils;
import edu.prog2.model.Persona;

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
    p.setPassword(Utils.MD5(p.getPassword()));
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
  public JSONObject get(String id) throws Exception{
    Persona persona = this.clase.getConstructor(String.class).newInstance(id);
    int i = list.indexOf(persona);
    return i > -1 ? get(i) : null;
  }

  @Override
  public Persona getItem(String id) throws Exception{
    JSONObject json= get(id);
    return this.clase.getConstructor(JSONObject.class).newInstance(json);
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
      list.add(this.clase.getConstructor(JSONObject.class).newInstance(jsonObj));
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
