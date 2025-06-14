package uni.fmi.masters.talkify.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uni.fmi.masters.talkify.R
import uni.fmi.masters.talkify.model.channel.Channel
import uni.fmi.masters.talkify.model.channel.ChannelCreateUpdateRequest
import uni.fmi.masters.talkify.model.message.Message
import uni.fmi.masters.talkify.model.message.MessageCreateRequest
import uni.fmi.masters.talkify.model.user.User
import uni.fmi.masters.talkify.service.adapters.FriendsAdapter
import uni.fmi.masters.talkify.service.adapters.GroupChatsAdapter
import uni.fmi.masters.talkify.service.adapters.MessageAdapter
import uni.fmi.masters.talkify.service.adapters.UserSearchAdapter
import uni.fmi.masters.talkify.service.api.ChannelApi
import uni.fmi.masters.talkify.service.api.FriendshipApi
import uni.fmi.masters.talkify.service.api.MessageApi
import uni.fmi.masters.talkify.service.api.UserApi
import javax.inject.Inject

@AndroidEntryPoint
class TalkifyActivity : AppCompatActivity() {

    private var selectedChannelId: String? = null
    private var currentUser: User? = null

    @Inject lateinit var userApi: UserApi
    @Inject lateinit var channelApi: ChannelApi
    @Inject lateinit var messageApi: MessageApi
    @Inject lateinit var friendshipApi: FriendshipApi

    private lateinit var friendsRecyclerView: RecyclerView
    private lateinit var groupChatsRecyclerView: RecyclerView
    private lateinit var messagesRecyclerView: RecyclerView

    private val friendsAdapter by lazy { FriendsAdapter(emptyList(), selectedChannelId) { onUserSelected(it) } }
    private val groupChatsAdapter by lazy { GroupChatsAdapter(emptyList(), selectedChannelId) { onChannelSelected(it) } }
    private val messageAdapter by lazy { MessageAdapter(emptyList(), currentUser) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_talkify)
        findViewById<Button>(R.id.sendButton).setOnClickListener { sendMessage() }
        loadChannelCreateBtn()
        loadUsersSearchBtn()

        // Initialize RecyclerViews
        friendsRecyclerView = findViewById(R.id.friendsRecyclerView)
        groupChatsRecyclerView = findViewById(R.id.groupChatsRecyclerView)
        messagesRecyclerView = findViewById(R.id.messagesRecyclerView)

        // Set LayoutManagers
        friendsRecyclerView.layoutManager = LinearLayoutManager(this)
        groupChatsRecyclerView.layoutManager = LinearLayoutManager(this)
        messagesRecyclerView.layoutManager = LinearLayoutManager(this)

        friendsRecyclerView.adapter = friendsAdapter
        groupChatsRecyclerView.adapter = groupChatsAdapter
        messagesRecyclerView.adapter = messageAdapter

        // Fetch and display data for users and channels
        lifecycleScope.launch {
            currentUser = userApi.getCurrent().body()
            messageAdapter.updateCurrentUser(currentUser)
            loadUsers()
            loadChannels()
        }
    }

    private fun sendMessage() {
        val message = findViewById<EditText>(R.id.messageInput).text.toString()
        if (message.isNotBlank()) {
            lifecycleScope.launch {
                try {
                    messageApi.create(MessageCreateRequest(message, selectedChannelId!!))
                    findViewById<EditText>(R.id.messageInput).text.clear()
                    loadMessages(selectedChannelId)
                } catch (e: Exception) {
                    // TODO
                }
            }
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
                val adapter = FriendsAdapter(users, selectedChannelId) { user ->
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
                val adapter = GroupChatsAdapter(channels, selectedChannelId) { channel ->
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

    private fun loadMessages(channelId: String?) {
        if (channelId.isNullOrBlank()) {
            // No channelId, display fallback message
            messageAdapter.updateMessages(
                listOf(Message(id = "", text = "Please select chat", sender = "", sentAt = "", editedAt = "", _links = mapOf()))
            )
        } else {
            // Fetch messages using channelId
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = messageApi.getAllByCriteria(channelId, 0, 1000, "sentAt,asc")
                    if (response.isSuccessful) {
                        val messages = response.body()?._embedded?.get("messages") ?: emptyList()
                        runOnUiThread {
                            messageAdapter.updateMessages(messages)
                            messagesRecyclerView.scrollToPosition(messagesRecyclerView.adapter!!.itemCount - 1)
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this@TalkifyActivity, "Failed to load messages", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(this@TalkifyActivity, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun onUserSelected(user: User) {
        selectedChannelId = user.privateChannelId
        loadMessages(user.privateChannelId)
    }

    private fun onChannelSelected(channel: Channel) {
        selectedChannelId = channel.id
        loadMessages(channel.id)
    }

    private fun loadChannelCreateBtn() {
        val createChannelBtn = findViewById<Button>(R.id.createChannelBtn)
        createChannelBtn.setOnClickListener {
            // Inflate the dialog layout
            val dialogView = layoutInflater.inflate(R.layout.dialog_create_channel, null)
            val inputField = dialogView.findViewById<EditText>(R.id.channelNameInput)

            val dialog = AlertDialog.Builder(this)
                .setTitle("Create Channel")
                .setView(dialogView)
                .setPositiveButton("Create") { _, _ ->
                    val channelName = inputField.text.toString().trim()
                    if (channelName.isNotEmpty()) {
                        lifecycleScope.launch {
                            if (channelApi.create(ChannelCreateUpdateRequest(channelName)).code() == 201) {
                                loadChannels()
                                Toast.makeText(
                                    this@TalkifyActivity,
                                    "Channel '$channelName' created!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    this@TalkifyActivity,
                                    "Channel with this name already exists!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        Toast.makeText(this, "Channel name can't be empty.", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancel", null)
                .create()

            dialog.show()
        }
    }

    private fun loadUsersSearchBtn() {
        val searchUserBtn = findViewById<Button>(R.id.searchBtn)
        searchUserBtn.setOnClickListener {
            val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_user_search, null)
            val dialog = AlertDialog.Builder(this)
                .setView(dialogView)
                .setTitle("Search Users")
                .setNegativeButton("Cancel", null)
                .create()

            val searchInput = dialogView.findViewById<EditText>(R.id.searchInput)
            val recyclerView = dialogView.findViewById<RecyclerView>(R.id.userRecyclerView)

            val adapter = UserSearchAdapter(emptyList()) { user ->
                lifecycleScope.launch {
                    if (friendshipApi.addFriend(user.id).code() == 201) {
                        Toast.makeText(this@TalkifyActivity, "Added ${user.username} as friend", Toast.LENGTH_SHORT).show()
                        loadUsers()
                        dialog.cancel()
                    }
                }
            }

            lifecycleScope.launch {
                val users = userApi.getAllByCriteria(
                    search = "",
                    username = "",
                    email = "",
                    inChannelId = "",
                    notInChannelId = "",
                    onlyFriends = false,
                    active = null,
                    page = 0,
                    size = 20,
                    sort = "username"
                ).body()?._embedded?.get("users")?.filter { user -> !user._links["addFriend"]?.href.isNullOrEmpty() }
                adapter.updateList(users ?: emptyList())
            }

            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = adapter

            // Filter users on text change
            searchInput.addTextChangedListener {
                lifecycleScope.launch {
                    val users = userApi.getAllByCriteria(
                        search = it.toString(),
                        username = "",
                        email = "",
                        inChannelId = "",
                        notInChannelId = "",
                        onlyFriends = false,
                        active = null,
                        page = 0,
                        size = 20,
                        sort = "username"
                    ).body()?._embedded?.get("users")?.filter { user -> !user._links["addFriend"]?.href.isNullOrEmpty() }
                    adapter.updateList(users ?: emptyList())
                }
            }

            dialog.show()
        }
    }
}