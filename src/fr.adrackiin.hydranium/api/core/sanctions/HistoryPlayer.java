package fr.adrackiin.hydranium.api.core.sanctions;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.core.dbwrapper.APData;
import fr.adrackiin.hydranium.api.utils.APHash;

import java.util.List;

public class HistoryPlayer extends APData {

    private static final APHash<String, Integer> values = new APHash<>();
    private static int id;

    static {
        values.put("n",0);
        id = 1;
    }

    public HistoryPlayer(){
        this.setSource(this);
        this.set("n", 0);
    }

    public void set(String type, Object value){
        if(values.get(type) == null){
            values.put("history" + id, id);
            id ++;
        }
        set(values.get(type), value);
    }

    public Object get(String type){
        return get(values.get(type));
    }

    public List getHistory(int n){
        return (List) this.get("history" + n);
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

    public List getLastBan(){
        List last = null;
        List history;
        int i = (int)this.get("n");
        while(i > 0 && last == null){
            history = getHistory(i);
            if(history.get(0).equals("ban") && history.get(6).equals("") && ((int)history.get(5) == -1 || ((int)history.get(3) + (int)history.get(5) * 3600) > (System.currentTimeMillis() / 1000))){
                last = history;
            }
            i--;
        }
        return last;
    }

    public List getLastMute(){
        List last = null;
        List history;
        int i = (int)this.get("n");
        while(i > 0 && last == null){
            history = getHistory(i);
            if(history.get(0).equals("mute") && history.get(6).equals("") && ((int)history.get(5) == -1 || ((int)history.get(3) + (int)history.get(5) * 3600) > (System.currentTimeMillis() / 1000))){
                last = history;
            }
            i--;
        }
        return last;
    }
}
