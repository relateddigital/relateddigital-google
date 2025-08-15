import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.relateddigital.googleexampleapp.R // TODO: Kendi R sınıfınızın yolunu yazın
import com.relateddigital.relateddigital_google.model.Message // TODO: Kendi Message sınıfınızın yolunu yazın

class NotificationAdapter(private var notifications: List<Message>) :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleView: TextView = itemView.findViewById(R.id.notification_title)
        val messageView: TextView = itemView.findViewById(R.id.notification_message)
        val dateView: TextView = itemView.findViewById(R.id.notification_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notifications[position]
        holder.titleView.text = notification.title
        holder.messageView.text = notification.message
        holder.dateView.text = notification.date
    }

    override fun getItemCount() = notifications.size
}