package org.redex.backend.controller.simulacion.simulador;

import com.google.common.collect.TreeMultimap;
import org.redex.backend.controller.simulacion.Ventana;
import pe.albatross.zelpers.miscelanea.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NavigableMap;
import java.util.stream.Collectors;

public class SortedList<X extends Comparable, Y> {

    private TreeMultimap<X, Y> inner;

    public static SortedList create() {
        SortedList sl = new SortedList();
        sl.inner = TreeMultimap.create();
        return sl;
    }


    private X cleanKey(X key) {
        X firstKey = inner.asMap().firstKey();

        if (key == null) {
            return firstKey;
        }

        if (key.compareTo(firstKey) < 0) {
            return firstKey;
        }

        X lastKey = inner.asMap().lastKey();

        if (key.compareTo(lastKey) > 0) {
            return lastKey;
        }

        return key;
    }

    private List<Y> extract(NavigableMap<X, Collection<Y>> map) {
        return map.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    public List<Y> allAfter(X start) {
        Assert.isNotNull(inner, "Not initialized");
        X key = cleanKey(start);
        return extract(inner.asMap().tailMap(key, false));
    }

    public List<Y> inWindow(Ventana window) {
        return inWindow((X) window.getInicio(), (X) window.getFin());
    }

    public List<Y> inWindow(X start, X end) {
        Assert.isNotNull(inner, "Not initialized");
        X startKey = cleanKey(start);
        X endKey = cleanKey(end);

        if (startKey.equals(endKey)) {
            if(!inner.containsKey(startKey)){
                return new ArrayList<>();
            }
            return inner.asMap().get(startKey).stream().collect(Collectors.toList());
        } else {
            return extract(inner.asMap().tailMap(startKey, false).headMap(endKey, true));

        }
    }

    public void add(X key, Y obj) {
        Assert.isNotNull(inner, "Not initialized");
        inner.put(key, obj);
    }
}
