package com.neovia.gidr.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.neovia.gidr.model.Incident;


public class ReadExcel {
	
    private static final Logger LOGGER = LogManager.getLogger(ReadExcel.class);

	public ReadExcel() {
	}

	public List<Incident> getIncidentList(String pathExcel) {

		List<Incident> lista = new ArrayList<>();

		FileInputStream file = null;
		try {
			file = new FileInputStream(new File(pathExcel));
		} catch (FileNotFoundException e) {
			LOGGER.error("The system can not find the file in the specified path: " + pathExcel);
		}

		XSSFWorkbook workbook = null;
		try {
			workbook = new XSSFWorkbook(file);
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}

		XSSFSheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();
		Row row;

		// RECORREMOS TODAS LAS FILAS PARA MOSTRAR EL CONTENIDO DE CADA CELDA
		// SALTAMOS LA CABECERA
		row = rowIterator.next();
		boolean validatedRow;
		Incident incidence = null;

		while (rowIterator.hasNext()) {

			incidence = new Incident();
			row = rowIterator.next();
			validatedRow = true;
			// OBTENEMOS EL ITERATOR QUE PERMITE RECORRES TODAS LAS CELDAS DE UNA FILA
			Iterator<Cell> cellIterator = row.cellIterator();
			Cell celda;

			while (cellIterator.hasNext()) {

				celda = cellIterator.next();

				int cellNumber = celda.getColumnIndex();

				// SI EL NUMERO DE CELDA ES EL DEL RMA LO GUARDAMOS
				if (cellNumber == 0) {

					if (celda.getCellType() == Cell.CELL_TYPE_STRING) {
						incidence.setRma(celda.getStringCellValue());
					} else {
						LOGGER.error("Value of column " + celda.getColumnIndex() + " in row " + row.getRowNum()
						+ " is not text type");
						validatedRow = false;
					}
				}
				// SI EL NUMERO DE CELDA ES ES EL PART NUMBER LO GUARDAMOS
				if (cellNumber == 1) {

					if (celda.getCellType() == Cell.CELL_TYPE_STRING) {
						incidence.setPartNumber(celda.getStringCellValue());
					} else {
						LOGGER.error("Value of column " + celda.getColumnIndex() + " in row " + row.getRowNum()
						+ " is not text type");
						validatedRow = false;
					}
				}
				// SI EL NUMERO DE CELDA ES EL ORIGEN LO GUARDAMOS
				if (cellNumber == 5) {

					if (celda.getCellType() == Cell.CELL_TYPE_STRING) {
						incidence.setPartNumber(celda.getStringCellValue());
					} else {
						LOGGER.error("Value of column " + celda.getColumnIndex() + " in row " + row.getRowNum()
						+ " is not text type");
						validatedRow = false;
					}
				}

			}
			if (validatedRow) {
				lista.add(incidence);
			}
		}
		return lista;
	}

}
