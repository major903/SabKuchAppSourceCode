package vedam.subkuch.ui.matrimonial.editProfile.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import vedam.subkuch.R;
import vedam.subkuch.network.models.Photos;
import vedam.subkuch.utils.ImageSetter;
import vedam.subkuch.utils.UiUtil;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {
    private List<Photos> listOfPhotos;
    private Activity activity;

    public ImagesAdapter(List<Photos> listOfPhotos, Activity activity) {
        this.listOfPhotos = listOfPhotos;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_images, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        if (listOfPhotos.get(position).getImage() == null && listOfPhotos.get(position).getImageUrl().equalsIgnoreCase("")) {
            holder.imageView.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_add));
            holder.imageViewCross.setVisibility(View.GONE);
        } else if (listOfPhotos.get(position).getImage() != null) {
            holder.imageViewCross.setVisibility(View.VISIBLE);
            holder.imageView.setImageBitmap(listOfPhotos.get(position).getImageBitMap());
        } else {
            holder.imageViewCross.setVisibility(View.VISIBLE);
            UiUtil.setImageView(new ImageSetter.ImageBuilder(activity)
                    .setTarget(holder.imageView)
                    .setDefaults()
                    .setImageLink(listOfPhotos.get(position).getImageUrl())
                    .build());
        }
    }

    @Override
    public int getItemCount() {
        return listOfPhotos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView imageViewCross;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            imageViewCross = itemView.findViewById(R.id.imageViewCross);
        }
    }
}
