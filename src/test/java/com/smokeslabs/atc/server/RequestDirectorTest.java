package com.smokeslabs.atc.server;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.exceptions.base.MockitoException;

public class RequestDirectorTest {
    private HttpClient httpClient;

    private final String testRawHttpRequestWithJson = new StringBuilder()
            .append("POST /endpoint HTTP/1.1\r\n")
            .append("Host: api.example.com\r\n")
            .append("Accept: application/json\r\n")
            .append("Content-Length: 23\r\n")
            .append("Content-Type: application/json\r\n\r\n")
            .append("{\"message\": \"request\"}\r\n")
            .toString();

    private final String testRawHttpRequestNoContent = new StringBuilder()
            .append("GET /endpoint HTTP/1.1\r\n")
            .append("Host: api.example.com\r\n")
            .append("Accept: application/json\r\n\r\n")
            .toString();

    @BeforeEach
    public void setupEach() throws IOException, InterruptedException {
        // mock the response headers first
        var httpResponseHeaders = mock(HttpHeaders.class);
        var httpResponseHeadersMap = new HashMap<String, List<String>>();
        httpResponseHeadersMap.put("Host", Arrays.asList("api.weather.gov"));
        httpResponseHeadersMap.put("Accept", Arrays.asList("application/json"));
        httpResponseHeadersMap.put(":status", Arrays.asList("200"));
        httpResponseHeadersMap.put("Content-Length", Arrays.asList("23"));
        Optional<String> optionalHostValue = Optional.of("api.weather.gov");
        when(httpResponseHeaders.firstValue("Host")).thenReturn(optionalHostValue);
        Optional<String> optionalAcceptValue = Optional.of("application/json");
        when(httpResponseHeaders.firstValue("Accept")).thenReturn(optionalAcceptValue);
        Optional<String> optionalStatusValue = Optional.of("201");
        when(httpResponseHeaders.firstValue(":status")).thenReturn(optionalStatusValue);
        Optional<String> optionalContentLength = Optional.of("24");
        when(httpResponseHeaders.firstValue("Content-Length")).thenReturn(optionalContentLength);

        when(httpResponseHeaders.map()).thenReturn(httpResponseHeadersMap);

        // mock the rest of the stdlib HttpResponse
        var httpResponse = (HttpResponse<String>) mock(HttpResponse.class);
        when(httpResponse.headers()).thenReturn(httpResponseHeaders);
        when(httpResponse.statusCode()).thenReturn(200);
        when(httpResponse.body()).thenReturn("{\"message\": \"response\"}");

        // mock the client
        httpClient = mock(HttpClient.class);
        when(httpClient.send(any(HttpRequest.class), eq(BodyHandlers.ofString()))).thenReturn(httpResponse);

        // make the static method return out httpClient
        try {
            MockedStatic<HttpClient> mockStaticHttpClient = mockStatic(HttpClient.class);
            mockStaticHttpClient.when(HttpClient::newHttpClient).thenReturn(httpClient);
            when(HttpClient.newHttpClient()).thenReturn(httpClient);
        } catch (MockitoException e) {
            if (!e.getMessage().contains("static mocking is already registered in the current thread")) {
                throw e;
            }
        }
    }

    @Test
    public void directRequest_ShouldGetAtcHttpResponse_WhenGivenAtcHttpRequest() {
    }
}
