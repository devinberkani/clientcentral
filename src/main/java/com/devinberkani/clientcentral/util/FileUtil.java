package com.devinberkani.clientcentral.util;

import com.devinberkani.clientcentral.dto.ClientDto;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileUtil {

    public static String TYPE = "text/csv";
    static String[] HEADERS = { "First Name", "Last Name", "Address", "Phone Number", "Email", "Birthday" };

    public static boolean hasCSVFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType())
                || Objects.equals(file.getContentType(), "application/vnd.ms-excel");
    }

    public static List<ClientDto> csvToClientDto(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.builder().setHeader(HEADERS).setSkipHeaderRecord(true).setIgnoreHeaderCase(true).setTrim(true).build())) {

            List<ClientDto> clientDtoList = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                ClientDto client = new ClientDto();
                client.setFirstName(csvRecord.get("First Name"));
                client.setLastName(csvRecord.get("Last Name"));
                client.setAddress(csvRecord.get("Address"));
                client.setPhoneNumber(csvRecord.get("Phone Number"));
                client.setEmail(csvRecord.get("Email"));
                client.setBirthday(LocalDate.parse(csvRecord.get("Birthday")));
                clientDtoList.add(client);
            }
            return clientDtoList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

}
