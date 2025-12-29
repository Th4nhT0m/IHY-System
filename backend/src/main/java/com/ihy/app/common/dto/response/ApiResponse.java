package com.ihy.app.common.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL) // Null properties do not appear in objects.
public class ApiResponse<T> {
    String code;
    String message;
    T result;

    @Builder.Default
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime timestamp = LocalDateTime.now();
}

