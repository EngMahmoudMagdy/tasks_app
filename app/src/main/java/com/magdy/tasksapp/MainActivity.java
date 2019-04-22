package com.magdy.tasksapp;

import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.magdy.tasksapp.adapters.TaskAdapter;
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

    @BindView(R.id.coordinator)
    CoordinatorLayout coordinator;

    TaskAdapter adapter;
    List<Task> taskList;
    Popup popup;

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
                Task task = new Task(popup.getEditText().getText().toString());
                taskList.add(task);
                adapter.notifyDataSetChanged();
                popup.dismiss();
            }
        });
        taskList.add(new Task("Hello"));
        taskList.add(new Task("welcome"));
        taskList.add(new Task("Good"));
        taskList.add(new Task("Hello"));
        taskList.add(new Task("Hello"));

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
    }

}
