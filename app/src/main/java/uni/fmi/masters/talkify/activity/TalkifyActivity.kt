package uni.fmi.masters.talkify.activity

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uni.fmi.masters.talkify.R
import uni.fmi.masters.talkify.model.channel.Channel
import uni.fmi.masters.talkify.model.user.User
import uni.fmi.masters.talkify.service.adapters.FriendsAdapter
import uni.fmi.masters.talkify.service.adapters.GroupChatsAdapter
import uni.fmi.masters.talkify.service.api.ChannelApi
import uni.fmi.masters.talkify.service.api.UserApi

class TalkifyActivity(
    private val userApi: UserApi,
    private val channelApi: ChannelApi)
    : AppCompatActivity() {

    private lateinit var friendsRecyclerView: RecyclerView
    private lateinit var groupChatsRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_talkify)

        // Initialize RecyclerViews
        friendsRecyclerView = findViewById(R.id.friendsFragmentContainer)
        groupChatsRecyclerView = findViewById(R.id.groupChatsFragmentContainer)

        // Set LayoutManagers
        friendsRecyclerView.layoutManager = LinearLayoutManager(this)
        groupChatsRecyclerView.layoutManager = LinearLayoutManager(this)

        // Fetch and display data for users and channels
        lifecycleScope.launch {
            loadUsers();
            loadChannels();
        }
    }

    private suspend fun loadUsers() {
        try {
            val response = withContext(Dispatchers.IO) {
                userApi.getAllByCriteria(
                    search = "",
                    username = "",
                    email = "",
                    inChannelId = "",
                    notInChannelId = "",
                    onlyFriends = true,
                    active = true,
                    page = 0,
                    size = 20,
                    sort = "username"
                )
            }

            if (response.isSuccessful) {
                val users = response.body()?.embedded?.get("users") ?: emptyList()
                val adapter = FriendsAdapter(users) { user ->
                    onUserSelected(user)
                }
                friendsRecyclerView.adapter = adapter
            } else {
                // Handle failure (e.g., show a message)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun loadChannels() {
        try {
            val response = withContext(Dispatchers.IO) {
                channelApi.getAllByCriteria(
                    name = "",
                    userId = "",
                    ownerId = "",
                    adminId = "",
                    guestId = "",
                    active = true,
                    page = 0,
                    size = 20,
                    sort = "name"
                )
            }

            if (response.isSuccessful) {
                val channels = response.body()?.embedded?.get("channels") ?: emptyList()
                val adapter = GroupChatsAdapter(channels) { channel ->
                    onChannelSelected(channel)
                }
                groupChatsRecyclerView.adapter = adapter
            } else {
                // Handle failure (e.g., show a message)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun onUserSelected(user: User) {
        // Show chat UI for the selected user
        // Update the chat header, load messages, etc.
        findViewById<TextView>(R.id.chatHeader).text = "Chat with ${user.username}"
    }

    private fun onChannelSelected(channel: Channel) {
        // Show chat UI for the selected channel
        // Update the chat header, load messages, etc.
        findViewById<TextView>(R.id.chatHeader).text = "Channel: ${channel.name}"
    }

}