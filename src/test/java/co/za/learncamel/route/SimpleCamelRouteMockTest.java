package co.za.learncamel.route;

import co.za.learncamel.LearnCamelApplication;
import org.apache.camel.*;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("mock")
@CamelSpringBootTest
@SpringBootTest(classes = LearnCamelApplication.class)
@ContextConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class SimpleCamelRouteMockTest {

    @Autowired
    CamelContext camelContext;
    
    @Autowired
    Environment environment;

    @EndpointInject("mock:{{fromRoute}}")
    protected MockEndpoint fromRoute;
    
    @EndpointInject("{{toRoute1}}")
    protected MockEndpoint toRoute;

    @Autowired
    ProducerTemplate producerTemplate;
    
    
    @Test
    public void testMoveFileMock() throws InterruptedException {
        assertEquals(ServiceStatus.Started, camelContext.getStatus());

        String message = "type,sku#,itemdescription,price\n" +
                "ADD,100,SAMSUNG TV,500\n" +
                "ADD,101,SAMSUNG PHONE,500";

        toRoute.expectedBodiesReceived(message);
        toRoute.expectedMessageCount(1);

        producerTemplate.sendBodyAndHeader(environment.getProperty("startRoute"), 
                message, "env", "mock");
        
        toRoute.assertIsSatisfied();

    }
}