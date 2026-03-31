package org.seqra.spring.proxy

/**
 * DTO for URL fetch requests.
 * The [url] field carries user-controlled input through the coroutine data-flow.
 */
data class FetchRequest(
    val url: String = "",
    val headers: Map<String, String> = emptyMap()
)
