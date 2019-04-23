package com.magdy.tasksapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.magdy.tasksapp.R;
import com.magdy.tasksapp.activities.TaskDetailsActivity;
import com.magdy.tasksapp.helpers.ConstantMembers;
import com.magdy.tasksapp.models.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.Holder> implements Filterable {
    private Context context;
    private List<Task> taskList, taskListAll;
    private DatabaseReference databaseReference;

    public TaskAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
        this.taskListAll = taskList;
        databaseReference = FirebaseDatabase.getInstance().getReference(ConstantMembers.TASKS);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_task, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, final int position) {
        final Task task = taskList.get(position);
        holder.name.setText(task.getTitle());
        SimpleDateFormat dateFormat = new SimpleDateFormat(ConstantMembers.DATE_PATTERN, Locale.US);
        holder.date.setText(dateFormat.format(new Date(task.getUpdatedAt())));
        holder.isDoneCheck.setOnCheckedChangeListener(null);
        holder.isDoneCheck.setChecked(task.getDone());
        holder.isDoneCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isPressed()) {
                    databaseReference.child(task.getKey())
                            .child(ConstantMembers.DONE)
                            .setValue(b);
                }
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TaskDetailsActivity.class);
                intent.putExtra(ConstantMembers.TASK, task);
                context.startActivity(intent);
            }
        });

        holder.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                databaseReference.child(task.getKey()).child(ConstantMembers.PRIORITY).setValue(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                if (charSequence.toString().trim().isEmpty()) {
                    taskList = taskListAll;
                } else if (charSequence.toString().equals("done")) {
                    taskList = new ArrayList<>();
                    for (Task task : taskListAll) {
                        if (task.getDone())
                            taskList.add(task);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = taskList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults results) {
                taskList = (List<Task>) results.values;
                notifyDataSetChanged();
            }
        };
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
