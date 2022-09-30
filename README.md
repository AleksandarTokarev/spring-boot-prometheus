```
https://docs.spring.io/spring-boot/docs/2.1.2.RELEASE/reference/html/production-ready-endpoints.html  
https://zerokspot.com/weblog/2016/05/26/selective-metric-collection-with-prometheus/ (best article)  
https://www.robustperception.io/dropping-metrics-at-scrape-time-with-prometheus/  
https://prometheus.io/docs/prometheus/latest/configuration/configuration/#relabel_config  
https://stackoverflow.com/questions/65273879/how-to-ignore-or-include-specific-metrics-in-prometheus  
https://stackoverflow.com/questions/59070150/monitor-only-one-namespace-metrics-prometheus-with-kubernetes  
https://stackoverflow.com/questions/66350980/prometheus-config-to-ignore-scraping-of-metrics-for-a-specific-namespace-in-kube  
https://twitter.com/ATokarev90/status/1575873976936472577  
https://stackoverflow.com/questions/48603285/prometheus-endpoint-not-working-spring-boot-2-0-0-rc1-spring-webflux-enabled    
https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#_per_meter_properties  
https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#actuator.metrics  
https://stackoverflow.com/questions/54422023/how-to-specify-a-whitelist-of-the-metrics-i-want-to-use-in-spring-boot-with-micr  
https://spring.io/blog/2018/03/16/micrometer-spring-boot-2-s-new-application-metrics-collector 
https://www.tutorialworks.com/spring-boot-prometheus-micrometer/
``` 
Example config for `prometheus.yml`
```
scrape_configs:
  - job_name: "myservice"
    target_groups:
      - targets:
          - "localhost:9999"
    metric_relabel_configs:
      - source_labels: [__name__]
        regex: go_(.*)
        action: drop
      - source_labels: [__name__]
        regex: http_(.*)
        action: drop
```

docker-compose up  
docker-compose down  
http://localhost:8080/doit  
http://localhost:8080/actuator/prometheus  
http://localhost:9090/  

# -------------------------
For Spring based apps, there are 2 ways to disable/enable metrics:  

 Via Properties (where you disable all, and then enable a specific prefix - like jvm, http etc)
```
management.endpoints.web.exposure.include=prometheus,health

management.metrics.enable.all=false
management.metrics.enable.application=true
management.metrics.enable.jvm=true
management.metrics.enable.http=true
management.metrics.enable.disk=true
```
 Via Custom Config
```
@Configuration
public class CustomMetricsConfig {

    @Bean
    public MeterRegistryCustomizer<MeterRegistry> metricsToExpose() {
        return registry -> registry
                .config()
                .meterFilter(MeterFilter.denyUnless(this::isMetricToInclude));
    }

    private boolean isMetricToInclude(Meter.Id id) {
        return id.getName().startsWith("app") || id.getName().startsWith("http");
    }
}
```