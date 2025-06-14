package uni.fmi.masters.talkify.service.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uni.fmi.masters.talkify.R
import uni.fmi.masters.talkify.model.user.User

class FriendsAdapter(
    private val users: List<User>,
    private var selectedChannelId: String?,
    private val onClick: (User) -> Unit
) : RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return FriendsViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int = users.size

    inner class FriendsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(android.R.id.text1)

        fun bind(user: User) {
            textView.text = user.username

            if (user.privateChannelId == selectedChannelId) {
                itemView.setBackgroundResource(R.drawable.border_selected)
            } else {
                itemView.setBackgroundResource(android.R.color.transparent)
            }

            itemView.setOnClickListener {
                val previousSelected = selectedChannelId
                selectedChannelId = user.privateChannelId

                notifyItemChanged(adapterPosition)
                val prevIndex = users.indexOfFirst { it.privateChannelId == previousSelected }
                if (prevIndex != -1) notifyItemChanged(prevIndex)

                onClick(user)
            }
        }
    }
}