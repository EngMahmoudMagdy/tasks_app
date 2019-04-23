package com.magdy.tasksapp.activities;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.magdy.tasksapp.R;
import com.magdy.tasksapp.adapters.TaskAdapter;
import com.magdy.tasksapp.helpers.ConstantMembers;
import com.magdy.tasksapp.helpers.Popup;
import com.magdy.tasksapp.helpers.RecyclerItemTouchHelper;
import com.magdy.tasksapp.models.Task;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.addTask)
    FloatingActionButton addTask;

    @BindView(R.id.titleText)
    TextView titleText;


    @BindView(R.id.back)
    LinearLayout back;

    @BindView(R.id.filter)
    LinearLayout filter;

    @BindView(R.id.filterText)
    TextView filterText;

    @BindView(R.id.progress)
    RelativeLayout progress;

    @BindView(R.id.coordinator)
    CoordinatorLayout coordinator;

    TaskAdapter adapter;
    List<Task> taskList;
    Popup popup;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        taskList = new ArrayList<>();
        adapter = new TaskAdapter(this, taskList);
        recycler.setAdapter(adapter);
        popup = new Popup(MainActivity.this);
        popup.setButtoClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!popup.getEditText().getText().toString().trim().isEmpty()) {
                    Task task = new Task(popup.getEditText().getText().toString());
                    progress.setVisibility(View.VISIBLE);
                    databaseReference.push().setValue(task).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                            progress.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                popup.dismiss();
                            } else {
                                Toast.makeText(getBaseContext(), "Error adding new task", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else
                    Toast.makeText(getBaseContext(), "Empty Task!", Toast.LENGTH_SHORT).show();
            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filterText.getTextColors().getDefaultColor() == getResources().getColor(R.color.colorPrimary)) {
                    adapter.getFilter().filter("");
                    filterText.setTextColor(getResources().getColor(R.color.grey));
                    filterText.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.filter, 0, 0);
                } else {
                    adapter.getFilter().filter("done");
                    filterText.setTextColor(getResources().getColor(R.color.colorPrimary));
                    filterText.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.filter_blue, 0, 0);
                }
            }
        });


        progress.setVisibility(View.VISIBLE);
        databaseReference = FirebaseDatabase.getInstance().getReference(ConstantMembers.TASKS);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                taskList.clear();
                progress.setVisibility(View.GONE);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Task task = snapshot.getValue(Task.class);
                    if (task != null) {
                        task.setKey(snapshot.getKey());
                        taskList.add(task);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recycler);

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.getEditText().setText("");
                popup.showDialog();
            }
        });


    }

    @Override
    public void onBackPressed() {
        if (popup != null)
            if (popup.getDialog().isShowing())
                popup.dismiss();
            else super.onBackPressed();
        else super.onBackPressed();

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        String name = taskList.get(viewHolder.getAdapterPosition()).getTitle();

        // backup of removed item for undo purpose
        final Task deletedItem = taskList.get(viewHolder.getAdapterPosition());
        final int deletedIndex = viewHolder.getAdapterPosition();

        // remove the item from recycler view
        adapter.removeItem(viewHolder.getAdapterPosition());

        // showing snack bar with Undo option
        Snackbar snackbar = Snackbar
                .make(coordinator, name + " removed from tasks!", Snackbar.LENGTH_LONG);
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // undo is selected, restore the deleted item
                adapter.restoreItem(deletedItem, deletedIndex);
            }
        });
        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                progress.setVisibility(View.VISIBLE);
                databaseReference.child(deletedItem.getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                        progress.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                        } else {
                            Toast.makeText(getBaseContext(), "Error removing task\n" + deletedItem.getTitle(), Toast.LENGTH_SHORT).show();
                            adapter.restoreItem(deletedItem, deletedIndex);
                        }
                    }
                });
            }
        });

    }

}
