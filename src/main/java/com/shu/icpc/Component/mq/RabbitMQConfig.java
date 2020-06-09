package com.shu.icpc.Component.mq;

import org.springframework.stereotype.Component;

@Component
public class RabbitMQConfig {
    public final String exchange = "shu_icpc";
    public final String routeKey = "mail";
    public final String queeu = "shu";

    public String getExchange() {
        return exchange;
    }

    public String getRouteKey() {
        return routeKey;
    }

    public String getQueeu() {
        return queeu;
    }
}
