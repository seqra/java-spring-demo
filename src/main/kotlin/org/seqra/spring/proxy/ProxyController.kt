package org.seqra.spring.proxy

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Controller exposing URL-fetch endpoints.
 *
 * Every endpoint passes user-controlled input (URL) to [UrlFetchService.fetch]
 * without validation, creating an SSRF vulnerability.  The taint travels through
 * a Kotlin coroutine [scope.launch] dispatched on [Dispatchers.IO].
 */
@RestController
@RequestMapping("/api/proxy")
class ProxyController(private val fetchService: UrlFetchService) {

    /**
     * Fetch via JSON body — taint flows through [FetchRequest] DTO field.
     */
    @PostMapping("/fetch")
    fun fetchUrl(@RequestBody request: FetchRequest): ResponseEntity<String> {
        return try {
            val body = fetchService.fetch(request.url, request.headers)
            ResponseEntity.ok(body)
        } catch (e: Exception) {
            ResponseEntity.badRequest().body("Fetch failed: ${e.message}")
        }
    }

    /**
     * Fetch via query parameter — taint flows directly from request param.
     */
    @GetMapping("/fetch")
    fun fetchByParam(@RequestParam url: String): ResponseEntity<String> {
        return try {
            val body = fetchService.fetch(url)
            ResponseEntity.ok(body)
        } catch (e: Exception) {
            ResponseEntity.badRequest().body("Fetch failed: ${e.message}")
        }
    }
}
