package com.rest.template.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PraticaModelResponseDTO {
	
	private String esitoAcquisizione;
	private String message;
	private String codiceFiscale;
	private String numeroPosizioneFg;
}
