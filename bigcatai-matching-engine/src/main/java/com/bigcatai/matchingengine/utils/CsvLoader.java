package com.bigcatai.matchingengine.utils;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;

public class CsvLoader {

    public List<String[]> readAll(String filePath) throws Exception {
        
        return this.readAll(new FileReader(filePath));
    }
    
    public List<String[]> readAll(File file) throws Exception {
        
        return this.readAll(new FileReader(file));
    }
    
    public List<String[]> readAll(Reader reader) throws Exception {
        
        List<String[]> list = new ArrayList<String[]>();
        CSVReader csvReader = new CSVReader(reader);
        String[] line;
        while ((line = csvReader.readNext()) != null) {
            list.add(line);
        }
        reader.close();
        csvReader.close();
        return list;
    }
    
}
