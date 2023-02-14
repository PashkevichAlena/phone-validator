package com.neotech.validator.utils.parser;

import com.neotech.validator.model.dto.CountryDto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HtmlParser {
    private static final String PATH_TO_TABLES = "div.mw-parser-output > table";
    private static final String TABLE_CLASS = "wikitable sortable";
    private static final String PATH_TO_TABLE_HEADERS = "tr > th";
    private static final String TABLE_HEADER = "Country, Territory or Service";
    private static final String TABLE_BODY = "tBody";
    private static final String TABLE_LINE = "tr";
    private static final String TABLE_ROW = "td";
    private static final int INDEX_OF_COUNTRY_NAME = 0;
    private static final int INDEX_OF_CALLING_CODES = 1;

    public static List<CountryDto> parse(String html) {
        Document document = Jsoup.parse(html);
        Element table = findTable(document);

        List<CountryDto> countries = new ArrayList<>();
        List<Element> tableLines = table.select(TABLE_LINE);
        tableLines.forEach(tableLine -> {
            if (isHeader(tableLines, tableLine)) return;

            Elements rows = tableLine.select(TABLE_ROW);
            countries.add(
                    new CountryDto(
                            selectCountryName(rows),
                            selectCallingCodes(rows)
                    ));
        });
        return countries;
    }

    private static boolean isHeader(List<Element> tableLines, Element tableLine) {
        return tableLines.indexOf(tableLine) == 0;
    }

    private static Element findTable(Document document) {
        Elements tables = document.select(PATH_TO_TABLES);
        List<Element> tablesWithClass = tables.stream()
                .filter(it -> it.className().equals(TABLE_CLASS))
                .collect(Collectors.toList());
        Element table = tablesWithClass.stream()
                .filter(it ->
                        it.select(PATH_TO_TABLE_HEADERS).stream()
                                .anyMatch(header -> header.text().equals(TABLE_HEADER)))
                .findFirst()
                .orElse(null);
        return table.select(TABLE_BODY).get(0);
    }

    private static String selectCountryName(Elements rows) {
        return trimWhiteSpaces(rows.get(INDEX_OF_COUNTRY_NAME).text());
    }

    private static List<String> selectCallingCodes(Elements rows) {
        return splitByComma(removeWhiteSpaces(rows.get(INDEX_OF_CALLING_CODES).select("a").get(0).text()));
    }

    private static String trimWhiteSpaces(String value) {
        return value.trim().replace("\u00A0", "");
    }

    private static String removeWhiteSpaces(String value) {
        return value.replaceAll(" ", "");
    }

    private static List<String> splitByComma(String value) {
        return Stream.of(value.split(",")).collect(Collectors.toList());
    }
}
