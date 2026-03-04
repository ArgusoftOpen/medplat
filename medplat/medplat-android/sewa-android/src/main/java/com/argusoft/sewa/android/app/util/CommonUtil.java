package com.argusoft.sewa.android.app.util;

import android.text.InputFilter;

import java.util.regex.Pattern;

/**
 * Defines methods for CommonUtil
 *
 * @author prateek
 * @since 02/02/23 9:56 PM
 */
public class CommonUtil {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private CommonUtil() {
        throw new IllegalStateException("Utility Class");
    }

    public boolean isEmailValid(String email) {
        return VALID_EMAIL_ADDRESS_REGEX.matcher(email).matches();
    }

    public static InputFilter getLengthInputFilter(int max) {
        return new InputFilter.LengthFilter(max);
    }

    public static InputFilter getOnlyNumberInputFilter() {
        return (source, start, end, spanned, i2, i3) -> {
            StringBuilder builder = new StringBuilder();
            String allowedChar = "1234567890";
            for (int i = start; i < end; i++) {
                char c = source.charAt(i);
                if (allowedChar.contains(Character.toString(c))) {
                    builder.append(c);
                }
            }

            // If all characters are valid, return null, otherwise only return the filtered characters
            boolean allCharactersValid = (builder.length() == end - start);
            return allCharactersValid ? null : builder.toString();
        };
    }

    public static InputFilter getAlphanumericInputFilter() {
        return (source, start, end, spanned, i2, i3) -> {
            StringBuilder builder = new StringBuilder();
            for (int i = start; i < end; i++) {
                char c = source.charAt(i);
                if (Character.isLetterOrDigit(c)) {
                    builder.append(c);
                }
            }

            // If all characters are valid, return null, otherwise only return the filtered characters
            boolean allCharactersValid = (builder.length() == end - start);
            return allCharactersValid ? null : builder.toString();
        };
    }

    public static InputFilter getAlphanumericWithSpaceInputFilter() {
        return (source, start, end, spanned, i2, i3) -> {
            StringBuilder builder = new StringBuilder();
            for (int i = start; i < end; i++) {
                char c = source.charAt(i);
                if (Character.isLetterOrDigit(c) || Character.isSpaceChar(c)) {
                    builder.append(c);
                }
            }

            // If all characters are valid, return null, otherwise only return the filtered characters
            boolean allCharactersValid = (builder.length() == end - start);
            return allCharactersValid ? null : builder.toString();
        };
    }

    public static InputFilter getMemberIdInputFilter() {
        return (source, start, end, spanned, i2, i3) -> {
            StringBuilder builder = new StringBuilder();
            String allowedChar = "ANan1234567890";
            for (int i = start; i < end; i++) {
                char c = source.charAt(i);
                if (allowedChar.contains(Character.toString(c))) {
                    builder.append(c);
                }
            }

            // If all characters are valid, return null, otherwise only return the filtered characters
            boolean allCharactersValid = (builder.length() == end - start);
            return allCharactersValid ? null : builder.toString();
        };
    }

    public static InputFilter getFamilyIdInputFilter() {
        return (source, start, end, spanned, i2, i3) -> {
            StringBuilder builder = new StringBuilder();
            String allowedChar = "FMNfmn/1234567890";
            for (int i = start; i < end; i++) {
                char c = source.charAt(i);
                if (allowedChar.contains(Character.toString(c))) {
                    builder.append(c);
                }
            }

            // If all characters are valid, return null, otherwise only return the filtered characters
            boolean allCharactersValid = (builder.length() == end - start);
            return allCharactersValid ? null : builder.toString();
        };
    }

    public static InputFilter getEmailIdInputFilter() {
        return (source, start, end, spanned, i2, i3) -> {
            StringBuilder builder = new StringBuilder();

            String allowedChar = "@!#$%&'*+-/=?^_`{|}~.";
            for (int i = start; i < end; i++) {
                char c = source.charAt(i);
                if (allowedChar.contains(Character.toString(c)) || Character.isLetterOrDigit(c)) {
                    builder.append(c);
                }
            }

            // If all characters are valid, return null, otherwise only return the filtered characters
            boolean allCharactersValid = (builder.length() == end - start);
            return allCharactersValid ? null : builder.toString();
        };
    }
}
