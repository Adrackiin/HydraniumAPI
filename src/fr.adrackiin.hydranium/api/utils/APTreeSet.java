package fr.adrackiin.hydranium.api.utils;

import java.util.*;

public class APTreeSet<T> {

    private final TreeSet<T> set;

    public APTreeSet(){
        this.set = new TreeSet<>();
    }

    public APTreeSet(TreeSet<T> set) {
        this.set = set;
    }

    public APTreeSet(List<T> set) {
        this.set = new TreeSet<>();
        this.set.addAll(set);
    }

    public void clear(){
        this.set.clear();
    }

    public void add(T type){
        this.set.add(type);
    }

    public void add(Collection<T> type){
        this.set.addAll(type);
    }

    public void add(T[] type){
        this.set.addAll(Arrays.asList(type));
    }

    public void remove(T type){
        this.set.remove(type);
    }

    public void remove(){
        this.set.remove(this.first());
    }

    public boolean contains(T type){
        return this.set.contains(type);
    }

    public Iterator<T> iterator(){
        return this.set.iterator();
    }

    public TreeSet<T> copy(){
        return new TreeSet<>(this.set);
    }

    public List<T> copyNoOrdered(){
        return new ArrayList<>(this.set);
    }

    public T first(){
        return this.iterator().next();
    }

    public T get(int i){
        return this.copyNoOrdered().get(i);
    }

    public Object[] toArray(){
        return this.set.toArray();
    }

    public boolean isEmpty(){
        return this.set.isEmpty();
    }

    public int getSize(){
        return this.set.size();
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
