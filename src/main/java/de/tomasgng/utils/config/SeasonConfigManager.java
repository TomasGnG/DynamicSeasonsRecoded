package de.tomasgng.utils.config;

import de.tomasgng.utils.PluginLogger;
import de.tomasgng.utils.config.pathproviders.SeasonConfigPathProvider;
import de.tomasgng.utils.config.utils.ConfigExclude;
import de.tomasgng.utils.config.utils.ConfigPair;
import de.tomasgng.utils.enums.SeasonType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class SeasonConfigManager {

    private File currentConfigFile;

    private final File folder = new File("plugins/DynamicSeasons/seasons/");
    private final File springConfigFile = new File(folder.getPath() + "/spring_config.yml");
    private final File summerConfigFile = new File(folder.getPath() + "/summer_config.yml");
    private final File fallConfigFile = new File(folder.getPath() + "/fall_config.yml");
    private final File winterConfigFile = new File(folder.getPath() + "/winter_config.yml");

    private YamlConfiguration cfg = YamlConfiguration.loadConfiguration(springConfigFile);
    private final MiniMessage mm = MiniMessage.miniMessage();

    public SeasonConfigManager() {
        createFiles();
    }

    public void createFiles() {
        if(!folder.exists())
            folder.mkdirs();

        List<File> files = List.of(springConfigFile, summerConfigFile, fallConfigFile, winterConfigFile);

        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);

            if(file.exists()) {
                setConfigFile(SeasonType.values()[i]);
                setMissingConfigPaths();
                save();
                continue;
            }

            try {
                file.createNewFile();
                setConfigFile(SeasonType.values()[i]);
                setAllConfigPaths();
                save();
            } catch (IOException e) {
                PluginLogger.getInstance().error("Couldn't create file '" + file.getName() + "'!" + System.lineSeparator() + e.getLocalizedMessage());
            }
        }
    }

    private void setAllConfigPaths() {
        List<ConfigPair> configPairs = new ArrayList<>();
        List<ConfigPair> commentConfigPairs = new ArrayList<>();
        List<Class> pathProviders = new ArrayList<>();

        pathProviders.add(SeasonConfigPathProvider.class);

        pathProviders.forEach(pathProvider -> {
            List<Field> fieldList = Arrays.stream(pathProvider.getDeclaredFields()).filter(field -> Modifier.isStatic(field.getModifiers())).toList();
            for (Field field : fieldList) {
                try {
                    if(field.getAnnotation(ConfigExclude.class) == null)
                        configPairs.add((ConfigPair) field.get(ConfigPair.class));
                    else {
                        ConfigExclude configExclude = field.getAnnotation(ConfigExclude.class);

                        if(!configExclude.excludeComments())
                            commentConfigPairs.add((ConfigPair) field.get(ConfigPair.class));
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        configPairs.forEach(this::set);
        commentConfigPairs.forEach(this::setComments);
    }

    private void setMissingConfigPaths() {
        List<ConfigPair> configPairs = new ArrayList<>();
        List<ConfigPair> commentConfigPairs = new ArrayList<>();
        List<Class> pathProviders = new ArrayList<>();

        pathProviders.add(SeasonConfigPathProvider.class);

        pathProviders.forEach(pathProvider -> {
            List<Field> fieldList = Arrays.stream(pathProvider.getDeclaredFields()).filter(field -> Modifier.isStatic(field.getModifiers())).toList();
            for (Field field : fieldList) {
                try {
                    ConfigPair pair = (ConfigPair) field.get(ConfigPair.class);

                    if(field.getAnnotation(ConfigExclude.class) == null) {
                        if(field.getName().contains("EXAMPLE")) {
                            if(!cfg.isSet(pair.getPath().split("\\.")[0]))
                                configPairs.add(pair);
                        } else if(!cfg.isSet(pair.getPath()))
                            configPairs.add(pair);
                    } else {
                        ConfigExclude configExclude = field.getAnnotation(ConfigExclude.class);

                        if(!configExclude.excludeComments() && !cfg.isSet(pair.getPath()))
                            commentConfigPairs.add((ConfigPair) field.get(ConfigPair.class));
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        configPairs.forEach(this::set);
        commentConfigPairs.forEach(this::setComments);
    }

    private void reload() {
        cfg = YamlConfiguration.loadConfiguration(currentConfigFile);
    }

    private void save() {
        try {
            cfg.save(currentConfigFile);
            reload();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setConfigFile(SeasonType newSeasonType) {
        switch (newSeasonType) {
            case SPRING:
                currentConfigFile = springConfigFile;
                reload();
                break;
            case SUMMER:
                currentConfigFile = summerConfigFile;
                reload();
                break;
            case FALL:
                currentConfigFile = fallConfigFile;
                reload();
                break;
            case WINTER:
                currentConfigFile = winterConfigFile;
                reload();
                break;
        }
    }

    public Object getObjectValue(ConfigPair pair) {
        reload();
        return cfg.get(pair.getPath(), pair.getValue());
    }

    public String getStringValue(ConfigPair pair) {
        reload();
        return cfg.getString(pair.getPath(), pair.getStringValue());
    }

    public boolean getBooleanValue(ConfigPair pair) {
        reload();
        return cfg.getBoolean(pair.getPath(), pair.getBooleanValue());
    }

    public int getIntegerValue(ConfigPair pair) {
        reload();
        return cfg.getInt(pair.getPath(), pair.getIntegerValue());
    }

    public double getDoubleValue(ConfigPair pair) {
        reload();
        return cfg.getDouble(pair.getPath(), pair.getDoubleValue());
    }

    public Map<String, Object> getValuesFromBase(ConfigPair base) {
        ConfigurationSection section = cfg.getConfigurationSection(base.getPath());

        if(section == null)
            return new HashMap<>();

        return section.getValues(false);
    }

    public Set<String> getKeysFromBase(ConfigPair base) {
        ConfigurationSection section = cfg.getConfigurationSection(base.getPath());

        if(section == null)
            return new HashSet<>();

        return section.getKeys(false);
    }

    public List<String> getStringListValue(ConfigPair pair) {
        reload();
        return cfg.getStringList(pair.getPath());
    }

    public Component getComponentValue(ConfigPair pair) {
        String value = getStringValue(pair);

        if(value == null || value.isBlank())
            return null;

        try {
            return mm.deserialize(value);
        } catch (Exception e) {
            PluginLogger.getInstance().warn("The message {" + value + "} is not in MiniMessage format! Source (" + pair.getPath() + ")" + System.lineSeparator() + e.getMessage());
            return pair.getValue() != null ? mm.deserialize(pair.getStringValue()) : null;
        }
    }

    public SeasonType getCurrentSelectedSeasonType() {
        if(currentConfigFile.equals(summerConfigFile))
            return SeasonType.SUMMER;

        if(currentConfigFile.equals(fallConfigFile))
            return SeasonType.FALL;

        if(currentConfigFile.equals(winterConfigFile))
            return SeasonType.WINTER;

        if(currentConfigFile.equals(springConfigFile))
            return SeasonType.SPRING;

        return null;
    }

    private void set(ConfigPair pair) {
        cfg.set(pair.getPath(), pair.getValue());

        if(pair.hasComments())
            cfg.setComments(pair.getPath(), pair.getComments());
    }

    public void set(ConfigPair pair, Object newValue) {
        cfg.set(pair.getPath(), newValue);
        save();
    }

    private void setComments(ConfigPair pair) {
        if(pair.hasComments())
            cfg.setComments(pair.getPath(), pair.getComments());
    }

}
