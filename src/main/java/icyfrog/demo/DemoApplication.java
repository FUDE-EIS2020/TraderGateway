package icyfrog.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;


@RestController
@EnableConfigurationProperties(UriConfiguration.class)
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder, UriConfiguration uriConfiguration)  {
        String httpUri = uriConfiguration.getHttpbin();
        String fudeUserServer = uriConfiguration.getFudeUserServer();
        System.out.println("fude user: " + fudeUserServer);
        return builder.routes()
                // TODO:按照这个格式写就可以了，.path的路径要和.uri的一样，就会从gateway跳转到orderProcess
                .route(p -> p
                        .path("/test/getAllProducts")
                        //.filters(f -> f.addRequestHeader("Router","Gateway"))
                        .uri( httpUri +"/getAllProducts"))
                .route(p -> p
                        .path("/createOrder")
                        .uri( httpUri + "/createOrder"))
                .route(p -> p
                        .path("/traderOrderHistory")
                        .uri( httpUri + "/traderOrderHistory"))
                .route(p -> p
                        .path("/brokerOrderHistory")
                        .uri( httpUri + "/brokerOrderHistory"))
                .route(p -> p
                        .path("/test/redisGet")
                        //.filters(f -> f.addRequestHeader("Router","Gateway"))
                        .uri( httpUri +"/test/redisGet"))
                .route(p -> p
                        .path("/test/redisSet")
                        //.filters(f -> f.addRequestHeader("Router","Gateway"))
                        .uri( httpUri +"/test/redisSet"))
                .route(p -> p
                        .path("/auth/trader/login")
                        .uri( fudeUserServer + "/auth/trader/login"))
                .route(p -> p
                        .path("/auth/trader/register")
                        .uri( fudeUserServer + "/auth/trader/register"))
                // TODO: WebSocket need to be done!
                .route("websocket_route", r -> r.path("/webSocket")
                        .uri("ws://127.0.0.1:6605"))
                .build();
    }

}

@ConfigurationProperties
class UriConfiguration {

    // TODO: 后端地址有变的时候更改这里就可以
    private String httpbin = "http://localhost:8089";

    @Value("${fudeUserServer}")
    private String fudeUserServer;

    public String getHttpbin() {
        return httpbin;
    }

    public void setHttpbin(String httpbin) {
        this.httpbin = httpbin;
    }

    public String getFudeUserServer() {
        return fudeUserServer;
    }

    public void setFudeUserServer(String fudeUserServer) {
        this.fudeUserServer = fudeUserServer;
    }
}
