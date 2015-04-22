package org.kudrenko.telegram.ui.chat;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;
import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TdApi;
import org.kudrenko.telegram.R;
import org.kudrenko.telegram.adapters.AbsPagingAdapter;
import org.kudrenko.telegram.ui.common.AbsRefreshableActivity;
import org.kudrenko.telegram.ui.drawer.DrawerWorker;

@EActivity(R.layout.activity_chats)
public class ChatsActivity extends AbsRefreshableActivity<TdApi.Chat, ChatsActivity.ChatsAdapter.ViewHolder, ChatsActivity.ChatsAdapter> {

    @Bean
    DrawerWorker drawerWorker;

    @ViewById
    TextView title;

    @Override
    protected void afterViews() {
        drawerWorker.initDrawer();
        title.setText(R.string.title_chat);
        super.afterViews();
    }

    @Override
    protected void update(final boolean reload) {
        application.send(new TdApi.GetChats(reload ? 0 : adapter.getCount(), LIMIT), new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.TLObject object) {
                if (object instanceof TdApi.Chats) {
                    setData(((TdApi.Chats) object).chats, reload);
                }
                refreshLayout.setRefreshing(false);
                Log.i(TAG + " - GetChats", String.valueOf(object));
            }
        });
    }

    @Override
    protected ChatsAdapter createAdapter() {
        return new ChatsAdapter(this);
    }

    static class ChatsAdapter extends AbsPagingAdapter<TdApi.Chat, ChatsAdapter.ViewHolder> {

        public ChatsAdapter(Context mContext) {
            super(mContext);
        }

        @Override
        protected void bindView(ViewHolder viewHolder, TdApi.Chat item_) {
            viewHolder.chatName.setText(getChatName(item_));
            viewHolder.chatLastMessage.setText(getChatLastMessage(item_));
            displayImage(getChatAvatar(item_), viewHolder.chatAvatar);
        }

        @Override
        protected void bindHolder(ViewHolder viewHolder, View convertView) {
            viewHolder.chatName = (TextView) convertView.findViewById(R.id.chat_name);
            viewHolder.chatLastMessage = (TextView) convertView.findViewById(R.id.chat_last_message);

            viewHolder.chatAvatar = (ImageView) convertView.findViewById(R.id.chat_avatar);
        }

        @Override
        protected int layout() {
            return R.layout.item_chat;
        }

        @Override
        protected ViewHolder createHolder() {
            return new ViewHolder();
        }

        private String getChatName(TdApi.Chat chat) {
            TdApi.ChatInfo type = chat.type;
            if (type instanceof TdApi.GroupChatInfo) {
                TdApi.GroupChat groupChat = ((TdApi.GroupChatInfo) type).groupChat;
                return groupChat.title;
            } else if (type instanceof TdApi.PrivateChatInfo) {
                TdApi.User user = ((TdApi.PrivateChatInfo) type).user;
                return user.lastName + user.firstName;
            }
            return "";
        }

        private String getChatLastMessage(TdApi.Chat chat) {
            TdApi.MessageContent messageContent = chat.topMessage.message;
            if (messageContent instanceof TdApi.MessageText) {
                return ((TdApi.MessageText) messageContent).text;
            } else return "Content";
        }

        private String getChatAvatar(TdApi.Chat chat) {
            TdApi.ChatInfo type = chat.type;
            if (type instanceof TdApi.GroupChatInfo) {
                TdApi.GroupChat groupChat = ((TdApi.GroupChatInfo) type).groupChat;
                return groupChat.photoSmall.toString();
            } else if (type instanceof TdApi.PrivateChatInfo) {
                TdApi.User user = ((TdApi.PrivateChatInfo) type).user;
                return user.photoSmall.toString();
            }
            return "";
        }

        static class ViewHolder {
            TextView chatName;
            TextView chatLastMessage;

            ImageView chatAvatar;
        }
    }

    @ItemClick(android.R.id.list)
    void onChatSelect(TdApi.Chat chat) {
        ChatActivity_.intent(this).chatId(chat.id).lastMessage(chat.lastReadInboxMessageId).start();
    }
}
