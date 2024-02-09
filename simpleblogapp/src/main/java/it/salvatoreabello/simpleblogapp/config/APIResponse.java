package it.salvatoreabello.simpleblogapp.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.*;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class APIResponse <T>{
    private int statusCode;
    private String statusMessage;
    private int returnedObjects;
    private int totalObjects;
    private T payload;

    @JsonInclude(Include.NON_NULL)
    private APIError errors;
}

