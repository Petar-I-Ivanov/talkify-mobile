package uni.fmi.masters.talkify.model.common

import uni.fmi.masters.talkify.model.user.User

data class PagedModel<T>(
    val page: Page,
    val embedded: Map<String, List<T>>,
    val links: Map<String, Link>
)