package com.ootd.be.util;

import java.util.Collection;

public class CollectionUtils {

    public static int size(Collection<?> collection) {
        return collection == null ? 0 : collection.size();
    }

}
