package dev.lyons.configapi.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;

public final class ConfigFormatting {

	public static boolean isJsonString(String s) {
		try {
			new JSONObject(s);
			return true;
		} catch (JSONException e) {
			return false;
		} catch (Throwable err) {
			throw new RuntimeException(err);
		}
	}

	public static boolean isYAMLString(String s) {
		Yaml yaml = new Yaml();
		Object obj = yaml.load(s);
		return obj != null;
	}

	private static String json2yaml(String json) throws InvalidFormatType {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> obj = null;
		try {
			obj = mapper.readValue(json, new TypeReference<Map<String, Object>>() {
			});
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
		DumperOptions opts = new DumperOptions();
		opts.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		return new Yaml(opts).dump(obj);
	}

	private static String yaml2json(String yaml) throws InvalidFormatType {
		ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
		Object obj = null;
		try {
			obj = yamlReader.readValue(yaml, Object.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
		ObjectMapper jsonWriter = new ObjectMapper();
		try {
			return jsonWriter.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public static String toJson(String s) throws InvalidFormatType {
		if (isJsonString(s)) {
			return s;
		}
		if (isYAMLString(s)) {
			return yaml2json(s);
		} else {
			throw new InvalidFormatType();
		}
	}

	public static String toYaml(String s) throws InvalidFormatType {
		if (isJsonString(s)) {
			return json2yaml(s);
		} else if (isYAMLString(s)) {
			return s;
		} else {
			throw new InvalidFormatType();
		}
	}

	public static class InvalidFormatType extends Throwable {

	}

}
