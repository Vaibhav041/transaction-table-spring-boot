package com.table.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class DataController {

    // class for the parent data
    public static class Data {
        public long id;
        public String sender;
        public String receiver;
        public long totalAmount;

        public Data() {
            // Default constructor (no-argument constructor)
        }

        public Data(long id, String sender, String reciever, long totalAmount) {
            this.id = id;
            this.sender = sender;
            this.receiver = reciever;
            this.totalAmount = totalAmount;
        }
    }
    // child data class
    public static class ChildData {
        public long id;
        public long parentId;
        public long paidAmount;

        public ChildData() {
            // Default constructor (no-argument constructor)
        }

        public ChildData(long id, long parentId, long paidAmount) {
            this.id = id;
            this.parentId = parentId;
            this.paidAmount = paidAmount;

        }

        public long getParentId() {
            return this.parentId;
        }
    }

    // for reading data from the Parent.json file
    private static List<Data> loadDataFromJsonFile(String source) {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<Data>> typeReference = new TypeReference<List<Data>>() {
        };
        InputStream inputStream = TypeReference.class.getResourceAsStream(source);
        try {
            List<Data> bookList = mapper.readValue(inputStream, typeReference);
            System.out.println(bookList);
            return bookList;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Reading data from child.json
    private static List<ChildData> loadChildDataFromJsonFile(String source) {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<ChildData>> typeReference = new TypeReference<List<ChildData>>() {
        };
        InputStream inputStream = TypeReference.class.getResourceAsStream(source);
        try {
            List<ChildData> bookList = mapper.readValue(inputStream, typeReference);
            System.out.println(bookList);
            return bookList;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Route to get Parent data on the client side using pagination
    @RequestMapping("/getdata")
    public static List<Data> parentData(@RequestParam(defaultValue = "1") int page) {
        List<Data> bookList = loadDataFromJsonFile("/json/Parent.json");

        // logic to get startIndex and endIndex to be sent as response.
        int pageSize = 2;
        int totalItems = bookList.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalItems);

        List<Data> pageBooks = bookList.subList(startIndex, endIndex);

        return pageBooks;
    }

    // Using parentId to get all the children data.
    @RequestMapping("/get-parent-data")
    public static List<ChildData> childFun(@RequestParam(defaultValue = "1") long parentId) {
        List<ChildData> childList = loadChildDataFromJsonFile("/json/Child.json");
        List<ChildData> ansList = new ArrayList<ChildData>();
        for (ChildData child : childList) {
            if (child.getParentId() == parentId) {
                ansList.add(child);
            }
        }
        return ansList;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }
}