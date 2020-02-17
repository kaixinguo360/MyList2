package com.my.list.service.filter;

public class Tag {

    private boolean strict;
    private String value;
    private Long id;
    private String type;

    public Tag() {}
    public Tag(Long id) {
        this(id, null, true, null);
    }
    public Tag(String value) {
        this(null, value, true, "tag");
    }
    public Tag(String value, boolean strict) {
        this(null, value, strict, "tag");
    }
    private Tag(Long id, String value, boolean strict, String type) {
        this.strict = strict;
        this.value = value;
        this.id = id;
        this.type = type;
    }

    // ---- Setter & Getter ---- //
    public boolean isStrict() {
        return strict;
    }
    public void setStrict(boolean strict) {
        this.strict = strict;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
