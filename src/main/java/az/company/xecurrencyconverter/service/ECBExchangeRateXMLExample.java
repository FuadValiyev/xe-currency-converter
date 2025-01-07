package az.company.xecurrencyconverter.service;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import org.w3c.dom.Document;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ECBExchangeRateXMLExample {

    private static final String ECB_DAILY_RATES_URL = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";

    public static void main(String[] args) {
        try {
            // 1. XML Məlumatını Çəkmək
            URL url = new URL(ECB_DAILY_RATES_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/xml");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                                           + conn.getResponseCode());
            }

            // 2. XML-i Parse Etmək
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(conn.getInputStream());
            doc.getDocumentElement().normalize();

            // 3. XPath İstifadə Edərək Məzənnələri Əldə Etmək
            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xpath = xpathFactory.newXPath();
            XPathExpression expr = xpath.compile("//Cube/Cube/Cube");

            org.w3c.dom.NodeList nodeList = (org.w3c.dom.NodeList) expr.evaluate(doc, XPathConstants.NODESET);

            Map<String, Double> rates = new HashMap<>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                org.w3c.dom.Node node = nodeList.item(i);
                String currency = node.getAttributes().getNamedItem("currency").getNodeValue();
                String rateStr = node.getAttributes().getNamedItem("rate").getNodeValue();
                double rate = Double.parseDouble(rateStr);
                rates.put(currency, rate);
            }

            conn.disconnect();

            // 4. Məzənnələri Göstərmək
            System.out.println("Avro (EUR) Əsaslı Günlük Məzənnələr:");
            for (Map.Entry<String, Double> entry : rates.entrySet()) {
                System.out.println("Valyuta: " + entry.getKey() + " - Məzənnə: " + entry.getValue());
            }

            // 5. Valyuta Çevrilmə Funksiyası
            String targetCurrency = "USD";
            double amountEUR = 100.0;
            double convertedAmount = convertCurrency(amountEUR, "EUR", targetCurrency, rates);
            System.out.println(amountEUR + " EUR = " + convertedAmount + " " + targetCurrency);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Valyuta çevrilmə funksiyası.
     *
     * @param amount         Çevriləcək miqdar
     * @param fromCurrency   Əsas valyuta
     * @param toCurrency     Hədəf valyuta
     * @param rates          Məzənnə cədvəli
     * @return Çevrilmiş miqdar
     */
    public static double convertCurrency(double amount, String fromCurrency, String toCurrency, Map<String, Double> rates) {
        if (fromCurrency.equals("EUR")) {
            Double toRate = rates.get(toCurrency);
            if (toRate == null) {
                throw new IllegalArgumentException("Valyuta tapılmadı: " + toCurrency);
            }
            return amount * toRate;
        } else if (toCurrency.equals("EUR")) {
            Double fromRate = rates.get(fromCurrency);
            if (fromRate == null) {
                throw new IllegalArgumentException("Valyuta tapılmadı: " + fromCurrency);
            }
            return amount / fromRate;
        } else {
            Double fromRate = rates.get(fromCurrency);
            Double toRate = rates.get(toCurrency);
            if (fromRate == null || toRate == null) {
                throw new IllegalArgumentException("Valyuta tapılmadı: " + fromCurrency + " və ya " + toCurrency);
            }
            return (amount / fromRate) * toRate;
        }
    }
}
