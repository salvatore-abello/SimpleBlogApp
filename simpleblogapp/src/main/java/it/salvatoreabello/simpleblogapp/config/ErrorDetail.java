package it.salvatoreabello.simpleblogapp.config;

import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorDetail {
    private String field;
    private String source;
    private String code;
    private String message;

}
