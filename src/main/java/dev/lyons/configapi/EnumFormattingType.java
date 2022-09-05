package dev.lyons.configapi;

import dev.lyons.configapi.util.ConfigFormatting;

public enum EnumFormattingType {
	YAML,
	JSON,
	;

	public String formatConfig(String configString) {
		try {
			switch (this) {
				case YAML:
					return ConfigFormatting.toYaml(configString);
				case JSON:
					return ConfigFormatting.toJson(configString);
			}
		} catch (Throwable err) {
			throw new RuntimeException(err);
		}
		return configString;
	}
}
