package uni.fmi.masters.talkify.service.adapters

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uni.fmi.masters.talkify.R
import uni.fmi.masters.talkify.model.user.User

class FriendsAdapter(
    private val users: List<User>,
    private var selectedChannelId: String?,
    private val onClick: (User) -> Unit,
    private val onRemoveFriend: (User) -> Unit
) : RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_chat, parent, false)
        return FriendsViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int = users.size

    inner class FriendsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val username: TextView = itemView.findViewById(R.id.username)
        private val removeFriendIcon: ImageView = itemView.findViewById(R.id.removeFriend)

        fun bind(user: User) {
            username.text = user.username

            itemView.setBackgroundResource(
                if (user.privateChannelId == selectedChannelId) R.drawable.border_selected
                else android.R.color.transparent
            )

            itemView.setOnClickListener {
                val previousSelected = selectedChannelId
                selectedChannelId = user.privateChannelId

                notifyItemChanged(adapterPosition)
                val prevIndex = users.indexOfFirst { it.privateChannelId == previousSelected }
                if (prevIndex != -1) notifyItemChanged(prevIndex)

                onClick(user)
            }

            removeFriendIcon.setOnClickListener {
                AlertDialog.Builder(itemView.context)
                    .setTitle("Remove Friend")
                    .setMessage("Do you want to remove '${user.username}' from friends?")
                    .setPositiveButton("Yes") { dialog, _ ->
                        onRemoveFriend(user)
                        dialog.dismiss()
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }
        }
    }
}