package com.example.newag.mvp.model.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Translation {

    private Integer code;
    private String msg;
    private DataDTO data;
    private Long timestamp;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        private String token;
    }

}
