package core.di.factory.support;

public enum InjectType {
    INJECT_CONSTRUCTOR("생성자 주입"),
    INJECT_FIELD("setter, field 주입"),
    INJECT_NO("기본 생성자");

    private final String description;

    InjectType(String description) {
        this.description = description;
    }
}
