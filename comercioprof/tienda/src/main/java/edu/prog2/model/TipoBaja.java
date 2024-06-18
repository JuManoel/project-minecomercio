package edu.prog2.model;

import org.json.JSONArray;
import org.json.JSONObject;
//los tipos de baja que pueden ocurrir

public enum TipoBaja {
    AJUSTE("Ajuste"),
    DAÑADO("Dañado"),
    DAÑO("Daño"),
    DEFECTUOSO("Defectuoso"),
    DESCONTINUADO("Descontinuado"),
    ERROR_REGISTRO("Error Registro"),
    PERDIDO_HURTO("Perdido Hurto"),
    VENCIDO("Vencido"),
    INDEFINIDO("Indefinido");

    private final String value;

    private TipoBaja(String value) {
        this.value = value;
    }

    /**
     * Devuelve el valor de un constante enumerada en formato humano
     * Ejemplo: System.out.println(tp.getValue()); // devuelve: Aseo general
     * 
     * @return El valor del argumento value, recibido por el constructor
     */
    public String getValue() {
        return value;
    }

    /**
     * Dado un string, devuelve la constante enumerada correspondiente. Ejemplo:
     * TipoBaja.getEnum("Aseo general") devuelve TipoBaja.ASEO_GENERAL
     * no confundir con TipoBaja.valueOf("CONSTANTE_ENUMERADA")
     * 
     * @param value La expresión para humanos correspondiente a la constante
     *              enumerada
     * @return La constante enumerada
     */
    public static TipoBaja getEnum(String value) {
        if (value == null) {
            throw new IllegalArgumentException();
        }

        for (TipoBaja tp : values()) {
            if (value.equalsIgnoreCase(tp.getValue())) {
                return tp;
            }
        }
        throw new IllegalArgumentException();
    }

    public static JSONObject getAll() {
        JSONArray jsonArr = new JSONArray();
        for (TipoBaja tp : values()) {
            jsonArr.put(new JSONObject().put("ordinal", tp.ordinal()).put("key", tp).put("value", tp.value));
        }
        return new JSONObject().put("message", "ok").put("data", jsonArr);
    }
}
