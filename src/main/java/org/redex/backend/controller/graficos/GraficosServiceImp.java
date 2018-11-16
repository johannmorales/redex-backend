/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.redex.backend.controller.graficos;

import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Oscar
 */
@Service
@Transactional(readOnly = true)
public class GraficosServiceImp implements GraficosService{
    
    @PersistenceUnit
    private EntityManagerFactory emf;
    
    
    @Override
    public void paquetesXoficinaXfecha_linea(int id,String fechaI,String fechaF){
        EntityManager em = emf.createEntityManager();
        String q = "select count(*),date(instante_registro) from paquete " +
            "where id_oficina_origen= "+ id +"and instante_registro between '"+fechaI+"' and '"+fechaF+"' " +
            "group by date(instante_registro);";
        List<Object[]> arr_cust = (List<Object[]>)em.createQuery(q)
                              .getResultList();
        Iterator it = arr_cust.iterator();
        int cont = 1;
        
        while (it.hasNext()) {
        }
    }
    
    @Override
    public void paquetesXvuelosXfecha(String fechaI,String fechaF){
        EntityManager em = emf.createEntityManager();
        String q = "select count(*), id_oficina_origen from paquete " +
            "where  instante_registro between '"+fechaI+"' and '"+fechaF+"'" +
            "group by id_oficina_origen ;";
        List<Object[]> arr_cust = (List<Object[]>)em.createQuery(q)
                              .getResultList();
        Iterator it = arr_cust.iterator();
        int cont = 1;
        
        while (it.hasNext()) {
        }
    }
    
    @Override
    public void paquetesXoficinasXfecha_barra(String fechaI,String fechaF){
        EntityManager em = emf.createEntityManager();
        String q = "select sum(va.capacidad_actual) Suma,o1.codigo Inicio,o2.codigo Fin,time(v.hora_inicio) as hora  " +
            "from vuelo_agendado va, vuelo v, plan_vuelo pv,oficina o1, " +
            "oficina o2 " +
            "where va.id_vuelo = v.id and pv.estado = 'ACTIVO' and v.id_plan_vuelo=pv.id " +
            "and v.id_oficina_origen = o1.id and v.id_oficina_destino = o2.id and va.fecha_fin  between '"+fechaI+"' and '"+fechaF+"'" +
            "group by va.id_vuelo " +
            "order by sum(va.capacidad_actual) desc limit 10;";
        List<Object[]> arr_cust = (List<Object[]>)em.createQuery(q)
                              .getResultList();
        Iterator it = arr_cust.iterator();
        int cont = 1;
        
        while (it.hasNext()) {
        }
    }
    
}
