package loggerplusplus;

import burp.IBurpExtenderCallbacks;
import com.coreyd97.BurpExtenderUtilities.IGsonProvider;
import com.coreyd97.BurpExtenderUtilities.ILogProvider;
import com.coreyd97.BurpExtenderUtilities.PreferenceFactory;
import com.google.gson.reflect.TypeToken;
import loggerplusplus.filter.ColorFilter;
import loggerplusplus.filter.LogFilter;
import loggerplusplus.filter.SavedFilter;
import loggerplusplus.userinterface.LogTableColumn;

import java.util.*;

import static loggerplusplus.Globals.*;

public class LoggerPreferenceFactory extends PreferenceFactory {

    private HashMap<UUID, ColorFilter> defaultColorFilters;
    private ArrayList<LogTableColumn> defaultlogTableColumns;

    public LoggerPreferenceFactory(IGsonProvider gsonProvider, ILogProvider logProvider, IBurpExtenderCallbacks callbacks){
        super("LoggerPlusPlus", gsonProvider, logProvider, callbacks);
    }

    public LoggerPreferenceFactory(IGsonProvider gsonProvider, IBurpExtenderCallbacks callbacks){
        super("LoggerPlusPlus", gsonProvider, callbacks);
    }

    @Override
    protected void createDefaults(){
        defaultColorFilters = this.gsonProvider.getGson().fromJson(
                Globals.DEFAULT_COLOR_FILTERS_JSON, new TypeToken<HashMap<UUID, ColorFilter>>(){}.getType());
        defaultlogTableColumns = this.gsonProvider.getGson().fromJson(
                Globals.DEFAULT_LOG_TABLE_COLUMNS_JSON, new TypeToken<List<LogTableColumn>>() {}.getType());
    }

    @Override
    protected void registerTypeAdapters(){
        this.gsonProvider.registerTypeAdapter(LogFilter.class, new LogFilter.FilterSerializer());
        this.gsonProvider.registerTypeAdapter(LogTableColumn.class, new LogTableColumn.ColumnSerializer());
    }

    @Override
    protected void registerSettings() {
        prefs.addGlobalSetting(PREF_LOG_TABLE_SETTINGS, new TypeToken<List<LogTableColumn>>() {}.getType(), defaultlogTableColumns);
        prefs.addGlobalSetting(PREF_LAST_USED_VERSION, Double.class, Globals.VERSION);
        prefs.addGlobalSetting(PREF_IS_DEBUG, Boolean.class, false);
        prefs.addGlobalSetting(PREF_UPDATE_ON_STARTUP, Boolean.class, true);
        prefs.addGlobalSetting(PREF_ENABLED, Boolean.class, true);
        prefs.addGlobalSetting(PREF_RESTRICT_TO_SCOPE, Boolean.class, false);
        prefs.addGlobalSetting(PREF_LOG_GLOBAL, Boolean.class, true);
        prefs.addGlobalSetting(PREF_LOG_PROXY, Boolean.class, true);
        prefs.addGlobalSetting(PREF_LOG_SPIDER, Boolean.class, true);
        prefs.addGlobalSetting(PREF_LOG_INTRUDER, Boolean.class, true);
        prefs.addGlobalSetting(PREF_LOG_SCANNER, Boolean.class, true);
        prefs.addGlobalSetting(PREF_LOG_REPEATER, Boolean.class, true);
        prefs.addGlobalSetting(PREF_LOG_SEQUENCER, Boolean.class, true);
        prefs.addGlobalSetting(PREF_LOG_EXTENDER, Boolean.class, true);
        prefs.addGlobalSetting(PREF_LOG_TARGET_TAB, Boolean.class, true);
        prefs.addGlobalSetting(PREF_COLOR_FILTERS, new TypeToken<Map<UUID, ColorFilter>>() {}.getType(), defaultColorFilters);
        prefs.addGlobalSetting(PREF_SAVED_FILTERS, new TypeToken<List<SavedFilter>>() {}.getType(), new ArrayList<SavedFilter>());
        prefs.addGlobalSetting(PREF_SORT_COLUMN, Integer.class, -1);
        prefs.addGlobalSetting(PREF_SORT_ORDER, String.class, "ASCENDING");
        prefs.addGlobalSetting(PREF_RESPONSE_TIMEOUT, Integer.class, 60000);
        prefs.addGlobalSetting(PREF_MAXIMUM_ENTRIES, Integer.class, 5000);
        prefs.addGlobalSetting(PREF_SEARCH_THREADS, Integer.class, 5);
        prefs.addGlobalSetting(PREF_AUTO_IMPORT_PROXY_HISTORY, Boolean.class, false);
        prefs.addGlobalSetting(PREF_LOG_OTHER_LIVE, Boolean.class, true);
        prefs.addGlobalSetting(PREF_ELASTIC_ADDRESS, String.class, "127.0.0.1");
        prefs.addGlobalSetting(PREF_ELASTIC_PORT, Integer.class, 9300);
        prefs.addGlobalSetting(PREF_ELASTIC_CLUSTER_NAME, String.class, "elasticsearch");
        prefs.addGlobalSetting(PREF_ELASTIC_INDEX, String.class, "logger");
        prefs.addGlobalSetting(PREF_ELASTIC_DELAY, Integer.class, 120);

        prefs.addVolatileSetting(PREF_AUTO_SAVE, Boolean.class, false);
        prefs.addVolatileSetting(PREF_AUTO_SCROLL, Boolean.class, true);
    }

}
