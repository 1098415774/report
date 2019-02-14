package com.sh.base.xls;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class CreateXlsFile {

    private HSSFWorkbook workbook;

    private File file;

    private HSSFCellStyle titlestyle;

    private HSSFCellStyle datastyle;

    private String[] title;

    private List<String[]> data;

    private Class paramclass;

    public CreateXlsFile(File file){
        this(file,null);
    }

    public CreateXlsFile(File file,Class paramclass){
        this.file = file;
        this.paramclass = paramclass;
        initialize();
    }

    private void initialize(){

        workbook = new HSSFWorkbook();
        HSSFFont titlefont = workbook.createFont();
        titlefont.setFontHeightInPoints((short) 10);
        titlefont.setFontName("Times New Roman");
        titlefont.setBold(true);

        HSSFFont datafont = workbook.createFont();
        datafont.setFontHeightInPoints((short) 10);
        datafont.setFontName("黑体");

        titlestyle = workbook.createCellStyle();
        titlestyle.setFont(titlefont);
        titlestyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);

        datastyle = workbook.createCellStyle();
        datastyle.setFont(datafont);
        datastyle.setAlignment(HorizontalAlignment.LEFT);
    }

    public void setTitle(String[] title){
        this.title = title;
    }

    public void setData(List<String[]> data){
        this.data = data;
    }

    public File write()throws Exception{
        HSSFSheet sheet = workbook.createSheet();
        int rowsize = 0;
        int istitle = 0;
        if (title != null){
            istitle = 1;
        }
        rowsize = data.size();
        if (title != null){
            HSSFRow row = sheet.createRow(0);
            row.setHeightInPoints(20);
            for (int cellindex = 0; cellindex < title.length; cellindex++){
                HSSFCell cell = row.createCell(cellindex);
                cell.setCellStyle(titlestyle);
                cell.setCellValue(title[cellindex]);

            }
        }
        for (int index = 0; index < rowsize; index++){
            HSSFRow row = sheet.createRow(index + istitle);
            String[] datas = data.get(index);
            for (int cellindex = 0; cellindex < datas.length; cellindex++){
                HSSFCell cell = row.createCell(cellindex);
                cell.setCellStyle(datastyle);
                cell.setCellValue(datas[cellindex]);
            }
        }

        if (title != null){
            for (int index = 0; index < title.length; index++){
                sheet.autoSizeColumn(index);
            }
        }else {
            if (data != null && data.size() > 0){
                for (int index = 0; index < data.get(0).length; index++){
                    sheet.autoSizeColumn(index);
                }
            }
        }

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        workbook.write(fileOutputStream);
        fileOutputStream.close();
        return file;
    }


}
