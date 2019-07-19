package vedam.subkuch.ui.chat;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.List;

import vedam.subkuch.R;
import vedam.subkuch.db.chat.Chat;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.utils.AppPrefs;
import vedam.subkuch.utils.DateTimeUtils;
import vedam.subkuch.utils.UiUtil;


/**
 * Created by nadeemansari on 01/04/16.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private List<Chat> chats = new ArrayList<>();
    private Context context;

    ChatAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.fragment_chat_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Chat chat = chats.get(position);
        int status = chat.getStatus();

        String myId = AppPrefs.getPrefsUserId(context);
        if (status == Constants.CHAT_STATUS_DELIVERED)
            holder.ivSent.setImageResource(R.drawable.ic_done_all_black_18dp);
        else if (status == Constants.CHAT_STATUS_SENT_BUT_NOT_DELIVERED) {
            holder.ivSent.setImageResource(R.drawable.ic_done_black_18dp);
        } else if (status == Constants.CHAT_STATUS_NOT_SENT) {
            holder.ivSent.setImageResource(R.drawable.baseline_schedule_black_18);
        } else {
            holder.ivSent.setImageResource(R.drawable.ic_done_all_black_18dp);
        }

        String fromId = chat.getFromProfileId();
        // else
        // img_sent.setVisibility(View.GONE);
        if (fromId.equals(myId)) {
            holder.ivSent.setVisibility(View.VISIBLE);
            holder.llAlignment.setGravity(Gravity.END);
            holder.llAlignment.setBackgroundResource(R.drawable.white_absolute);
            holder.llLayout.setGravity(Gravity.END);
            holder.llRow.setGravity(Gravity.END);
        } else {
            holder.ivSent.setVisibility(View.GONE);
            holder.llAlignment.setGravity(Gravity.START);
            holder.llAlignment.setBackgroundResource(R.drawable.white_layer);
            holder.llLayout.setGravity(Gravity.START);
            holder.llRow.setGravity(Gravity.START);
        }
        UiUtil.setTextView(holder.tvMessage, chat.getMessage());
        try {
            holder.tvMessageDate.setVisibility(View.VISIBLE);
            UiUtil.setTextView(holder.tvMessageDate,
                    DateTimeUtils.getFormattedDate(Long.parseLong(chat.getTimeStamp()), DateTimeUtils.DATE_FORMAT_4));
        } catch (NumberFormatException e) {
            Crashlytics.logException(e);
            holder.tvMessageDate.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvMessage;
        private TextView tvMessageDate;
        LinearLayout llRow;
        LinearLayout llLayout;
        LinearLayout llAlignment;
        private ImageView ivSent;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            llAlignment = itemView.findViewById(R.id.ll_alignment);
            llLayout = itemView.findViewById(R.id.ll_container);
            llRow = itemView.findViewById(R.id.message_row_layout);
            ivSent = itemView.findViewById(R.id.chat_row_sent);
            tvMessage = itemView.findViewById(R.id.message_text);
            tvMessageDate = itemView.findViewById(R.id.message_text_date);
        }

        /*public <E> void bind(final E item, final int position, final OnListViewItemClickListener listener) {

            itemView.setOnClickListener(v -> {
                if (listener != null)
                    listener.onItemClick(item, position, itemView, null);
            });
        }*/
    }

    void setChat(List<Chat> chats) {
        this.chats = chats;
        notifyDataSetChanged();
    }
}
