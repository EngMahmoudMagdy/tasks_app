package com.magdy.tasksapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.magdy.tasksapp.R;
import com.magdy.tasksapp.models.Task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.Holder> {
    private Context context;
    private List<Task> taskList;

    public TaskAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_task, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        final Task task = taskList.get(position);
        holder.name.setText(task.getTitle());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd yyyy", Locale.US);
        holder.date.setText(dateFormat.format(new Date(task.getUpdatedAt())));
        holder.isDoneCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                task.setDone(b);
            }
        });

        LinearLayout tabStrip = ((LinearLayout) holder.tabLayout.getChildAt(0));
        for (int i = 0; i < tabStrip.getChildCount(); i++) {
            tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }
        switch (task.getPriority()) {
            case 1:
                if (holder.tabLayout.getTabAt(1) != null)
                    holder.tabLayout.getTabAt(1).select();
                break;
            case 2:
                if (holder.tabLayout.getTabAt(2) != null)
                    holder.tabLayout.getTabAt(2).select();
                break;
            default:
                if (holder.tabLayout.getTabAt(0) != null)
                    holder.tabLayout.getTabAt(0).select();
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.view_foreground)
        public LinearLayout view_foreground;
        @BindView(R.id.view_background)
        public RelativeLayout view_background;
        @BindView(R.id.isDoneCheck)
        CheckBox isDoneCheck;
        @BindView(R.id.tabLayout)
        TabLayout tabLayout;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.date)
        TextView date;

        Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void removeItem(int position) {
        taskList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, taskList.size());
    }

    public void restoreItem(Task item, int position) {
        taskList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }
}
