/*
 * Copyright © 2021-2022, RezzedUp <https://github.com/LeafCommunity/Persistence>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
/**
 * community.leaf.persistence:<b>json-persistent-data-container</b>
 */
module community.leaf.persistence.json
{
    requires static pl.tlinkowski.annotation.basic;
    
    requires community.leaf.persistence;
    requires gson;
    requires org.bukkit;
    
    exports community.leaf.persistence.json;
}
