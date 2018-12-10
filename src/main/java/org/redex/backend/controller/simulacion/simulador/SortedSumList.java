package org.redex.backend.controller.simulacion.simulador;

import java.util.TreeMap;

public class SortedSumList<X extends Comparable, Y extends Number> {
    private TreeMap<X, Y> inner;

    public static SortedSumList create() {
        SortedSumList list = new SortedSumList();
        list.inner = new TreeMap();
        return list;
    }

    public void add(X x, Y y) {
        if (inner.containsKey(x)) {
            inner.replace(x, (Y) (Integer) (inner.get(x).intValue() + y.intValue()));
        } else {
            if (inner.isEmpty()) {
                inner.put(x, y);
            } else {
                X lastKey = inner.lastKey();
                inner.put(x, (Y) (Integer) (inner.get(lastKey).intValue() + y.intValue()));
            }
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
        } else {
            return inner.get(actualKey);
        }
    }

    public Y getLastBefore(X x) {
        X actualKey = getLastKeyBefore(getLastKeyBeforeOrEqual(x));
        if (actualKey == null) {
            return (Y) (Integer) 0;
        } else {
            return inner.get(actualKey);
        }
    }

}
