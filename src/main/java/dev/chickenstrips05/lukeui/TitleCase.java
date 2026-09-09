package dev.chickenstrips05.lukeui;

// https://gist.github.com/dipu-bd/939502e190f430603a7a48f358a895d9
// @dipu-bd
// dipu-bd/TitleCase.java
// I did NOT make this code

public class TitleCase {
    private static int getCharType(char ch) {
        if (Character.isLowerCase(ch)) {
            return 0;
        } else if (Character.isUpperCase(ch)) {
            return 1;
        } else if (Character.isDigit(ch)) {
            return 2;
        }
        return -1;
    }

    public static String titleCase(String text) {
        if (text == null || text.length() == 0) {
            return text;
        }

        char[] str = text.toCharArray();
        StringBuilder sb = new StringBuilder();

        boolean capRepeated = false;
        for (int i = 0, prev = -1, next; i < str.length; ++i, prev = next) {
            next = getCharType(str[i]);
            // trace consecutive capital cases
            if (prev == 1 && next == 1) {
                capRepeated = true;
            } else if (next != 0) {
                capRepeated = false;
            }
            // next is ignorable
            if (next == -1) {
                // System.out.printf("case 0, %d %d %s\n", prev, next, sb.toString());
                continue; // does not append anything
            }
            // prev and next are of same type
            if (prev == next) {
                sb.append(str[i]);
                // System.out.printf("case 1, %d %d %s\n", prev, next, sb.toString());
                continue;
            }
            // next is not an alphabet
            if (next == 2) {
                sb.append(str[i]);
                // System.out.printf("case 2, %d %d %s\n", prev, next, sb.toString());
                continue;
            }
            // next is an alphabet, prev was not +
            // next is uppercase and prev was lowercase
            if (prev == -1 || prev == 2 || prev == 0) {
                if (sb.length() != 0) {
                    sb.append(' ');
                }
                sb.append(Character.toUpperCase(str[i]));
                // System.out.printf("case 3, %d %d %s\n", prev, next, sb.toString());
                continue;
            }
            // next is lowercase and prev was uppercase
            if (prev == 1) {
                if (capRepeated) {
                    sb.insert(sb.length() - 1, ' ');
                    capRepeated = false;
                }
                sb.append(str[i]);
                // System.out.printf("case 4, %d %d %s\n", prev, next, sb.toString());
            }
        }
        String output = sb.toString().trim();
        output = (output.length() == 0) ? text : output;
        //return output;

        // Capitalize all words (Optional)
        String[] result = output.split(" ");
        for (int i = 0; i < result.length; ++i) {
            result[i] = result[i].charAt(0) + result[i].substring(1).toLowerCase();
        }
        output = String.join(" ", result);
        return output;
    }
}
