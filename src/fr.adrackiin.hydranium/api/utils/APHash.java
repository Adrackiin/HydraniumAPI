package fr.adrackiin.hydranium.api.utils;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

public class APHash<T,U> {

    private final Hashtable<T,U> hash;

    public APHash(){
        this.hash = new Hashtable<>();
    }

    public APHash(Hashtable<T,U> set) {
        this.hash = set;
    }

    public void put(T key, U value){
        if(!this.contains(key)) {
            this.hash.put(key, value);
        }
    }

    public void remove(T type){
        this.hash.remove(type);
    }

    public boolean contains(T type){
        return this.hash.containsKey(type);
    }

    public Iterator<T> itKeys(){
        return this.hash.keySet().iterator();
    }

    public Iterator<U> itValues(){
        return this.hash.values().iterator();
    }

    public void clear(){
        this.hash.clear();
    }

    public U get(T key){
        if(this.contains(key)){
            return this.hash.get(key);
        }
        return null;
    }

    public void update(T key, U value){
        if(this.contains(key)) {
            this.hash.replace(key, value);
        } else {
            this.put(key, value);
        }
    }

    public boolean isEmpty(){
        return this.hash.isEmpty();
    }

    public int getSize(){
        return this.hash.size();
    }

    public List<T> getKeys(){
        return new ArrayList<>(this.hash.keySet());
    }

    public List<U> getValues(){
        return new ArrayList<>(this.hash.values());
    }

    @Override
    public String toString(){
        return "APHash{" +
                "hash=" + hash +
                '}';
    }
}
