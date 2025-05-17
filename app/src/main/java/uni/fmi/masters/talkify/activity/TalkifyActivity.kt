package uni.fmi.masters.talkify.activity

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
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
import javax.inject.Inject

@AndroidEntryPoint
class TalkifyActivity : AppCompatActivity() {

    @Inject lateinit var userApi: UserApi
    @Inject lateinit var channelApi: ChannelApi

    private lateinit var friendsRecyclerView: RecyclerView
    private lateinit var groupChatsRecyclerView: RecyclerView

    private val friendsAdapter by lazy { FriendsAdapter(emptyList()) { onUserSelected(it) } }
    private val groupChatsAdapter by lazy { GroupChatsAdapter(emptyList()) { onChannelSelected(it) } }

    private var selectedChannelId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_talkify)

        // Initialize RecyclerViews
        friendsRecyclerView = findViewById(R.id.friendsRecyclerView)
        groupChatsRecyclerView = findViewById(R.id.groupChatsRecyclerView)

        // Set LayoutManagers
        friendsRecyclerView.layoutManager = LinearLayoutManager(this)
        groupChatsRecyclerView.layoutManager = LinearLayoutManager(this)

        friendsRecyclerView.adapter = friendsAdapter
        groupChatsRecyclerView.adapter = groupChatsAdapter

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
                val users = response.body()?._embedded?.get("users") ?: emptyList()
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
                val channels = response.body()?._embedded?.get("channels") ?: emptyList()
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
        selectedChannelId = user.privateChannelId
    }

    private fun onChannelSelected(channel: Channel) {
        selectedChannelId = channel.id
    }
}