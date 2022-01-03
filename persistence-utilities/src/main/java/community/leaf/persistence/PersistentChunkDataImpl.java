/*
 * Copyright © 2021-2022, RezzedUp <https://github.com/LeafCommunity/Persistence>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.persistence;

import community.leaf.persistence.keys.Namespaced;
import org.bukkit.Chunk;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.Objects;

final class PersistentChunkDataImpl extends ProxiedPersistentDataContainer implements PersistentChunkData
{
    private final Namespaced namespace;
    private final Chunk chunk;
    
    PersistentChunkDataImpl(Namespaced namespace, Chunk chunk)
    {
        this.namespace = Objects.requireNonNull(namespace, "namespace");
        this.chunk = Objects.requireNonNull(chunk, "chunk");
    }
    
    @Override
    public Namespaced namespace() { return namespace; }
    
    @Override
    protected PersistentDataContainer container() { return chunk.getPersistentDataContainer(); }
    
    @Override
    public Chunk chunk() { return chunk; }
}
