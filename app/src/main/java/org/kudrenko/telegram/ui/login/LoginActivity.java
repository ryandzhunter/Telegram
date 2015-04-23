package org.kudrenko.telegram.ui.login;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.drinkless.td.libcore.telegram.TdApi;
import org.kudrenko.telegram.R;
import org.kudrenko.telegram.otto.events.AuthStateUpdateEvent;
import org.kudrenko.telegram.ui.chat.ChatsActivity_;
import org.kudrenko.telegram.ui.common.AbsTelegramActivity;
import org.kudrenko.telegram.ui.login.fragment.AbsLoginFragment;
import org.kudrenko.telegram.ui.login.fragment.CodeInputFragment_;
import org.kudrenko.telegram.ui.login.fragment.NameInputFragment_;
import org.kudrenko.telegram.ui.login.fragment.PhoneInputFragment_;

@EActivity(R.layout.activity_login)
public class LoginActivity extends AbsTelegramActivity {
    public static final int PAGE_COUNT = 3;
    public static final int IDX_PAGE_PHONE = 0;
    public static final int IDX_PAGE_CODE = 1;
    public static final int IDX_PAGE_NAME = 2;

    @ViewById
    ViewPager pager;

    @ViewById(R.id.action_bar)
    LinearLayout actionBar;

    protected LoginFragmentsPagerAdapter adapter;

    @AfterViews
    void afterViews() {
        adapter = new LoginFragmentsPagerAdapter(this);
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(adapter);
        pager.setOffscreenPageLimit(PAGE_COUNT);
        adapter.onPageSelected(IDX_PAGE_PHONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        send(new TdApi.AuthGetState());
    }

    @Subscribe
    public void onAuthStateUpdate(AuthStateUpdateEvent event) {
        TdApi.AuthState state = event.state;
        onAuthStateSet(state);
    }

    private void onAuthStateSet(TdApi.AuthState state) {
        if (state.getConstructor() == TdApi.AuthStateWaitSetPhoneNumber.CONSTRUCTOR) {
            pager.setCurrentItem(IDX_PAGE_PHONE, true);
        } else if (state.getConstructor() == TdApi.AuthStateWaitSetCode.CONSTRUCTOR) {
            pager.setCurrentItem(IDX_PAGE_CODE, true);
        } else if (state.getConstructor() == TdApi.AuthStateWaitSetName.CONSTRUCTOR) {
            pager.setCurrentItem(IDX_PAGE_NAME, true);
        } else if (state.getConstructor() == TdApi.AuthStateOk.CONSTRUCTOR) {
            ChatsActivity_.intent(this).start();
            finish();
        }
    }

    public void showCountriesDialog() {
    }

    public void scrollBack() {
        //todo возможно, что после телефона будет ввод имени, а не кода
        int currentItem = pager.getCurrentItem();
        if (currentItem > 0) {
            pager.setCurrentItem(currentItem - 1, true);
            send(new TdApi.AuthReset());
        } else finish();
    }

    @Override
    public void onBackPressed() {
        scrollBack();
    }

    class LoginFragmentsPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {
        protected ActionBarWorker worker;

        public LoginFragmentsPagerAdapter(LoginActivity activity) {
            super(activity.getSupportFragmentManager());
            worker = new ActionBarWorker(activity);
        }

        @Override
        public Fragment getItem(int position) {
            return worker.create(position);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            worker.open(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    class ActionBarWorker implements View.OnClickListener {
        protected LoginActivity activity;
        protected LinearLayout actionBar;
        protected LoginFragmentsPagerAdapter adapter;

        public ActionBarWorker(LoginActivity activity) {
            this.activity = activity;
            this.actionBar = activity.actionBar;
            this.adapter = activity.adapter;
        }

        public void open(int position) {
            actionBar.removeAllViews();
            switch (position) {
                case IDX_PAGE_PHONE:
                    View.inflate(activity, R.layout.item_toolbar_login_phone, actionBar);
                    break;
                case IDX_PAGE_CODE:
                    View.inflate(activity, R.layout.item_toolbar_login_code, actionBar);
                    break;
                case IDX_PAGE_NAME:
                    View.inflate(activity, R.layout.item_toolbar_login_name, actionBar);
                    break;
            }
            View optionsIcon = actionBar.findViewById(R.id.menu_options_icon);
            if (optionsIcon != null)
                optionsIcon.setOnClickListener(this);

            View menuIcon = actionBar.findViewById(R.id.menu_icon);
            if (menuIcon != null)
                menuIcon.setOnClickListener(this);
        }

        public AbsLoginFragment create(int position) {
            switch (position) {
                case IDX_PAGE_PHONE:
                    return PhoneInputFragment_.builder().build();
                case IDX_PAGE_CODE:
                    return CodeInputFragment_.builder().build();
                case IDX_PAGE_NAME:
                    return NameInputFragment_.builder().build();
            }
            return null;
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.menu_options_icon:
                    activity.onConfirm();
                    break;
                case R.id.menu_icon:
                    activity.scrollBack();
                    break;
            }
        }
    }

    private void onConfirm() {
        AbsLoginFragment fragment = (AbsLoginFragment) getSupportFragmentManager().findFragmentByTag(
                "android:switcher:" + R.id.pager + ":" + pager.getCurrentItem());
        if (fragment != null) {
            fragment.onConfirm();
        }
    }
}
