package vedam.subkuch.ui.jobs.jobmela;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.interfaces.OnListViewItemClickListener;
import vedam.subkuch.ui.jobs.models.JobType;

public class JobTypeAdapter extends RecyclerView.Adapter<JobTypeAdapter.JobTypeHolder> {

    private ArrayList<JobType> alJobType;
    private OnListViewItemClickListener listViewItemClickListener;

    JobTypeAdapter(ArrayList<JobType> alJobType, OnListViewItemClickListener listViewItemClickListener) {
        this.alJobType = alJobType;
        this.listViewItemClickListener = listViewItemClickListener;
    }

    @NonNull
    @Override
    public JobTypeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_job_type_list_item, parent, false);
        return new JobTypeHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull JobTypeHolder holder, int position) {


        JobType jobType = alJobType.get(position);

        holder.cbJobType.setText(jobType.getJobTypeName());
        holder.cbJobType.setOnCheckedChangeListener((buttonView, isChecked) -> {
            jobType.setChecked(isChecked);
            if (listViewItemClickListener != null)
                listViewItemClickListener.onItemClick(jobType, position, buttonView, null);
        });
        holder.cbJobType.setChecked(jobType.isChecked());
    }

    @Override
    public int getItemCount() {
        return alJobType.size();
    }

    class JobTypeHolder extends RecyclerView.ViewHolder {

        private CheckBox cbJobType;

        JobTypeHolder(View itemView) {
            super(itemView);
            cbJobType = itemView.findViewById(R.id.cb_job_type);
        }
    }
}
