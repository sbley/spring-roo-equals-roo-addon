package de.saxsys.roo.equals.addon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.roo.classpath.details.annotations.AnnotationAttributeValue;
import org.springframework.roo.classpath.details.annotations.AnnotationMetadataBuilder;
import org.springframework.roo.model.JavaType;

public class Annotation {
    private Annotation() {

    }

    public static AnnotationMetadataBuilder annotation(String fqn) {
        return new AnnotationMetadataBuilder(new JavaType(fqn));
    }

    public static AnnotationMetadataBuilder annotation(String fqn,
            AnnotationAttributeValue<?>... attributes) {
        return new AnnotationMetadataBuilder(new JavaType(fqn),
                Arrays.asList(attributes));
    }

    public static List<AnnotationMetadataBuilder> operator_plus(
            AnnotationMetadataBuilder b1, AnnotationMetadataBuilder b2) {
        List<AnnotationMetadataBuilder> list = new ArrayList<AnnotationMetadataBuilder>();
        list.add(b1);
        list.add(b2);
        return list ;
    }

}
