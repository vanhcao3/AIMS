package isd.aims.main.controller;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        PlaceOrderControllerTest.class
})
public class ValidateDeliveryInfoTestSuite {
    // No implementation needed; JUnit 5 will run the tests automatically
}
