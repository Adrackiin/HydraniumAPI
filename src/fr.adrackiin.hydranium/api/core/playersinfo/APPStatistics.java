package fr.adrackiin.hydranium.api.core.playersinfo;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.core.dbwrapper.APData;
import fr.adrackiin.hydranium.api.utils.APHash;

public class APPStatistics extends APData {

    private static final APHash<String, Integer> values = new APHash<>();

    static {
        values.put("game-played",0);
        values.put("play-time",1);
        values.put("win",2);
        values.put("death",3);
        values.put("killed-players",4);
        values.put("inflicted-damage",5);
        values.put("taken-damage",6);
        values.put("mined-block",7);
        values.put("placed-block",8);
        values.put("diamond-mined",9);
        values.put("gold-mined",10);
        values.put("iron-mined",11);
        values.put("lapis-mined",12);
        values.put("redstone-mined",13);
        values.put("quartz-mined",14);
        values.put("coal-mined",15);
        values.put("emerald-mined",16);
        values.put("mob-killed",17);
        values.put("monster-killed",18);
        values.put("venimous-killed",19);
        values.put("items-crafted",20);
        values.put("potion-crafted",21);
        values.put("item-cooked",22);
        values.put("item-enchanted",23);
        values.put("tnt-crafted",24);
        values.put("arrow-fired",25);
        values.put("enderpearl-launched",26);
        values.put("food-eaten",27);
        values.put("gapple-eaten",28);
        values.put("ghead-eaten",29);
    }

    public APPStatistics(){
        this.setSource(this);
    }

    public void set(String type, Object value){
        set(values.get(type), value);
    }

    public Object get(String type){
        return get(values.get(type));
    }

    public void add(String type, int value){
        try {
            set(type, (int) get(type) + value);
        } catch(NullPointerException e){
            APICore.getPlugin().logServer("No Stat found: " + type);
        }
    }

    public void add(String type){
        add(type, 1);
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
