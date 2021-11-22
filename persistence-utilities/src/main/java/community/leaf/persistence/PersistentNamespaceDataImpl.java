/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/Persistence>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.persistence;

import community.leaf.persistence.keys.Namespaced;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.Objects;

final class PersistentNamespaceDataImpl extends ProxiedPersistentDataContainer implements PersistentNamespaceData
{
	private final Namespaced namespace;
	private final PersistentDataContainer container;
	
	PersistentNamespaceDataImpl(Namespaced namespace, PersistentDataContainer container)
	{
		this.namespace = Objects.requireNonNull(namespace, "namespace");
		this.container = Objects.requireNonNull(container, "container");
	}
	
	@Override
	public Namespaced namespace() { return namespace; }
	
	@Override
	protected PersistentDataContainer container() { return container; }
}
