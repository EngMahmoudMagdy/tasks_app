package com.magdy.tasksapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.magdy.tasksapp.R;
import com.magdy.tasksapp.models.Comment;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.Holder> {
    private Context context;
    private List<Comment> commentList;

    public CommentAdapter(Context context, List<Comment> taskList) {
        this.context = context;
        this.commentList = taskList;
    }

    @NonNull
    @Override
    public CommentAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentAdapter.Holder(LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.Holder holder, int position) {
        final Comment comment = commentList.get(position);
        holder.name.setText(comment.getDetails());
        holder.time.setText(getTimeAgo(comment.getUpdatedAt()));
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.time)
        TextView time;

        Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static String getTimeAgo(long time) {

        if (time < 1000000000000L) {
            time *= 1000;
        }

        long now = Calendar.getInstance().getTimeInMillis();
        if (time > now || time <= 0) {
            return "in the future";
        }

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "moments ago.";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago.";
        } else if (diff < 60 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago.";
        } else if (diff < 2 * HOUR_MILLIS) {
            return "an hour ago.";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago.";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday.";
        } else {
            return diff / DAY_MILLIS + " days ago.";
        }
    }
}
