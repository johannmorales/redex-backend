package org.redex.backend.controller.simulacion.simulador;

import java.util.ArrayList;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class SortedSumList<X extends Comparable, Y extends Number> {
    private TreeMap<X, Y> mem;
    private TreeMap<X, Y> inner;

    public static SortedSumList create() {
        SortedSumList list = new SortedSumList();
        list.inner = new TreeMap();
        list.mem = new TreeMap();
        return list;
    }

    private Y sum(Y y1, Y y2) {
        return (Y) (Integer) (y1.intValue() + y2.intValue());
    }

    public void reset() {
        this.mem = new TreeMap<>();
    }

    public void add(X x, Y y) {
        if(y.intValue() == 0){
            return;
        }

        if (inner.containsKey(x)) {
            inner.replace(x, sum(inner.get(x), y));
        } else {
            inner.put(x, y);
        }
    }

    private X getLastKeyBeforeOrEqual(X x) {
        if (x == null) return null;

        if (inner.containsKey(x)) {
            return x;
        } else {
            return inner.floorKey(x);
        }
    }

    private X getLastKeyBeforeMem(X x) {
        if (x == null) return null;

        if (mem.containsKey(x)) {
            return mem.lowerKey(x);
        } else {
            return mem.floorKey(x);
        }
    }

    private X getLastKeyBefore(X x) {
        if (x == null) return null;

        if (inner.containsKey(x)) {
            return inner.lowerKey(x);
        } else {
            return inner.floorKey(x);
        }
    }

    public Y get(X x) {
        X actualKey = getLastKeyBeforeOrEqual(x);

        if (actualKey == null) {
            return (Y) (Integer) 0;
        }

        if (mem.containsKey(actualKey)) {
            return mem.get(actualKey);
        } else {
            X lastKeyMem = getLastKeyBeforeMem(x);
            Y lastValueMem;

            if (lastKeyMem == null) {
                lastValueMem = (Y) (Integer) 0;
            } else {
                lastValueMem = mem.get(lastKeyMem);
            }

            NavigableMap<X, Y> noProcesado;

            if (lastKeyMem == null) {
                noProcesado = inner.headMap(actualKey, true);
            } else {
                noProcesado = inner.tailMap(lastKeyMem, false).headMap(actualKey, true);
            }

            for (Map.Entry<X, Y> entry : noProcesado.entrySet()) {
                lastValueMem = sum(lastValueMem, entry.getValue());
                mem.put(entry.getKey(), lastValueMem);
            }
        }

        return mem.get(actualKey);
    }

    public Y getLastBefore(X x) {
        X actualKey = getLastKeyBefore(getLastKeyBeforeOrEqual(x));
        if (actualKey == null) {
            return (Y) (Integer) 0;
        } else {
            return this.get(actualKey);
        }
    }


    public void removeBeforeOrEqual(X x){
        if(inner.isEmpty()){
            return;
        }
        ArrayList<X> toBeDeleted = new ArrayList<>();
        for (Map.Entry<X, Y> entry : this.inner.entrySet()) {
            if(entry.getKey().compareTo(x) <= 0){
                toBeDeleted.add(entry.getKey());
            }
        }

        for (X x1 : toBeDeleted) {
            inner.remove(x1);
        }
    }
}
