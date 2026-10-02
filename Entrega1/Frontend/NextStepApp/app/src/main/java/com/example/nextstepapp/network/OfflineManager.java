package com.example.nextstepapp.network;

import android.content.Context;
import android.content.SharedPreferences;

public class OfflineManager {

    private static final String PREFERENCES =
            "proxima_etapa_preferences";

    private static final String OFFLINE_MODE =
            "offline_mode";

    private static SharedPreferences preferences;

    private OfflineManager() {
    }

    private static void iniciar(Context context) {

        if (preferences == null) {
            preferences =
                    context.getSharedPreferences(
                            PREFERENCES,
                            Context.MODE_PRIVATE
                    );
        }
    }

    public static void setOfflineMode(
            Context context,
            boolean offline
    ) {

        iniciar(context);

        preferences
                .edit()
                .putBoolean(
                        OFFLINE_MODE,
                        offline
                )
                .apply();
    }

    public static boolean isOffline(
            Context context
    ) {

        iniciar(context);

        return preferences.getBoolean(
                OFFLINE_MODE,
                false
        );
    }

    public static boolean isOnline(
            Context context
    ) {

        return !isOffline(context);
    }
}