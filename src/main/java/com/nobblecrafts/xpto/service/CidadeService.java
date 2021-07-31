package com.nobblecrafts.xpto.service;

import java.util.List;

import com.nobblecrafts.xpto.model.CidadeModel;
import com.nobblecrafts.xpto.model.CidadesPorEstadoModel;
import com.nobblecrafts.xpto.model.DistantCityModel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CidadeService {

  Page<CidadeModel> getAllCidades(Pageable page);

  CidadeModel updateCidade(CidadeModel cidade);

  Page<CidadeModel> getCapitals(Pageable pageable);

  CidadesPorEstadoModel getStateWithLargestNumberOfCities();

  CidadesPorEstadoModel getStateWithLessCities();

  Long getNumberOfCities();

  CidadesPorEstadoModel getNumberOfCitiesPerState(String state);

  CidadeModel getCityById(Long ibgeId);

  Page<CidadeModel> getCitiesByState(String state, Pageable pageable);

  CidadeModel addCity(CidadeModel cidade);

  void deleteCity(Long ibgeId);

  Page<CidadeModel> getCitiesByColumnValue(String column, String value, Pageable pageable);

  Long getNumberOfCitiesByColumnValue(String column, String value);

  Long getTotalOfRecords();

  List<DistantCityModel> getMostDistantCities();

  void saveAllCities(List<CidadeModel> cidades);

}
