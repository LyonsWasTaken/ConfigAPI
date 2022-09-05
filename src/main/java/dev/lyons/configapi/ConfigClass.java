package dev.lyons.configapi;

import com.google.gson.*;
import dev.lyons.configapi.annotations.Config;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;

public interface ConfigClass extends Serializable {

	ExclusionStrategy exclusionStrategy = new ExclusionStrategy() {
		@Override
		public boolean shouldSkipField(FieldAttributes fieldAttributes) {
			if (ConfigClass.class.isAssignableFrom(fieldAttributes.getDeclaringClass())) {
				return fieldAttributes.getAnnotation(Config.class) == null;
			}
			return false;
		}

		@Override
		public boolean shouldSkipClass(Class<?> aClass) {
			return false;
		}
	};

	default Gson getGson() {
		return new GsonBuilder()
			.registerTypeAdapter(this.getClass(), (InstanceCreator<?>) type -> this)
			.setPrettyPrinting()
			.setExclusionStrategies(exclusionStrategy)
			.create();
	}

	default String getConfigData() throws IOException {
		return new String(Files.readAllBytes(this.getFile().toPath()));
	}

	EnumFormattingType getFormatType();

	File getFile();

	default void preInit() {

	}

	default void postInit() {

	}

}
