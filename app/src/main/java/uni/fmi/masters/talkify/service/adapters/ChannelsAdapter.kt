package uni.fmi.masters.talkify.service.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uni.fmi.masters.talkify.model.channel.Channel

class GroupChatsAdapter(
    private val channels: List<Channel>,
    private val onClick: (Channel) -> Unit
) : RecyclerView.Adapter<GroupChatsAdapter.GroupChatsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupChatsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return GroupChatsViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupChatsViewHolder, position: Int) {
        val channel = channels[position]
        holder.bind(channel)
    }

    override fun getItemCount(): Int = channels.size

    inner class GroupChatsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(android.R.id.text1)

        fun bind(channel: Channel) {
            textView.text = channel.name
            itemView.setOnClickListener { onClick(channel) }
        }
    }
}