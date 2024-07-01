package de.tomasgng.utils.config;

import de.tomasgng.utils.config.pathproviders.SeasonDataPathProvider;
import de.tomasgng.utils.config.utils.ConfigExclude;
import de.tomasgng.utils.config.utils.ConfigPair;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SeasonDataManager {
    private final File folder = new File("plugins/DynamicSeasons");
    private final File configFile = new File("plugins/DynamicSeasons/data.yml");

    private YamlConfiguration cfg = YamlConfiguration.loadConfiguration(configFile);

    public SeasonDataManager() {
        createFiles();
    }

    private void createFiles() {
        if(!folder.exists())
            folder.mkdirs();

        if(configFile.exists())
            return;

        try {
            configFile.createNewFile();

            reload();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        setAllConfigPaths();
        save();
    }

    private void setAllConfigPaths() {
        List<ConfigPair> configPairs = new ArrayList<>();
        List<ConfigPair> commentConfigPairs = new ArrayList<>();
        List<Class> pathProviders = new ArrayList<>();

        pathProviders.add(SeasonDataPathProvider.class);

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

    private void reload() {
        cfg = YamlConfiguration.loadConfiguration(configFile);
    }

    private void save() {
        try {
            cfg.save(configFile);
            reload();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Object getObjectValue(ConfigPair pair) {
        return getObject(pair);
    }

    public String getStringValue(ConfigPair pair) {
        return getString(pair);
    }

    public boolean getBooleanValue(ConfigPair pair) {
        return getBoolean(pair);
    }

    public int getIntegerValue(ConfigPair pair) {
        return getInteger(pair);
    }

    public List<String> getStringListValue(ConfigPair pair) {
        return getStringList(pair);
    }

    private Object getObject(ConfigPair pair) {
        reload();
        return cfg.get(pair.getPath(), pair.getValue());
    }

    private String getString(ConfigPair pair) {
        reload();
        return cfg.getString(pair.getPath(), pair.getStringValue());
    }

    private boolean getBoolean(ConfigPair pair) {
        reload();
        return cfg.getBoolean(pair.getPath(), pair.getBooleanValue());
    }

    private int getInteger(ConfigPair pair) {
        reload();
        return cfg.getInt(pair.getPath(), pair.getIntegerValue());
    }

    private List<String> getStringList(ConfigPair pair) {
        reload();
        return cfg.getStringList(pair.getPath());
    }

    private void set(ConfigPair pair) {
        cfg.set(pair.getPath(), pair.getValue());

        if(pair.hasComments())
            cfg.setComments(pair.getPath(), pair.getComments());
    }

    private void setComments(ConfigPair pair) {
        cfg.setComments(pair.getPath(), pair.getComments());
    }

    public void set(ConfigPair pair, Object newValue) {
        cfg.set(pair.getPath(), newValue);
        save();
    }
}
