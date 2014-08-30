package io.github.avatarhurden.tribalwarsengine.objects;

import java.util.LinkedHashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import database.Edif�cio;

public class VillageModel implements EditableObject {
	
	private JSONObject json;
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	/**
	 * Construtor com as informa��es do modelo j� criadas. Para gerar um modelo vazio,
	 * usar o construtor sem par�metros
	 * 
	 * @param json com as informa��es do modelo
	 */
	public VillageModel(JSONObject json) {
		this.json = json;
	}
	
	public VillageModel() {
		this(new JSONObject());
	
		setStartingValues();
	}
	
	private void setStartingValues() {
		
		setName("Novo Modelo");
		setBuildings(new Buildings());
		
	}
	
    private Object get(String chave, Object def) {
        try {
            return json.get(chave);
        } catch (JSONException e) {
            return def;
        }
    }

    private void set(String chave, Object valor) {
        json.put(chave, valor);
    }
    
    public JSONObject getJson() {
        return json;
    }
    
    public String toString() {
    	return getName();
    }
    
    // Getters
    
    public String getName() {
    	return (String) get("name", "");
    }
    
    public Buildings getBuildings() {
    	return gson.fromJson(get("buildings", "").toString(), Buildings.class);
    }
    
    // Setters
    
    public VillageModel setName(String name) {
    	set("name", name);
    	return this;
    }
    
    public VillageModel setBuildings(Buildings buildings) {
    	
    	Buildings toSave = new Buildings();
    	for (Edif�cio u : toSave.getEdif�cios()) {
    		if (buildings.contains(u))
    			toSave.addBuilding(buildings.getBuilding(u));
    	}
    	
    	set("buildings", new JSONObject(gson.toJson(toSave)));
    	return this;
        
    }
    
    public LinkedHashMap<String, String> getFieldNames() {
    	LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
    	
        map.put("name", "Nome");
        map.put("buildings", "");
        
        return map;
    }
    
}
