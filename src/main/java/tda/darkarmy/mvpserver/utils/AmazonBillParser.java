package tda.darkarmy.mvpserver.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AmazonBillParser {
    public static void parseBill(String text) {
        // Example Regex Patterns
        Pattern orderIdPattern = Pattern.compile("Order ID:\\s*(\\w+)");
        Pattern itemPattern = Pattern.compile("Item:\\s*(.*)");
        Pattern pricePattern = Pattern.compile("Price:\\s*\\$([0-9.]+)");

        // Find Order ID
        Matcher orderIdMatcher = orderIdPattern.matcher(text);
        if (orderIdMatcher.find()) {
            System.out.println("Order ID: " + orderIdMatcher.group(1));
        }

        // Find Items and Prices
        Matcher itemMatcher = itemPattern.matcher(text);
        Matcher priceMatcher = pricePattern.matcher(text);

        while (itemMatcher.find() && priceMatcher.find()) {
            System.out.println("Item: " + itemMatcher.group(1));
            System.out.println("Price: $" + priceMatcher.group(1));
        }
    }
}

