package com.example.admin.mealplanner2new.Fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Fragment
import android.app.FragmentManager
import android.app.FragmentTransaction
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.admin.mealplanner2new.R
import com.example.admin.mealplanner2new.Views.ShowExercisesActivity
import com.example.admin.mealplanner2new.Views.StartExerciseActivity
import kotlinx.android.synthetic.main.fragment_start_exercise.*
import android.os.AsyncTask
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.admin.mealplanner2new.Common.SessionManager
import com.example.admin.mealplanner2new.Models.*
import com.example.admin.mealplanner2new.Service.MyJobIntentService
import com.google.gson.Gson


class StartExerciseFragment : Fragment() {

    lateinit var roomDb: WordRoomDatabase
    var countDownTimer: CountDownTimer? = null
    var endValue = 30L
    var remainingTime = endValue
    var isCountDownTimerEnable = false
    var progressStatus = 0
    var isfromPauseButton = false
    var exersiceName = ""
    var exerciseId = 0
    var exerciseReps = ""
    lateinit var contexts: Context
    lateinit var exerciseList: ArrayList<Exercise>
    lateinit var workOutId: String
    var timeInMilliseconds = 0L
    var dateOf = ""
    lateinit var u_id: String
    lateinit var taslId: String
    val PHOTO_URL = "http://code-fuel.in/healthbotics/storage/app/public/exercise/"


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        contexts = context!! as StartExerciseActivity
        exerciseList = (contexts as StartExerciseActivity).exerciseList
        workOutId = (contexts as StartExerciseActivity).workOutId
        taslId = (contexts as StartExerciseActivity).task_id

    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        exerciseList = (activity as StartExerciseActivity).exerciseList
        workOutId = (activity as StartExerciseActivity).workOutId
        taslId = (activity as StartExerciseActivity).task_id
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        u_id = SessionManager(activity).keyUId

        if (arguments != null) {
            endValue = arguments.getLong("time", 0L)
            exerciseId = arguments.getInt("ex_id")
            exerciseReps = arguments.getString("ex_rep")
            exersiceName = arguments.getString("ex_name")
            dateOf = arguments.getString("day")
            remainingTime = endValue
            roomDb = WordRoomDatabase.getDatabase(activity.applicationContext)
        }

        GetExersiceTask(roomDb.wordDao()).execute()

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater!!.inflate(R.layout.fragment_start_exercise, container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setProgress(progressStatus, endValue.toInt())

        val seconds = (endValue * 1000 / 1000).toInt() % 60
        val minutes = (endValue * 1000 / (1000 * 60) % 60).toInt()
        val hours = (endValue * 1000 / (1000 * 60 * 60) % 24).toInt()
        val newtime = hours.toString() + ":" + minutes + ":" + seconds
        tvCountDown?.text = newtime.toString()
        tvExerciseName?.text = exersiceName
        tvNoOfRep?.text = exerciseReps
        btnNext?.visibility = View.GONE
        Glide.with(activity).load(PHOTO_URL.plus(exerciseList[exerciseId].photo)).into(ivPhoto)



        btnSkip?.setOnClickListener {


            if (SystemClock.elapsedRealtime() - timeInMilliseconds < 1000) {
                return@setOnClickListener
            }
            timeInMilliseconds = SystemClock.elapsedRealtime()

            if (exerciseId < exerciseList.size - 1 && !isCountDownTimerEnable) {


                val startExerciseFragment = StartExerciseFragment()
                val bundle = Bundle()
                bundle.putLong("time", exerciseList[exerciseId + 1].timeOfRep)
                bundle.putString("ex_name", exerciseList[exerciseId + 1].name)
                bundle.putString("ex_rep", exerciseList[exerciseId + 1].reps)
                bundle.putInt("ex_id", exerciseId + 1)
                bundle.putString("day", dateOf)
                startExerciseFragment.arguments = bundle

                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.animator.fragment_slide_left_enter,
                                R.animator.fragment_slide_left_exit, R.animator.fragment_slide_right_enter,
                                R.animator.fragment_slide_right_exit)
                        .add(R.id.container_exercise, startExerciseFragment, (exerciseId).toString())
                        .addToBackStack((exerciseId).toString())
                        .hide(this@StartExerciseFragment)
                        .commit()
            } else if (exerciseId >= exerciseList.size - 1 && !isCountDownTimerEnable) {

//                Toast.makeText(activity!!, "End of Exercise", Toast.LENGTH_LONG).show()
//                //fragmentManager.popBackStack(StartExerciseFragment::class.java.simpleName,FragmentManager.POP_BACK_STACK_INCLUSIVE)
//                val intent = Intent(activity, ShowExercisesActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                startActivity(intent)

                val savedArrayList: ArrayList<Exercise> = ArrayList()


                for (items in exerciseList) {
                    if (items.isCompleted) {
                        savedArrayList.add(items)
                    }
                }

                val savedExerciseData = SavedExerciseData()
                savedExerciseData.id = workOutId
                savedExerciseData.u_id = u_id
                savedExerciseData.task_id = taslId
                savedExerciseData.exerciseArrayList = savedArrayList

                val finalSubmitExerciseFragment = FinalSubmitExerciseFragment()
                val bundle = Bundle()
                finalSubmitExerciseFragment.arguments = bundle

                bundle.putParcelable("data", savedExerciseData)



                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.animator.fragment_slide_left_enter,
                                R.animator.fragment_slide_left_exit, R.animator.fragment_slide_right_enter,
                                R.animator.fragment_slide_right_exit)
                        .add(R.id.container_exercise, finalSubmitExerciseFragment, FinalSubmitExerciseFragment::class.java.simpleName)
                        .addToBackStack(FinalSubmitExerciseFragment::class.java.simpleName)
                        .hide(this@StartExerciseFragment)
                        .commit()

            }


        }


        btnNext?.setOnClickListener {

            if (!isCountDownTimerEnable && remainingTime == 0L && exerciseId < exerciseList.size - 1) {

                val startExerciseFragment = StartExerciseFragment()
                val bundle = Bundle()
                bundle.putLong("time", exerciseList[exerciseId + 1].timeOfRep)
                bundle.putString("ex_name", exerciseList[exerciseId + 1].name)
                bundle.putString("ex_rep", exerciseList[exerciseId + 1].reps)
                bundle.putInt("ex_id", exerciseId + 1)
                bundle.putString("day", dateOf)
                startExerciseFragment.arguments = bundle

                exerciseList[exerciseId].day = dateOf
                exerciseList[exerciseId].isCompleted = true

                // insertAsyncTask(roomDb.wordDao()).execute(exerciseList[exerciseId])
                val savedArrayList: ArrayList<Exercise> = ArrayList()


                for (items in exerciseList) {
                    if (items.isCompleted) {
                        savedArrayList.add(items)
                    }
                }

                val savedExerciseData = SavedExerciseData()
                savedExerciseData.id = workOutId
                savedExerciseData.u_id = u_id
                savedExerciseData.task_id = taslId

                savedExerciseData.exerciseArrayList = savedArrayList



                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.animator.fragment_slide_left_enter,
                                R.animator.fragment_slide_left_exit, R.animator.fragment_slide_right_enter,
                                R.animator.fragment_slide_right_exit)
                        .add(R.id.container_exercise, startExerciseFragment, (exerciseId).toString())
                        .addToBackStack((exerciseId).toString())
                        .hide(this@StartExerciseFragment)
                        .commit()


            } else if (!isCountDownTimerEnable && remainingTime == 0L && exerciseId >= exerciseList.size - 1) {

                exerciseList[exerciseId].day = dateOf
                exerciseList[exerciseId].isCompleted = true


                val savedArrayList: ArrayList<Exercise> = ArrayList()


                for (items in exerciseList) {
                    if (items.isCompleted) {
                        savedArrayList.add(items)
                    }
                }

                val savedExerciseData = SavedExerciseData()
                savedExerciseData.id = workOutId
                savedExerciseData.u_id = u_id
                savedExerciseData.task_id = taslId

                savedExerciseData.exerciseArrayList = savedArrayList


                val finalSubmitExerciseFragment = FinalSubmitExerciseFragment()
                val bundle = Bundle()
                bundle.putParcelable("data", savedExerciseData)
                finalSubmitExerciseFragment.arguments = bundle


                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.animator.fragment_slide_left_enter,
                                R.animator.fragment_slide_left_exit, R.animator.fragment_slide_right_enter,
                                R.animator.fragment_slide_right_exit)
                        .add(R.id.container_exercise, finalSubmitExerciseFragment, FinalSubmitExerciseFragment::class.java.simpleName)
                        .addToBackStack(FinalSubmitExerciseFragment::class.java.simpleName)
                        .hide(this@StartExerciseFragment)
                        .commit()


            }


        }


        ivPlay?.setOnClickListener {

            if (!isCountDownTimerEnable && remainingTime > 0) {

                startCountDown(remainingTime)
                ivPlay?.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp)

            } else if (remainingTime > 0) {

                if (countDownTimer != null) {
                    countDownTimer!!.cancel()
                    countDownTimer!!.onFinish()
                }

                ivPlay?.setImageResource(R.drawable.ic_play_circle_outline_black_24dp)

            }


        }


        ivReset.setOnClickListener {

            if (countDownTimer != null) {
                countDownTimer!!.cancel()
                countDownTimer!!.onFinish()
                remainingTime = endValue

                val seconds = (remainingTime * 1000 / 1000).toInt() % 60
                val minutes = (remainingTime * 1000 / (1000 * 60) % 60).toInt()
                val hours = (remainingTime * 1000 / (1000 * 60 * 60) % 24).toInt()
                val newtime = hours.toString() + ":" + minutes + ":" + seconds
                tvCountDown?.text = newtime.toString()
                progressStatus = 0
                setProgress(progressStatus, remainingTime.toInt())
            }


        }


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }


    override fun onResume() {
        super.onResume()
    }

    fun startCountDown(endTime: Long) {
        countDownTimer = object : CountDownTimer(endTime * 1000, 1000) {
            override fun onFinish() {


                if (remainingTime.toInt() <= 1) {

                    btnNext?.visibility = View.VISIBLE

                }


                if (remainingTime.toInt() == 1) {
                    isCountDownTimerEnable = false
                    progressStatus += 1
                    remainingTime -= 1
                    ivPlay?.setImageResource(R.drawable.ic_play_circle_outline_black_24dp)

                    setProgress(progressStatus, endValue.toInt())
                    Log.e("progress", progressStatus.toString())

                    val seconds = (remainingTime * 1000 / 1000).toInt() % 60
                    val minutes = (remainingTime * 1000 / (1000 * 60) % 60).toInt()
                    val hours = (remainingTime * 1000 / (1000 * 60 * 60) % 24).toInt()
                    val newtime = hours.toString() + ":" + minutes + ":" + seconds
                    tvCountDown.text = newtime.toString()


                } else {
                    isCountDownTimerEnable = false

                    ivPlay?.setImageResource(R.drawable.ic_play_circle_outline_black_24dp)

                    setProgress(progressStatus, endValue.toInt())
                    //  Log.e("progress",progressStatus.toString())

                    val seconds = (remainingTime * 1000 / 1000).toInt() % 60
                    val minutes = (remainingTime * 1000 / (1000 * 60) % 60).toInt()
                    val hours = (remainingTime * 1000 / (1000 * 60 * 60) % 24).toInt()
                    val newtime = hours.toString() + ":" + minutes + ":" + seconds
                    tvCountDown?.text = newtime.toString()
                }


            }

            override fun onTick(millisUntilFinished: Long) {

                progressStatus += 1
                setProgress(progressStatus, endValue.toInt())
                //Log.e("progress",progressStatus.toString())

                remainingTime = millisUntilFinished / 1000
                Log.e("remaining", remainingTime.toString())

                isCountDownTimerEnable = true
                val seconds = (millisUntilFinished / 1000).toInt() % 60
                val minutes = (millisUntilFinished / (1000 * 60) % 60).toInt()
                val hours = (millisUntilFinished / (1000 * 60 * 60) % 24).toInt()
                val newtime = hours.toString() + ":" + minutes + ":" + seconds

                tvCountDown?.text = newtime

//                if (remainingTime == 1L){
//                    countDownTimer?.cancel()
//                    countDownTimer?.onFinish()
//                }


            }

        }

        countDownTimer!!.start()

    }

    fun setProgress(startTime: Int, endTime: Int) {

        progress_bar?.progress = startTime.toFloat()
        progress_bar?.progressMax = endTime.toFloat()

    }


    @SuppressLint("StaticFieldLeak")
    private inner class insertAsyncTask internal constructor(private val mAsyncTaskDao: WordDao) : AsyncTask<Exercise, Void, Void>() {

        override fun doInBackground(vararg params: Exercise): Void? {
            mAsyncTaskDao.insert(params[0])
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            // Toast.makeText(activity!!, "Successfully Saved", Toast.LENGTH_LONG).show()
        }
    }


    private inner class GetExersiceTask internal constructor(private val mAsyncTaskDao: WordDao) : AsyncTask<Void, Void, List<Exercise>>() {
        override fun doInBackground(vararg params: Void?): List<Exercise> {
            var myList = mAsyncTaskDao.getAllData(dateOf)
            return myList

        }

        override fun onPostExecute(result: List<Exercise>?) {
            super.onPostExecute(result)
            Log.e("size", result?.size.toString())
        }


    }

}