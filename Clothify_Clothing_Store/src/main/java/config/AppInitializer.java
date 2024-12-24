package config;

import util.HibernateUtil;

public class AppInitializer {
    public static void initialize() {
        HibernateUtil.getInstance();
    }

    public static void shutdown() {
        HibernateUtil.getInstance().shutdown();
    }
}
