package com.nobblecrafts.xpto.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.nobblecrafts.xpto.model.CidadeModel;
import com.nobblecrafts.xpto.util.CidadeMapper;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DataServiceImpl implements DataService {

  private final CidadeMapper mapper = new CidadeMapper();

  /**
   * Depois implemente alguma tolerância a erros de leitura aqui
   */
  @Override
  public List<CidadeModel> readFile(String path) {
    log.info("Reading file from {}", path);
    String line = null;
    List<CidadeModel> cidades = new ArrayList<>();

    try (BufferedReader reader = new BufferedReader(new FileReader(path, StandardCharsets.UTF_8))) {
      reader.readLine();
      while ((line = reader.readLine()) != null) {
        var city = mapper.convertFromCsvToEntity(line);
        cidades.add(city);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return cidades;
  }

}
