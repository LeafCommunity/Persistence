/*
 * Copyright © 2021-2022, RezzedUp <https://github.com/LeafCommunity/Persistence>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.persistence.keys;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

import java.util.Locale;
import java.util.Objects;

@FunctionalInterface
public interface PluginNamespace extends Namespaced
{
    static PluginNamespace of(Plugin plugin)
    {
        Objects.requireNonNull(plugin, "plugin");
        return () -> plugin;
    }
    
    Plugin plugin();
    
    @Override
    default String namespace()
    {
        return plugin().getName().toLowerCase(Locale.ROOT);
    }
    
    @Override
    default NamespacedKey key(String key)
    {
        return new NamespacedKey(plugin(), key);
    }
}
