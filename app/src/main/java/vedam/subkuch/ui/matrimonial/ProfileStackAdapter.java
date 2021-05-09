package vedam.subkuch.ui.matrimonial;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.ui.matrimonial.models.DatingProfile;
import vedam.subkuch.utils.ImageSetter;
import vedam.subkuch.utils.UiUtil;

public class ProfileStackAdapter extends RecyclerView.Adapter<ProfileStackAdapter.ViewHolder> {

    private Context context;
    private ArrayList<DatingProfile> datingProfiles;

    ProfileStackAdapter(Context context, ArrayList<DatingProfile> datingProfiles) {

        this.context = context;
        this.datingProfiles = datingProfiles;
    }

    @NonNull
    @Override
    public ProfileStackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.fragment_show_profiles_list_item, parent, false);
        return new ProfileStackAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileStackAdapter.ViewHolder holder, int position) {

        DatingProfile datingProfile = datingProfiles.get(position);

        if (datingProfile.getImagesList() != null && datingProfile.getImagesList().size() > 0)
            UiUtil.setImageView(new ImageSetter.ImageBuilder(context)
                    .setImageLink(datingProfile.getImagesList().get(0).getImage())
                    .setErrorResource(R.drawable.placeholder)
                    .setPlaceholderResource(R.drawable.placeholder)
                    .setTarget(holder.ivProfile)
                    .build());
        else
            holder.ivProfile.setImageResource(R.drawable.placeholder);
    }

    @Override
    public int getItemCount() {
        return datingProfiles.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivProfile;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
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
