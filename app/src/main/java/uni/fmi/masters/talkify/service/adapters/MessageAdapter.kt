package uni.fmi.masters.talkify.service.adapters

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uni.fmi.masters.talkify.R
import uni.fmi.masters.talkify.model.message.Message
import uni.fmi.masters.talkify.model.user.User

class MessageAdapter(
    private var messages: List<Message>,
    private var currentUser: User?) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val messageTextView: TextView = view.findViewById(R.id.messageTextView)
        val senderTextView: TextView = view.findViewById(R.id.senderTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.messageTextView.text = message.text
        holder.senderTextView.text = message.sender

        if (message.sender == currentUser?.username) {
            val params = holder.messageTextView.layoutParams as LinearLayout.LayoutParams
            params.gravity = Gravity.END
            holder.messageTextView.layoutParams = params

            val senderParams = holder.senderTextView.layoutParams as LinearLayout.LayoutParams
            senderParams.gravity = Gravity.END
            holder.senderTextView.layoutParams = senderParams
        } else {
            val params = holder.messageTextView.layoutParams as LinearLayout.LayoutParams
            params.gravity = Gravity.START
            holder.messageTextView.layoutParams = params

            val senderParams = holder.senderTextView.layoutParams as LinearLayout.LayoutParams
            senderParams.gravity = Gravity.START
            holder.senderTextView.layoutParams = senderParams
        }
    }

    override fun getItemCount(): Int = messages.size

    fun updateMessages(newMessages: List<Message>) {
        messages = newMessages
        notifyDataSetChanged()
    }

    fun updateCurrentUser(user: User?) {
        currentUser = user
        notifyDataSetChanged()
    }
}