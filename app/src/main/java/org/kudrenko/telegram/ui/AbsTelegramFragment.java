package org.kudrenko.telegram.ui;

import android.support.v4.app.Fragment;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EFragment;
import org.kudrenko.telegram.TelegramApplication;

@EFragment
public abstract class AbsTelegramFragment extends Fragment {
    @App
    protected TelegramApplication application;
}
