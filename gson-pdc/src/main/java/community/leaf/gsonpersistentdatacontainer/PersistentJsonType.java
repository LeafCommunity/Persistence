/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/GsonPersistentDataContainer>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.gsonpersistentdatacontainer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.persistence.PersistentDataContainer;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@SuppressWarnings("NullableProblems")
public class PersistentJsonType
{
	private PersistentJsonType() { throw new UnsupportedOperationException(); }
	
	public static final JsonCompatiblePrimitive<Byte> BYTE =
		JsonCompatiblePrimitive.of(Byte.class, JsonElement::getAsByte, JsonPrimitiveSetter.number());
	
	public static final JsonCompatiblePrimitive<String> STRING =
		JsonCompatiblePrimitive.of(String.class, JsonElement::getAsString, JsonObject::addProperty);
	
	public static final JsonCompatiblePrimitive<PersistentDataContainer> TAG_CONTAINER =
		JsonCompatiblePrimitive.of(
			PersistentDataContainer.class,
			(element) -> new GsonPersistentDataContainer(element.getAsJsonObject()),
			(object, key, primitive) -> object.add(key, ((GsonPersistentDataContainer) primitive).json())
		);
	
	public static final JsonCompatiblePrimitive<PersistentDataContainer[]> TAG_CONTAINER_ARRAY =
		JsonCompatiblePrimitive.of(
			PersistentDataContainer[].class,
			(element) ->
			{
				List<PersistentDataContainer> containers = new ArrayList<>();
				for (JsonElement item : element.getAsJsonArray()) { containers.add(TAG_CONTAINER.getFromJson(item)); }
				return containers.toArray(PersistentDataContainer[]::new);
			},
			(object, key, primitive) ->
			{
				JsonArray array = new JsonArray();
				
				Arrays.stream(primitive)
				.map(container -> (GsonPersistentDataContainer) container)
				.map(GsonPersistentDataContainer::json)
				.forEach(array::add);
				
				object.add(key, array);
			}
		);
	
	public static final Map<Class<?>, JsonCompatiblePrimitive<?>> TYPES;
	
	static
	{
		Map<Class<?>, JsonCompatiblePrimitive<?>> compatible = new LinkedHashMap<>();
		
		Arrays.stream(PersistentJsonType.class.getDeclaredFields())
			.filter(field -> Modifier.isStatic(field.getModifiers()))
			.filter(field -> JsonCompatiblePrimitive.class.isAssignableFrom(field.getType()))
			.flatMap(field -> {
				try { return Stream.of((JsonCompatiblePrimitive<?>) field.get(null)); }
				catch (IllegalAccessException e) { return Stream.empty(); }
			})
			.forEach(primitive -> compatible.put(primitive.getPrimitiveType(), primitive));
		
		TYPES = Map.copyOf(compatible);
	}
}
