package com.example.atry.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.atry.R
import com.example.atry.databinding.ActivityHomePageBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInOptionsExtension
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataSet
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.request.SessionReadRequest
import java.util.*
import java.util.concurrent.TimeUnit


class HomePageActivity : AppCompatActivity() {
    var binding: ActivityHomePageBinding? = null
    private val APP_TAG = "HomePage"
    private val TAG = "home"
    private var gsc: GoogleSignInClient? = null
    private var gso: GoogleSignInOptions? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        gsc = GoogleSignIn.getClient(this, gso!!)
        val fitnessOptions: GoogleSignInOptionsExtension = FitnessOptions.builder()
                .addDataType(DataType.TYPE_SLEEP_SEGMENT, FitnessOptions.ACCESS_READ)
                .build()
        val account = GoogleSignIn.getAccountForExtension(this, fitnessOptions)
        Log.d(TAG, "onCreate: " + account.email)
        if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    this,  // your activity
                    1,  // e.g. 1
                    account,
                    fitnessOptions)
        } else {
            val SLEEP_STAGE_NAMES = arrayOf(
                    "Unused",
                    "Awake (during sleep)",
                    "Sleep",
                    "Out-of-bed",
                    "Light sleep",
                    "Deep sleep",
                    "REM sleep"
            )

            val yesterday = Calendar.getInstance();
            yesterday.add(Calendar.DATE, -2);
            Log.d(TAG, "onCreate: " + System.currentTimeMillis())

            val request = SessionReadRequest.Builder()
                    .readSessionsFromAllApps()
                    // By default, only activity sessions are included, so it is necessary to explicitly
                    // request sleep sessions. This will cause activity sessions to be *excluded*.
                    .includeSleepSessions()
                    // Sleep segment data is required for details of the fine-granularity sleep, if it is present.
                    .read(DataType.TYPE_SLEEP_SEGMENT)
                    .setTimeInterval(yesterday.timeInMillis, System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                    .build()

            Fitness.getSessionsClient(this, GoogleSignIn.getAccountForExtension(this, fitnessOptions))
                    .readSession(request)
                    .addOnSuccessListener { response ->
                        for (session in response.sessions) {
                            val sessionStart = session.getStartTime(TimeUnit.MILLISECONDS)
                            val sessionEnd = session.getEndTime(TimeUnit.MILLISECONDS)
                            Log.i(TAG, "Sleep between $sessionStart and $sessionEnd")

                            // If the sleep session has finer granularity sub-components, extract them:
//                            val dataSets = response.getDataSet(session)
//                            for (dataSet in dataSets) {
//                                for (point in dataSet.dataPoints) {
//                                    val sleepStageVal = point.getValue(Field.FIELD_SLEEP_SEGMENT_TYPE).asInt()
//                                    val sleepStage = SLEEP_STAGE_NAMES[sleepStageVal]
//                                    val segmentStart = point.getStartTime(TimeUnit.MILLISECONDS)
//                                    val segmentEnd = point.getEndTime(TimeUnit.MILLISECONDS)
//                                    Log.i(TAG, "\t* Type $sleepStage between $segmentStart and $segmentEnd")
//                                }
//                            }
                        }
                    }



        }
        binding!!.bottomNavigationView.setOnItemSelectedListener { item: MenuItem ->
            Log.d("TAG2", "This is a debug log message.")
            when (item.itemId) {
                R.id.settings_nav -> replaceFragment(SettingsFragment())
                R.id.homePage -> replaceFragment(HomePageFragment())
            }
            true
        }
    }

    fun setAlarm(context: Context?, cal_alarm: Calendar) {
        val manager = getSystemService(ALARM_SERVICE) as AlarmManager
        val myIntent = Intent(context, PlayMusic::class.java)
        myIntent.action = "start"
        val pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0)
        manager[AlarmManager.RTC_WAKEUP, cal_alarm.timeInMillis] = pendingIntent
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.homeFrame, fragment)
        fragmentTransaction.commit()
    }

    private fun dumpDataSet(dataSet: DataSet) {
        Log.i(TAG, "Data returned for Data type: \${dataSet.dataType.name}")
        for (dp in dataSet.dataPoints) {
            Log.i(TAG, "Data point:")
            Log.i(TAG, "\tType: \${dp.dataType.name}")
            Log.i(TAG, "\tStart: \${dp.getStartTimeString()}")
            Log.i(TAG, "\tEnd: \${dp.getEndTimeString()}")
            for (field in dp.dataType.fields) {
                Log.i(TAG, "\tField: " + field.name + " Value: " + dp.getValue(field))
            }
        }
    }
}