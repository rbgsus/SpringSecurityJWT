package com.sorteoapp.sorteoapp.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetImagenDto {
    private Long id;
    private String contenidoBase64;
}
