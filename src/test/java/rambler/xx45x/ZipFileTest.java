package rambler.xx45x;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;


import java.io.InputStream;

import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.assertj.core.api.Assertions.assertThat;

public class ZipFileTest {

    @Test
    void ZipFiles() throws Exception {
        ZipFile zipFile = new ZipFile("src/test/resources/zipFile.zip");

         ZipEntry xlsxFile = zipFile.getEntry("HW.xlsx");
         try (InputStream is = zipFile.getInputStream(xlsxFile)) {
             XLS parsed = new XLS(is);
             assertThat(parsed.excel
                     .getSheetAt(0)
                     .getRow(1)
                     .getCell(1)
                     .getStringCellValue())
                     .isEqualTo("Ivan");
         }

        ZipEntry pdfFile = zipFile.getEntry("HW.pdf");
        try (InputStream pdfStream = zipFile.getInputStream(pdfFile)) {
            PDF parsed = new PDF(pdfStream);
            assertThat(parsed.text).contains("Пример pdf");

        }
        ZipEntry csvFile = zipFile.getEntry("call-recording-export.csv");
        try (InputStream csvStream = zipFile.getInputStream(csvFile)) {
            CSVReader reader = new CSVReader(new InputStreamReader(csvStream));
            List<String[]> list = reader.readAll();
            assertThat(list)
                    .hasSize(10)
                    .contains(
                            new String[]{"Month", "Months of storage", "Minutes stored", "Amount charged"},
                            new String[]{"2015-01", "1", "10", "0"});
        }
    }
}
