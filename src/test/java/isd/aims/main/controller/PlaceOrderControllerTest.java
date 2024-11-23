package isd.aims.main.controller;

import org.junit.jupiter.api.Test;

import isd.aims.main.controller.PlaceOrderController;
import isd.aims.main.entity.media.Media;
import isd.aims.main.entity.order.Order;
import isd.aims.main.entity.order.OrderMedia;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

public class PlaceOrderControllerTest {

    private final PlaceOrderController controller = new PlaceOrderController();

    @Test
    public void testValidatePhoneNumber() {
        // Valid Equivalent Class
        assertTrue(controller.validatePhoneNumber("0123456789"), "Valid number with spaces as separators");
        assertTrue(controller.validatePhoneNumber("012-345-6789"), "Valid number with dashes as separators");
        assertTrue(controller.validatePhoneNumber("012.345.6789"), "Valid number with dots as separators");
        assertTrue(controller.validatePhoneNumber("012 345-6789"), "Valid number with mixed separators (space + dash)");
        assertTrue(controller.validatePhoneNumber("012-345.6789"), "Valid number with mixed separators (dash + dot)");
        // Invalid Equivalent Class
        assertFalse(controller.validatePhoneNumber("012--3456789"), "Consecutive separators (--)");
        assertFalse(controller.validatePhoneNumber("01234567890"), "Contains more than 10 digits");
        assertFalse(controller.validatePhoneNumber("1234567890"), "Does not start with 0");
        assertFalse(controller.validatePhoneNumber("0123-456789-"), "Ends with a separator (-)");
        assertFalse(controller.validatePhoneNumber("-0123456789"), "Starts with a separator (-)");
        assertFalse(controller.validatePhoneNumber("012a3456789"), "Contains non-numeric characters (a)");
        assertFalse(controller.validatePhoneNumber("012345"), "Less than 10 digits");
    }

    @Test
    public void testValidateName() {
        // Valid Equivalent Class
        assertTrue(controller.validateName("Nguyễn Văn An"), "Valid name");
        assertTrue(controller.validateName("Nguyễn Thị Minh An"), "Valid name with multiple words");
        assertTrue(controller.validateName("Nguyễn Văn-A"), "Valid name with hyphen");
        assertTrue(controller.validateName("Nguyễn Văn An"), "Valid name with spaces");
        // Invalid Equivalent Class
        assertFalse(controller.validateName(""), "Empty name");
        assertFalse(controller.validateName(" "), "Name with only spaces");
        assertFalse(controller.validateName("Nguyễn Văn An123"), "Name with digits");
        assertFalse(controller.validateName("Nguyễn @Văn"), "Name with invalid character '@'");
        assertFalse(controller.validateName("Nguyễn--Văn"), "Name with consecutive hyphens");
        assertFalse(controller.validateName("Nguyễn "), "Name with trailing space");
        assertFalse(controller.validateName(" Nguyễn"), "Name with leading space");
        assertFalse(controller.validateName("Nguyễn  Văn"), "Name with consecutive spaces");
    }

    @Test
    void testValidateAddress() {
        PlaceOrderController controller = new PlaceOrderController();
        // Valid Equivalent Class
        assertTrue(controller.validateAddress("123 Nguyen Trai Street"), "Valid address without special characters");
        assertTrue(controller.validateAddress("45/9 Le Loi, District 1"), "Valid address with slashes and comma");
        assertTrue(controller.validateAddress("Phu Nhuan District, Ho Chi Minh City"),
                "Valid address with spaces and commas");
        assertTrue(controller.validateAddress("12-A, Vo Van Kiet Street"), "Valid address with hyphens and numbers");
        assertTrue(controller.validateAddress("Apartment 2A, 456 Hai Ba Trung"),
                "Valid address with numbers and English text");
        // Invalid Equivalent Class
        assertFalse(controller.validateAddress(" 123 Nguyen Trai"), "Address with leading space");
        assertFalse(controller.validateAddress("123 Nguyen Trai "), "Address with trailing space");
        assertFalse(controller.validateAddress("123  Nguyen Trai"), "Address with consecutive spaces");
        assertFalse(controller.validateAddress("123,, Nguyen Trai"), "Address with consecutive commas");
        assertFalse(controller.validateAddress(""), "Empty address");
        assertFalse(controller.validateAddress("!!!"), "Address with only punctuation");
        assertFalse(controller.validateAddress("123 Nguyen, Trai----"), "Address with consecutive hyphens");
        assertFalse(controller.validateAddress("Hai"), "Too short address");
        assertFalse(controller.validateAddress("123"), "Too short address");
    }

    @Test
    void testValidDeliveryInfo() throws InterruptedException, IOException {
        HashMap<String, String> validInfo = new HashMap<>();
        validInfo.put("name", "Nguyen Van A");
        validInfo.put("phoneNumber", "012 345 6789");
        validInfo.put("address", "123 Nguyen Trai Street");
        assertDoesNotThrow(() -> controller.validateDeliveryInfo(validInfo));
    }

    @Test
    void testInvalidDeliveryInfoMissingName() throws InterruptedException, IOException {
        HashMap<String, String> invalidInfo = new HashMap<>();
        invalidInfo.put("name", "");
        invalidInfo.put("phoneNumber", "012 345 6789");
        invalidInfo.put("address", "123 Nguyen Trai Street");
        assertThrows(IllegalArgumentException.class, () -> controller.validateDeliveryInfo(invalidInfo));
    }

    @Test
    void testValidDeliveryInfoMissingPhoneNumber() throws InterruptedException, IOException {
        HashMap<String, String> validInfo = new HashMap<>();
        validInfo.put("name", "Nguyen Van A");
        validInfo.put("phoneNumber", "");
        validInfo.put("address", "123 Nguyen Trai Street");
        assertDoesNotThrow(() -> controller.validateDeliveryInfo(validInfo));
    }

    @Test
    void testValidDeliveryInfoMissingAddress() throws InterruptedException, IOException {
        HashMap<String, String> validInfo = new HashMap<>();
        validInfo.put("name", "Nguyen Van A");
        validInfo.put("phoneNumber", "012 345 6789");
        validInfo.put("address", "");
        assertDoesNotThrow(() -> controller.validateDeliveryInfo(validInfo));
    }

    @Test
    void testValidDeliveryInfoInvalidName() throws InterruptedException, IOException {
        HashMap<String, String> validInfo = new HashMap<>();
        validInfo.put("name", "Nguyen Van @");
        validInfo.put("phoneNumber", "012 345 6789");
        validInfo.put("address", "123 Nguyen Trai Street");
        assertDoesNotThrow(() -> controller.validateDeliveryInfo(validInfo));
    }

    @Test
    void testValidDeliveryInfoInvalidPhoneNumber() throws InterruptedException, IOException {
        HashMap<String, String> validInfo = new HashMap<>();
        validInfo.put("name", "Nguyen Van A");
        validInfo.put("phoneNumber", "012");
        validInfo.put("address", "123 Nguyen Trai Street");
        assertDoesNotThrow(() -> controller.validateDeliveryInfo(validInfo));
    }

    @Test
    void testValidDeliveryInfoInvalidAddress() throws InterruptedException, IOException {
        HashMap<String, String> validInfo = new HashMap<>();
        validInfo.put("name", "Nguyen Van A");
        validInfo.put("phoneNumber", "012 345 6789");
        validInfo.put("address", "1@");
        assertDoesNotThrow(() -> controller.validateDeliveryInfo(validInfo));
    }

    @Test
    public void testShippingFeeWithoutRushOrder() throws SQLException {
        Media media = new Media();
        OrderMedia orderMedia1 = new OrderMedia(media, 1, 50000, 1.0, false); // Regular order, 1kg
        OrderMedia orderMedia2 = new OrderMedia(media, 1, 40000, 0.5, false); // Regular order, 0.5kg

        Order order = new Order();
        order.addOrderMedia(orderMedia1);
        order.addOrderMedia(orderMedia2);

        HashMap<String, String> deliveryInfo = new HashMap<>();
        deliveryInfo.put("city", "Hanoi");
        order.setDeliveryInfo(deliveryInfo);

        int shippingFee = order.getShippingFees();

        assertEquals(22000, shippingFee, "Shipping fee for non-rush order should be 22,000 VND.");
    }

    @Test
    public void testShippingFeeWithRushOrder() throws SQLException {
        Media media = new Media();
        OrderMedia orderMedia1 = new OrderMedia(media, 1, 50000, 1.0, true);
        OrderMedia orderMedia2 = new OrderMedia(media, 1, 40000, 0.5, false);

        Order order = new Order();
        order.addOrderMedia(orderMedia1);
        order.addOrderMedia(orderMedia2);

        HashMap<String, String> deliveryInfo = new HashMap<>();
        deliveryInfo.put("city", "Hanoi");
        order.setDeliveryInfo(deliveryInfo);

        int shippingFee = order.getShippingFees();

        assertEquals(32000, shippingFee, "Shipping fee for rush order should include an additional 10,000 VND.");
    }

    @Test
    public void testShippingFeeForFreeShipping() throws SQLException {
        Media media = new Media();
        OrderMedia orderMedia1 = new OrderMedia(media, 1, 200000, 1.0, false);
        OrderMedia orderMedia2 = new OrderMedia(media, 1, 200000, 0.5, false);

        Order order = new Order();
        order.addOrderMedia(orderMedia1);
        order.addOrderMedia(orderMedia2);

        HashMap<String, String> deliveryInfo = new HashMap<>();
        deliveryInfo.put("city", "Hanoi");
        order.setDeliveryInfo(deliveryInfo);

        int shippingFee = order.getShippingFees();

        assertEquals(0, shippingFee, "Shipping fee should be 0 VND for orders over 100,000 VND.");
    }

    @Test
    public void testShippingFeeForOutOfCity() throws SQLException {
        Media media = new Media();
        OrderMedia orderMedia1 = new OrderMedia(media, 1, 50000, 1.0, false);
        OrderMedia orderMedia2 = new OrderMedia(media, 1, 40000, 0.5, false);

        Order order = new Order();
        order.addOrderMedia(orderMedia1);
        order.addOrderMedia(orderMedia2);

        HashMap<String, String> deliveryInfo = new HashMap<>();
        deliveryInfo.put("city", "Da Nang");
        order.setDeliveryInfo(deliveryInfo);

        int shippingFee = order.getShippingFees();

        assertEquals(30000, shippingFee,
                "Shipping fee should be 30,000 VND for orders outside Hanoi or Ho Chi Minh City.");
    }

    @Test
    public void testShippingFeeWithOverweightItem() throws SQLException {
        Media media = new Media();
        OrderMedia orderMedia1 = new OrderMedia(media, 1, 50000, 10.0, false);

        Order order = new Order();
        order.addOrderMedia(orderMedia1);

        HashMap<String, String> deliveryInfo = new HashMap<>();
        deliveryInfo.put("city", "Hanoi");
        order.setDeliveryInfo(deliveryInfo);

        int shippingFee = order.getShippingFees();

        assertEquals(72500, shippingFee, "Shipping fee for a 10kg item should be calculated properly.");
    }
}
