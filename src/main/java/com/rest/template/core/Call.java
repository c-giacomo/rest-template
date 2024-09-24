package com.rest.template.core;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.rest.template.dto.PraticaModelResponseDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Call {
	
	private final RestTemplateBuilder restTemplateBuilder;
	
	@SuppressWarnings("rawtypes")
	public List<PraticaModelResponseDTO> call(HttpHeaders headers, String requestJson) throws JsonMappingException, JsonProcessingException {
		
		List<PraticaModelResponseDTO> resList = new ArrayList<>();
		try {
			
			RestTemplate restTemplate = restTemplateBuilder.build();
			HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);
			
			ResponseEntity<List<PraticaModelResponseDTO>> response = restTemplate.exchange("http://localhost:8086/AGEVOLO-TRANSFORMER/api/v1/agevolo/checkGaranzieEsterne", HttpMethod.POST, entity, 
					new ParameterizedTypeReference<List<PraticaModelResponseDTO>>() {});
			
			resList = response.getBody();
			
		} catch (HttpStatusCodeException e) {
			ResponseEntity response = ResponseEntity.status(e.getRawStatusCode()).headers(e.getResponseHeaders()).body(e.getResponseBodyAsString());
			PraticaModelResponseDTO dto = new PraticaModelResponseDTO();
	
			switch (response.getStatusCode()) {
				case BAD_REQUEST: dto.setEsitoAcquisizione("ValoriNonConsentiti");
				break;
				case UNAUTHORIZED: dto.setEsitoAcquisizione("AutenticazioneFallita");
				break;
				default:
			}
			resList.add(dto);
		} catch (Exception e) {
			PraticaModelResponseDTO dto = new PraticaModelResponseDTO();
			dto.setEsitoAcquisizione("ErroreDiSistema");
			resList.add(dto);
		}
		
		return resList;
	}

}
