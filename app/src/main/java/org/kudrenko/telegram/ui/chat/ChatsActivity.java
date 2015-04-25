package org.kudrenko.telegram.ui.chat;

import android.text.TextUtils;
import android.widget.TextView;

import com.mikepenz.materialdrawer.Drawer;
import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TdApi;
import org.kudrenko.telegram.R;
import org.kudrenko.telegram.adapters.ChatsAdapter;
import org.kudrenko.telegram.model.Profile;
import org.kudrenko.telegram.otto.events.UpdateFileEvent;
import org.kudrenko.telegram.ui.common.AbsRefreshableActivity;
import org.kudrenko.telegram.ui.drawer.DrawerWorker;
import org.kudrenko.telegram.utils.NetworkHelper;

@EActivity(R.layout.activity_chats)
public class ChatsActivity extends AbsRefreshableActivity<TdApi.Chat, ChatsAdapter.ViewHolder, ChatsAdapter> {

    @Bean
    DrawerWorker drawerWorker;

    @ViewById
    TextView title;

    @Bean
    ChatsAdapter chatsAdapter;

    protected Drawer.Result drawerResult;

    @Override
    protected void afterViews() {
        super.afterViews();
        title.setText(R.string.title_chat);
    }

    private void loadUserInfo() {
        if (drawerResult == null)
            send(new TdApi.GetMe(), resultHandler(new Client.ResultHandler() {
                @Override
                public void onResult(TdApi.TLObject object) {
                    if (object.getConstructor() == TdApi.User.CONSTRUCTOR) {
                        initDrawer((TdApi.User) object);
                    }
                }
            }));
    }

    @UiThread
    protected void initDrawer(TdApi.User user) {
        String username = TextUtils.isEmpty(user.firstName) ? user.username : user.firstName + " " + user.lastName;
        drawerResult = drawerWorker.initDrawer(new Profile(username, user.phoneNumber, user.photoSmall));
    }

    @Override
    protected void update(final boolean reload) {
        send(new TdApi.GetChats(reload ? 0 : adapter.getCount(), LIMIT), resultHandler(new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.TLObject object) {
                if (object instanceof TdApi.Chats) {
                    setData(((TdApi.Chats) object).chats, reload);
                    loadUserInfo();
                }
                refreshLayout.setRefreshing(false);
            }
        }));
    }

    @Override
    protected ChatsAdapter createAdapter() {
        return chatsAdapter;
    }

    @Subscribe
    public void onFileUpdate(UpdateFileEvent event) {
        adapter.updateFile(event.file);
    }

    @ItemClick(android.R.id.list)
    void onChatSelect(TdApi.Chat chat) {
//        ChatActivity_.intent(this).chatId(chat.id).lastMessage(chat.lastReadInboxMessageId).start();
    }

    @Receiver(actions = {"android.net.conn.CONNECTIVITY_CHANGE", "android.net.wifi.WIFI_STATE_CHANGED"})
    void onConnectionChange() {
        if (NetworkHelper.isOnline(this)) {
            title.setText(R.string.title_chat);
        } else title.setText(R.string.waiting_for_network);
    }
}
