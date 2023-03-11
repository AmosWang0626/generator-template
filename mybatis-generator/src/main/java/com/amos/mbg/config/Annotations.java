package com.amos.mbg.config;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public enum Annotations {

    DATA("data", "@Data", "lombok.Data"),
    GETTER("getter", "@Getter", "lombok.Getter"),
    SETTER("setter", "@Setter", "lombok.Setter"),
    BUILDER("builder", "@Builder", "lombok.Builder"),
    ALL_ARGS_CONSTRUCTOR("allArgsConstructor", "@AllArgsConstructor", "lombok.AllArgsConstructor"),
    NO_ARGS_CONSTRUCTOR("noArgsConstructor", "@NoArgsConstructor", "lombok.NoArgsConstructor"),
    ACCESSORS("accessors", "@Accessors", "lombok.experimental.Accessors"),
    TO_STRING("toString", "@ToString", "lombok.ToString");

    private final String name;
    private final String paramName;
    private final List<String> options;
    private final FullyQualifiedJavaType javaType;

    Annotations(String paramName, String name, String className) {
        this.name = name;
        this.paramName = paramName;
        this.options = new ArrayList<>();
        this.javaType = new FullyQualifiedJavaType(className);
    }

    public static Annotations getValueOf(String paramName) {
        for (Annotations annotation : Annotations.values()) {
            if (String.CASE_INSENSITIVE_ORDER.compare(paramName, annotation.paramName) == 0) {
                return annotation;
            }
        }

        return null;
    }

    public static Collection<Annotations> getDependencies(Annotations annotation) {
        if (annotation == ALL_ARGS_CONSTRUCTOR) {
            return Collections.singleton(NO_ARGS_CONSTRUCTOR);
        } else {
            return Collections.emptyList();
        }
    }

    // A trivial quoting.
    // Because Lombok annotation options type is almost String or boolean.
    private static String quote(String value) {
        if (Boolean.TRUE.toString().equals(value) || Boolean.FALSE.toString().equals(value)) {
            // case of boolean, not passed as an array.
            return value;
        }
        return value.replaceAll("[\\w]+", "\"$0\"");
    }

    public void appendOptions(String key, String value) {
        String keyPart = key.substring(key.indexOf(".") + 1);
        String valuePart = value.contains(",") ? String.format("{%s}", value) : value;
        this.options.add(String.format("%s=%s", keyPart, quote(valuePart)));
    }

    public String asAnnotation() {
        if (options.isEmpty()) {
            return name;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append("(");
        boolean first = true;
        for (String option : options) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(option);
        }
        sb.append(")");
        return sb.toString();
    }

    public FullyQualifiedJavaType getJavaType() {
        return javaType;
    }
}
