package org.redex.backend.dao.dashboard.hibernate;

import org.redex.backend.dao.dashboard.PaquetesVueloDAO;
import org.redex.backend.model.dashboard.PaquetesVuelo;
import org.redex.backend.model.envios.VueloAgendado;
import org.redex.backend.model.envios.VueloAgendadoEstadoEnum;
import org.redex.backend.model.general.EstadoEnum;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import javax.persistence.PersistenceContext;

@Repository
public class PaquetesVueloDAOH implements PaquetesVueloDAO {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<PaquetesVuelo> top(LocalDate inicio, LocalDate fin, Integer cantidad) {

        Query sql = em.createQuery(" "
                + "   select new org.redex.backend.model.dashboard.PaquetesVuelo (v, sum(va.capacidadActual)) "
                + "   from VueloAgendado va "
                + "       inner join va.vuelo v"
                + "       inner join v.planVuelo pv"
                + "   where"
                + "       v.estado = :ESTADO_ACTIVO and "
                + "       pv.estado = :ESTADO_ACTIVO and "
                + "       va.fechaInicio > :INICIO and "
                + "       va.fechaFin < :FIN "
                + "   group by v "
                + "   order by sum(va.capacidadActual) desc ");

        sql.setParameter("ESTADO_ACTIVO", EstadoEnum.ACTIVO);
        sql.setParameter("INICIO", LocalDateTime.of(inicio, LocalTime.MIN));
        sql.setParameter("FIN",  LocalDateTime.of(fin, LocalTime.MAX));

        sql.setMaxResults(cantidad);



        return sql.getResultList();
    }

    @Override
    public List<PaquetesVuelo> bottom(LocalDate inicio, LocalDate fin, Integer cantidad) {
        Query sql = em.createQuery(" "
                + "   select new org.redex.backend.model.dashboard.PaquetesVuelo (v, sum(va.capacidadActual)) "
                + "   from VueloAgendado va "
                + "       inner join va.vuelo v"
                + "       inner join v.planVuelo pv"
                + "   where"
                + "       v.estado = :ESTADO_ACTIVO and "
                + "       pv.estado = :ESTADO_ACTIVO and "
                + "       va.fechaInicio > :INICIO and "
                + "       va.fechaFin < :FIN "
                + "   group by v "
                + "   order by sum(va.capacidadActual) asc ");

        sql.setParameter("ESTADO_ACTIVO", EstadoEnum.ACTIVO);
        sql.setParameter("INICIO", LocalDateTime.of(inicio, LocalTime.MIN));
        sql.setParameter("FIN",  LocalDateTime.of(fin, LocalTime.MAX));

        sql.setMaxResults(cantidad);

        return sql.getResultList();
    }

}
