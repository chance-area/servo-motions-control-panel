package ru.chancearea.servomotionscontrolpanel.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.*;

import ru.chancearea.servomotionscontrolpanel.GlobalConstants;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public abstract class ExcelFilesCreator {
    public static void createExelFile(float[][][] _arrPoints, JCheckBox[] _arrCheckBoxes, String[] _checkBoxTexts, float _marginX, float _marginY, int _kofZoom) {
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < _arrCheckBoxes.length; i++) if (_arrCheckBoxes[i].isSelected()) indexes.add(i);

        if (indexes.size() > 0) {
            try (XSSFWorkbook workbook = new XSSFWorkbook()) {
                XSSFSheet sheet = workbook.createSheet("Графики");
                for (int i = 0; i < (indexes.size() + 1); i++) sheet.setColumnWidth(i, 4095);

                XSSFFont font = workbook.createFont();
                font.setFontHeight(14);
                font.setFontName("Times New Roman");

                XSSFFont fontBold = workbook.createFont();
                fontBold.setFontHeight(font.getFontHeight());
                fontBold.setFontName(font.getFontName());
                fontBold.setBold(true);
                fontBold.setColor(IndexedColors.WHITE.index);

                CellStyle cellStyle = workbook.createCellStyle();
                cellStyle.setAlignment(HorizontalAlignment.CENTER);
                cellStyle.setBorderLeft(BorderStyle.MEDIUM);
                cellStyle.setBorderTop(BorderStyle.MEDIUM);
                cellStyle.setBorderRight(BorderStyle.MEDIUM);
                cellStyle.setBorderBottom(BorderStyle.MEDIUM);
                cellStyle.setFont(font);

                CellStyle cellStyleOrange = workbook.createCellStyle();
                cellStyleOrange.cloneStyleFrom(cellStyle);
                cellStyleOrange.setFillForegroundColor(IndexedColors.ORANGE.index);
                cellStyleOrange.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                cellStyleOrange.setFont(fontBold);

                Row headerRow = sheet.createRow(0);

                Cell hCellTime = headerRow.createCell(0, CellType.STRING);
                hCellTime.setCellStyle(cellStyleOrange);
                hCellTime.setCellValue("t, мс");

                for (int i = 0; i < indexes.size(); i++) {
                    Cell hCellValue = headerRow.createCell((i + 1), CellType.STRING);
                    hCellValue.setCellStyle(cellStyleOrange);
                    hCellValue.setCellValue(_checkBoxTexts[ indexes.get(i) ]);
                }

                cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);

                for (int j = 0; j < _arrPoints[0].length; j++) {
                    Row valuesRow = sheet.createRow( (j + 1) );

                    Cell vCellTime = valuesRow.createCell(0, CellType.NUMERIC);
                    vCellTime.setCellStyle(cellStyle);
                    vCellTime.setCellValue(Math.floor(((_arrPoints[0][j][0] - _marginX) / GlobalConstants.FPS_LIMIT) * 1000));

                    for (int i = 0; i < indexes.size(); i++) {
                        Cell vCell = valuesRow.createCell((i + 1), CellType.NUMERIC);
                        vCell.setCellStyle(cellStyle);
                        vCell.setCellValue((_marginY - _arrPoints[ indexes.get(i) ][j][1]) / _kofZoom);
                    }
                }

                for (int i = 0; i < indexes.size(); i++) {
                    XSSFDrawing drawing     = sheet.createDrawingPatriarch();
                    XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 4, 1 + 24 * i, 20, 22 + i * 24);

                    XSSFChart chart = drawing.createChart(anchor);
                    chart.setTitleText("График #" + (i + 1) + " (" + _checkBoxTexts[ indexes.get(i) ] + ")");
                    chart.setTitleOverlay(false);

                    XDDFChartLegend legend = chart.getOrAddLegend();
                    legend.setPosition(LegendPosition.RIGHT);

                    XDDFNumericalDataSource<Double> t = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, _arrPoints[0].length, 0, 0));
                    XDDFNumericalDataSource<Double> v = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, _arrPoints[0].length, (i + 1), (i + 1)));

                    XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
                    bottomAxis.setTitle("t, мс");
                    bottomAxis.setMaximum(_arrPoints[0].length);

                    XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
                    leftAxis.setTitle(_checkBoxTexts[ indexes.get(i) ]);
                    leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);
                    leftAxis.setCrossBetween(AxisCrossBetween.BETWEEN);

                    XDDFChartData data = chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);
                    data.setVaryColors(false);

                    XDDFLineChartData.Series series = (XDDFLineChartData.Series) data.addSeries(t, v);
                    series.setTitle(_checkBoxTexts[ indexes.get(i) ], null);
                    series.setSmooth(false);
                    series.setMarkerStyle(MarkerStyle.NONE);
                    chart.plot(data);
                }

                // Saving xlsx file
                File folder = new File("./" + GlobalConstants.SAVE_EXCEL_FILES_FOLDER_NAME);
                boolean isFolderExists = folder.exists();

                if (!isFolderExists && !folder.isFile()) isFolderExists = folder.mkdir();
                if (isFolderExists && folder.canWrite()) {
                    try (FileOutputStream outputStream = new FileOutputStream(folder.getPath() + "/" + (Objects.requireNonNull(folder.list()).length + 1) + "_graph_" + (new SimpleDateFormat("dd-MM-yyyy")).format(new Date()) + ".xlsx")) {
                        workbook.write(outputStream);
                        workbook.close();

                        if (GlobalConstants.IS_DEBUG_MODE) System.out.println("Файл создан успешно!");
                    } catch (IOException e) {
                        //e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
    }
}
