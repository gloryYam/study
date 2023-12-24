package io.lmy.app.modules.account.domain.supprot;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Convert;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Convert
public class ListStringConverter implements AttributeConverter<List<String>,String> {

    /**
     * attribute 가 널이 아니면 String.join(",",a)가 실행돼서 ,로 구분된 문자열로 결합
     * 널이면 빈문자열
     */
    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        return Optional.ofNullable(attribute)
                .map(a -> String.join(",", a))
                .orElse("");
    }

    /**
     * dbData 문자열을 쉼표로 구분하여 분할한 후, 분할된 문자열을 List<String>으로
     * 변환하는 과정
     */
    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        return Stream.of(dbData.split(","))
                .collect(Collectors.toList());
    }
}
