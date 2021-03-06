package com.example.admin.mealplanner2new.Views

import android.app.FragmentTransaction
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.admin.mealplanner2new.Fragments.StartExerciseFragment
import com.example.admin.mealplanner2new.Models.Exercise
import com.example.admin.mealplanner2new.R

import kotlinx.android.synthetic.main.activity_start_exercise.*


class StartExerciseActivity : AppCompatActivity() {

    lateinit var exerciseList: ArrayList<Exercise>
    lateinit var workOutId: String
    private var id = 0
    private var dateOf = ""
    lateinit var task_id: String
    public var status = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_exercise)
        setSupportActionBar(toolbar)

        if (intent != null) {

            exerciseList = intent.getParcelableArrayListExtra("data")         // Data is coming from  ExerciseDetailActivity.java
            dateOf = intent.getStringExtra("day")
            workOutId = intent.getStringExtra("work_id")
            task_id = intent.getStringExtra("task_id")
            status = intent.getStringExtra("status")

        }


        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {

            if (fragmentManager.backStackEntryCount > 0) {
                fragmentManager.popBackStack()
            } else {
                finish()
            }

        }


        val startExerciseFragment = StartExerciseFragment()
        val bundle = Bundle()
        bundle.putLong("time", exerciseList[0].timeOfRep)
        bundle.putString("ex_name", exerciseList[0].name)
        bundle.putString("ex_rep", exerciseList[0].reps)
        bundle.putInt("ex_id", id)
        bundle.putString("day", dateOf)
        bundle.putString("from", "200")
        startExerciseFragment.arguments = bundle

        fragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .add(R.id.container_exercise, startExerciseFragment, id.toString())
                //.addToBackStack(id.toString())
                .commit()


    }


}
