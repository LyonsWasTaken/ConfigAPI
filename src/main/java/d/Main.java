package d;

import dev.lyons.configapi.ConfigAPI;
import dev.lyons.configapi.ConfigClass;
import dev.lyons.configapi.EnumFormattingType;
import dev.lyons.configapi.annotations.Config;

import java.io.File;
import java.lang.reflect.Field;

public class Main implements ConfigClass {

	@Config
	String test = "i like this config library its really cool!";
	@Config
	int i = 0;
	@Config
	double d = 0.0d;
	@Config
	int j = 0;

	public static void main(String[] args) {
		Main main = new Main();
		for (Field field : Main.class.getDeclaredFields()) {
			try {
				field.setAccessible(true);
				System.out.println("FIELD NAME: " + field.getName() + "\t\t" + "FIELD DATA: " + field.get(main));
			} catch (Throwable ignored) {

			}
		}
		for (int i = 0; i < 3; ++i) {
			System.out.println();
		}
		ConfigAPI.getInstance().register(main);
		ConfigAPI.getInstance().loadAll();
		ConfigAPI.getInstance().saveAll();
		for (Field field : Main.class.getDeclaredFields()) {
			try {
				field.setAccessible(true);
				System.out.println("FIELD NAME: " + field.getName() + "\t\t" + "FIELD DATA: " + field.get(main));
			} catch (Throwable ignored) {

			}
		}
	}

	@Override
	public EnumFormattingType getFormatType() {
		return EnumFormattingType.YAML;
	}

	@Override
	public File getFile() {
		return new File(new File("."), "configclassthing." + getFormatType().name().toLowerCase());
	}
}
