package com.kairos.utils;

public class Regex {

    public static final String CPF =
            "^([0-9]{11}|[0-9]{3}\\.[0-9]{3}\\.[0-9]{3}-[0-9]{2})$";

    public static final String CEP =
            "^([0-9]{8}|[0-9]{5}-[0-9]{3})";

    public static final String EMAIL =
            "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";

    public static final String CNPJ =
            "^([0-9]{14}|[0-9]{2}\\.[0-9]{3}\\.[0-9]{3}/[0-9]{4}-[0-9]{2})$";
}
