package vedam.subkuch.ui.inbox;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.ui.inbox.models.Inbox;
import vedam.subkuch.utils.DateTimeUtils;
import vedam.subkuch.utils.UiUtil;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Inbox> inboxes;

    InboxAdapter(Context context, ArrayList<Inbox> inboxes) {

        this.context = context;
        this.inboxes = inboxes;
    }

    @NonNull
    @Override
    public InboxAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.fragment_inbox_list_item, parent, false);
        return new InboxAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InboxAdapter.ViewHolder holder, int position) {

        Inbox inbox = inboxes.get(position);

        UiUtil.setTextView(holder.tvSubject, inbox.getTitle());
        UiUtil.setTextView(holder.tvBody, inbox.getMessage());
        UiUtil.setTextView(holder.tvDateTime, DateTimeUtils.getFormattedDate(inbox.getCreatedAt(),
                DateTimeUtils.DATE_TIME_FORMAT_1, DateTimeUtils.DATE_FORMAT_2));
    }

    @Override
    public int getItemCount() {
        return inboxes.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvSubject;
        private TextView tvDateTime;
        private TextView tvBody;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubject = itemView.findViewById(R.id.tv_subject);
            tvDateTime = itemView.findViewById(R.id.tv_date_time);
            tvBody = itemView.findViewById(R.id.tv_body);
        }
    }
}
