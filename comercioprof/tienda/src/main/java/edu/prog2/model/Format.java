package edu.prog2.model;

import org.json.JSONObject;

/**
 * Sólo las clases que implementen esta interfaz podrán utilizarse
 * para exportar a los formatos de finidos sin dar muchas vueltas.
 */

public interface Format {
    @Override
    public boolean equals(Object obj);

    @Override
    public String toString();

    public JSONObject toJSONObject();
}
