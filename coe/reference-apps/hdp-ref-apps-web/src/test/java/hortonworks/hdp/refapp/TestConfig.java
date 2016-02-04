package hortonworks.hdp.refapp;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages={"hortonworks.hdp.refapp.storm", "hortonworks.hdp.refapp.registry", "hortonworks.hdp.refapp.falcon", "hortonworks.hdp.refapp.config.app", "hortonworks.hdp.refapp.hbase" })
public class TestConfig {

}
