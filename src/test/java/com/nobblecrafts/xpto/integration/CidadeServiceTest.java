package com.nobblecrafts.xpto.integration;

import com.nobblecrafts.xpto.exception.BadRequestException;
import com.nobblecrafts.xpto.model.CidadeModel;
import com.nobblecrafts.xpto.service.CidadeService;
import com.nobblecrafts.xpto.service.DataService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Cidade Service Integration Test")
public class CidadeServiceTest {

  @Autowired
  private CidadeService cidadeService;

  @Autowired
  private DataService dataService;

  @Value("${resource}")
  private String path;

  private PageRequest pageable = PageRequest.of(0, 15);

  @BeforeEach
  public void test() {
    var cidades = dataService.readFile(path);
    cidadeService.saveAllCities(cidades);
  }

  @Test
  @DisplayName("getAllCidades")
  public void getAllCidades() {
    var page = cidadeService.getAllCidades(PageRequest.of(0, 20));
    Assertions.assertNotNull(page);
    log.info("\n\nPage {}\n\n", page.getContent());
    Assertions.assertTrue(page.getSize() == 20);
  }

  @Test
  @DisplayName("updateCidade")
  public void updateCidade() {
    var page = cidadeService.getAllCidades(PageRequest.of(0, 10));
    log.info("\n\nGET-ALL-CIDADES {}\n\n", page.getContent());
    var cidade = page.getContent().get(0);
    cidade = cidade.withAltNames("AltNameTest");
    var update = cidadeService.updateCidade(cidade);
    Assertions.assertNotNull(update);
    Assertions.assertEquals(update, cidade);
    Assertions.assertTrue(update.getAltNames() == "AltNameTest");
  }

  @Test
  @DisplayName("getCapitals")
  public void getCapitals() {
    var page = cidadeService.getCapitals(PageRequest.of(0, 15));
    Assertions.assertNotNull(page);
    Assertions.assertTrue(page.getNumberOfElements() <= 15);
    Assertions.assertTrue(page.getContent().stream().allMatch(e -> e.getCapital()));
  }

  @Test
  @DisplayName("getStateWithLargestNumberOfCities")
  public void getStateWithLargestNumberOfCities() {
    var state = cidadeService.getStateWithLargestNumberOfCities();
    Assertions.assertNotNull(state);
    Assertions.assertTrue(() -> {
      return state.getCidades() > 0;
    });
  }

  @Test
  @DisplayName("getStateWithLessCities")
  public void getStateWithLessCities() {
    var state = cidadeService.getStateWithLessCities();
    Assertions.assertNotNull(state);
    Assertions.assertTrue(() -> {
      return state.getCidades() > 0;
    });
  }

  @Test
  @DisplayName("getNumberOfCities")
  public void getNumberOfCities() {
    var count = cidadeService.getNumberOfCities();
    Assertions.assertNotNull(count);
    Assertions.assertTrue(count > 0L);
  }

  @Test
  @DisplayName("getNumberOfCitiesPerState")
  public void getNumberOfCitiesPerState() {
    var model = cidadeService.getNumberOfCitiesPerState("MG");
    log.info("\nMODEL\n{} - {}\n", model.getCidades(), model.getEstado());
    Assertions.assertNotNull(model);
    Assertions.assertTrue(() -> {
      return StringUtils.hasText(model.getEstado()) && model.getCidades() > 0;
    });
  }

  @Test
  @DisplayName("getCityById")
  public void getCityByIdWrong() {
    Assertions.assertThrows(BadRequestException.class, () -> {
      cidadeService.getCityById(99999999L);
    });
  }

  @Test
  @DisplayName("getCityById")
  public void getCityByCorrectId() {
    var page = cidadeService.getAllCidades(PageRequest.of(0, 10));
    var ibgeId = page.getContent().get(0).getIbgeId();
    var find = cidadeService.getCityById(ibgeId);
    Assertions.assertNotNull(find);
    Assertions.assertEquals(ibgeId, find.getIbgeId());
  }

  @Test
  @DisplayName("getCitiesByState")
  public void getCitiesByState() {
    var page = cidadeService.getCitiesByState("SP", PageRequest.of(0, 10));
    Assertions.assertNotNull(page);
    Assertions.assertFalse(page.getContent().stream().anyMatch(city -> !city.getUf().equalsIgnoreCase("SP")));
  }

  @Test
  @DisplayName("addCity")
  public void addCity() {
    var city = CidadeModel.builder().ibgeId(200300400L).name("Test City").uf("GO").build();
    var saved = cidadeService.addCity(city);
    Assertions.assertEquals(city, saved);
  }

  @Test
  @DisplayName("deleteCity")
  public void deleteCityWrong() {
    Assertions.assertThrows(BadRequestException.class, () -> {
      cidadeService.deleteCity(40249240L);
    });
  }

  @Test
  @DisplayName("deleteCityCorrect")
  public void deleteCity() {
    var city = CidadeModel.builder().ibgeId(200300400L).name("Test City").uf("GO").build();
    cidadeService.addCity(city);
    cidadeService.deleteCity(city.getIbgeId());
    Assertions.assertThrows(BadRequestException.class, () -> {
      cidadeService.getCityById(40249240L);
    });
  }

  @Test
  @DisplayName("getTotalOfRecords")
  public void getTotalOfRecords() {
    var count = cidadeService.getTotalOfRecords();
    Assertions.assertNotNull(count);
    Assertions.assertTrue(count > 0);
  }

  @Test
  @DisplayName("getMostDistantCities")
  public void getMostDistantCities() {
    var list = cidadeService.getMostDistantCities();
    Assertions.assertNotNull(list);
    Assertions.assertEquals(list.size(), 2);
    Assertions.assertEquals(list.get(0).getEq(), list.get(1).getEq());
  }

  /**
   * Cities Column Value
   */

  @Test
  @DisplayName("getCitiesByColumnValue::uf")
  public void getCitiesByColumnValueUf() {
    var page = cidadeService.getCitiesByColumnValue("uf", "sp", pageable);
    Assertions.assertNotNull(page);
    Assertions.assertTrue(page.getSize() <= 15);
    Assertions.assertTrue(page.getContent().stream()
      .allMatch(city -> city.getUf().equalsIgnoreCase("sp")));
  }

  @Test
  @DisplayName("getCitiesByColumnValue::name")
  public void getCitiesByColumnValueName() {
    var page = cidadeService.getCitiesByColumnValue("name", "ab", pageable);

    Assertions.assertNotNull(page);
    Assertions.assertTrue(page.getSize() <= 15);
    Assertions.assertTrue(page.getContent().stream()
      .map(CidadeModel::getName)
      .map(String::toLowerCase)
      .allMatch(name -> name.contains("ab")));
  }

  @Test
  @DisplayName("getCitiesByColumnValue::name")
  public void getCitiesByColumnValueCapital() {
    var page = cidadeService.getCitiesByColumnValue("capital", "true", pageable);
    page.getContent().stream().map(CidadeModel::getCapital).forEach(System.out::println);
    Assertions.assertNotNull(page);
    Assertions.assertTrue(page.getSize() <= 15);
    Assertions.assertTrue(page.getContent().stream()
      .allMatch(CidadeModel::getCapital));
  }

  @Test
  @DisplayName("getCitiesByColumnValue::lon")
  public void getCitiesByColumnValueLongitude() {
    var page = cidadeService.getCitiesByColumnValue("lon", "3", pageable);
    
    Assertions.assertNotNull(page);
    Assertions.assertTrue(page.getSize() <= 15);
    Assertions.assertTrue(page.getContent().stream()
      .map(CidadeModel::getLongitude)
      .map(lon -> Double.toString(lon))
      .allMatch(lon -> lon.contains("3")));
  }

  @Test
  @DisplayName("getCitiesByColumnValue::lat")
  public void getCitiesByColumnValueLatitude() {
    var page = cidadeService.getCitiesByColumnValue("lat", "99", pageable);
    
    Assertions.assertNotNull(page);
    Assertions.assertTrue(page.getSize() <= 15);
    Assertions.assertTrue(page.getContent().stream()
      .map(CidadeModel::getLatitude)
      .map(lat -> Double.toString(lat))
      .allMatch(lat -> lat.contains("99")));
  }

  @Test
  @DisplayName("getCitiesByColumnValue::noaccents")
  public void getCitiesByColumnValueNoAccents() {
    var page = cidadeService.getCitiesByColumnValue("no_accents", "ba", pageable);
    
    Assertions.assertNotNull(page);
    Assertions.assertTrue(page.getSize() <= 15);
    Assertions.assertTrue(page.getContent().stream()
      .map(CidadeModel::getNoAccents)
      .map(String::toLowerCase)
      .allMatch(n -> n.contains("ba")));
  }

  @Test
  @DisplayName("getCitiesByColumnValue::altbanes")
  public void getCitiesByColumnValueAltNames() {
    var page = cidadeService.getCitiesByColumnValue("alternative_names", "ba", pageable);
    
    Assertions.assertNotNull(page);
    log.info("\n\nPAge Size {}\n\n", page.getSize());
    Assertions.assertTrue(page.getContent().size() == 0);
    // Assertions.assertTrue(page.getContent().stream()
    //   .map(CidadeModel::getAltNames)
    //   .map(String::toLowerCase)
    //   .allMatch(n -> n.contains("ba")));
  }

  @Test
  @DisplayName("getCitiesByColumnValue::micro")
  public void getCitiesByColumnValueMicroregion() {
    var page = cidadeService.getCitiesByColumnValue("microregion", "ba", pageable);
    
    Assertions.assertNotNull(page);
    Assertions.assertTrue(page.getSize() <= 15);
    Assertions.assertTrue(page.getContent().stream()
      .map(CidadeModel::getMicroregion)
      .map(String::toLowerCase)
      .allMatch(n -> n.contains("ba")));
  }

  @Test
  @DisplayName("getCitiesByColumnValue::meso")
  public void getCitiesByColumnValueMesoregion() {
    var page = cidadeService.getCitiesByColumnValue("mesoregion", "ba", pageable);
    
    Assertions.assertNotNull(page);
    Assertions.assertTrue(page.getSize() <= 15);
    Assertions.assertTrue(page.getContent().stream()
      .map(CidadeModel::getMesoregion)
      .map(String::toLowerCase)
      .allMatch(n -> n.contains("ba")));
  }

  /**
   * Count column value
   */

  @Test
  @DisplayName("countCitiesByColumnValue::uf")
  public void countCitiesByColumnValueUf() {
    var count = cidadeService.getNumberOfCitiesByColumnValue("uf", "sp");
    Assertions.assertNotNull(count);
    Assertions.assertTrue(count > 0);
  }

  @Test
  @DisplayName("countCitiesByColumnValue::name")
  public void countCitiesByColumnValueName() {
    var count = cidadeService.getNumberOfCitiesByColumnValue("name", "sp");
    Assertions.assertNotNull(count);
    Assertions.assertTrue(count > 0);
  }

  @Test
  @DisplayName("countCitiesByColumnValue::capital")
  public void countCitiesByColumnValueCapital() {
    var count = cidadeService.getNumberOfCitiesByColumnValue("capital", "true");
    Assertions.assertNotNull(count);
    Assertions.assertTrue(count > 0);
  }

  @Test
  @DisplayName("countCitiesByColumnValue::lon")
  public void countCitiesByColumnValueLongitude() {
    var count = cidadeService.getNumberOfCitiesByColumnValue("lon", "32");
    Assertions.assertNotNull(count);
    Assertions.assertTrue(count > 0);
  }

  @Test
  @DisplayName("countCitiesByColumnValue::lat")
  public void countCitiesByColumnValueLatitude() {
    var count = cidadeService.getNumberOfCitiesByColumnValue("lat", "33");
    Assertions.assertNotNull(count);
    Assertions.assertTrue(count > 0);
  }

  @Test
  @DisplayName("countCitiesByColumnValue::noaccents")
  public void countCitiesByColumnValueNoAccents() {
    var count = cidadeService.getNumberOfCitiesByColumnValue("no_accents", "ba");
    Assertions.assertNotNull(count);
    Assertions.assertTrue(count > 0);
  }

  @Test
  @DisplayName("countCitiesByColumnValue::altbanes")
  public void countCitiesByColumnValueAltNames() {
    var count = cidadeService.getNumberOfCitiesByColumnValue("alternative_names", "ba");
    Assertions.assertNotNull(count);
    Assertions.assertTrue(count == 0);
  }

  @Test
  @DisplayName("countCitiesByColumnValue::micro")
  public void countCitiesByColumnValueMicroregion() {
    var count = cidadeService.getNumberOfCitiesByColumnValue("microregion", "ba");
    Assertions.assertNotNull(count);
    Assertions.assertTrue(count > 0);
  }

  @Test
  @DisplayName("countCitiesByColumnValue::meso")
  public void countCitiesByColumnValueMesoregion() {
    var count = cidadeService.getNumberOfCitiesByColumnValue("mesoregion", "ba");
    Assertions.assertNotNull(count);
    Assertions.assertTrue(count > 0);
  }


}
