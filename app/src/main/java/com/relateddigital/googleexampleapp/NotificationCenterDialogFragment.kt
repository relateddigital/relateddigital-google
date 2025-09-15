import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.relateddigital.googleexampleapp.R // TODO: Kendi R sınıfınızın yolunu yazın
import com.relateddigital.relateddigital_google.RelatedDigital // TODO: Kendi RelatedDigital sınıfınızın yolunu yazın
import com.relateddigital.relateddigital_google.push.PushMessageInterface
import com.relateddigital.relateddigital_google.model.Message
import com.relateddigital.relateddigital_google.util.SharedPref // TODO: Kendi SharedPref sınıfınızın yolunu yazın
import org.json.JSONArray

class NotificationCenterDialogFragment : DialogFragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var loadingIndicator: ProgressBar
    private lateinit var messageView: TextView
    private lateinit var closeButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_notification_center, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recycler_view_dialog)
        loadingIndicator = view.findViewById(R.id.loading_indicator_dialog)
        messageView = view.findViewById(R.id.message_view_dialog)
        closeButton = view.findViewById(R.id.btn_close)

        setupRecyclerView()
        loadNotifications()

        closeButton.setOnClickListener { dismiss() }
    }

    override fun onStart() {
        super.onStart()
        // Dialog'u genişlet
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun loadNotifications() {
        showLoading()

        val pushMessageInterface = object : PushMessageInterface {
            override fun success(messages: List<Message>) {
                if (messages.isNotEmpty()) {
                    showContent(messages)
                    val gson = Gson()
                    messages.forEachIndexed { index, msg ->
                        val json = gson.toJson(msg)  // tek objeyi JSON string'e çevir
                        Log.d("GetPushMessage", "[$index] $json")
                    }
                } else {
                    showMessage("Gösterilecek bildirim bulunmuyor.")
                }
            }

            override fun fail(errorMessage: String) {
                showMessage("Hata: $errorMessage")
            }
        }

        val hostActivity = requireActivity() as? AppCompatActivity

        if (hostActivity != null) {
            // Eğer Activity bir AppCompatActivity ise fonksiyonu çağır
            val loginID = SharedPref.readString(requireContext(), com.relateddigital.relateddigital_google.constants.Constants.NOTIFICATION_LOGIN_ID_KEY)
            if(loginID.isEmpty()) {
                RelatedDigital.getPushMessages(hostActivity, pushMessageInterface)
            } else {
                RelatedDigital.getPushMessagesWithID(hostActivity, pushMessageInterface)
            }
        } else {
            // Eğer değilse, hata ver.
            showMessage("Hata: Bu fragment sadece AppCompatActivity içinde çalışabilir.")
        }
    }

    private fun showLoading() {
        loadingIndicator.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        messageView.visibility = View.GONE
    }

    private fun showContent(messages: List<Message>) {
        recyclerView.adapter = NotificationAdapter(messages)
        loadingIndicator.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        messageView.visibility = View.GONE
    }

    private fun showMessage(message: String) {
        messageView.text = message
        loadingIndicator.visibility = View.GONE
        recyclerView.visibility = View.GONE
        messageView.visibility = View.VISIBLE
    }
}