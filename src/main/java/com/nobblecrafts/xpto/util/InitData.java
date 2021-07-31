package com.nobblecrafts.xpto.util;

import com.nobblecrafts.xpto.repository.CidadeRepository;
import com.nobblecrafts.xpto.service.CidadeService;
import com.nobblecrafts.xpto.service.DataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
// @Scope("test")
@Slf4j
public class InitData implements CommandLineRunner {

  @Autowired
  private DataService service;

  @Autowired
  private CidadeRepository repository;

  @Autowired
  private CidadeService cidadeService;

  @Value("${resource}")
  private String path;

  @Override
  public void run(String... args) throws Exception {
    // var cidades = service.readFile(path);
    // repository.saveAll(cidades);
    // // var recover = repository.findAll();
    // // log.info("Recovery {}", recover);
    // var state = repository.getStateWithLargestNumberOfCities();
    // log.info("State {}", state);
  }

}
