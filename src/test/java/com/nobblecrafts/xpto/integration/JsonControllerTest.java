package com.nobblecrafts.xpto.integration;

import java.util.List;

import com.nobblecrafts.xpto.model.CidadeModel;
import com.nobblecrafts.xpto.model.CidadesPorEstadoModel;
import com.nobblecrafts.xpto.service.CidadeService;
import com.nobblecrafts.xpto.service.DataService;
import com.nobblecrafts.xpto.util.PageableWrapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Json Controller Integration Test")
@AutoConfigureTestDatabase
public class JsonControllerTest {

  @Autowired
  private TestRestTemplate rest;

  @Autowired
  private DataService dataService;

  @Autowired
  private CidadeService cidadeService;

  @LocalServerPort
  private int port;

  @Value("${resource}")
  private String path;

  @BeforeEach
  public void test() {
    var cidades = dataService.readFile(path);
    cidadeService.saveAllCities(cidades);
  }

  @Test
  @DisplayName("Retornar somente as cidades que são capitais ordenadas por nome")
  public void getCapitals() {

    String url = "/api/capitals";

    var data = rest.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<PageableWrapper<CidadeModel>>() {
    }).getBody();
    log.info("DATA\n{}", data.getSize());
    Assertions.assertNotNull(data);
    Assertions.assertEquals(data.getSize(), 20);
  }

  @Test
  @DisplayName("Retornar o nome do estado com a maior e menor quantidade de cidades e a quantidade de cidades")
  public void getStatesWithMoreAndLessCities() {

    String url = "/api/states-more-less-cities";

    var data = rest.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<CidadesPorEstadoModel>>() {
    }).getBody();

    Assertions.assertNotNull(data);
    Assertions.assertEquals(data.size(), 2);
    Assertions.assertTrue(data.stream().allMatch(d -> d.getCidades() != null && d.getEstado() != null));
    Assertions.assertTrue(!data.get(0).getEstado().equalsIgnoreCase(data.get(1).getEstado()));
    Assertions.assertTrue(data.get(0).getCidades() > (data.get(1).getCidades()));
  }

  @Test
  @DisplayName("Retornar a quantidade de cidades por estado")
  public void getQuantityOfCitiesPerState() {

    String state = "MG";
    String url = String.format("/api/quantity-cities-per-state?state=%s", state);

    var data = rest.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<CidadesPorEstadoModel>() {
    }).getBody();

    log.info("DATA\n{}", data);
    Assertions.assertNotNull(data);
    Assertions.assertTrue(data.getCidades() > 1);
  }

  @Test
  @DisplayName("Obter os dados da cidade informando o id do IBGE")
  public void getCityById() {

    String ibgeId = "1100015";
    String url = String.format("/api/search-city-by-id?ibgeId=%s", ibgeId);

    var data = rest.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<CidadeModel>() {
    }).getBody();

    log.info("DATA\n{}", data);
    Assertions.assertNotNull(data);
    Assertions.assertTrue(data.getIbgeId() == Long.parseLong(ibgeId));
  }

  @Test
  @DisplayName("Retornar o nome das cidades baseado em um estado selecionado")
  public void getCityByState() {

    String state = "SP";
    String url = String.format("/api/get-cities-by-state?state=%s", state);

    var data = rest.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<PageableWrapper<CidadeModel>>() {
    }).getBody();

    log.info("DATA\n{}", data);
    Assertions.assertNotNull(data);
    Assertions.assertTrue(data.getContent().stream().map(CidadeModel::getUf).map(String::toLowerCase)
        .allMatch(uf -> uf.equalsIgnoreCase(state)));
  }

  @Test
  @DisplayName("Permitir adicionar uma nova Cidade")
  public void addCity() {

    var city = CidadeModel.builder().ibgeId(91291239123L).name("Cidade Teste").build();

    String url = "/api/add-city";

    var data = rest.postForEntity(url, city, CidadeModel.class);

    log.info("DATA\n{}", data);
    Assertions.assertNotNull(data);
    Assertions.assertEquals(data.getBody(), city);
    Assertions.assertEquals(data.getStatusCode(), HttpStatus.CREATED);
  }

  @Test
  @DisplayName("Permitir deletar uma cidade com um id que existe")
  public void deleteCityCorrect() {

    String ibgeId = "1100015";

    String url = String.format("/api/delete-city?ibgeId=%s", ibgeId);

    var data = rest.exchange(url, HttpMethod.DELETE, null, Void.class);

    log.info("DATA\n{}", data);
    Assertions.assertNotNull(data);
    Assertions.assertEquals(data.getStatusCode(), HttpStatus.NO_CONTENT);
  }

  @Test
  @DisplayName("Permitir deletar uma cidade com um id que não existe")
  public void deleteCityIncorrect() {

    String ibgeId = "932012392";

    String url = String.format("/api/delete-city?ibgeId=%s", ibgeId);


    var data = rest.exchange(url, HttpMethod.DELETE, null, Void.class);

    Assertions.assertNotNull(data);
    Assertions.assertEquals(data.getStatusCode(), HttpStatus.BAD_REQUEST);
    
  }

  /**
   * Cansei =(
   */
}
