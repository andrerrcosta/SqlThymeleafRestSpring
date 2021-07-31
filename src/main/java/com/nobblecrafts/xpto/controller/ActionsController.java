package com.nobblecrafts.xpto.controller;

import com.nobblecrafts.xpto.service.CidadeService;
import com.nobblecrafts.xpto.service.DataService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("actions")
@RequiredArgsConstructor
public class ActionsController {

  private final CidadeService cidadeService;
  private final DataService dataService;

  @PostMapping("read-csv")
  public String readCsv(@Value("${resource}") final String path) {
    var cidades = dataService.readFile(path);
    cidadeService.saveAllCities(cidades);
    return "redirect:/cidades";
  }

}
