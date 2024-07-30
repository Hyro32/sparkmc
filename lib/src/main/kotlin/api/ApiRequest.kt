package api

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

object ApiRequest {
    private val client = OkHttpClient()
    private val JSON = "application/json".toMediaType()

    fun get(url: String): String {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).execute().use { response ->
            return response.body!!.string()
        }
    }

    fun post(url: String, body: String) {
        val request = Request.Builder()
            .url(url)
            .post(body.toRequestBody(JSON))
            .build()

        client.newCall(request).execute().use { response ->
            println(response.body!!.string())
        }
    }

    fun put(url: String, body: String) {
        val request = Request.Builder()
            .url(url)
            .put(body.toRequestBody(JSON))
            .build()

        client.newCall(request).execute().use { response ->
            println(response.body!!.string())
        }
    }

    fun delete(url: String) {
        val request = Request.Builder()
            .url(url)
            .delete()
            .build()

        client.newCall(request).execute().use { response ->
            println(response.body!!.string())
        }
    }

    fun patch(url: String, body: String) {
        val request = Request.Builder()
            .url(url)
            .patch(body.toRequestBody(JSON))
            .build()

        client.newCall(request).execute().use { response ->
            println(response.body!!.string())
        }
    }
}