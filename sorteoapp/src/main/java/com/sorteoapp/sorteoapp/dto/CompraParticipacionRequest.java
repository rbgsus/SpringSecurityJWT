package com.sorteoapp.sorteoapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompraParticipacionRequest {
    private Long tarjetaId;
    private Integer numero;
}

