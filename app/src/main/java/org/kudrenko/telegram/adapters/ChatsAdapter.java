package org.kudrenko.telegram.adapters;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.EBean;
import org.drinkless.td.libcore.telegram.TdApi;
import org.kudrenko.telegram.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@EBean
public class ChatsAdapter extends AbsFileContainerPagingAdapter<TdApi.Chat, ChatsAdapter.ViewHolder> {

    public ChatsAdapter() {
        super();
    }

    @Override
    public void updateFile(TdApi.FileLocal file) {
        for (TdApi.Chat chat : items) {
            TdApi.ChatInfo type = chat.type;
            TdApi.File chatFile;
            if (type instanceof TdApi.GroupChatInfo) {
                TdApi.GroupChat groupChat = ((TdApi.GroupChatInfo) type).groupChat;
                chatFile = groupChat.photoSmall;

                if (chatFile.getConstructor() == TdApi.FileEmpty.CONSTRUCTOR && ((TdApi.FileEmpty) chatFile).id == file.id) {
                    groupChat.photoSmall = file;
                    break;
                }
            } else if (type instanceof TdApi.PrivateChatInfo) {
                TdApi.User user = ((TdApi.PrivateChatInfo) type).user;
                chatFile = user.photoSmall;

                if (chatFile.getConstructor() == TdApi.FileEmpty.CONSTRUCTOR && ((TdApi.FileEmpty) chatFile).id == file.id) {
                    user.photoSmall = file;
                    break;
                }
            }
        }
    }

    @Override
    protected void bindView(ViewHolder viewHolder, TdApi.Chat item_) {
        viewHolder.chatName.setText(getChatName(item_));
        viewHolder.chatLastMessage.setText(getChatLastMessage(item_));
        viewHolder.chatTime.setText(convertTime(item_.topMessage.forwardDate));
        displayImage(getChatAvatar(item_), viewHolder.chatAvatar);
    }

    @Override
    protected void bindHolder(ViewHolder viewHolder, View convertView) {
        viewHolder.chatName = (TextView) convertView.findViewById(R.id.chat_name);
        viewHolder.chatLastMessage = (TextView) convertView.findViewById(R.id.chat_last_message);
        viewHolder.chatTime = (TextView) convertView.findViewById(R.id.chat_time);

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

    private TdApi.File getChatAvatar(TdApi.Chat chat) {
        TdApi.ChatInfo type = chat.type;
        if (type instanceof TdApi.GroupChatInfo) {
            TdApi.GroupChat groupChat = ((TdApi.GroupChatInfo) type).groupChat;
            return groupChat.photoSmall;
        } else if (type instanceof TdApi.PrivateChatInfo) {
            TdApi.User user = ((TdApi.PrivateChatInfo) type).user;
            return user.photoSmall;
        }
        return null;
    }

    public static class ViewHolder {
        TextView chatName;
        TextView chatLastMessage;
        TextView chatTime;

        ImageView chatAvatar;
    }

    private String convertTime(int forwardDate) {
        Date date = new Date(forwardDate);
        return SimpleDateFormat.getTimeInstance(DateFormat.SHORT).format(date);
    }
}
