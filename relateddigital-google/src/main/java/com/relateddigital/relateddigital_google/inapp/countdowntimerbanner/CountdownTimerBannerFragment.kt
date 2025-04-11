package com.relateddigital.relateddigital_google.inapp.countdowntimerbanner

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.relateddigital.relateddigital_google.R
import com.relateddigital.relateddigital_google.databinding.FragmentCountdownTimerBannerBinding
import com.relateddigital.relateddigital_google.inapp.InAppNotificationState
import java.util.Timer
import java.util.TimerTask

/**
 * A simple [Fragment] subclass.
 * Use the [CountdownTimerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CountdownTimerBannerFragment : Fragment()  {

    companion object {

        private const val LOG_TAG = "CountdownTimerBannerFragment"
        fun newInstance(stateId: Int, inAppState: InAppNotificationState?): CountdownTimerBannerFragment {
            val fragment = CountdownTimerBannerFragment()
            val args = Bundle()
            args.putInt("stateIdKey", stateId)
            args.putParcelable("inAppStateKey", inAppState)
            fragment.arguments = args
            return fragment
        }
        private const val ARG_PARAM1 = "stateIdKey"
        private const val ARG_PARAM2 = "inAppStateKey"
        private const val TIMER_SCHEDULE: Short = 1000
        private const val TIMER_PERIOD: Short = 1000
    }
    private var mWeekNum: Short = 0
    private var mDayNum: Short = 0
    private var mHourNum: Short = 0
    private var mMinuteNum: Short = 0
    private var mSecondNum: Short = 0
    private var mTimer: Timer? = null
    private var mIsTop = false
    private lateinit var binding: FragmentCountdownTimerBannerBinding
    private val ARG_PARAM1 = "stateIdKey"
    private val ARG_PARAM2 = "inAppStateKey"
    private var mStateId = 0
    private var mInAppState: InAppNotificationState? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mStateId = it.getInt(ARG_PARAM1)
            mInAppState = it.getParcelable(ARG_PARAM2)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (mTimer != null) {
            mTimer!!.cancel()
        }
        //TODO: save the remaining time according to the format here
        //TODO: save the json string here
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCountdownTimerBannerBinding.inflate(inflater, container, false)

        adjustTimerViewBot()

        return binding.root
    }

    private fun adjustTimerViewBot() {
        setTimerValues()
        //TODO check the format here and set the visibilities of the views accordingly
        //TODO: convert bigger part like week to smaller parts like day if necessary according to the format
        binding.dayNumBottom.text = mDayNum.toString()
        binding.dayNumBottom.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        binding.hourNumBottom.text = mHourNum.toString()
        binding.hourNumBottom.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        binding.minuteNumBottom.text = mMinuteNum.toString()
        binding.minuteNumBottom.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        binding.secNumBottom.text = mSecondNum.toString()
        binding.secNumBottom.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        startTimer()
    }


    private fun setTimerValues() {
        //TODO: When real data came, adjust here accordingly
        mWeekNum = 3
        mDayNum = 5
        mHourNum = 17
        mMinuteNum = 0
        mSecondNum = 6
    }

    private fun startTimer() {
        mTimer = Timer("CountDownTimer", false)
        val task: TimerTask = object : TimerTask() {
            override fun run() {
                reAdjustTimerViews()
            }
        }
        mTimer!!.schedule(task, TIMER_SCHEDULE.toLong(), TIMER_PERIOD.toLong())
    }


    private fun reAdjustTimerViews() {
        calculateTimeFields()
        requireActivity().runOnUiThread {
            try {

                    if (binding.dayNumBottom.visibility != View.GONE) {
                        binding.dayNumBottom.text = mDayNum.toString()
                    }
                    if (binding.hourNumBottom.visibility != View.GONE) {
                        binding.hourNumBottom.text = mHourNum.toString()
                    }
                    if (binding.minuteNumBottom.visibility != View.GONE) {
                        binding.minuteNumBottom.text = mMinuteNum.toString()
                    }
                    if (binding.secNumBottom.visibility != View.GONE) {
                        binding.secNumBottom.text = mSecondNum.toString()
                    }

            } catch (e: Exception) {
                e.printStackTrace()
                Log.e(LOG_TAG, "The fields for countdown timer could not be set!")
            }
        }
    }

    private fun calculateTimeFields() {
        //TODO: Adjust the logic here for each format. For example, if there is no week field
        //in the format do not set day to max 6 below.
        if (mSecondNum > 0) {
            mSecondNum--
        } else {
            mSecondNum = 59
            if (mMinuteNum > 0) {
                mMinuteNum--
            } else {
                mMinuteNum = 59
                if (mHourNum > 0) {
                    mHourNum--
                } else {
                    mHourNum = 23
                    if (mDayNum > 0) {
                        mDayNum--
                    } else {
                        mDayNum = 6
                        if (mWeekNum > 0) {
                            mWeekNum--
                        } else {
                            expireTime()
                        }
                    }
                }
            }
        }
    }


    private fun expireTime() {
        mSecondNum = 0
        mMinuteNum = 0
        mHourNum = 0
        mDayNum = 0
        mWeekNum = 0
        if (mTimer != null) {
            mTimer!!.cancel()
        }
        requireActivity().runOnUiThread { Toast.makeText(activity, getString(R.string.time_is_over), Toast.LENGTH_LONG).show() }
    }




}