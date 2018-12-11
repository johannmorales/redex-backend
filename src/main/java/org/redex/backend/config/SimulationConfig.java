package org.redex.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "simulation")
public class SimulationConfig {

    private Boolean mismasCapacidadesOficinas;
    private Boolean mismasCapacidadesVuelos;
    private Integer capacidadOficina;
    private Integer capacidadVuelo;

    public Boolean getMismasCapacidadesOficinas() {
        return mismasCapacidadesOficinas;
    }

    public void setMismasCapacidadesOficinas(Boolean mismasCapacidadesOficinas) {
        this.mismasCapacidadesOficinas = mismasCapacidadesOficinas;
    }

    public Boolean getMismasCapacidadesVuelos() {
        return mismasCapacidadesVuelos;
    }

    public void setMismasCapacidadesVuelos(Boolean mismasCapacidadesVuelos) {
        this.mismasCapacidadesVuelos = mismasCapacidadesVuelos;
    }

    public Integer getCapacidadOficina() {
        return capacidadOficina;
    }

    public void setCapacidadOficina(Integer capacidadOficina) {
        this.capacidadOficina = capacidadOficina;
    }

    public Integer getCapacidadVuelo() {
        return capacidadVuelo;
    }

    public void setCapacidadVuelo(Integer capacidadVuelo) {
        this.capacidadVuelo = capacidadVuelo;
    }
}
