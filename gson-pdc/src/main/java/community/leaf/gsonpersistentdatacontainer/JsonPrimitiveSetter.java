/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/GsonPersistentDataContainer>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.gsonpersistentdatacontainer;

import com.google.gson.JsonObject;

@FunctionalInterface
public interface JsonPrimitiveSetter<T>
{
	static <T extends Number> JsonPrimitiveSetter<T> number() { return JsonObject::addProperty; }
	
	void setInJson(JsonObject object, String key, T primitive);
}
