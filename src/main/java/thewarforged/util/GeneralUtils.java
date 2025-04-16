package thewarforged.util;

public class GeneralUtils {
    public static String arrToString(Object[] arr) {
        if (arr == null)
            return null;
        if (arr.length == 0)
            return "";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length - 1; ++i) {
            sb.append(arr[i]).append(", ");
        }
        sb.append(arr[arr.length - 1]);
        return sb.toString();
    }

    public static String removePrefix(String ID) {
        return ID.substring(ID.indexOf(":") + 1);
    }

    public static void easyPrint(String msg) {
        easyPrint(msg, "+++++");
    }

    public static void easyPrint(String msg, String divider) {
        System.out.println(divider);
        System.out.println(msg);
        System.out.println(divider);
    }
}
