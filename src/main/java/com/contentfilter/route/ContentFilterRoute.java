package com.contentfilter.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ContentFilterRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        from("direct:filter")
                .routeId("filter-route")
                .choice()
                    .when().jsonpath("$.users[?(@.active == true)]")
                    .setBody().jsonpath("$.users[?(@.active == true)]")
                    .log("Filtered active items successfully")
                    .process(exchange -> {
                        List<?> filteredList = exchange.getIn().getBody(List.class);

                        Map<String, Object> filteredListPayload = new HashMap<>();
                        filteredListPayload.put("users", filteredList);

                        exchange.getIn().setBody(filteredListPayload);
                    })
                .otherwise()
                    .log("No active items found")
                    .setBody().constant(Collections.singletonMap("users", new ArrayList<>()))
                .end();
    }
}
