package az.company.xecurrencyconverter.util;

import lombok.experimental.UtilityClass;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@UtilityClass
public class Utility {

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty() || str.trim().isEmpty() || str.isBlank();
    }

    public static boolean isNullOrEmpty(Object obj) {
        return obj == null;
    }

    public static double toDouble(String str) {
        double result = 0;
        if (str == null || str.isEmpty() || !isNumeric(str)) {
            return result;
        } else {
            return Double.parseDouble(str);
        }
    }

    public static String toString(double dbl) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');

        DecimalFormat df = new DecimalFormat("#,###.###", symbols);
        df.setMaximumFractionDigits(3);
        df.setMinimumFractionDigits(0);
        df.setGroupingUsed(true);

        return df.format(dbl);
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static String getTodayDateForBankApi() {
        return getTodayDate("dd.MM.yyyy");
    }

    public static String getTodayDate(String format) {
        return formatDate(new Date(), format);
    }

    public static String formatDate(Date date, String format) {
        if (date == null) return "";
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }
}
