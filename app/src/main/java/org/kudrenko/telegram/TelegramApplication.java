package org.kudrenko.telegram;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EApplication;
import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TG;
import org.drinkless.td.libcore.telegram.TdApi;
import org.kudrenko.telegram.otto.OttoBus;
import org.kudrenko.telegram.otto.events.AuthStateUpdateEvent;
import org.kudrenko.telegram.otto.events.UpdateFileEvent;
import org.kudrenko.telegram.storage.CountriesDatabaseHelper;

import java.io.File;
import java.io.IOException;

@EApplication
public class TelegramApplication extends Application {
    public static final String TAG = TelegramApplication.class.getName();

    private Client client;
    public CountriesDatabaseHelper helper;

    @Bean
    OttoBus ottoBus;

    @Override
    public void onCreate() {
        super.onCreate();

        initClient();
        initCountryDB();
        initDrawerImageLoader();
    }

    private void initDrawerImageLoader() {
        DrawerImageLoader.init(new DrawerImageLoader.IDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Picasso.with(imageView.getContext()).load(new File(uri.getPath())).placeholder(placeholder).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Picasso.with(imageView.getContext()).cancelRequest(imageView);
            }

            @Override
            public Drawable placeholder(Context ctx) {
                return null;
            }
        });
    }

    private void initCountryDB() {
        helper = new CountriesDatabaseHelper(this);
    }

    private void initClient() {
        setDir();

        TG.setUpdatesHandler(new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.TLObject object) {
                if (object.getConstructor() == TdApi.UpdateFile.CONSTRUCTOR) {
                    TdApi.UpdateFile file = (TdApi.UpdateFile) object;
                    ottoBus.post(new UpdateFileEvent(new TdApi.FileLocal(file.fileId, file.size, file.path)));
                }
                Log.i(TAG + " - setUpdatesHandler", object.toString());
            }
        });
        client = TG.getClientInstance();
    }

    private void setDir() {
        String path = getFilesDir().getPath();
        File file = new File(path);
        if (!file.exists())
            try {
                file.createNewFile();
            } catch (IOException ignored) {
                //nothing
            }

        TG.setDir(path + "/");
    }

    private void onAuthStateUpdate(TdApi.AuthState authState) {
        ottoBus.post(new AuthStateUpdateEvent(authState));
    }

    public void send(TdApi.TLFunction function) {
        send(function, null);
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
