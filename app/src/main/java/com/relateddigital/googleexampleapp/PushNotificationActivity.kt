package com.relateddigital.googleexampleapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.relateddigital.googleexampleapp.databinding.ActivityPushNotificationBinding
import com.relateddigital.relateddigital_google.RelatedDigital
import com.relateddigital.relateddigital_google.model.EmailPermit
import com.relateddigital.relateddigital_google.model.GsmPermit
import com.relateddigital.relateddigital_google.model.Message
import com.relateddigital.relateddigital_google.push.EuromessageCallback
import com.relateddigital.relateddigital_google.push.PushMessageInterface
import com.relateddigital.relateddigital_google.push.PushNotificationManager
import com.relateddigital.relateddigital_google.push.TestPush
import com.relateddigital.relateddigital_google.util.AppUtils
import com.relateddigital.relateddigital_google.util.SharedPref
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class PushNotificationActivity : AppCompatActivity()  {
    companion object{
        private const val LOG_TAG = "PushNotificationActivity"
    }
    private lateinit var binding: ActivityPushNotificationBinding
    private lateinit var activity: Activity
    private var isFirstResume = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPushNotificationBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        activity = this
        setupUI()
    }

    private fun setupUI() {
        showToken()
        createSpinners()
        setupPermissionButton()
        setupPayloadButton()
        setupTemplatePushButtons()
        setupSyncButton()
        setupRegisterEmailButton()
        setupDeleteAllPushButton()
        setupDeletePushWithIdButton()


    }

    private fun showToken() {
        binding.etToken.setText(RelatedDigital.getRelatedDigitalModel(this).getToken())
    }

    private fun deletefromGpm() {

        binding.btnTextDeletePushWithIdFromGpm.setOnClickListener {
            RelatedDigital.deletePushMessageByIdFromLSPM(this,"75d7ed18-0bac-433d-a1ff-21395a5c5679")
        }

        binding.btnTextDeleteAllPushFromGpm.setOnClickListener {
            RelatedDigital.deleteAllPushMessagesFromLSPM(this)
        }

    }

    private fun createSpinners() {
        val registerEmailCommercialSpinnerItems = arrayOf(
            Constants.EURO_RECIPIENT_TYPE_BIREYSEL,
            Constants.EURO_RECIPIENT_TYPE_TACIR
        )
        val aa1: ArrayAdapter<*> = ArrayAdapter<Any?>(
            this,
            android.R.layout.simple_spinner_item,
            registerEmailCommercialSpinnerItems
        )
        aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.registeremailcommercialSpinner.adapter = aa1
        val registerEmailPermitSpinnerItems =
            arrayOf<String?>(Constants.EMAIL_PERMIT_ACTIVE, Constants.EMAIL_PERMIT_PASSIVE)
        val aa2: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, registerEmailPermitSpinnerItems)
        aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.registeremailpermitSpinner.adapter = aa2
    }

    private fun setupPermissionButton() {
        binding.btnPermission.setOnClickListener {
            RelatedDigital.requestNotificationPermission(activity)
        }
    }

    private fun setupPayloadButton() {
        binding.btnPayload.setOnClickListener {
            val pushMessageInterface: PushMessageInterface = object : PushMessageInterface {
                override fun success(pushMessages: List<Message>) {
                    Toast.makeText(
                        applicationContext,
                        "Successful",
                        Toast.LENGTH_SHORT
                    ).show()
                    //for loop pushMessage List
                    for (pushMessage in pushMessages) {
                        Log.d("PushMessage",pushMessage.getParams().toString())
                    }
                }

                override fun fail(errorMessage: String) {
                    Toast.makeText(
                        applicationContext,
                        "Failure",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Something went wrong. You may consider warning the user:
                }
            }

            val loginID = SharedPref.readString(applicationContext, com.relateddigital.relateddigital_google.constants.Constants.NOTIFICATION_LOGIN_ID_KEY)
            if(loginID.isEmpty()) {
                RelatedDigital.getPushMessages(activity, pushMessageInterface)
            } else {
                RelatedDigital.getPushMessagesWithID(activity, pushMessageInterface)
            }
        }
    }

    private fun setupTemplatePushButtons() {
        binding.btnText.setOnClickListener {
            val notificationId = Random().nextInt()
            val pushNotificationManager = PushNotificationManager()
            val message: Message = Gson().fromJson(TestPush.testText, Message::class.java)
            pushNotificationManager.generateNotification(
                applicationContext,
                message,
                null,
                notificationId
            )
        }


        binding.btnImage.setOnClickListener {
            val notificationId = Random().nextInt()
            val pushNotificationManager = PushNotificationManager()
            val message: Message = Gson().fromJson(TestPush.testImage, Message::class.java)
            pushNotificationManager.generateNotification(
                applicationContext, message, AppUtils.getBitMapFromUri(
                    applicationContext, message.mediaUrl!!
                ), notificationId
            )
        }

        binding.btnCarousel.setOnClickListener {
            val notificationId = Random().nextInt()
            val pushNotificationManager = PushNotificationManager()
            val message: Message = Gson().fromJson(TestPush.testCarousel, Message::class.java)
            pushNotificationManager.generateCarouselNotification(
                applicationContext,
                message,
                notificationId
            )
        }
    }

    private fun setupSyncButton() {
        binding.btnSync.setOnClickListener {
            if(binding.autotext.text.toString().isNotEmpty()) {
                RelatedDigital.setGsmPermit(this, GsmPermit.ACTIVE)
                RelatedDigital.setTwitterId(this, "testTwitterId")
                RelatedDigital.setEmail(this, binding.autotext.text.toString())
                RelatedDigital.setFacebookId(this, "testFacebookId")
                RelatedDigital.setRelatedDigitalUserId(this,  binding.autotextkey.text.toString())
                RelatedDigital.setPhoneNumber(this, "testPhoneNumber")
                RelatedDigital.setUserProperty(this, "instagram", "testInstagramId")

                val callback: EuromessageCallback = object : EuromessageCallback {
                    override fun success() {
                        Toast.makeText(applicationContext, "Successful", Toast.LENGTH_LONG).show()
                    }

                    override fun fail(errorMessage: String?) {
                        Toast.makeText(applicationContext, "Failure", Toast.LENGTH_LONG).show()
                    }

                }
                RelatedDigital.sync(this, callback)
            } else {
                Toast.makeText(this, "Invalid email", Toast.LENGTH_LONG).show()
            }


        }
    }

    private fun setupRegisterEmailButton() {
        binding.btnRegisteremail.setOnClickListener {
            if(binding.registeremailAutotext.text.toString().isNotEmpty()) {
                var isCommercial = false
                val isCommercialText =
                    java.lang.String.valueOf(binding.registeremailcommercialSpinner.selectedItem)
                if (isCommercialText == Constants.EURO_RECIPIENT_TYPE_TACIR) {
                    isCommercial = true
                }

                var emailPermit = EmailPermit.ACTIVE
                val isEmailPermitActiveText =
                    java.lang.String.valueOf(binding.registeremailpermitSpinner.selectedItem)
                if (isEmailPermitActiveText == Constants.EMAIL_PERMIT_PASSIVE) {
                    emailPermit = EmailPermit.PASSIVE
                }

                RelatedDigital.setGsmPermit(this, GsmPermit.ACTIVE)
                RelatedDigital.setTwitterId(this, "testTwitterId")
                RelatedDigital.setFacebookId(this, "testFacebookId")
                RelatedDigital.setRelatedDigitalUserId(this, "testRelatedDigitalUserId")
                RelatedDigital.setPhoneNumber(this, "testPhoneNumber")
                RelatedDigital.setUserProperty(this, "instagram", "testInstagramId")

                val callback: EuromessageCallback = object : EuromessageCallback {
                    override fun success() {
                        Toast.makeText(applicationContext, "Successful", Toast.LENGTH_LONG).show()
                    }

                    override fun fail(errorMessage: String?) {
                        Toast.makeText(applicationContext, "Failure", Toast.LENGTH_LONG).show()
                    }

                }
                RelatedDigital.registerEmail(
                    this, binding.registeremailAutotext.text.toString(),
                    emailPermit, isCommercial, callback
                )
            } else {
                Toast.makeText(this, "Invalid email", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            val bundle = intent.extras
            if (bundle != null) {
                val message = intent.extras!!.getSerializable("message") as Message?
                if (message != null) {
                    handlePush(message, intent)
                } else {
                    // Carousel push notification : an item was clicked
                    val itemClickedUrl = bundle.getString("CarouselItemClickedUrl")
                    if (itemClickedUrl != null && itemClickedUrl != "") {
                        try {
                            val viewIntent = Intent(Intent.ACTION_VIEW, Uri.parse(itemClickedUrl))
                            viewIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(viewIntent)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
        setPushParamsUI()
    }

    override fun onResume() {
        super.onResume()
        if(isFirstResume) {
            isFirstResume = false
            if (intent != null) {
                val bundle = intent.extras
                if (bundle != null) {
                    val message = intent.extras!!.getSerializable("message") as Message?
                    if (message != null) {
                        handlePush(message, intent)
                    } else {
                        // Carousel push notification : an item was clicked
                        val itemClickedUrl = bundle.getString("CarouselItemClickedUrl")
                        if (itemClickedUrl != null && itemClickedUrl != "") {
                            try {
                                val viewIntent =
                                    Intent(Intent.ACTION_VIEW, Uri.parse(itemClickedUrl))
                                viewIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(viewIntent)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            }
            setPushParamsUI()
        }
    }

    private fun handlePush(message: Message, intent: Intent) {
        // Send open report
        RelatedDigital.sendPushNotificationOpenReport(this, message)

        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        val lastPushTime = dateFormat.format(Calendar.getInstance().time)
        SharedPref.writeString(applicationContext, Constants.LAST_PUSH_TIME, lastPushTime)
        SharedPref.writeString(
            applicationContext,
            Constants.LAST_PUSH_PARAMS,
            GsonBuilder().create().toJson(message.getParams())
        )
    }

    private fun setPushParamsUI() {
        val payloadStr = StringBuilder()
        payloadStr.append(SharedPref.readString(applicationContext, Constants.LAST_PUSH_TIME)).append("\n\n")
        val lastPushParamsString: String =
            SharedPref.readString(applicationContext, Constants.LAST_PUSH_PARAMS)
        val gson = Gson()
        val paramsType = object : TypeToken<Map<String?, String?>?>() {}.type
        val params = gson.fromJson<Map<String, String>>(lastPushParamsString, paramsType)
            ?: return
        for (param in params.entries) {
            payloadStr.append(param.key).append(" : ").append(param.value).append("\n\n")
        }
        binding.payloadView.text = payloadStr.toString()
    }

    private fun setupDeleteAllPushButton() {
        binding.btnTextDeleteAllPush.setOnClickListener{


            RelatedDigital.deleteAllPushNotifications(this)

        }

    }

    private fun setupDeletePushWithIdButton() {
        binding.btnTextDeletePushWithId.setOnClickListener{


            RelatedDigital.deletePushNotificationWithId(this)

        }

    }
}