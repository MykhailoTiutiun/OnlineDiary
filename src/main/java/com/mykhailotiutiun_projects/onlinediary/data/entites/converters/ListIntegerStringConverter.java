package com.mykhailotiutiun_projects.onlinediary.data.entites.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Converter(autoApply = true)
public class ListIntegerStringConverter implements AttributeConverter<List<Integer>, String> {
    public String convertToDatabaseColumn(List<Integer> attribute) {
        if (attribute == null) {
            return null;
        }
        StringBuilder str = new StringBuilder();
        for (Integer val : attribute) {
            if (str.length() > 0) {
                str.append(",");
            }
            str.append(val);
        }
        return str.toString();
    }

    public List<Integer> convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        List<Integer> list = new ArrayList<>();
        StringTokenizer tokeniser = new StringTokenizer(dbData, ",");
        while (tokeniser.hasMoreTokens()) {
            list.add(Integer.valueOf(tokeniser.nextToken()));
        }
        return list;
    }
}
