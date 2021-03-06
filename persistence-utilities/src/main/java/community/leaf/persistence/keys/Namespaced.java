/*
 * Copyright © 2021-2022, RezzedUp <https://github.com/LeafCommunity/Persistence>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.persistence.keys;

import org.bukkit.NamespacedKey;

public interface Namespaced
{
    String namespace();
    
    NamespacedKey key(String key);
    
    default boolean isWithinNamespace(NamespacedKey key)
    {
        return namespace().equals(key.getNamespace());
    }
}
