package fr.adrackiin.hydranium.api.core.playersinfo.settings;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.core.dbwrapper.APData;
import fr.adrackiin.hydranium.api.utils.APHash;

public class APPSettings extends APData {

    private static final APHash<String, Integer> values = new APHash<>();

    static {
        values.put("private-message",0);
    }

    public APPSettings(){
        this.setSource(this);
    }

    public void set(String type, Object value){
        set(values.get(type), value);
    }

    public Object get(String type){
        return get(values.get(type));
    }

    public void deserialize(String string){
        //System.out.println(string + " | " + values);
        APICore.getPlugin().getAPDataManager().deserialize(string, this, values);
    }

    public String serialize(){
        return APICore.getPlugin().getAPDataManager().serialize(this, values);
    }

    public APHash<String, Integer> getTexts(){
        return values;
    }

}
