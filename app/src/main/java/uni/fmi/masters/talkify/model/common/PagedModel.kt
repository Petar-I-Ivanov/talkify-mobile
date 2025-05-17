package uni.fmi.masters.talkify.model.common

data class PagedModel<T>(
    val page: Page,
    val _embedded: Map<String, List<T>>,
    val _links: Map<String, Link>
)