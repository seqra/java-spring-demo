package org.seqra.spring.proxy

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URI

/**
 * Service that fetches content from a user-supplied URL using Kotlin coroutines.
 *
 * Vulnerability: SSRF — the caller-provided URL is passed directly to [java.net.URL.openConnection]
 * without any validation or allow-list check, letting an attacker reach internal
 * services (e.g. cloud metadata endpoints, internal APIs).
 */
@Service
class UrlFetchService {

    private val scope = CoroutineScope(Dispatchers.IO)

    /**
     * Fetches the content at [url] by launching a coroutine on [scope].
     * No URL validation is performed — intentionally vulnerable.
     */
    fun fetch(url: String, headers: Map<String, String> = emptyMap()): String {
        val deferred = CompletableDeferred<String>()

        scope.launch {
            try {
                val connection = URI.create(url).toURL().openConnection()

                headers.forEach { (k, v) -> connection.setRequestProperty(k, v) }
                connection.connect()

                val body = BufferedReader(InputStreamReader(connection.getInputStream())).use { reader ->
                    reader.readText()
                }
                deferred.complete(body)
            } catch (e: Exception) {
                deferred.completeExceptionally(e)
            }
        }

        return kotlinx.coroutines.runBlocking { deferred.await() }
    }
}
