package com.rest.template.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PraticaModelDTO {
	
	private Date dataEstrazione;
	
	private Date dataRiferimento;
	
	private String ragioneSociale;
	
	private String codiceFiscale;
	
	private String numeroPosizioneFg;
	
	private BigDecimal importoOriginaleLinea;
	
	private Boolean flagCreditoFirma;
	
	private Integer numeroRate;
	
	private Date dataScadenzaFido;
	
	private Date dataRevocaFido;
	
	private BigDecimal debitoResiduo;
	
	private Date dataPrimaRataNonPagata;
	
	private String prodotto;

}
