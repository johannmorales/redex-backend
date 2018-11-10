package org.redex.backend.repository;

import org.redex.backend.model.envios.Paquete;
import org.redex.backend.model.rrhh.Oficina;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

    @Query(""
            + " select paq from Paquete paq "
//            + "       join fetch paq.personaOrigen po "
//            + "       join fetch po.tipoDocumentoIdentidad tdocpo "
//            + "       join fetch paq.personaDestino pd "
//            + "       join fetch pd.tipoDocumentoIdentidad tdocpd "
//            + "       join fetch paq.oficinaOrigen oo "
//            + "       join fetch paq.oficinaDestino od "
//            + "       join fetch oo.pais paiso"
//            + "       join fetch od.pais paisd"
//            + " where"
//            + "     ( "
//            + "         paq.codigoRastreo like %:q% or "
//            + "         concat(po.nombres, ' ', po.materno, ' ', po.paterno) like %:q% or "
//            + "         concat(po.paterno, ' ', po.materno, ' ', po.nombres) like %:q% or "
//            + "         concat(po.nombres, ' ', po.materno, ' ', po.paterno) like %:q% or "
//            + "         concat(tdocpo.simbolo, ' ', po.numeroDocumentoIdentidad) like %:q% or "
//            + "         concat(pd.nombres, ' ', pd.materno, ' ', pd.paterno) like %:q% or "
//            + "         concat(pd.paterno, ' ', pd.materno, ' ', pd.nombres) like %:q% or "
//            + "         concat(pd.nombres, ' ', pd.materno, ' ', pd.paterno) like %:q% or "
//            + "         concat(tdocpd.simbolo, ' ', pd.numeroDocumentoIdentidad) like %:q% "
//            + "     ) "
            + "     order by paq.id desc ")
    Page<Paquete> crimsonList(@Param("q") String q, Pageable pagination);

    @Query(""
            + " select paq from Paquete paq "
//            + "       join fetch paq.personaOrigen po "
//            + "       join fetch po.tipoDocumentoIdentidad tdocpo "
//            + "       join fetch paq.personaDestino pd "
//            + "       join fetch pd.tipoDocumentoIdentidad tdocpd "
//            + "       join fetch paq.oficinaOrigen oo "
//            + "       join fetch paq.oficinaDestino od "
//            + "       join fetch oo.pais paiso"
//            + "       join fetch od.pais paisd"
//            + " where"
//            + "     (oo = :o or od = :o) and "
//            + "     ( "
//            + "         paq.codigoRastreo like %:q% or "
//            + "         concat(po.nombres, ' ', po.materno, ' ', po.paterno) like %:q% or "
//            + "         concat(po.paterno, ' ', po.materno, ' ', po.nombres) like %:q% or "
//            + "         concat(po.nombres, ' ', po.materno, ' ', po.paterno) like %:q% or "
//            + "         concat(tdocpo.simbolo, ' ', po.numeroDocumentoIdentidad) like %:q% or "
//            + "         concat(pd.nombres, ' ', pd.materno, ' ', pd.paterno) like %:q% or "
//            + "         concat(pd.paterno, ' ', pd.materno, ' ', pd.nombres) like %:q% or "
//            + "         concat(pd.nombres, ' ', pd.materno, ' ', pd.paterno) like %:q% or "
//            + "         concat(tdocpd.simbolo, ' ', pd.numeroDocumentoIdentidad) like %:q% "
//            + "     ) "
            + "     order by paq.id desc ")
    Page<Paquete> crimsonListByOficina(@Param("q") String q, @Param("o") Oficina oficina, Pageable pagination);
}
