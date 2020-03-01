package com.my.list.controller;

import com.my.list.exception.DataException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.Enumeration;

@RequestMapping("/proxy")
@Controller
public class ProxyController {

    private static final String HOOK; static {
        HOOK = "<script>ORIGIN='%s'</script>\n" +
            "<script>PATH='%s'</script>\n" +
            "<script src='//code.jquery.com/jquery-3.3.1.min.js'></script>\n" +
            "<script src='//unpkg.com/xhook@latest/dist/xhook.min.js'></script>\n" +
            "<link rel='stylesheet' href='/proxy/hook/hook.css'></link>\n" +
            "<script src='/proxy/hook/hook.js'></script>\n";
    }
    private final Base64.Encoder encoder = Base64.getUrlEncoder();
    private final Base64.Decoder decoder = Base64.getUrlDecoder();
    private final RestTemplate restTemplate = new RestTemplate();

    @ResponseBody
    @RequestMapping(value = "/static/{url64}")
    public ResponseEntity<byte[]> proxyStaticAssets(
        HttpServletRequest request,
        @PathVariable String url64,
        @RequestBody(required = false) byte[] body
    ) {

        String urlStr = decodeBase64Url(url64);
        
        // Get url
        URL url = null;
        try { url = new URL(urlStr); } catch (MalformedURLException ignored) {}

        // Get method
        HttpMethod method = HttpMethod.resolve(request.getMethod());
        method = method == null ? HttpMethod.GET : method;
        
        // Get headers
        HttpHeaders headers = getHeaders(request, url == null ? request.getRemoteHost() : url.getHost());

        // Exchange request
        try {
            HttpEntity<byte[]> exchangeRequest = new HttpEntity<>(body, headers);
            return restTemplate.exchange(urlStr, method, exchangeRequest, byte[].class);
        } catch (RestClientResponseException e) {
            HttpStatus status = HttpStatus.resolve(e.getRawStatusCode());
            status = status == null ? HttpStatus.INTERNAL_SERVER_ERROR : status;
            return new ResponseEntity<>(e.getResponseBodyAsByteArray(), status);
        } catch (ResourceAccessException e) {
            return new ResponseEntity<>(e.getMessage().getBytes(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage().getBytes(), HttpStatus.BAD_REQUEST);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/page/{url64}")
    public ResponseEntity<byte[]> proxyPage(HttpServletRequest request, @PathVariable String url64) {

        URL url; Document doc;
        String urlStr = decodeBase64Url(url64);

        // Get document
        try {
            url = new URL(urlStr);
            Connection connect = Jsoup.connect(urlStr);
            HttpHeaders headers = getHeaders(request, url.getHost());
            headers.set("Accept-Encoding", "gzip, deflate");
            Connection data = connect.headers(headers.toSingleValueMap());
            doc = data.get();
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage().getBytes(), HttpStatus.BAD_REQUEST);
        }

        // Set referrer policy
        Elements referrers = doc.select("meta[name=referrer]");
        if (referrers.size() > 0) {
            for (Element referrer : referrers) {
                referrer.attr("content", "never");
            }
        } else {
            doc.head().append("<meta name=\"referrer\" content=\"never\">");
        }

        // Replace with absolute url
        Elements scripts = doc.select("script[src]");
        for (Element script : scripts) {
            String src = script.absUrl("src");
            if (!StringUtils.isEmpty(src)) {
                script.attr("src", src);
            }
        }
        for (Element link : doc.select("link[href]")) {
            link.attr("href", link.absUrl("href"));
        }
        for (Element a : doc.select("a[href]")) {
            a.attr("href", "/proxy/page/" + encodeBase64Url(a.absUrl("href")));
        }

        // Add hook script
        addHook(doc, url);

        return new ResponseEntity<>(doc.toString().getBytes(doc.charset()), HttpStatus.OK);
    }


    // ------------ Util Methods ------------ //
    private void addHook(Document doc, URL url) {
        String protocol = url.getProtocol() + "://";
        String host = url.getHost();
        String port = url.getPort() == -1 ? "" : ":" + url.getPort();
        String base = protocol + host + port;
        doc.head().prepend(String.format(HOOK, base, url.getFile()));
    }
    private HttpHeaders getHeaders(HttpServletRequest request, String host) {

        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();

        while(headerNames!=null && headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);

            if(headerName != null && headerValue != null) {
                headers.add(headerName, headerName.toLowerCase().equals("host") ? host : headerValue);
            }
        }

        return headers;
    }

    private String encodeBase64Url(String url) {
        try {
            if (url != null) {
                return new String(encoder.encode(url.getBytes()));
            } else {
                return "";
            }
        } catch (IllegalArgumentException e) {
            throw new DataException("Bad Request, encode base64 url failed, " +
                "url='" + url + "'");
        }
    }
    private String decodeBase64Url(String url64) {
        try {
            return new String(decoder.decode(url64));
        } catch (IllegalArgumentException e) {
            throw new DataException("Bad Request, decode base64 url failed, " +
                "url64='" + url64 + "'");
        }
    }
}