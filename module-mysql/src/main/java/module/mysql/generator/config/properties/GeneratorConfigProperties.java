package module.mysql.generator.config.properties;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lubin
 * @create 2020.12.31
 */
@ConfigurationProperties(prefix = "generator")
public class GeneratorConfigProperties implements InitializingBean {

    private final String DATA_CENTER_ID = "DATA_CENTER_ID";

    private final String MACHINE_ID = "MACHINE_ID";

    private Integer dataCenterId;

    private Integer machineId;

    public GeneratorConfigProperties() {
        this.dataCenterId = 1;
        this.machineId = 0;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String envDataCenterId = System.getenv(DATA_CENTER_ID);
        String envMachineId = System.getenv(MACHINE_ID);
        if (envDataCenterId != null && envMachineId != null) {
            dataCenterId = Integer.valueOf(envDataCenterId);
            machineId = Integer.valueOf(envMachineId);
        }
    }

    public Integer getDataCenterId() {
        return dataCenterId;
    }

    public void setDataCenterId(Integer dataCenterId) {
        this.dataCenterId = dataCenterId;
    }

    public Integer getMachineId() {
        return machineId;
    }

    public void setMachineId(Integer machineId) {
        this.machineId = machineId;
    }
}
