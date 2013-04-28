package com.russia.meetster.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public final class YLUtils {
	public static <T extends Object> String join(String joiner, List<T> array) {
		String result = "";
		for (int i = 0; i < array.size(); i++) {
			String string = String.valueOf(array.get(i));
			if (i != 0) {
				result += joiner;
			}
			result += string;
		}
		return result;
	}
	
	public static String join(String joiner, String[] array) {
		return join(joiner, Arrays.asList(array));
	}
	
	public static String fillTemplate(String template, HashMap<String,String> mappings) {
		// Takes a format string in the form "...{variable name}..."
		String result = new String(template);
		for (Entry<String, String> entry : mappings.entrySet()) {
			String keyRegex = Pattern.quote("{" + entry.getKey() + "}");
			String valueString = entry.getValue();
			result = result.replaceAll(keyRegex, valueString);
		}
		return result;
	}
}
