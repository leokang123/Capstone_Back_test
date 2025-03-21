package com.example.mobileapi.Enum;

public enum WasteStatus {
    COLLECTING,   // 수집됨
    MOVING,       // 이동됨
    STORED,     // 저장됨
    DISPOSED;     // 배출됨

    public WasteStatus getNextStep() {
        return switch (this) {
            case COLLECTING -> MOVING;
            case MOVING -> STORED;
            default -> throw new IllegalStateException("Unexpected event");
        };
    }
    public WasteStatus getLastStep() {
        return switch (this) {
            case STORED -> DISPOSED;
            default -> throw new IllegalStateException("Unexpected event");
        };
    }

    public WasteStatus getStep(String step) {
        return switch (step) {
            case "COLLECTING" -> COLLECTING;
            case "MOVING" -> MOVING;
            case "STORED" -> STORED;
            case "DISPOSED" -> DISPOSED;
            default -> throw new IllegalStateException("Unexpected event");
        };
    }
}
