package fr.adrackiin.hydranium.api.management.data;

public class Data {

    private final String type;
    private final Object value;

    public Data(String type, Object value){
        this.type = type;
        this.value = value;
    }

    public String getType(){
        return type;
    }

    public Object getValue(){
        return value;
    }
}
