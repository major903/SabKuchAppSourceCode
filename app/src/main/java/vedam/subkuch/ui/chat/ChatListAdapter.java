package vedam.subkuch.ui.chat;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.db.chat.Chat;
import vedam.subkuch.db.chat.ChatRepository;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.interfaces.OnListViewItemClickListener;
import vedam.subkuch.ui.matrimonial.models.DatingProfile;
import vedam.subkuch.utils.DateTimeUtils;
import vedam.subkuch.utils.ImageSetter;
import vedam.subkuch.utils.UiUtil;

import static vedam.subkuch.utils.AppUtil.deNull;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<DatingProfile> datingProfiles;
    private OnListViewItemClickListener listViewItemClickListener;
    private static ChatRepository chatRepository;

    ChatListAdapter(Context context, ArrayList<DatingProfile> datingProfiles, OnListViewItemClickListener listViewItemClickListener) {

        this.context = context;
        this.datingProfiles = datingProfiles;
        this.listViewItemClickListener = listViewItemClickListener;
        chatRepository = new ChatRepository(context);
    }

    @NonNull
    @Override
    public ChatListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.fragment_list_chat_list_item, parent, false);
        return new ChatListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListAdapter.ViewHolder holder, int position) {

        DatingProfile datingProfile = datingProfiles.get(position);
        /*chatRepository.getLatestChatMessage(datingProfile.getProfileId()).observe(lifecycleOwner,
                latestChatMessage -> bindValues(latestChatMessage, holder, datingProfile));*/

        new ChatAsyncTask(holder, datingProfile).execute();
//        Observable.fromCallable(() -> chatRepository.getLatestChatMessage(datingProfile.getProfileId()))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(latestChatMessage -> bindValues(latestChatMessage, holder, datingProfile), Crashlytics::logException);

        if (datingProfile.getImagesList() != null && datingProfile.getImagesList().length > 0)
            UiUtil.setImageView(new ImageSetter.ImageBuilder(context)
                    .setImageLink(datingProfile.getImagesList()[0].getImage())
                    .setPlaceholderResource(R.drawable.placeholder_small)
                    .setErrorResource(R.drawable.placeholder_small)
                    .setTarget(holder.ivProfile)
                    .build());
        else
            holder.ivProfile.setImageResource(R.drawable.placeholder_small);
        holder.bind(datingProfile, position, listViewItemClickListener);
    }

    private static void bindValues(Chat latestChatMessage, ViewHolder holder, DatingProfile datingProfile) {

        UiUtil.setTextView(holder.tvName, deNull(datingProfile.getFirstName()));
        if (latestChatMessage != null) {
            UiUtil.setTextView(holder.tvTime, DateTimeUtils.getFormattedDate(Long.parseLong(latestChatMessage.getTimeStamp()), DateTimeUtils.DATE_FORMAT_4));
            UiUtil.setTextView(holder.tvMessage, latestChatMessage.getMessage());

            if (latestChatMessage.getFromProfileId().equals(datingProfile.getProfileId()) &&
                    (latestChatMessage.getStatus() == Constants.CHAT_STATUS_SENT_BUT_NOT_DELIVERED
                            || latestChatMessage.getStatus() == Constants.CHAT_STATUS_NOT_SENT))
                holder.tvMessage.setTypeface(holder.tvMessage.getTypeface(), Typeface.BOLD);
            else
                holder.tvMessage.setTypeface(holder.tvMessage.getTypeface(), Typeface.NORMAL);

        } else {
            holder.tvMessage.setVisibility(View.GONE);
            holder.tvTime.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return datingProfiles.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivProfile;
        private TextView tvName;
        private TextView tvTime;
        private TextView tvMessage;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvMessage = itemView.findViewById(R.id.tv_message);
            ivProfile = itemView.findViewById(R.id.iv_profile);
        }

        public <E> void bind(final E item, final int position, final OnListViewItemClickListener listener) {

            itemView.setOnClickListener(v -> {
                if (listener != null)
                    listener.onItemClick(item, position, itemView, null);
            });
        }
    }

    private static class ChatAsyncTask extends AsyncTask<Void, Void, Chat> {

        private ViewHolder viewHolder;
        private DatingProfile datingProfile;

        ChatAsyncTask(ViewHolder viewHolder, DatingProfile datingProfile) {
            this.viewHolder = viewHolder;
            this.datingProfile = datingProfile;
        }

        @Override
        protected Chat doInBackground(final Void... params) {
            return chatRepository.getLatestChatMessage(datingProfile.getProfileId());
        }

        @Override
        protected void onPostExecute(Chat latestChatMessage) {
            super.onPostExecute(latestChatMessage);
            bindValues(latestChatMessage, viewHolder, datingProfile);
        }
    }
}
