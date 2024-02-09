package it.salvatoreabello.simpleblogapp.config;

import lombok.*;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class APIError {
    @Builder.Default
    private Date timestamp = new Date();
    private String path;
    private List<ErrorDetail> details;
}
