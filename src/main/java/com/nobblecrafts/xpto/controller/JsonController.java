package com.nobblecrafts.xpto.controller;

import java.util.Arrays;
import java.util.List;

import com.nobblecrafts.xpto.model.CidadeModel;
import com.nobblecrafts.xpto.model.CidadesPorEstadoModel;
import com.nobblecrafts.xpto.model.DistantCityModel;
import com.nobblecrafts.xpto.model.PostResponse;
import com.nobblecrafts.xpto.service.CidadeService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class JsonController {

  private final CidadeService service;

  /**
   * 2. Retornar somente as cidades que são capitais ordenadas por nome;
   */
  @GetMapping("capitals")
  public ResponseEntity<Page<CidadeModel>> getCapitals(Pageable pageable) {
    var output = service.getCapitals(pageable);
    return new ResponseEntity<>(output, HttpStatus.OK);
  }

  /**
   * 3. Retornar o nome do estado com a maior e menor quantidade de cidades e a
   * quantidade de cidades;
   */
  @GetMapping("states-more-less-cities")
  public ResponseEntity<List<CidadesPorEstadoModel>> getStatesWithMoreAndLessCities() {
    var more = service.getStateWithLargestNumberOfCities();
    var less = service.getStateWithLessCities();

    return new ResponseEntity<>(Arrays.asList(more, less), HttpStatus.OK);
  }

  /**
   * 4. Retornar a quantidade de cidades por estado;
   */

  @GetMapping("quantity-cities-per-state")
  public ResponseEntity<CidadesPorEstadoModel> getQuantityOfCitiesPerState(@RequestParam("state") String state) {
    var output = service.getNumberOfCitiesPerState(state);
    // log.info("output {}", output.getCidades(), output.getEstado());
    return new ResponseEntity<>(output, HttpStatus.OK);
  }

  /**
   * 5. Obter os dados da cidade informando o id do IBGE;
   */
  @GetMapping("search-city-by-id")
  public ResponseEntity<CidadeModel> getCityById(@RequestParam("ibgeId") Long ibgeId) {
    var output = service.getCityById(ibgeId);
    return new ResponseEntity<>(output, HttpStatus.OK);
  }

  /**
   * 6. Retornar o nome das cidades baseado em um estado selecionado;
   */
  @GetMapping("get-cities-by-state")
  public ResponseEntity<Page<CidadeModel>> getCityByState(@RequestParam("state") String state, Pageable pageable) {
    var output = service.getCitiesByState(state, pageable);
    return new ResponseEntity<>(output, HttpStatus.OK);
  }

  /**
   * 7. Permitir adicionar uma nova Cidade;
   */
  @PostMapping("add-city")
  public ResponseEntity<CidadeModel> addCity(@RequestBody CidadeModel model) {
    var output = service.addCity(model);
    return new ResponseEntity<>(output, HttpStatus.CREATED);
  }

  /**
   * 8. Permitir deletar uma cidade;
   */
  @DeleteMapping("delete-city")
  public ResponseEntity<PostResponse> deleteCity(@RequestParam Long ibgeId) {
    service.deleteCity(ibgeId);
    return new ResponseEntity<>(PostResponse.builder().message("Cidade Deletada").build(), HttpStatus.NO_CONTENT);
  }

  /**
   * 9. Permitir selecionar uma coluna (do CSV) e através dela entrar com uma
   * string para filtrar. retornar assim todos os objetos que contenham tal
   * string;
   */
  @GetMapping("filter-by-column")
  public ResponseEntity<Page<CidadeModel>> filterByColumn(@RequestParam("column") String column,
      @RequestParam("keyword") String keyword, Pageable pageable) {
    var output = service.getCitiesByColumnValue(column, keyword, pageable);
    return new ResponseEntity<>(output, HttpStatus.OK);
  }

  /**
   * 10. Retornar a quantidade de registro baseado em uma coluna. Não deve contar
   * itens iguais;
   */
  @GetMapping("get-records-by-column")
  public ResponseEntity<Long> getRecordsByColumn(@RequestParam("column") String column,
      @RequestParam("keyword") String keyword) {
    var output = service.getNumberOfCitiesByColumnValue(column, keyword);
    return new ResponseEntity<>(output, HttpStatus.OK);
  }

  /**
   * 11. Retornar a quantidade de registros total;
   */
  @GetMapping("get-total-of-records")
  public ResponseEntity<Long> getTotalOfRecords() {
    var total = service.getNumberOfCities();
    return new ResponseEntity<>(total, HttpStatus.OK);
  }

  /**
   * 12. Dentre todas as cidades, obter as duas cidades mais distantes uma da
   * outra com base na localização (distância em KM em linha reta);
   * 
   */
  @GetMapping("most-distant-cities")
  public ResponseEntity<List<DistantCityModel>> getMostDistantCities() {
    var output = service.getMostDistantCities();
    return new ResponseEntity<>(output, HttpStatus.OK);
  }

}
