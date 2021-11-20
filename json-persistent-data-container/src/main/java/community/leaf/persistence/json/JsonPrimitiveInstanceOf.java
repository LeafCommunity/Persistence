package community.leaf.persistence.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.util.function.Predicate;

@FunctionalInterface
public interface JsonPrimitiveInstanceOf
{
	private static boolean ifPrimitive(JsonElement element, Predicate<JsonPrimitive> isInstance)
	{
		return element instanceof JsonPrimitive && isInstance.test((JsonPrimitive) element);
	}
	
	static JsonPrimitiveInstanceOf primitive(Predicate<JsonPrimitive> isInstance)
	{
		return element -> ifPrimitive(element, isInstance);
	}
	
	static JsonPrimitiveInstanceOf number()
	{
		return element -> ifPrimitive(element, JsonPrimitive::isNumber);
	}
	
	boolean isInstance(JsonElement element);
}
