package org.redex.backend.model.envios;

import org.redex.backend.model.general.Persona;
import org.redex.backend.model.rrhh.Oficina;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;
import org.redex.backend.model.AppConstants;
import org.redex.backend.model.auditoria.ModificacionAuditable;

@Entity
@Table(name = "paquete")
public class Paquete extends ModificacionAuditable implements Serializable, Comparable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_persona_origen", nullable = false)
    private Persona personaOrigen;

    @ManyToOne
    @JoinColumn(name = "id_persona_destino", nullable = false)
    private Persona personaDestino;

    @ManyToOne
    @JoinColumn(name = "id_oficina_origen", nullable = false)
    private Oficina oficinaOrigen;

    @ManyToOne
    @JoinColumn(name = "id_oficina_destino", nullable = false)
    private Oficina oficinaDestino;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private PaqueteEstadoEnum estado;

    @Column(nullable = false)
    private LocalDateTime fechaIngreso;

    @Column
    private String descripcion;

    @Column
    private LocalDateTime fechaSalida;

    @Column(nullable = false, unique = true)
    private String codigoRastreo;

    @OrderBy("orden ASC")
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "paquete", fetch = FetchType.LAZY)
    private List<PaqueteRuta> paqueteRutas;

    @Transient
    private Boolean rutaGenerada;

    @Column
    private Boolean notiRegistro;

    @Column
    private Boolean notiAbordados;

    @Column
    private Boolean notiLlegada;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean esIntercontinental() {
        return getOficinaDestino().getPais().getContinente() != getOficinaOrigen().getPais().getContinente();
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

    public Persona getPersonaOrigen() {
        return personaOrigen;
    }

    public void setPersonaOrigen(Persona personaOrigen) {
        this.personaOrigen = personaOrigen;
    }

    public Persona getPersonaDestino() {
        return personaDestino;
    }

    public void setPersonaDestino(Persona personaDestino) {
        this.personaDestino = personaDestino;
    }

    @Deprecated
    public ZonedDateTime getFechaRegistro() {
        Instant i = super.getInstanteRegistro();
        return i.atZone(ZoneId.of(AppConstants.UTC));
    }

    @Deprecated
    public void setFechaRegistro(ZonedDateTime momento) {
        super.setInstanteRegistro(momento.toInstant());
    }

    @Override
    public String toString() {
        return String.format("%s -> %s (%s) [%s]",
                getOficinaOrigen().getCodigo(),
                getOficinaDestino().getCodigo(),
                DateTimeFormatter.ofPattern("dd/MM HH:mm").format(this.getFechaIngreso()),
                esIntercontinental() ? "INTERCONTINENTAL" : "CONTINENTAL");
    }

    public PaqueteEstadoEnum getEstado() {
        return estado;
    }

    public void setEstado(PaqueteEstadoEnum estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDateTime fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public LocalDateTime getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDateTime fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public String getCodigoRastreo() {
        return codigoRastreo;
    }

    public void setCodigoRastreo(String codigoRastreo) {
        this.codigoRastreo = codigoRastreo;
    }

    public String getFechaIngresoString() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").format(getFechaIngreso());
    }

    public List<PaqueteRuta> getPaqueteRutas() {
        return paqueteRutas;
    }

    public void setPaqueteRutas(List<PaqueteRuta> paqueteRutas) {
        this.paqueteRutas = paqueteRutas;
    }

    public Boolean getRutaGenerada() {
        return rutaGenerada;
    }

    public void setRutaGenerada(Boolean rutaGenerada) {
        this.rutaGenerada = rutaGenerada;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getNotiRegistro() {
        return notiRegistro;
    }

    public void setNotiRegistro(Boolean notiRegistros) {
        this.notiRegistro = notiRegistros;
    }

    public Boolean getNotiAbordados() {
        return notiAbordados;
    }

    public void setNotiAbordados(Boolean notiAbordados) {
        this.notiAbordados = notiAbordados;
    }

    public Boolean getNotiLlegada() {
        return notiLlegada;
    }

    public void setNotiLlegada(Boolean notiLlegada) {
        this.notiLlegada = notiLlegada;
    }

    @Override
    public int compareTo(Object o) {
        return Integer.compare(this.hashCode(), o.hashCode());
    }
}
