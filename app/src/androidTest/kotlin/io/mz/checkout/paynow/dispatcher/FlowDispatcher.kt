/*
 * Copyright 2025 Martin Zangl
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.mz.checkout.paynow.dispatcher

import android.content.Context
import androidx.annotation.RawRes
import io.mz.checkout.paynow.dispatcher.flow.JsonFlow
import java.util.PriorityQueue
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import mockwebserver3.Dispatcher
import mockwebserver3.MockResponse
import mockwebserver3.RecordedRequest
import okhttp3.Headers

class FlowDispatcher : Dispatcher() {

  private val comparator =
    Comparator<JsonFlow> { flow1, flow2 -> flow1.step.compareTo(flow2.step) }
  private val priorityQueue: PriorityQueue<JsonFlow> = PriorityQueue(comparator)

  private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

  override fun dispatch(request: RecordedRequest): MockResponse {
    if (priorityQueue.isEmpty()) {
      return MockResponse().newBuilder().code(500).build()
    } else {
      val flow = priorityQueue.poll() ?: throw IllegalStateException()
      return MockResponse().newBuilder()
        .code(flow.code)
        .headers(buildHeaders(flow.headers))
        .body(flow.body)
        .build()
    }
  }

  fun build(context: Context, @RawRes flow: Int) {
    val flow: List<JsonFlow> = json.decodeFromStream(context.resources.openRawResource(flow))
    priorityQueue.addAll(flow)
  }

  private fun buildHeaders(headers: Map<String, String>): Headers {
    val builder = Headers.Builder()
    for (header in headers)
      builder.add(header.key, header.value)
    return builder.build()
  }

  override fun close() {
    super.close()
    priorityQueue.clear()
  }
}
