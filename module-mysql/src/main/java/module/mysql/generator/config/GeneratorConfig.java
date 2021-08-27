package module.mysql.generator.config;


import module.mysql.generator.SnowflakeGenerator;
import module.mysql.generator.config.properties.GeneratorConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lubin
 * @create 2019.12.04
 */
@Configuration
@EnableConfigurationProperties({GeneratorConfigProperties.class})
public class GeneratorConfig {

    @Autowired
    private GeneratorConfigProperties generatorConfigProperties;

    @Bean(name = "snowflakeGenerator")
    public SnowflakeGenerator snowflakeGenerator() {
        return new SnowflakeGenerator(generatorConfigProperties.getDataCenterId().longValue(), generatorConfigProperties.getMachineId().longValue());
    }

}
