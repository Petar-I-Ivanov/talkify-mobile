package uni.fmi.masters.talkify.model.channel

import uni.fmi.masters.talkify.model.common.Link

data class Channel(
    val id: String,
    val name: String,
    val _links: Map<String, Link>
)
