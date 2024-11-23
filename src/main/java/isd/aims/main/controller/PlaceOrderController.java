package isd.aims.main.controller;

import isd.aims.main.entity.cart.Cart;
import isd.aims.main.entity.cart.CartMedia;
import isd.aims.main.entity.invoice.Invoice;
import isd.aims.main.entity.order.Order;
import isd.aims.main.entity.order.OrderMedia;
import isd.aims.main.utils.Utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Logger;

/**
 * This class controls the flow of place order usecase in our AIMS project
 * 
 * @author nguyenlm
 */
public class PlaceOrderController extends BaseController {

    /**
     * Just for logging purpose
     */
    private static Logger LOGGER = Utils.getLogger(PlaceOrderController.class.getName());

    /**
     * This method checks the avalibility of product when user click PlaceOrder
     * button
     * 
     * @throws SQLException
     */
    public void placeOrder() throws SQLException {
        Cart.getCart().checkAvailabilityOfProduct();
    }

    /**
     * This method creates the new Order based on the Cart
     * 
     * @return Order
     * @throws SQLException
     */
    @SuppressWarnings("unchecked")
    public Order createOrder() throws SQLException {
        Order order = new Order();
        for (Object object : Cart.getCart().getListMedia()) {
            CartMedia cartMedia = (CartMedia) object;

            OrderMedia orderMedia = new OrderMedia(
                    cartMedia.getMedia(),
                    cartMedia.getQuantity(),
                    cartMedia.getPrice(),
                    cartMedia.getWeight(),
                    false);

            // Add the OrderMedia object to the Order
            order.getlstOrderMedia().add(orderMedia);
        }
        return order;
    }

    /**
     * This method creates the new Invoice based on order
     * 
     * @param order
     * @return Invoice
     */
    public Invoice createInvoice(Order order) {
        return new Invoice(order);
    }

    /**
     * This method takes responsibility for processing the shipping info from user
     * 
     * @param info
     * @throws InterruptedException
     * @throws IOException
     */
    @SuppressWarnings("rawtypes")
    public void processDeliveryInfo(HashMap info) throws InterruptedException, IOException {
        LOGGER.info("Process Delivery Info");
        LOGGER.info(info.toString());
        validateDeliveryInfo(info);
    }

    /**
     * The method validates the info
     * 
     * @param info
     * @throws InterruptedException
     * @throws IOException
     */
    public void validateDeliveryInfo(HashMap<String, String> info) throws InterruptedException, IOException {
        if (info == null || info.isEmpty()) {
            throw new IllegalArgumentException("Delivery information cannot be empty");
        }

        String name = info.get("name");
        String phoneNumber = info.get("phoneNumber");
        String address = info.get("address");

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (!validateName(name)) {
            throw new IllegalArgumentException("Invalid name format");
        }

        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be empty");
        }
        if (!validatePhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("Invalid phone number format");
        }

        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Address cannot be empty");
        }
        if (!validateAddress(address)) {
            throw new IllegalArgumentException("Invalid address format");
        }
    }

    public boolean validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return false;
        }

        if (phoneNumber.charAt(0) != '0') {
            return false;
        }

        int digitCount = 0;
        boolean lastWasSeparator = false;

        for (int i = 0; i < phoneNumber.length(); i++) {
            char c = phoneNumber.charAt(i);

            if (Character.isDigit(c)) {
                digitCount++;
                lastWasSeparator = false;
            } else if (c == ' ' || c == '-' || c == '.') {
                if (lastWasSeparator || digitCount == 0) {
                    return false;
                }
                lastWasSeparator = true;
            } else {
                return false;
            }
        }

        return digitCount == 10 && !lastWasSeparator;
    }

    public boolean validateName(String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }

        if (name.startsWith(" ") || name.endsWith(" ")) {
            return false;
        }

        name = name.trim();

        if (name.length() < 2 || name.length() > 50) {
            return false;
        }

        String validCharacters = "aáàảãạâấầẩẫậăắằẳẵặ"
                + "eéèẻẽẹêếềểễệ"
                + "iíìỉĩị"
                + "oóòỏõọôốồổỗộơớờởỡợ"
                + "uúùủũụưứừửữự"
                + "yýỳỷỹỵ"
                + "AÁÀẢÃẠÂẤẦẨẪẬĂẮẰẲẴẶ"
                + "EÉÈẺẼẸÊẾỀỂỄỆ"
                + "IÍÌỈĨỊ"
                + "OÓÒỎÕỌÔỐỒỔỖỘƠỚỜỞỠỢ"
                + "UÚÙỦŨỤƯỨỪỬỮỰ"
                + "YÝỲỶỸỴ"
                + "đĐ";

        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (!Character.isLetter(c) && c != ' ' && c != '-' && validCharacters.indexOf(c) == -1) {
                return false;
            }
        }

        if (name.contains("  ") || name.contains("--")) {
            return false;
        }

        return true;
    }

    public boolean validateAddress(String address) {
        if (address == null || address.isEmpty()) {
            return false;
        }

        if (address.startsWith(" ") || address.endsWith(" ")) {
            return false;
        }

        if (address.length() < 5 || address.length() > 100) {
            return false;
        }

        boolean containsLetterOrDigit = false;
        for (int i = 0; i < address.length(); i++) {
            char c = address.charAt(i);

            if (Character.isLetterOrDigit(c) || c == ' ' || c == ',' || c == '.' || c == '-' || c == '/') {
                if (Character.isLetterOrDigit(c)) {
                    containsLetterOrDigit = true;
                }
            } else {
                return false;
            }
        }

        for (int i = 1; i < address.length(); i++) {
            char prev = address.charAt(i - 1);
            char curr = address.charAt(i);

            if ((prev == ' ' && curr == ' ') ||
                    (prev == ',' && curr == ',') ||
                    (prev == '.' && curr == '.') ||
                    (prev == '-' && curr == '-') ||
                    (prev == '/' && curr == '/')) {
                return false;
            }
        }

        return containsLetterOrDigit;
    }

    /**
     * This method calculates the shipping fees of order
     * 
     * @param order
     * @return shippingFee
     */
    public int calculateShippingFee(Order order) {
        int shippingFee = 0;
        int rushOrderCount = 0;
        double heaviestItemWeight = 0.0;
        boolean isInHanoiOrHCM = false;

        for (Object object : order.getlstOrderMedia()) {
            OrderMedia orderMedia = (OrderMedia) object;

            if (orderMedia.getRushOrder()) {
                rushOrderCount++;
            }

            double itemWeight = orderMedia.getWeight();
            if (itemWeight > heaviestItemWeight) {
                heaviestItemWeight = itemWeight;
            }
        }

        if (rushOrderCount > 0) {
            shippingFee += rushOrderCount * 10000;
        }

        if (isInHanoiOrHCM) {
            if (heaviestItemWeight <= 3) {
                shippingFee += 22000;
            } else {
                shippingFee += 22000 + (int) ((heaviestItemWeight - 3) / 0.5) * 2500;
            }
        } else {
            if (heaviestItemWeight <= 0.5) {
                shippingFee += 30000;
            } else {
                shippingFee += 30000 + (int) ((heaviestItemWeight - 0.5) / 0.5) * 2500;
            }
        }

        int totalAmount = order.getAmount();
        int freeShippingLimit = 100000;
        int maxFreeShippingFee = 25000;

        double nonRushTotalAmount = 0;
        for (Object object : order.getlstOrderMedia()) {
            OrderMedia orderMedia = (OrderMedia) object;
            if (!orderMedia.getRushOrder()) {
                nonRushTotalAmount += orderMedia.getPrice();
            }
        }

        if (nonRushTotalAmount > freeShippingLimit) {
            shippingFee = Math.min(shippingFee, maxFreeShippingFee);
        }

        return shippingFee;
    }

}
