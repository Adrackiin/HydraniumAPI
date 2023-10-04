package fr.adrackiin.hydranium.api.core.sanctions;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.core.dbwrapper.APData;
import fr.adrackiin.hydranium.api.utils.APHash;

public class BannedPlayer extends APData {

    private static final APHash<String, Integer> values = new APHash<>();

    static {
        values.put("uuid",0);
        values.put("name",1);
        values.put("reason",2);
        values.put("expire",3);
    }

    public BannedPlayer(){
        this.setSource(this);
    }

    public void set(String type, Object value){
        set(values.get(type), value);
    }

    public Object get(String type){
        return get(values.get(type));
    }

    public void deserialize(String string){
        APICore.getPlugin().getAPDataManager().deserialize(string, this, values);
    }

    public String serialize(){
        return APICore.getPlugin().getAPDataManager().serialize(this, values);
    }

    public APHash<String, Integer> getTexts(){
        return values;
    }

}
