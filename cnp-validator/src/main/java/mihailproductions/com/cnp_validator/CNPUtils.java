package mihailproductions.com.cnp_validator;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

final class CNPUtils {
    public static boolean isValidCNP(String cnp) {
        final int cnpLength = 13;
        if (cnp != null) {
            cnp = cnp.trim();
        } else return false;
        return cnp.length() == cnpLength && cnp.matches("\\d+") && controlValidation(cnp);
    }

    private static boolean controlValidation(String cnp) {
        final String controlSequence = "279146358279";
        final int controlDivider = 11;
        int controlSum = 0;
        int controlDigit;
        for (int charIndex = 0; charIndex < controlSequence.length(); charIndex++) {
            controlSum += Character.getNumericValue(cnp.charAt(charIndex)) * Character.getNumericValue(controlSequence.charAt(charIndex));
        }
        if (controlSum % controlDivider == 10) {
            controlDigit = 1;
        } else {
            controlDigit = controlSum % 11;
        }
        return controlDigit + '0' == cnp.charAt(cnp.length() - 1);
    }

    public static String initializeSex(Context context, String cnp) {
        if ((cnp.charAt(0) - '0') % 2 == 1) {
            return context.getResources().getString(R.string.male);
        }
        return context.getResources().getString(R.string.female);
    }

    public static Date initializeDate(String cnp) {
        int yearIndex = cnp.charAt(0) - '0';
        String ddmm = cnp.substring(5, 7) + "/" + cnp.substring(3, 5) + "/";
        String yy = cnp.substring(1, 3);
        SimpleDateFormat normalDF = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat foreignDF = new SimpleDateFormat("dd/MM/yy");
        try {
            switch (yearIndex) {
                case 1:
                case 2:
                    return normalDF.parse(ddmm + "19" + yy);
                case 3:
                case 4:
                    return normalDF.parse(ddmm + "18" + yy);
                case 5:
                case 6:
                    return normalDF.parse(ddmm + "20" + yy);
                case 7:
                case 8:
                    return foreignDF.parse(ddmm + yy);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isCitizen(String cnp) {
        return !(cnp.charAt(0) == '7' || cnp.charAt(0) == '8');
    }

    public static String initializeCounty(Context context, String cnp) {
        int countyIndex = Integer.parseInt(cnp.substring(7, 9));
        String counties[] = context.getResources().getStringArray(R.array.counties);
        if (countyIndex == 51) {
            return counties[46];
        } else if (countyIndex == 52) {
            return counties[47];
        } else {
            return counties[countyIndex - 1];
        }
    }
}