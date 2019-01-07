package vedam.subkuch.ui.ask;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.ui.ask.models.Conversation;
import vedam.subkuch.ui.ask.models.Reply;
import vedam.subkuch.utils.UiUtil;

import static vedam.subkuch.utils.UiUtil.getTypeface;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Conversation> conversations;
    private AskReplyListener listener;

    ConversationAdapter(Context context, ArrayList<Conversation> conversations, AskReplyListener listener) {

        this.context = context;
        this.conversations = conversations;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ConversationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.fragment_conversation_list_item, parent, false);
        return new ConversationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationAdapter.ViewHolder holder, int position) {

        Conversation conversation = conversations.get(position);

        UiUtil.setTextView(holder.tvQuestion, getTopic(conversation));

        setAnswerListener(holder.tvAnswers, holder.ivTriangle, holder.rlSubContainer, conversation);
        setAnswers(holder.tvAnswers, holder.ivTriangle, conversation);
        holder.tvAnswers.setTypeface(getTypeface(context, context.getString(R.string.typeface_regular)));

        if (conversation.getReplayallowed().equals(context.getString(R.string.one))) {
            holder.btReply.setVisibility(View.VISIBLE);
            holder.btReply.setOnClickListener(view -> {
                if (listener != null)
                    listener.onReplyClick(conversation.getBlogid());
            });

        } else
            holder.btReply.setVisibility(View.GONE);
    }

    private String getTopic(Conversation conversation) {

        return conversation.getPostedon() + " " + conversation.getTopic();
    }


    private void setAnswerListener(TextView tvAnswer, ImageView ivTriangle, RelativeLayout rlSubContainer, Conversation conversation) {
        if (conversation.getBlogReplydetails().size() > 3) {
            ivTriangle.setVisibility(View.VISIBLE);
            rlSubContainer.setOnClickListener(v -> {
                if (conversation.isExpanded()) {
                    conversation.setExpanded(false);
                    setAnswers(tvAnswer, ivTriangle, conversation);
                } else {
                    conversation.setExpanded(true);
                    setAnswers(tvAnswer, ivTriangle, conversation);
                }
            });
        } else
            ivTriangle.setVisibility(View.GONE);
    }

    private void setAnswers(TextView tvAnswer, ImageView ivTriangle, Conversation conversation) {

        if (conversation.getBlogReplydetails().size() > 2)
            if (conversation.isExpanded()) {
                tvAnswer.setText(getFullAnswerString(conversation.getBlogReplydetails()));
                ivTriangle.setImageResource(R.drawable.baseline_expand_less_black_24dp);
            } else {
                ivTriangle.setImageResource(R.drawable.baseline_expand_more_black_24dp);
                if (conversation.getBlogReplydetails().size() > 0)
                    tvAnswer.setText(getIndividualAnswer(conversation.getBlogReplydetails().get(0)));
                else
                    tvAnswer.setText("");
            }
        else {
            tvAnswer.setText(getFullAnswerString(conversation.getBlogReplydetails()));
        }
    }

    private String getFullAnswerString(ArrayList<Reply> replies) {
        StringBuilder fullReplies = new StringBuilder();
        for (int i = 0; i < replies.size(); i++) {
            Reply reply = replies.get(i);
            if (i == replies.size() - 1)
                fullReplies.append(getIndividualAnswer(reply));
            else
                fullReplies.append(getIndividualAnswer(reply)).append("\n\n");
        }
        return fullReplies.toString();
    }

    private String getIndividualAnswer(Reply reply) {

        return reply.getUsername() + "\n" + reply.getUpdateddon() + " " + reply.getReplayMessage();
    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvQuestion;
        private TextView tvAnswers;
        private ImageView ivTriangle;
        private RelativeLayout rlSubContainer;
        private Button btReply;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestion = itemView.findViewById(R.id.tv_question);
            tvAnswers = itemView.findViewById(R.id.tv_answers);
            ivTriangle = itemView.findViewById(R.id.iv_triangle);
            rlSubContainer = itemView.findViewById(R.id.rl_sub_container);
            btReply = itemView.findViewById(R.id.bt_reply);
        }
    }

}
