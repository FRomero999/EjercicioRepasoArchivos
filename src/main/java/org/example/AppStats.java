package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
class AppStats implements Serializable {
    private String app;
    private Integer totalEvents;
    private Integer totalCrashes;
    private Double errorRatio;
}

