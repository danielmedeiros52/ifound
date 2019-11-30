package com.Ifound.enumerates;


public enum EnumUserType {
    OWNER(1,"owner"),
    FOUND(2,"found"),
    LOST(3,"lost"),
    UNKNOWN(0 , "N/A");


    private String type;
    private int id;

    EnumUserType( int id, String type) {
        this.type = type;
        this.id = id;
    }

    public static EnumUserType getFromString(String value) {
        return EnumUserType.valueOf(value.toUpperCase());
    }

    public static EnumUserType getFromInt(Integer value) {
        if (value != null) {
            for (EnumUserType enumUserType : EnumUserType.values()) {
                if (enumUserType.getId() == value) {
                    return enumUserType;
                }
            }
        }
        return UNKNOWN;
    }

    public String getType() {
        return type;
    }

    public int getId() {
        return id;
    }
}
