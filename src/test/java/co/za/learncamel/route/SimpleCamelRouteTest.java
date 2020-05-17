package co.za.learncamel.route;

import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.util.FileUtil;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("dev")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
class SimpleCamelRouteTest {
    
    @Autowired
    ProducerTemplate producerTemplate;
    
    @Autowired
    Environment environment;

    @BeforeEach
    void setUp() throws IOException {
        FileUtils.cleanDirectory(new File("data/input"));
        
        FileUtils.deleteDirectory(new File("data/output"));
    }

    @Test
    void testMoveFile() throws InterruptedException {
        
        String message = "type,sku#,itemdescription,price\n" +
                "ADD,100,SAMSUNG TV,500\n" +
                "ADD,101,SAMSUNG PHONE,500";
        
        String fileName = "fileTest.txt";
        
        producerTemplate.sendBodyAndHeader(environment.getProperty("fromRoute"),
                message, Exchange.FILE_NAME, fileName);
        
        Thread.sleep(1000);

        File outputFile = new File("data/output/"+fileName);
        
        assertTrue(outputFile.exists());
        
    }
}