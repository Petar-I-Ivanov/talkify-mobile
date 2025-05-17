package uni.fmi.masters.talkify.model.user

import uni.fmi.masters.talkify.model.common.Link

data class User(
    val id: String,
    val username: String,
    val email: String,
    val privateChannelId: String,
    val _links: Map<String, Link>
)
