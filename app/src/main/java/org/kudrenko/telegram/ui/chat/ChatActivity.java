package org.kudrenko.telegram.ui.chat;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TdApi;
import org.kudrenko.telegram.R;
import org.kudrenko.telegram.adapters.AbsPagingAdapter;
import org.kudrenko.telegram.ui.common.AbsRefreshableActivity;

@EActivity(R.layout.activity_chat)
public class ChatActivity extends AbsRefreshableActivity<TdApi.Message, ChatActivity.MessageAdapter.ViewHolder, ChatActivity.MessageAdapter> {
    @Extra("chat")
    long chatId;

    @Extra("last_message")
    int lastMessage;

    @Override
    protected void afterViews() {
        super.afterViews();
    }

    @Override
    protected MessageAdapter createAdapter() {
        return new MessageAdapter();
    }

    @Override
    protected void update(final boolean reload) {
        application.send(new TdApi.GetChatHistory(chatId, lastMessage, reload ? 0 : adapter.getCount(), LIMIT), new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.TLObject object) {
                if (object instanceof TdApi.Messages) {
                    setData(((TdApi.Messages) object).messages, reload);
                }
                refreshLayout.setRefreshing(false);
                Log.i(TAG + " - GetChats", String.valueOf(object));
            }
        });
    }

    class MessageAdapter extends AbsPagingAdapter<TdApi.Message, MessageAdapter.ViewHolder> {
        public MessageAdapter() {
            super();
        }

        @Override
        protected void displayImage(TdApi.File file, ImageView imageView) {
//            ChatActivity.this.displayImage(file, imageView);
        }

        @Override
        protected void bindView(ViewHolder viewHolder, TdApi.Message item_) {
            viewHolder.messageText.setText(getMessageText(item_));
        }

        private String getMessageText(TdApi.Message message) {
            TdApi.MessageContent content = message.message;
            if (content instanceof TdApi.MessageText) {
                return ((TdApi.MessageText) content).text;
            }
            return "Content";
        }

        @Override
        protected void bindHolder(ViewHolder viewHolder, View convertView) {
            viewHolder.messageText = (TextView) convertView.findViewById(R.id.message_text);
        }

        @Override
        protected int layout() {
            return R.layout.item_message;
        }

        @Override
        protected ViewHolder createHolder() {
            return new ViewHolder();
        }

        class ViewHolder {
            TextView messageText;
        }
    }
}
