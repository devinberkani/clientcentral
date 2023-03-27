package com.devinberkani.clientcentral.util;

import com.devinberkani.clientcentral.dto.ClientDto;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileUtil {

    private final static String TYPE = "text/csv";
    private final static String[] HEADERS = { "First Name", "Last Name", "Address", "Phone Number", "Email", "Birthday" };

    // check if file is a csv
    public static boolean hasCSVFormat(MultipartFile file) throws DateTimeParseException {
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
            throw new RuntimeException(e.getMessage());
        } catch (DateTimeParseException dateTimeParseException) {
            // exception thrown if birthday date is formatted incorrectly in CSV
            throw new DateTimeParseException("Birthday must be in format \"YYYY-MM-DD\": " + dateTimeParseException.getMessage(), dateTimeParseException.getParsedString(), dateTimeParseException.getErrorIndex());
        }
    }

}
