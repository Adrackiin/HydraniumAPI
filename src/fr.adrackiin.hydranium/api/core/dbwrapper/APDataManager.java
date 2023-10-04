package fr.adrackiin.hydranium.api.core.dbwrapper;

import fr.adrackiin.hydranium.api.utils.APHash;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class APDataManager {

    private final APHash<String, String> defaultDatas = new APHash<>();

    public void addDefaultData(String dataName, String defaultData){
        defaultDatas.put(dataName, defaultData);
    }

    public String getDefaultData(String dataName){
        return defaultDatas.get(dataName);
    }

    public void deserialize(String string, APData data, APHash<String, Integer> values){
        String[] tmp;
        for(String str : splitSettings(string)){
            tmp = split(str);
            data.set(values.get(tmp[0]), stringToValue(tmp[1]));
        }
    }

    public String serialize(APData data, APHash<String, Integer> values){
        StringBuilder object = new StringBuilder();
        APHash<Integer, Object> set = data.getObjects();
        for(String type : values.getKeys()){
            object.append(type).append(":").append(this.valueToString(set.get(values.get(type)))).append(",");
        }
        return object.substring(0, object.length() - 1);
    }

    private Object stringToValue(String valueString){
        if(valueString.equalsIgnoreCase("true") || valueString.equalsIgnoreCase("false")){
            return Boolean.parseBoolean(valueString);
        } else {
            if(valueString.startsWith("[")){
                valueString = valueString.substring(1);
                boolean inText = false;
                char[] decomposed = valueString.toCharArray();
                List<Object> list = new ArrayList<>();
                StringBuilder tmp = new StringBuilder();
                for(int i = 0, decomposedLength = decomposed.length; i < decomposedLength; i++){
                    if(i == decomposedLength - 1){
                        list.add(stringToValue(tmp.toString()));
                    } else {
                        char c = decomposed[i];
                        if(!inText && c == ','){
                            list.add(stringToValue(tmp.toString()));
                            tmp.delete(0, tmp.length());
                        } else {
                            if(c == '"' && (i < 1 || decomposed[i-1] != '\\')){
                                inText = !inText;
                            }
                            tmp.append(c);
                        }
                    }
                }
                return list;
            } else {
                try {
                    return Integer.parseInt(valueString);
                } catch(NumberFormatException e){
                    try {
                        return Float.parseFloat(valueString);
                    } catch(NumberFormatException e1){
                        if(valueString.length() == 0){
                            return "";
                        }
                        return valueString.substring(1, valueString.length() - 1);
                    }
                }
            }
        }
    }

    private String[] split(String setting){
        char[] decomposed = setting.toCharArray();
        String[] split = new String[2];
        StringBuilder tmp = new StringBuilder();
        for(int i = 0; i < decomposed.length; i ++){
            if(i == decomposed.length - 1){
                tmp.append(decomposed[i]);
                split[1] = tmp.toString();
            } else {
                if(split[0] == null && decomposed[i] == ':'){
                    split[0] = tmp.toString();
                    tmp.delete(0, tmp.length());
                } else {
                    tmp.append(decomposed[i]);
                }
            }
        }
        return split;
    }

    private String[] splitSettings(String settings){
        boolean inText = false;
        boolean inList = false;
        char[] decomposed = settings.toCharArray();
        List<String> split = new ArrayList<>();
        StringBuilder tmp = new StringBuilder();
        for(int i = 0; i < decomposed.length; i ++){
            if(i == decomposed.length - 1){
                tmp.append(decomposed[i]);
                split.add(tmp.toString());
            } else {
                if(!inText && !inList && decomposed[i] == ','){
                    split.add(tmp.toString());
                    tmp.delete(0, tmp.length());
                } else {
                    if(!inList && decomposed[i] == '"' && decomposed[i-1] != '\\'){
                        inText = !inText;
                    } else if(!inText && (decomposed[i] == '[' || decomposed[i] == ']')){
                        inList = !inList;
                    }
                    tmp.append(decomposed[i]);
                }
            }
        }
        return split.toArray(new String[0]);
    }

    private String valueToString(Object o){
        if(o instanceof Set){
            StringBuilder str = new StringBuilder("[");
            for(Object oSet : ((Set) o).toArray()){
                str.append(valueToString(oSet)).append(",");
            }
            return str.substring(0, str.length() - 1) + "]";
        } else if(o instanceof List){
            StringBuilder str = new StringBuilder("[");
            for(Object oList : ((List) o).toArray()){
                str.append(valueToString(oList)).append(",");
            }
            return str.substring(0, str.length() - 1) + "]";
        } else if(o instanceof Integer || o instanceof Float || o instanceof Boolean || o instanceof Long || o instanceof Double){
            return o + "";
        } else if(o instanceof String){
            return "\"" + o + "\"";
        }
        return "";
    }

}
