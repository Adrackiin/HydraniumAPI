package fr.adrackiin.hydranium.api.utils;

import fr.adrackiin.hydranium.api.APICore;

import java.util.*;

public class APList<T> {

    private ArrayList<T> list = new ArrayList<>();

    public APList(){
    }

    public APList(ArrayList<T> list) {
        this.list = list;
    }

    public APList(List<T> list) {
        this.list.addAll(list);
    }

    public void clear(){
        this.list.clear();
    }

    public void add(T type){
        this.list.add(type);
    }

    public void add(Collection<T> type){
        this.list.addAll(type);
    }

    public void add(T[] type){
        this.list.addAll(Arrays.asList(type));
    }

    public void remove(T type){
        this.list.remove(type);
    }

    public void remove(){
        this.list.remove(0);
    }

    public boolean contains(T type){
        return this.list.contains(type);
    }

    public Iterator<T> iterator(){
        return this.list.iterator();
    }

    public List<T> copy(){
        return new ArrayList<>(this.list);
    }

    public T first(){
        return this.get(0);
    }

    public T get(int i){
        return this.list.get(i);
    }

    public Object[] toArray(){
        return this.list.toArray();
    }

    public void seeList(){
        APICore.getPlugin().logServer(String.valueOf(this.list));
    }

    public boolean isEmpty(){
        return this.list.isEmpty();
    }

    public int getSize(){
        return this.list.size();
    }

    public boolean isNull(){
        boolean stop = false;
        for(Iterator<T> it = this.iterator(); it.hasNext(); ) {
            T t = it.next();
            if(t != null){
                stop = true;
            }
        }
        return !stop;
    }

}
