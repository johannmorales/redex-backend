package org.redex.backend.controller.reportes;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.time.Instant;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.redex.backend.model.AppConstants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ReportesServiceImp implements ReportesService {

    @PersistenceUnit
    private EntityManagerFactory emf;

    @Override
    public String paquetesXvuelo(Long id) {
        Workbook workbook = new XSSFWorkbook();
        CreationHelper createHelper = workbook.getCreationHelper();
        Sheet sheet = workbook.createSheet("Reporte");
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.RED.getIndex());
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        Row headerRow = sheet.createRow(0);

        String[] columns = {"Codigo", "Fecha Ingreso", "Oficina origen", "Oficina destino"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }
        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

        EntityManager em = emf.createEntityManager();
        String q = "select p.codigo_rastreo, p.fecha_ingreso, o.codigo as origen, o2.codigo as destino "
                + " from paquete_ruta pr, paquete p, oficina o, oficina o2  "
                + " where pr.id_vuelo_agendado=" + id + " and "
                + " pr.id_paquete =p.id  and o.id = p.id_oficina_origen and o2.id = p.id_oficina_destino";
        List<Object[]> arr_cust = (List<Object[]>) em.createNativeQuery(q)
                .getResultList();
        Iterator it = arr_cust.iterator();
        int cont = 1;

        while (it.hasNext()) {
            Object[] obj = (Object[]) it.next();
            Row row = sheet.createRow(cont);
            row.createCell(0).setCellValue(obj[0].toString());
            row.createCell(1).setCellValue(obj[1].toString());
            row.createCell(2).setCellValue(obj[2].toString());
            row.createCell(3).setCellValue(obj[3].toString());
            cont++;
        }
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }
        try {
            Random r = new Random();
            String filename = AppConstants.TMP_DIR + "Reporte_paquete_vuelo_" + Instant.now().toString().substring(0, 10) + r.nextInt(100000) + ".xlsx";
            FileOutputStream fileOut = new FileOutputStream(filename);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
            return filename;
        } catch (Exception e) {
            Logger.getLogger(ReportesServiceImp.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @Override
    public String enviosXfechas(String fI, String fF) {
        Workbook workbook = new XSSFWorkbook();
        CreationHelper createHelper = workbook.getCreationHelper();
        Sheet sheet = workbook.createSheet("Reporte");
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.RED.getIndex());
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        Row headerRow = sheet.createRow(0);

        String[] columns = {"Codigo", "Estado", "Fecha Ingreso", "Fecha Llegada",
            "Oficina Origen", "Oficina Destino"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }
        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

        EntityManager em = emf.createEntityManager();
        String q = ""
                + " select p.codigo_rastreo,p.fecha_ingreso, o1.codigo as Inicio, o2.codigo as Fin, va.fecha_fin "
                + " from paquete p "
                + "         inner join oficina o1 on p.id_oficina_origen = o1.id "
                + "         inner join oficina o2 on p.id_oficina_destino = o2.id, "
                + "     vuelo_agendado va, "
                + "     ( select id_paquete, id_vuelo_agendado, max(orden) from paquete_ruta group by id_paquete, id_vuelo_agendado ) aux "
                + " where  "
                + "     p.fecha_ingreso between STR_TO_DATE('" + fI + "','%d-%m-%Y') and STR_TO_DATE('" + fF + "','%d-%m-%Y') and "
                + "     aux.id_paquete = p.id and"
                + "     va.id = aux.id_vuelo_agendado ";

        List<Object[]> arr_cust = (List<Object[]>) em.createNativeQuery(q).getResultList();
        Iterator it = arr_cust.iterator();
        int cont = 1;

        while (it.hasNext()) {
            Object[] obj = (Object[]) it.next();
            Row row = sheet.createRow(cont);
            row.createCell(0).setCellValue(obj[0].toString());
            row.createCell(1).setCellValue(obj[1].toString());
            row.createCell(2).setCellValue(obj[2].toString());
            row.createCell(3).setCellValue(obj[3].toString());
            row.createCell(4).setCellValue(obj[4].toString());
            row.createCell(5).setCellValue(obj[5].toString());
            cont++;
        }

//        for(int i = 0; i < columns.length; i++) {
//            sheet.autoSizeColumn(i);
//        }

        try {
            Random r = new Random();
            String filename = "envio_fecha_" + Instant.now().toString().substring(0, 10) + r.nextInt(100000) + ".xlsx";
            FileOutputStream fileOut = new FileOutputStream(filename);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
            return filename;
        } catch (Exception e) {
            Logger.getLogger(ReportesServiceImp.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @Override
    public String paquetesXusuario(Long id) {
        Workbook workbook = new XSSFWorkbook();
        CreationHelper createHelper = workbook.getCreationHelper();
        Sheet sheet = workbook.createSheet("Reporte");
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.RED.getIndex());
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        Row headerRow = sheet.createRow(0);

        String[] columns = {"Codigo", "Estado", "Fecha Ingreso", "Fecha Llegada",
            "Oficina Origen", "Oficina Destino"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }
        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

        EntityManager em = emf.createEntityManager();
        String q = "select p.codigo_rastreo, p.estado, p.fecha_ingreso, va.fecha_fin,o.codigo as origen, o2.codigo as destino "
                + "from oficina o, paquete p, persona pr, oficina o2, "
                + "(select id_paquete,id_vuelo_agendado, max(orden) from paquete_ruta ) aux,"
                + "vuelo_agendado va "
                + "where p.id_oficina_origen = o.id and p.id_oficina_destino = o2.id and "
                + "aux.id_paquete = p.id and aux.id_vuelo_agendado = va.id and "
                + "p.id_persona_origen = pr.id and pr.numero_documento_identidad='" + id + "'";
        List<Object[]> arr_cust = (List<Object[]>) em.createNativeQuery(q)
                .getResultList();
        Iterator it = arr_cust.iterator();
        int cont = 1;

        while (it.hasNext()) {
            Object[] obj = (Object[]) it.next();
            Row row = sheet.createRow(cont);
            row.createCell(0).setCellValue(obj[0].toString());
            row.createCell(1).setCellValue(obj[1].toString());
            row.createCell(2).setCellValue(obj[2].toString());
            row.createCell(3).setCellValue(obj[3].toString());
            row.createCell(4).setCellValue(obj[4].toString());
            row.createCell(5).setCellValue(obj[5].toString());
            cont++;
        }
        
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        try {
            Random r = new Random();

            String filename = AppConstants.TMP_DIR + "Reporte_paquete_usuario_" + Instant.now().toString().substring(0, 10) + r.nextInt(100000) + ".xlsx";
            FileOutputStream fileOut = new FileOutputStream(filename);
            fileOut.close();
            workbook.close();
            return filename;
        } catch (Exception e) {
            Logger.getLogger(ReportesServiceImp.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @Override
    public String accionesXusuarioXoficinaXfecha() {
        return null;
    }
}
