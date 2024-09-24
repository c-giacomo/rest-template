package com.rest.template;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.template.core.Call;
import com.rest.template.dto.PraticaModelDTO;
import com.rest.template.dto.PraticaModelResponseDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class RestTemplateApplicationTests {
	
	@Autowired
	Call call;
	
	@Autowired
	ObjectMapper objectMapper;
	
	/**
	 * 
	 * @implSpec Questo test inoltra un json a partire da un DTO. La costruzione del DTO riduce praticamente a 0 gli errori di formato data o di inserimento di Stringhe al posto di Boolean
	 *           in quanto viene immediatamente segnalato un errore di compilazione. La trasformazione da DTO a JSON avviene per mezzo della chiamata al Jackson mapper.
	 *           In questo esempio manca l'header relativo all'autenticazione quindi si avrà un AutenticazioneFallita nel campo esitoAcquisizione
	 */
	@Test
	void invioDtoRisulta401() throws ParseException, JsonMappingException, JsonProcessingException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
		List<PraticaModelDTO> list = new ArrayList<>();
		PraticaModelDTO dto = new PraticaModelDTO();
		
		String data = "2022-03-03";
		String data1 = "2022-06-03";
		
		dto.setDataEstrazione(format.parse(data));
		dto.setDataRiferimento(format.parse(data1));
		dto.setRagioneSociale("ciao");
		dto.setCodiceFiscale("123456789");
		dto.setNumeroPosizioneFg("12");
		dto.setImportoOriginaleLinea(new BigDecimal(1258));
		dto.setFlagCreditoFirma(false);
		dto.setNumeroRate(3);
		dto.setDataScadenzaFido(format.parse(data));
		dto.setDataRevocaFido(format.parse(data));
		dto.setDebitoResiduo(new BigDecimal(1256));
		dto.setDataPrimaRataNonPagata(new SimpleDateFormat("yyyy-MM-dd").parse(data));
		dto.setProdotto("test");
		
		list.add(dto);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Accept", "*/*");
		headers.set("Accept-Encoding", "gzip, deflate, br");
		headers.set("Connection", "keep-alive");
		
		String requestJson = null;
		try {
			requestJson = objectMapper.writeValueAsString(list);
		} catch (JsonProcessingException e) {
			log.error(e.getMessage());
		}
		
		List<PraticaModelResponseDTO> response = call.call(headers, requestJson);
		assertEquals("AutenticazioneFallita", response.get(0).getEsitoAcquisizione());
	}
	
	/**
	 * 
	 * @implSpec Questo test inoltra un json costruito direttamente a stringa. Si aggiunga un token valido alla riga 94 (dopo Bearer, a partire dal ?) e si provi la chiamata. 
	 * 			 La stringa JSON è volutamente errata in modo che il risultato sia 400
	 */
	@Test
	void invioJsonRisulta400() throws ParseException, JsonMappingException, JsonProcessingException {
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJWazhUdHR0WEd0eW0xS3RIX3I4ZnZSTWc4MlpOb0N1ZmlLWkhIM2JhN0xNIn0.eyJleHAiOjE3MjcxODE5MzMsImlhdCI6MTcyNzE4MDEzMywianRpIjoiMTUzYWIxYTYtM2RhNi00NzI1LTgwMzMtNjBjZDYxYjU3MGE2IiwiaXNzIjoiaHR0cHM6Ly9hdXRoLXNlcnZpY2UucGVnYXNvMjAwMC5pdC9hdXRoL3JlYWxtcy9jYXNjZW4iLCJzdWIiOiJiNmJkMjJhMS0xZTFkLTQzZDgtYmE0MS1jOTkxNGI0M2QwMTciLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJhZ2V2b2xvLWFjY2VzcyIsImFsbG93ZWQtb3JpZ2lucyI6WyIiXSwic2NvcGUiOiJhYmkgZW1haWwgcHJvZmlsZSBvdSIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwiY2xpZW50SWQiOiJhZ2V2b2xvLWFjY2VzcyIsImNsaWVudEhvc3QiOiIxMC44MS4yMzQuMTM1Iiwicm9sZSI6WyJvZmZsaW5lX2FjY2VzcyIsIkFETUlOIiwidW1hX2F1dGhvcml6YXRpb24iLCJkZWZhdWx0LXJvbGVzLWNhc2NlbiJdLCJhYmkiOlsiKiJdLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJzZXJ2aWNlLWFjY291bnQtYWdldm9sby1hY2Nlc3MiLCJjbGllbnRBZGRyZXNzIjoiMTAuODEuMjM0LjEzNSIsImVtYWlsIjoiYWdldm9sb0BwZWdhc28yMDAwLml0In0.B2XgCCt97MiWjAzgmVsi9qak5bVEvaPt6woVhz-VAKcwcIcfl3fd3UCJLVyp7NyOoJjc7WisOVPni0VGa8HMVaiupXxO2Ay-5QKEXjTmkwSqQBZ0-zrwZVcvZFDg6hzpqTP4DcT52GN3x4X--JXzNV1Vfj6izIdR9a-2wYMO_wV8c-qqj541LDpGxxpAsrrxr57BwoyvvNCxInxVuyjzPOLU3sNX2C7xbqASUDz9VbZzw9mxJdz9kYFNB6y8a4iKB6DGNHwBWMcKSECC7tgGiwqOfhJwl1cjiWRAeTXVEUGO-VUfh-ytV1C6csrWAlnGMvTREW8M7x9BmgoRytVYdg");
		headers.set("Accept", "*/*");
		headers.set("Accept-Encoding", "gzip, deflate, br");
		headers.set("Connection", "keep-alive");
		
		String requestJson = "[{\"dataEstrazione\":\"02.03.2022\",\"dataRiferimento\":\"02.06.2022\",\"ragioneSociale\":\"ciao\",\"codiceFiscale\":\"123456789\",\"numeroPosizioneFg\":\"12\",\"importoOriginaleLinea\":1258,\"flagCreditoFirma\":false,\"numeroRate\":3,\"dataScadenzaFido\":\"02.03.2022\",\"dataRevocaFido\":\"02.03.2022\",\"debitoResiduo\":1256,\"dataPrimaRataNonPagata\":\"02.03.2022\",\"prodotto\":\"test\"}]";
		
		List<PraticaModelResponseDTO> response = call.call(headers, requestJson);
		assertEquals("ValoriNonConsentiti", response.get(0).getEsitoAcquisizione());
	}

}
