package dev.lyons.configapi;

import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ConfigAPI implements Iterable<ConfigClass> {
	private static final ConfigAPI instance = new ConfigAPI();
	private static final Logger logger = Logger.getLogger(ConfigAPI.class.getName());

	public static ConfigAPI getInstance() {
		return instance;
	}

	private ConfigAPI() {

	}

	private static final Map<Object, ConfigClass> configMap = new LinkedHashMap<>();

	public <T extends ConfigClass> T getConfigObject(Object key) {
		Object obj = configMap.get(key);
		if (obj == null) return null;
		return (T) obj;
	}

	public <T extends ConfigClass> T getConfigObject(Class<T> key) {
		ConfigClass obj = configMap.get(key);
		if (obj == null) return null;
		return (T) obj;
	}

	public void register(final Object key, final ConfigClass config) {
		configMap.putIfAbsent(key, config);
	}

	public void register(final ConfigClass config) {
		configMap.putIfAbsent(config.getClass(), config);
	}

	public void loadSpecificConfig(final ConfigClass config, boolean preInit, boolean postInit) {
		if (preInit) {
			config.preInit();
		}
		block1:
		try {
			if (!config.getFile().exists() || config.getConfigData().isEmpty()) { // load defaults
				if (!config.getFile().exists()) {
					config.getFile().getParentFile().mkdirs();
					config.getFile().createNewFile();
				}
				saveConfig(config);
				break block1;
			}
			String configData = config.getConfigData();
			configData = EnumFormattingType.JSON.formatConfig(configData);
			config.getGson().fromJson(configData, config.getClass());
		} catch (final Throwable err) {
			logger.log(Level.SEVERE, "BROKEN CONFIG AT: " + config.getClass().getSimpleName());
			throw new RuntimeException(err);
		}
		if (postInit) {
			config.postInit();
		}
	}

	public void loadAll() {
		this.forEach(ConfigClass::preInit);
		for (final ConfigClass config : this) {
			loadSpecificConfig(config, false, false);
		}
		this.forEach(ConfigClass::postInit);
	}

	public void saveConfig(final ConfigClass config) {
		try {
			String configString = config.getGson().toJson(config);
			configString = config.getFormatType().formatConfig(configString);
			final File file = config.getFile();
			file.getParentFile().mkdirs();
			if (!file.exists()) {
				file.createNewFile();
			}
			final OutputStream os = Files.newOutputStream(config.getFile().toPath());
			os.write(configString.getBytes());
			os.close();
		} catch (final Throwable err) {
			throw new RuntimeException(err);
		}
	}

	public void saveAll() {
		this.forEach(this::saveConfig);
	}

	@Override
	public Iterator<ConfigClass> iterator() {
		return configMap.values().iterator();
	}
}
