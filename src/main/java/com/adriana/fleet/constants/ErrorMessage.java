package com.adriana.fleet.constants;

public final class ErrorMessage {

    public static final String VEHICLE_NOT_FOUND = "Vehicle not found";
    public static final String DRIVER_NOT_FOUND = "Driver not found";
    public static final String VEHICLE_ASSIGNMENT_NOT_FOUND = "Vehicle assignment not found";

    public static final String VEHICLE_ALREADY_HAS_ACTIVE_ASSIGNMENT =
            "Vehicle already has an active assignment";

    public static final String DRIVER_ALREADY_HAS_ACTIVE_ASSIGNMENT =
            "Driver already has an active assignment";

    public static final String VEHICLE_ASSIGNMENT_IS_NOT_ACTIVE =
            "Vehicle assignment is not active";

    public static final String VEHICLE_ASSIGNMENT_IS_NOT_DELETED =
            "Vehicle assignment is not deleted";

    public static final String PAGE_MUST_BE_GREATER_THAN_OR_EQUAL_TO_ZERO =
            "Page must be greater than or equal to 0";

    public static final String SIZE_MUST_BE_GREATER_THAN_OR_EQUAL_TO_ONE =
            "Size must be greater than or equal to 1";

    public static final String SIZE_MUST_BE_LESS_THAN_OR_EQUAL_TO =
            "Size must be less than or equal to ";

    public static final String STATUS_MUST_BE_ACTIVE_OR_RELEASED =
            "Status must be ACTIVE or RELEASED";

    public static final String INVALID_SORT_FIELD =
            "Invalid sort field";

    public static final String SORT_DIRECTION_MUST_BE_ASC_OR_DESC =
            "Sort direction must be asc or desc";

    private ErrorMessage() {
    }
}