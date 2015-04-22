package org.kudrenko.telegram;

import android.app.Application;
import android.util.Log;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EApplication;
import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TG;
import org.drinkless.td.libcore.telegram.TdApi;
import org.kudrenko.telegram.otto.OttoBus;
import org.kudrenko.telegram.otto.events.AuthStateUpdateEvent;
import org.kudrenko.telegram.storage.CountriesDatabaseHelper;

@EApplication
public class TelegramApplication extends Application {
    public static final String TAG = TelegramApplication.class.getName();

    private Client client;
    private TdApi.AuthState authState;
    private CountriesDatabaseHelper countriesDatabaseHelper;

    @Bean
    OttoBus ottoBus;

    @Override
    public void onCreate() {
        super.onCreate();

        initClient();
        getCurrentAuthState();
        countriesDatabaseHelper = new CountriesDatabaseHelper(this);
    }

    private void initClient() {
        TG.setDir(getFilesDir().getPath());
        TG.setUpdatesHandler(new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.TLObject object) {
                Log.i(TAG + " - setUpdatesHandler", object.toString());
            }
        });
        client = TG.getClientInstance();
    }

    private void getCurrentAuthState() {
        client.send(new TdApi.AuthGetState(), new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.TLObject object) {
                onAuthStateUpdate((TdApi.AuthState) object);
            }
        });
    }

    private void onAuthStateUpdate(TdApi.AuthState authState) {
        this.authState = authState;
        ottoBus.post(new AuthStateUpdateEvent(authState));
    }

    public void send(TdApi.TLFunction function, final Client.ResultHandler handler) {
        if (client != null) {
            client.send(function, new Client.ResultHandler() {
                @Override
                public void onResult(TdApi.TLObject object) {
                    if (object instanceof TdApi.AuthState)
                        onAuthStateUpdate((TdApi.AuthState) object);

                    if (handler != null)
                        handler.onResult(object);
                }
            });
        }
    }
}
