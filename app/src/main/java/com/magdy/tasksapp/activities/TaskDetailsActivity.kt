package com.magdy.tasksapp.activities

import android.app.Activity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.View.GONE
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.firebase.database.*
import com.magdy.tasksapp.R
import com.magdy.tasksapp.adapters.CommentAdapter
import com.magdy.tasksapp.helpers.ConstantMembers
import com.magdy.tasksapp.models.Comment
import com.magdy.tasksapp.models.Task
import kotlinx.android.synthetic.main.activity_task_details.*
import kotlinx.android.synthetic.main.progress_layout.*
import java.text.SimpleDateFormat
import java.util.*


class TaskDetailsActivity : AppCompatActivity() {

    lateinit var commentAdapter: CommentAdapter
    lateinit var commentList: ArrayList<Comment>
    lateinit var task: Task
    lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_details)
        back.setOnClickListener { onBackPressed() }
        task = intent.getSerializableExtra(ConstantMembers.TASK) as Task
        databaseReference = FirebaseDatabase.getInstance().getReference(ConstantMembers.TASKS).child(task.key)
        titleText.text = task.title
        date.text = SimpleDateFormat(ConstantMembers.DATE_PATTERN, Locale.US).format(task.updatedAt)
        isDoneCheck.isChecked = task.done
        isDoneCheck.setOnCheckedChangeListener { compoundButton, b ->
            if (compoundButton.isPressed) {
                progress.visibility = View.VISIBLE
                databaseReference.child(ConstantMembers.DONE).setValue(b).addOnCompleteListener {
                    progress.visibility = GONE
                    if (it.isSuccessful)
                        Toast.makeText(baseContext, "Saved!", Toast.LENGTH_SHORT).show()
                    else
                        Toast.makeText(baseContext, "Error !", Toast.LENGTH_SHORT).show()
                }
            }
        }

        delete.setOnClickListener {
            progress.visibility = View.VISIBLE
            databaseReference.removeValue().addOnCompleteListener { task ->
                progress.visibility = View.GONE
                if (task.isSuccessful) {
                    onBackPressed()
                } else {
                    Toast.makeText(baseContext, "Error removing task\n" + this.task.title, Toast.LENGTH_SHORT).show()
                }
            }
        }
        commentList = ArrayList()
        commentAdapter = CommentAdapter(this, commentList)
        recycler.adapter = commentAdapter

        databaseReference.child(ConstantMembers.COMMENTS).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                commentList.clear()
                for (snapshot in dataSnapshot.children) {
                    val comment = snapshot.getValue(Comment::class.java)
                    if (comment != null)
                        commentList.add(comment)
                }
                commentAdapter.notifyDataSetChanged()
            }
        })
        tabLayout.getTabAt(task.priority)!!.select()
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                progress.visibility = View.VISIBLE
                databaseReference.child(ConstantMembers.PRIORITY).setValue(tab.position).addOnCompleteListener {
                    progress.visibility = GONE
                    if (it.isSuccessful)
                        Toast.makeText(baseContext, "Done changed priority!", Toast.LENGTH_SHORT).show()
                    else
                        Toast.makeText(baseContext, "Error changing priority!", Toast.LENGTH_SHORT).show()
                }

            }
        })
        send.setOnClickListener {
            if (!commentEditText.text.toString().isEmpty()) {
                progress.visibility = View.VISIBLE

                val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                databaseReference.child(ConstantMembers.COMMENTS).push().setValue(Comment(commentEditText.text.toString())).addOnCompleteListener {
                    progress.visibility = GONE
                    if (it.isSuccessful)
                        Toast.makeText(baseContext, "Done added comment!", Toast.LENGTH_SHORT).show()
                    else
                        Toast.makeText(baseContext, "Error adding comment!", Toast.LENGTH_SHORT).show()
                }
                inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
                commentEditText.setText("")

            } else
                Toast.makeText(baseContext, "Empty comment!", Toast.LENGTH_SHORT).show()
        }
    }
}
