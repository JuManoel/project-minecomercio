package edu.prog2.services;

import edu.prog2.helpers.Utils;
import edu.prog2.model.Persona;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class PersonaService implements Service<Persona> {

  private List<Persona> list;
  private final String fileName;
  private final Class<? extends Persona> clase;

  public PersonaService(Class<? extends Persona> clase) throws Exception {
    this.clase = clase;
    fileName = Utils.PATH + clase.getSimpleName() + ".json";
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
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'get'");
  }

  @Override
  public JSONObject get(String id) throws Exception {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'get'");
  }

  @Override
  public Persona getItem(String id) throws Exception {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getItem'");
  }

  @Override
  public JSONObject getAll() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getAll'");
  }

  @Override
  public final List<Persona> load() throws Exception {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'load'");
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
