/*
 * Copyright © 2021-2022, RezzedUp <https://github.com/LeafCommunity/Persistence>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.persistence;

import community.leaf.persistence.keys.Namespaced;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import pl.tlinkowski.annotation.basic.NullOr;

import java.util.Objects;
import java.util.stream.Stream;

public interface PersistentNamespaceData extends PersistentDataContainer
{
    static PersistentNamespaceData of(Namespaced namespace, PersistentDataContainer container)
    {
        return new PersistentNamespaceDataImpl(namespace, container);
    }
    
    static PersistentNamespaceData of(Namespaced namespace, PersistentDataHolder holder)
    {
        Objects.requireNonNull(holder, "holder");
        return of(namespace, holder.getPersistentDataContainer());
    }
    
    Namespaced namespace();
    
    default <T, Z> void set(String key, PersistentDataType<T, Z> type, Z value)
    {
        set(namespace().key(key), type, value);
    }
    
    default <T, Z> boolean has(String key, PersistentDataType<T, Z> type)
    {
        return has(namespace().key(key), type);
    }
    
    default <T, Z> @NullOr Z get(String key, PersistentDataType<T, Z> type)
    {
        return get(namespace().key(key), type);
    }
    
    default <T, Z> Z getOrDefault(String key, PersistentDataType<T, Z> type, Z defaultValue)
    {
        return getOrDefault(namespace().key(key), type, defaultValue);
    }
    
    default void remove(String key)
    {
        remove(namespace().key(key));
    }
    
    default Stream<NamespacedKey> keys()
    {
        return getKeys().stream();
    }
    
    default Stream<NamespacedKey> keysInNamespace()
    {
        return keys().filter(namespace()::isWithinNamespace);
    }
}
