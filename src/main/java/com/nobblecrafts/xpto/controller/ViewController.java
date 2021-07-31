package com.nobblecrafts.xpto.controller;

import java.util.Optional;
import java.util.stream.IntStream;

import com.nobblecrafts.xpto.model.CidadeModel;
import com.nobblecrafts.xpto.model.CidadesPorEstadoModel;
import com.nobblecrafts.xpto.model.PostResponse;
import com.nobblecrafts.xpto.service.CidadeService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
@Slf4j
public class ViewController {

  private final CidadeService cidadeService;

  @GetMapping
  public String defaultIndex() {
    return "redirect:/cidades";
  }

  @GetMapping("add-city")
  public String getCidades(Model model) {
    var cidade = new CidadeModel();
    model.addAttribute("cidade", cidade);
    return "adicionar";
  }

  @PostMapping("add-city")
  public String addCity(CidadeModel cidade, Model model) {
    var output = cidadeService.addCity(cidade);
    model.addAttribute("cidade", output);
    model.addAttribute("postResponse", PostResponse.builder().message("Cidade Adicionada com Sucesso").build());
    return "adicionar";

  }

  @GetMapping({ "capitals/{page}", "capitals" })
  public String getCapitals(@PathVariable(name = "page", required = false) Optional<Integer> page, Model model,
      @Param("sortField") String sortField, @Param("sortDir") String sortDir) {
    Pageable pageable = null;

    if (StringUtils.hasText(sortField))
      pageable = PageRequest.of(page.isPresent() ? page.get() - 1 : 0, 20, Sort.by(sortField));
    else
      pageable = PageRequest.of(page.isPresent() ? page.get() - 1 : 0, 20);

    var output = cidadeService.getCapitals(pageable);
    for (CidadeModel cidade : output.getContent()) {
      log.info("Capital {}", cidade);
    }

    model.addAttribute("totalOfPages", output.getTotalPages());
    model.addAttribute("capitalsPage", output.getContent());
    model.addAttribute("pages", IntStream.rangeClosed(1, output.getTotalPages()).toArray());
    model.addAttribute("currentPage", page);

    return "capitals";
  }

  @GetMapping("states-more-less-cities")
  public String getStatesWithMoreAndLessCities(Model model) {
    var more = cidadeService.getStateWithLargestNumberOfCities();
    var less = cidadeService.getStateWithLessCities();
    model.addAttribute("more", more);
    model.addAttribute("less", less);
    return "moreless";
  }

  @GetMapping({ "cidades/{page}", "cidades" })
  public String getAllCities(@PathVariable(name = "page", required = false) Optional<Integer> page, Model model,
      @Param("sortField") String sortField, @Param("sortDir") String sortDir) {
    Pageable pageable = null;

    if (StringUtils.hasText(sortField))
      pageable = PageRequest.of(page.isPresent() ? page.get() - 1 : 0, 20, Sort.by(sortField));
    else
      pageable = PageRequest.of(page.isPresent() ? page.get() - 1 : 0, 20);

    Page<CidadeModel> output = cidadeService.getAllCidades(pageable);
    model.addAttribute("totalOfPages", output.getTotalPages());
    model.addAttribute("cidadesPage", output.getContent());
    model.addAttribute("pages", IntStream.rangeClosed(1, output.getTotalPages()).toArray());
    model.addAttribute("currentPage", page);

    return "cidades";
  }

  @GetMapping({ "search-cities-by-state/{page}", "search-cities-by-state" })
  public String getSearchByState(@PathVariable(name = "page", required = false) Optional<Integer> page, Model model,
      @Param("state") String state, @Param("sortField") String sortField, @Param("sortDir") String sortDir) {
    Pageable pageable = null;

    if (StringUtils.hasText(state)) {
      if (StringUtils.hasText(sortField))
        pageable = PageRequest.of(page.isPresent() ? page.get() - 1 : 0, 20, Sort.by(sortField));
      else
        pageable = PageRequest.of(page.isPresent() ? page.get() - 1 : 0, 20);

      var output = cidadeService.getCitiesByState(state, pageable);

      model.addAttribute("totalOfPages", output.getTotalPages());
      model.addAttribute("cidadesPage", output.getContent());
      model.addAttribute("pages", IntStream.rangeClosed(1, output.getTotalPages()).toArray());
      model.addAttribute("currentPage", page);
      model.addAttribute("state", state);
    }

    return "searchcitiesbystate";
  }

  @GetMapping("search-city-by-id")
  public String getSearchCityById(Model model, @Param("ibgeId") Long ibgeId) {

    CidadeModel output = null;
    if (ibgeId != null) {
      output = cidadeService.getCityById(ibgeId);
    }

    model.addAttribute("cidade", output);
    model.addAttribute("ibgeId", ibgeId);

    return "searchcitybyid";
  }

  @GetMapping("quantity-cities-per-state")
  public String getQuantityOfCitiesPerState(Model model, @Param("state") String state) {

    CidadesPorEstadoModel output = null;

    if (StringUtils.hasText(state)) {
      output = cidadeService.getNumberOfCitiesPerState(state);
    }

    model.addAttribute("estado", output);
    model.addAttribute("state", state);

    return "quantitycitiesperstate";
  }

  @GetMapping("delete-city")
  public String getDeleteCity() {
    return "deletecity";
  }

  @DeleteMapping("delete-city")
  public String deleteCity(@RequestParam("ibgeId") Long ibgeId, Model model) {
    cidadeService.deleteCity(ibgeId);

    model.addAttribute("deleteResponse", PostResponse.builder().message("Cidade deletada com sucesso").build());
    return "deletecity";
  }

  @GetMapping({ "filter-by-column/{page}", "filter-by-column" })
  public String getFilterByColumn(@PathVariable(name = "page", required = false) Optional<Integer> page, Model model,
      @Param("column") String column, @Param("keyword") String keyword, @Param("sortField") String sortField,
      @Param("sortDir") String sortDir) {

    Pageable pageable = null;

    String[] fields = { "ibgeId", "uf", "name", "capital", "longitude", "latitude", "noAccents", "altNames",
        "microregion", "mesoregion" };

    model.addAttribute("fields", fields);

    log.info("\n\nFilterBy {}, {}\n\n", column, keyword);

    if (StringUtils.hasText(keyword)) {
      if (StringUtils.hasText(sortField))
        pageable = PageRequest.of(page.isPresent() ? page.get() - 1 : 0, 20, Sort.by(sortField));
      else
        pageable = PageRequest.of(page.isPresent() ? page.get() - 1 : 0, 20);

      var output = cidadeService.getCitiesByColumnValue(column, keyword, pageable);

      model.addAttribute("totalOfPages", output.getTotalPages());
      model.addAttribute("cidadesPage", output.getContent());
      model.addAttribute("pages", IntStream.rangeClosed(1, output.getTotalPages()).toArray());
      model.addAttribute("currentPage", page);
      model.addAttribute("keyword", keyword);
    }

    return "filterbycolumn";
  }

  @GetMapping("get-records-by-column")
  public String getRecordsByColumn(@Param("column") String column, @Param("keyword") String keyword, Model model) {

    String[] fields = { "ibgeId", "uf", "name", "capital", "longitude", "latitude", "noAccents", "altNames",
        "microregion", "mesoregion" };

    model.addAttribute("fields", fields);
    Long output = 0L;

    if(StringUtils.hasText(column) && StringUtils.hasText(keyword)) {
      output = cidadeService.getNumberOfCitiesByColumnValue(column, keyword);
    }

    model.addAttribute("records", output);

    return "getrecordsbycolumn";
  }

  @GetMapping("get-total-of-records")
  public String getTotalOfRecords(Model model) {
    var total = cidadeService.getNumberOfCities();
    model.addAttribute("totalOfRecords", total);
    return "totalofrecords";
  }

  @GetMapping("most-distant-cities")
  public String getMostDistantCities(Model model) {
    var output = cidadeService.getMostDistantCities();
    model.addAttribute("cityA", output.get(0));
    model.addAttribute("cityB", output.get(1));
    return "mostdistantcities";
  }

}
