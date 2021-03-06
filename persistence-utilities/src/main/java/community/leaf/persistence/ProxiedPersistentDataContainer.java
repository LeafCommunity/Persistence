/*
 * Copyright © 2021-2022, RezzedUp <https://github.com/LeafCommunity/Persistence>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.persistence;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import pl.tlinkowski.annotation.basic.NullOr;

import java.util.Set;

public abstract class ProxiedPersistentDataContainer implements PersistentDataContainer
{
    protected abstract PersistentDataContainer container();
    
    @Override
    public <T, Z> void set(NamespacedKey key, PersistentDataType<T, Z> type, Z value)
    {
        container().set(key, type, value);
    }
    
    @Override
    public <T, Z> boolean has(NamespacedKey key, PersistentDataType<T, Z> type)
    {
        return container().has(key, type);
    }
    
    @Override
    public <T, Z> @NullOr Z get(NamespacedKey key, PersistentDataType<T, Z> type)
    {
        return container().get(key, type);
    }
    
    @Override
    public <T, Z> Z getOrDefault(NamespacedKey key, PersistentDataType<T, Z> type, Z publicValue)
    {
        return container().getOrDefault(key, type, publicValue);
    }
    
    @Override
    public Set<NamespacedKey> getKeys()
    {
        return container().getKeys();
    }
    
    @Override
    public void remove(NamespacedKey key)
    {
        container().remove(key);
    }
    
    @Override
    public boolean isEmpty()
    {
        return container().isEmpty();
    }
    
    @Override
    public PersistentDataAdapterContext getAdapterContext()
    {
        return container().getAdapterContext();
    }
}
