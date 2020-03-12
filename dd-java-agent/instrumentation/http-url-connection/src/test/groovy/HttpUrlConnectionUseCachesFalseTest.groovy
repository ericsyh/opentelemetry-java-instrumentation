import datadog.trace.agent.test.base.HttpClientTest
import datadog.trace.instrumentation.http_url_connection.HttpUrlConnectionDecorator

import static datadog.trace.bootstrap.instrumentation.api.AgentTracer.activeScope

class HttpUrlConnectionUseCachesFalseTest extends HttpClientTest {

  @Override
  int doRequest(String method, URI uri, Map<String, String> headers, Closure callback) {
    HttpURLConnection connection = uri.toURL().openConnection()
    try {
      connection.setRequestMethod(method)
      headers.each { connection.setRequestProperty(it.key, it.value) }
      connection.setRequestProperty("Connection", "close")
      connection.useCaches = false
      def parentSpan = activeScope()
      def stream = connection.inputStream
      assert activeScope() == parentSpan
      stream.readLines()
      stream.close()
      callback?.call()
      return connection.getResponseCode()
    } finally {
      connection.disconnect()
    }
  }

  @Override
  String component() {
    return HttpUrlConnectionDecorator.DECORATE.component()
  }

  @Override
  boolean testCircularRedirects() {
    false
  }
}
