package com.grepp.matnam.app.controller.api.admin.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatLongResponse {
    private String label;
    private Long value;
}
