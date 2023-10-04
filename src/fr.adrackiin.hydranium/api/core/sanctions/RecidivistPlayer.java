package fr.adrackiin.hydranium.api.core.sanctions;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.core.dbwrapper.APData;
import fr.adrackiin.hydranium.api.utils.APHash;

public class RecidivistPlayer extends APData {

    private static final APHash<String, Integer> values = new APHash<>();

    static {
        values.put("insultes",0);
        values.put("dox",1);
        values.put("provocation",2);
        values.put("spoil",3);
        values.put("spam",4);
    }

    public RecidivistPlayer(){
        this.set("insultes", 0);
        this.set("dox", 0);
        this.set("provocation", 0);
        this.set("spoil", 0);
        this.set("spam", 0);
        this.setSource(this);
    }

    public void set(String type, Object value){
        set(values.get(type.toLowerCase()), value);
    }

    public Object get(String type){
        return get(values.get(type.toLowerCase()));
    }

    public int getRecidivismTime(String reason){
        int nbReci = (int)this.get(reason);
        int time = 0;
        switch(nbReci){
            case 0:
                time = 3*24;
                break;
            case 1:
                time = 5*24;
                break;
            case 2:
                time = 7*24;
                break;
            case 3:
            case 6:
                time = 15*24;
                break;
            case 4:
            case 7:
                time = 30*24;
                break;
            case 9:
                time = 60*24;
                break;
            case 10:
                time = 120*24;
                break;
            case 5:
                time = -5*24;
                break;
            case 8:
                time = -10*24;
                break;
            case 11:
                time = -15*24;
                break;
            case 12:
                time = -1;
                break;
        }
        return time;
    }

    public String getRecidivismReason(String reason){
        int nbReci = (int)this.get(reason);
        switch(nbReci){
            case 0:
                break;
            case 1:
                reason = reason + " + Récidive ";
                break;
            case 4:
                reason = reason + " + Récidive x4 (Prochain: Ban) ";
                break;
            case 10:
                reason = reason + " + Récidive x10 (Prochain: Ban) ";
                break;
            case 7:
                reason = reason + " + Récidive x7 (Prochain: Ban) ";
                break;
            default:
                reason = reason + " + Récidive x" + nbReci;
                break;
        }
        return reason;
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

    @Override
    public String toString(){
        return super.toString();
    }
}
