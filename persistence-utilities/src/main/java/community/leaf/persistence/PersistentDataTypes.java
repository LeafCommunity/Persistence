/*
 * Copyright © 2021-2022, RezzedUp <https://github.com/LeafCommunity/Persistence>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.persistence;

import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;
import java.util.function.Function;

public class PersistentDataTypes
{
    private PersistentDataTypes() { throw new UnsupportedOperationException(); }
    
    private static <T> PersistentDataType<T, T> primitive(Class<T> clazz)
    {
        return Persistent.dataType(clazz, clazz, Function.identity(), Function.identity());
    }
    
    public static final PersistentDataType<Byte, Byte> BYTE = primitive(Byte.class);
    
    public static final PersistentDataType<Short, Short> SHORT = primitive(Short.class);
    
    public static final PersistentDataType<Integer, Integer> INTEGER = primitive(Integer.class);
    
    public static final PersistentDataType<Long, Long> LONG = primitive(Long.class);
    
    public static final PersistentDataType<Float, Float> FLOAT = primitive(Float.class);
    
    public static final PersistentDataType<Double, Double> DOUBLE = primitive(Double.class);
    
    public static final PersistentDataType<String, String> STRING = primitive(String.class);
    
    public static final PersistentDataType<byte[], byte[]> BYTE_ARRAY = primitive(byte[].class);
    
    public static final PersistentDataType<int[], int[]> INTEGER_ARRAY = primitive(int[].class);
    
    public static final PersistentDataType<long[], long[]> LONG_ARRAY = primitive(long[].class);
    
    public static final PersistentDataType<PersistentDataContainer[], PersistentDataContainer[]> TAG_CONTAINER_ARRAY =
        primitive(PersistentDataContainer[].class);
    
    public static final PersistentDataType<PersistentDataContainer, PersistentDataContainer> TAG_CONTAINER =
        primitive(PersistentDataContainer.class);
    
    public static final PersistentDataType<Byte, Boolean> BOOLEAN =
        Persistent.dataType(
            Byte.class,
            Boolean.class,
            complex -> (byte) ((complex) ? 1 : 0),
            primitive -> primitive != 0
        );
    
    public static final PersistentDataType<long[], UUID> UUID =
        Persistent.dataType(
            long[].class,
            java.util.UUID.class,
            complex -> new long[] { complex.getMostSignificantBits(), complex.getLeastSignificantBits() },
            primitive -> new UUID(primitive[0], primitive[1])
        );
}
