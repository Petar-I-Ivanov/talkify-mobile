package uni.fmi.masters.talkify.service.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uni.fmi.masters.talkify.R
import uni.fmi.masters.talkify.model.channel.Channel

class GroupChatsAdapter(
    private val channels: List<Channel>,
    private var selectedChannelId: String?,
    private val onClick: (Channel) -> Unit,
    private val onRename: (Channel) -> Unit,
    private val onDelete: (Channel) -> Unit,
    private val onAddMember: (Channel) -> Unit
) : RecyclerView.Adapter<GroupChatsAdapter.GroupChatsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupChatsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_group_chat, parent, false)
        return GroupChatsViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupChatsViewHolder, position: Int) {
        val channel = channels[position]
        holder.bind(channel)
    }

    override fun getItemCount(): Int = channels.size

    inner class GroupChatsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val channelName: TextView = itemView.findViewById(R.id.channelName)
        private val menuIcon: ImageView = itemView.findViewById(R.id.menuIcon)

        fun bind(channel: Channel) {
            channelName.text = channel.name

            itemView.setBackgroundResource(
                if (channel.id == selectedChannelId) R.drawable.border_selected
                else android.R.color.transparent
            )

            itemView.setOnClickListener {
                val previousSelected = selectedChannelId
                selectedChannelId = channel.id

                notifyItemChanged(adapterPosition)
                val prevIndex = channels.indexOfFirst { it.id == previousSelected }
                if (prevIndex != -1) notifyItemChanged(prevIndex)

                onClick(channel)
            }

            menuIcon.setOnClickListener { view ->
                showPopupMenu(view, channel)
            }
        }

        private fun showPopupMenu(view: View, channel: Channel) {
            val popup = PopupMenu(view.context, view)
            popup.menuInflater.inflate(R.menu.channel_item_menu, popup.menu)

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_rename -> {
                        onRename(channel)
                        true
                    }
                    R.id.action_delete -> {
                        onDelete(channel)
                        true
                    }
                    R.id.action_add_member -> {
                        onAddMember(channel)
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
    }
}