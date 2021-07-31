package com.nobblecrafts.xpto.service;

import java.util.List;

import com.nobblecrafts.xpto.model.CidadeModel;

import org.springframework.stereotype.Service;

@Service
public interface DataService {

  List<CidadeModel> readFile(String path);
  
}
