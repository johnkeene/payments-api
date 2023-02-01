package com.charter.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;


@Repository
public class ApiRepository {
    private static final Logger LOG = LoggerFactory.getLogger(ApiRepository.class);
    List<Transaction> transactions;

    List<Transaction> getTransactions() throws IOException {
        String jsonRecords = IOUtils.toString(new ClassPathResource("transactions.json").getInputStream(), Charset.defaultCharset());
        ObjectMapper mapper = new ObjectMapper();
        transactions = mapper.readValue(jsonRecords, new TypeReference<List<Transaction>>(){});
        return transactions;
    }
}
