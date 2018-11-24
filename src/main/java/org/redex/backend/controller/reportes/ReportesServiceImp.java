package org.redex.backend.controller.reportes;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

import org.redex.backend.model.AppConstants;
import org.redex.backend.model.envios.Paquete;
import org.redex.backend.repository.PaquetesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.albatross.zelpers.file.excel.ExcelHelper;

@Service
@Transactional(readOnly = true)
public class ReportesServiceImp implements ReportesService {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    PaquetesRepository paquetesRepository;

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
    public String enviosXfechas(LocalDate inicio, LocalDate fin) {
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

        String[] columns = {
                "Codigo",
                "Fecha Ingreso",
                "Oficina Origen",
                "Oficina Destino"
        };

        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

        CellStyle dateCellStyle = workbook.createCellStyle();

        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

        List<Paquete> paquetes = paquetesRepository.findAllByRangoInicio(inicio.atStartOfDay(), fin.atTime(LocalTime.MAX));

        int cont = 1;

        for (Paquete paquete : paquetes) {
            Row row = sheet.createRow(cont++);
            row.createCell(0).setCellValue(paquete.getCodigoRastreo());
            row.createCell(1).setCellValue(paquete.getFechaIngresoString());
            row.createCell(2).setCellValue(paquete.getOficinaOrigen().getCodigo());
            row.createCell(3).setCellValue(paquete.getOficinaDestino().getCodigo());
        }

        try {
            Random r = new Random();
            String filename = "envio_fecha_" + Instant.now().toString().substring(0, 10) + r.nextInt(100000) + ".xlsx";
            FileOutputStream fileOut = new FileOutputStream(filename);
            workbook.write(fileOut);
            workbook.close();
            fileOut.close();
            return filename;
        } catch (Exception e) {
            Logger.getLogger(ReportesServiceImp.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }


    //  El sistema debera poder generar un reporte de los envíos seleccionando un
//  rango de fecha. El reporte tendrá los siguientes datos: El rango de fechas
//  que se a defido para generar le reporte, el codigo del paquete, la fecha de
//  llegada y el estado
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

        String q = "" +
                " select p.codigo_rastreo, p.estado, p.fecha_ingreso, o.codigo as origen, o2.codigo as destino " +
                " from paquete p " +
                "   inner join oficina o on p.id_oficina_origen = o.id " +
                "   inner join oficina o2 on p.id_oficina_destino = o2.id " +
                " where " +
                "   p.id_user_registro = :ID_USUARIO ";

        List<Object[]> arr_cust = (List<Object[]>) em.createNativeQuery(q).setParameter("ID_USUARIO", id)
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
            //row.createCell(5).setCellValue(obj[5].toString());
            cont++;
        }

        for (int i = 0; i < columns.length; i++) {
//            sheet.autoSizeColumn(i);
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

    @Override
    public String enviosXoficina(LocalDate inicio, LocalDate fin) {
        return null;
    }

    @Override
    public String enviosFinalizados(LocalDate inicio, LocalDate fin) {
        return null;
    }
}
