package com.contentfilter.route;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;


@Component
public class ContentFilterRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        onException(Exception.class)
                .handled(true)
                .process(exchange -> {
                    Exception ex = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);

                    Map<String, Object> errorBody = new HashMap<>();
                    errorBody.put("timestamp", ZonedDateTime.now().toString());
                    errorBody.put("message", ex.getMessage());
                    errorBody.put("status", 500);
                    errorBody.put("error", "Internal Server Error");

                    exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 500);
                    exchange.getMessage().setBody(errorBody);
                })
                .marshal().json(JsonLibrary.Jackson);

        restConfiguration()
                .component("netty-http")
                .host("{{server.host}}")
                .port("{{server.port}}");

        rest("/filter")
                .post("/")
                .consumes("application/json")
                .produces("application/json")
                .to("direct:filter");

        from("direct:filter")
                .routeId("filter-route-xslt")
                .unmarshal().json(JsonLibrary.Jackson)
                .marshal().jacksonXml(true)
                .to("xslt:xslt/filter-active.xsl")
                .log("Filtered active items successfully")
                .unmarshal().json(JsonLibrary.Jackson)
                .marshal().json(JsonLibrary.Jackson);
    }
}
