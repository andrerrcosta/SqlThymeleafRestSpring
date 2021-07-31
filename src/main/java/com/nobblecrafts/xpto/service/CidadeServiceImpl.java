package com.nobblecrafts.xpto.service;

import java.util.List;

import javax.transaction.Transactional;

import com.nobblecrafts.xpto.exception.BadRequestException;
import com.nobblecrafts.xpto.model.CidadeModel;
import com.nobblecrafts.xpto.model.CidadesPorEstadoModel;
import com.nobblecrafts.xpto.model.DistantCityModel;
import com.nobblecrafts.xpto.repository.CidadeRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CidadeServiceImpl implements CidadeService {

  private final CidadeRepository repository;

  @Override
  public Page<CidadeModel> getAllCidades(Pageable pageable) {
    return repository.findAll(pageable);
  }

  @Override
  public CidadeModel updateCidade(CidadeModel cidade) {
    var model = repository.findById(cidade.getIbgeId())
        .orElseThrow(() -> new BadRequestException("Cidade não encontrada"));
    return repository.save(cidade.withIbgeId(model.getIbgeId()));
  }

  @Override
  public Page<CidadeModel> getCapitals(Pageable pageable) {
    return repository.findByCapitalOrderByName(true, pageable);
  }

  @Override
  public CidadesPorEstadoModel getStateWithLargestNumberOfCities() {
    return repository.getStateWithLargestNumberOfCities();
  }

  @Override
  public CidadesPorEstadoModel getStateWithLessCities() {
    return repository.getStateWithLessCities();
  }

  @Override
  public Long getNumberOfCities() {
    return repository.count();
  }

  @Override
  public CidadesPorEstadoModel getNumberOfCitiesPerState(String state) {
    return repository.getCidadesPorEstado(state);
  }

  @Override
  public CidadeModel getCityById(Long ibgeId) {
    return repository.findById(ibgeId).orElseThrow(() -> new BadRequestException("Cidade não encontrada"));
  }

  @Override
  public Page<CidadeModel> getCitiesByState(String state, Pageable pageable) {
    return repository.findByUf(state, pageable);
  }

  @Override
  public CidadeModel addCity(CidadeModel cidade) {
    return repository.save(cidade);
  }

  @Override
  public void deleteCity(Long ibgeId) {
    repository.findById(ibgeId).orElseThrow(() -> new BadRequestException("Cidade não encontrada"));
    repository.deleteById(ibgeId);
  }

  @Override
  public Page<CidadeModel> getCitiesByColumnValue(String column, String value, Pageable pageable) {
    switch (column) {
    case "uf": {
      return repository.findByUfContainingIgnoreCase(value, pageable);
    }
    case "name": {
      return repository.findByNameContainingIgnoreCase(value, pageable);
    }
    case "capital": {
      return repository.findByCapital(Boolean.parseBoolean(value), pageable);
    }
    case "lon": {
      return repository.findByLongitudeContaining(value, pageable);
    }
    case "longitude": {
      return repository.findByLongitudeContaining(value, pageable);
    }
    case "lat": {
      return repository.findByLatitudeContaining(value, pageable);
    }
    case "latitude": {
      return repository.findByLatitudeContaining(value, pageable);
    }
    case "no_accents": {
      return repository.findByNoAccentsContainingIgnoreCase(value, pageable);
    }
    case "noAccents": {
      return repository.findByNoAccentsContainingIgnoreCase(value, pageable);
    }
    case "alternative_names": {
      return repository.findByAltNamesContainingIgnoreCase(value, pageable);
    }
    case "altNames": {
      return repository.findByAltNamesContainingIgnoreCase(value, pageable);
    }
    case "microregion": {
      return repository.findByMicroregionContainingIgnoreCase(value, pageable);
    }
    case "mesoregion": {
      return repository.findByMesoregionContainingIgnoreCase(value, pageable);
    }
    default: {
      return repository.queryForAllColumns(value, pageable);
    }
    }
  }

  @Override
  public Long getNumberOfCitiesByColumnValue(String column, String value) {
    switch (column) {
    case "uf":
      return repository.countByUfContainingIgnoreCase(value);

    case "name":
      return repository.countByNameContainingIgnoreCase(value);

    case "capital":
      return repository.countByCapital(Boolean.parseBoolean(value));

    case "lon":
      return repository.countByLongitudeContaining(value);

    case "longitude":
      return repository.countByLongitudeContaining(value);

    case "lat":
      return repository.countByLatitudeContaining(value);

    case "latitude":
      return repository.countByLatitudeContaining(value);

    case "no_accents":
      return repository.countByNoAccentsContainingIgnoreCase(value);

    case "noAccents":
      return repository.countByNoAccentsContainingIgnoreCase(value);

    case "alternative_names":
      return repository.countByAltNamesContainingIgnoreCase(value);

    case "altNames":
      return repository.countByAltNamesContainingIgnoreCase(value);

    case "microregion":
      return repository.countByMicroregionContainingIgnoreCase(value);

    case "mesoregion":
      return repository.countByMesoregionContainingIgnoreCase(value);

    default:
      return repository.countForAllColumns(value);
    }
  }

  @Override
  public Long getTotalOfRecords() {
    return repository.count();
  }

  @Override
  public List<DistantCityModel> getMostDistantCities() {
    return repository.heavyMetalQueryForMostDistantCities();
  }

  @Override
  public void saveAllCities(List<CidadeModel> cidades) {
    repository.saveAll(cidades);
  }

}
