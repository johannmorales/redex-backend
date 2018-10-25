package org.redex.backend.model.envios;

import org.redex.backend.model.rrhh.Oficina;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.redex.backend.model.general.EstadoEnum;

@Entity
@Table(name = "vuelo")
public class Vuelo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_plan_vuelo", nullable = false)
    private PlanVuelo planVuelo;

    @ManyToOne
    @JoinColumn(name = "id_oficina_origen", nullable = false)
    private Oficina oficinaOrigen;

    @ManyToOne
    @JoinColumn(name = "id_oficina_destino", nullable = false)
    private Oficina oficinaDestino;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoEnum estado;

    @Column(nullable = false)
    private LocalTime horaInicio;

    @Column(nullable = false)
    private LocalTime horaFin;

    @Column(nullable = false)
    private Integer capacidad;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Oficina getOficinaOrigen() {
        return oficinaOrigen;
    }

    public void setOficinaOrigen(Oficina oficinaOrigen) {
        this.oficinaOrigen = oficinaOrigen;
    }

    public Oficina getOficinaDestino() {
        return oficinaDestino;
    }

    public void setOficinaDestino(Oficina oficinaDestino) {
        this.oficinaDestino = oficinaDestino;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public PlanVuelo getPlanVuelo() {
        return planVuelo;
    }

    public void setPlanVuelo(PlanVuelo planVuelo) {
        this.planVuelo = planVuelo;
    }

    public boolean esDeUnDia() {
        return getHoraInicio().isBefore(getHoraFin());
    }

    public String getDuracion() {
        Duration d;
        if (horaFin.isAfter(horaInicio)) {
            d = Duration.between(horaInicio, horaFin);
        } else {
            d = Duration.between(horaInicio, LocalTime.MAX).plus(Duration.between(LocalTime.MIDNIGHT, horaFin));
        }

        return String.format("%02dh %02dm", d.getSeconds() / 3600, (d.getSeconds() % 3600) / 60);
    }

    public String getHoraInicioString() {
        return DateTimeFormatter.ofPattern("HH:mm").format(getHoraInicio());
    }

    public String getHoraFinString() {
        return DateTimeFormatter.ofPattern("HH:mm").format(getHoraFin());
    }

    public String getCodigo() {
        return String.format("%s-%s-%s-%s",
                getOficinaOrigen().getCodigo(),
                getOficinaDestino().getCodigo(),
                DateTimeFormatter.ofPattern("HH:mm").format(getHoraInicio()),
                DateTimeFormatter.ofPattern("HH:mm").format(getHoraFin())
        );
    }

    public EstadoEnum getEstado() {
        return estado;
    }

    public void setEstado(EstadoEnum estado) {
        this.estado = estado;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }
}
