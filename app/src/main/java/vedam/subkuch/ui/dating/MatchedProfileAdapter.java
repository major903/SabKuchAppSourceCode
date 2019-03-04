package vedam.subkuch.ui.dating;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.ui.dating.models.DatingProfile;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.ImageSetter;
import vedam.subkuch.utils.UiUtil;

public class MatchedProfileAdapter extends RecyclerView.Adapter<MatchedProfileAdapter.ViewHolder> {

    private Context context;
    private ArrayList<DatingProfile> datingProfiles;

    MatchedProfileAdapter(Context context, ArrayList<DatingProfile> datingProfiles) {

        this.context = context;
        this.datingProfiles = datingProfiles;
    }

    @NonNull
    @Override
    public MatchedProfileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.fragment_matched_profile_list_item, parent, false);
        return new MatchedProfileAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchedProfileAdapter.ViewHolder holder, int position) {

        DatingProfile datingProfile = datingProfiles.get(position);

        UiUtil.setTextView(holder.tvName, AppUtil.getFullName(datingProfile.getFirstName(), datingProfile.getLastName()));

        UiUtil.setImageView(new ImageSetter.ImageBuilder(context)
                .setImageLink(datingProfile.getImage())
                .setPlaceholderResource(R.drawable.white)
                .setErrorResource(R.drawable.white)
                .setTarget(holder.ivProfile)
                .build());
    }

    @Override
    public int getItemCount() {
        return datingProfiles.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivProfile;
        private TextView tvName;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            ivProfile = itemView.findViewById(R.id.iv_profile);
        }

        /*public <E> void bind(final E item, final int position, final OnListViewItemClickListener listener) {

            itemView.setOnClickListener(v -> {
                if (listener != null)
                    listener.onItemClick(item, position, itemView, null);
            });
        }*/
    }
}
