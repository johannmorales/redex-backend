package org.redex.backend.controller.simulacion.simulador;

import org.redex.backend.controller.simulacion.Ventana;
import pe.albatross.zelpers.miscelanea.Assert;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class SortedSimpleList<X extends Comparable, Y> {
    private Integer size;
    private TreeMap<X, ArrayList<Y>> inner;

    public static SortedSimpleList create() {
        SortedSimpleList s = new SortedSimpleList();
        s.inner = new TreeMap();
        s.size = 0;
        return s;
    }

    public void add(X x, Y y) {
        if (!inner.containsKey(x)) {
            inner.put(x, new ArrayList<>(100));
        }
        inner.get(x).add(y);
        size++;
    }

    public Integer size() {
        return size;
    }

    public Boolean isEmpty() {
        return size == 0;
    }

    public Y first() {
        if (size == 0) {
            return null;
        }
        List<Y> list = inner.firstEntry().getValue();
        int randomElementIndex = ThreadLocalRandom.current().nextInt(list.size()) % list.size();
        return list.get(randomElementIndex);
    }

    public ArrayList<Y> firstN(Integer n) {
        ArrayList<Y> toBeReturned = new ArrayList<>();
        Integer cont = 0;

        for (Map.Entry<X, ArrayList<Y>> entry : inner.entrySet()) {
            for (Y y : entry.getValue()) {
                toBeReturned.add(y);
                cont++;
                if (cont == n) break;
            }

            if (cont == n) break;
        }
        return toBeReturned;
    }

    private synchronized ArrayList<Y> extract(NavigableMap<X, ArrayList<Y>> map) {
        return map.values().stream().flatMap(Collection::stream).collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Y> allAfter(X start) {
        if (inner.isEmpty()) return new ArrayList<>();

        Assert.isNotNull(inner, "Not initialized");
        X key = cleanKey(start);
        return extract(inner.tailMap(key, false));
    }

    public List<Y> inWindow(Ventana window) {
        return inWindow((X) window.getInicio(), (X) window.getFin());
    }

    public synchronized ArrayList<Y> inWindow(X start, X end) {
        Assert.isTrue(start == null || start.compareTo(end) <= 0, "Ventana invalida");
        Assert.isNotNull(inner, "Not initialized");

        if (inner.isEmpty()) return new ArrayList<>();

        if (inner.firstKey().compareTo(end) > 0) {
            return new ArrayList<>();
        }

        if (start != null && start.compareTo(inner.lastKey()) > 0) {
            return new ArrayList<>();
        }

        X startKey = cleanKey(start);
        X endKey = cleanKey(end);

        if (startKey.equals(endKey)) {
            if (!inner.containsKey(startKey)) {
                return new ArrayList<>();
            }
            return new ArrayList<>(inner.get(startKey));
        } else {
            return extract(inner.tailMap(startKey, false).headMap(endKey, true));
        }
    }

    private X cleanKey(X key) {
        X firstKey = inner.firstKey();

        if (key == null) {
            return firstKey;
        }

        if (key.compareTo(firstKey) < 0) {
            return firstKey;
        }

        X lastKey = inner.lastKey();

        if (key.compareTo(lastKey) > 0) {
            return lastKey;
        }

        return key;
    }

    public void deleteBeforeOrEqual(X limit) {
        if (inner.isEmpty()) {
            return;
        }

        if (inner.firstKey().compareTo(limit) > 0) {
            return;
        }

        List<X> toBeDeleted = new ArrayList<>();

        for (X x : this.inner.keySet()) {
            if (x.compareTo(limit) <= 0) {
                toBeDeleted.add(x);
            }
        }

        for (X x : toBeDeleted) {
            inner.remove(x);
        }

    }

    public SortedSimpleList sub(Ventana window) {
        return sub((X) window.getInicio(), (X) window.getFin());
    }

    public SortedSimpleList sub(X start, X end) {
        SortedSimpleList simpleList = SortedSimpleList.create();

        Assert.isTrue(start == null || start.compareTo(end) <= 0, "Ventana invalida");
        Assert.isNotNull(inner, "Not initialized");

        if (inner.isEmpty()) return simpleList;

        if (inner.firstKey().compareTo(end) > 0) {
            return simpleList;
        }

        if (start != null && start.compareTo(inner.lastKey()) > 0) {
            return simpleList;
        }

        X startKey = cleanKey(start);
        X endKey = cleanKey(end);

        if (startKey.equals(endKey)) {
            if (!inner.containsKey(startKey)) {
                return simpleList;
            }
            simpleList.inner = new TreeMap(inner.tailMap(start, true).headMap(start, true));
        } else {
            simpleList.inner = new TreeMap(inner.tailMap(startKey, false).headMap(endKey, true));
        }

        return simpleList;

    }
}
