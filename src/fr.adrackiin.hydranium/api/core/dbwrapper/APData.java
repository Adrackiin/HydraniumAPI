package fr.adrackiin.hydranium.api.core.dbwrapper;

import fr.adrackiin.hydranium.api.utils.APHash;

public class APData {

    private final APHash<Integer, Object> objects = new APHash<>();
    private Object source;

    public APData(){
    }

    public APHash<Integer, Object> getObjects(){
        return objects;
    }

    public Object getSource(){
        return source;
    }

    public void setSource(Object source){
        this.source = source;
    }

    protected void set(Integer type, Object value){
        objects.update(type, value);
    }

    protected Object get(Integer type){
        return objects.get(type);
    }

    @Override
    public String toString(){
        return "APData{" +
                "objects=" + objects +
                '}';
    }
}
