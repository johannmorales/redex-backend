package org.redex.backend.repository;

import org.apache.tomcat.jni.Local;
import org.redex.backend.model.envios.Paquete;
import org.redex.backend.model.rrhh.Oficina;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaquetesRepository extends JpaRepository<Paquete, Long> {

    @Override
    @Query("" +
            " select paq from Paquete paq " +
            "   join fetch paq.personaOrigen po " +
            "   join fetch paq.personaDestino pd " +
            "   join fetch paq.oficinaOrigen oo " +
            "   join fetch paq.oficinaDestino od " +
            "   join fetch oo.pais " +
            "   join fetch od.pais " +
            " order by paq.id desc ")
    List<Paquete> findAll();

    @Query("" +
            " select paq from Paquete paq " +
            "   join fetch paq.oficinaOrigen oo " +
            "   join fetch paq.oficinaDestino od " +
            " where " +
            "   paq.fechaIngreso >= :ini and" +
            "   paq.fechaIngreso <= :fin" +
            " order by paq.id desc ")
    List<Paquete> findAllByRangoInicio(@Param("ini")LocalDateTime inicio, @Param("fin")LocalDateTime fin);

    @Query("" +
            " select paq from Paquete paq " +
            "   join fetch paq.oficinaOrigen oo " +
            "   join fetch paq.oficinaDestino od " +
            "   join fetch paq.personaOrigen " +
            "   join fetch paq.personaDestino " +
            " where " +
            "   paq.codigoRastreo = :cod ")
    Paquete findByCodigoRastreo(@Param("cod") String codigoRastreo);


    @Query(""
            + " select paq from Paquete paq "
            + "       join  paq.personaOrigen po "
            + "       join  po.tipoDocumentoIdentidad tdocpo "
            + "       join  paq.personaDestino pd "
            + "       join  pd.tipoDocumentoIdentidad tdocpd "
            + "       join  paq.oficinaOrigen oo "
            + "       join  paq.oficinaDestino od "
            + "       join  oo.pais paiso"
            + "       join  od.pais paisd"
            + " where"
            + "     ( "
            + "         paq.codigoRastreo like %:q% or "
            + "         concat(po.nombres, ' ', po.materno, ' ', po.paterno) like %:q% or "
            + "         concat(po.paterno, ' ', po.materno, ' ', po.nombres) like %:q% or "
            + "         concat(po.nombres, ' ', po.materno, ' ', po.paterno) like %:q% or "
            + "         concat(tdocpo.simbolo, ' ', po.numeroDocumentoIdentidad) like %:q% or "
            + "         concat(pd.nombres, ' ', pd.materno, ' ', pd.paterno) like %:q% or "
            + "         concat(pd.paterno, ' ', pd.materno, ' ', pd.nombres) like %:q% or "
            + "         concat(pd.nombres, ' ', pd.materno, ' ', pd.paterno) like %:q% or "
            + "         concat(tdocpd.simbolo, ' ', pd.numeroDocumentoIdentidad) like %:q% "
            + "         "
            + "     ) "
            + "     order by paq.id desc ")
    Page<Paquete> crimsonList(@Param("q") String q, Pageable pagination);

    @Query(""
            + " select paq from Paquete paq "
            + "       inner join paq.personaOrigen po "
            + "       inner join po.tipoDocumentoIdentidad tdocpo "
            + "       inner join paq.personaDestino pd "
            + "       inner join pd.tipoDocumentoIdentidad tdocpd "
            + "       inner join paq.oficinaOrigen oo "
            + "       inner join paq.oficinaDestino od "
            + "       inner join oo.pais paiso"
            + "       inner join od.pais paisd"
            + " where"
            + "     (oo = :o or od = :o) and  "
            + "     ( "
            + "         paq.codigoRastreo like %:q% or "
            + "         concat(po.nombres, ' ', po.materno, ' ', po.paterno) like %:q% or "
            + "         concat(po.paterno, ' ', po.materno, ' ', po.nombres) like %:q% or "
            + "         concat(po.nombres, ' ', po.materno, ' ', po.paterno) like %:q% or "
            + "         concat(tdocpo.simbolo, ' ', po.numeroDocumentoIdentidad) like %:q% or "
            + "         concat(pd.nombres, ' ', pd.materno, ' ', pd.paterno) like %:q% or "
            + "         concat(pd.paterno, ' ', pd.materno, ' ', pd.nombres) like %:q% or "
            + "         concat(pd.nombres, ' ', pd.materno, ' ', pd.paterno) like %:q% or "
            + "         concat(tdocpd.simbolo, ' ', pd.numeroDocumentoIdentidad) like %:q% "
            + "     ) "
            + "     order by paq.id desc ")
    Page<Paquete> crimsonListByOficina(@Param("q") String q, @Param("o") Oficina oficina, Pageable pagination);
}
