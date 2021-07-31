package com.nobblecrafts.xpto.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Entity
@Data
@Builder
@Table(name = "Cidade")
@NoArgsConstructor
@AllArgsConstructor
@With
public class CidadeModel {

  @Id
  // @GeneratedValue(strategy=GenerationType.TABLE)
  @Column(name = "ibge_id")
  Long ibgeId;

  @Column(name = "uf")
  String uf;

  @Column(name = "name")
  String name;

  @Column(name = "capital")
  Boolean capital;

  @Column(name = "lon")
  Double longitude;

  @Column(name = "lat")
  Double latitude;

  @Column(name = "no_accents")
  String noAccents;

  @Column(name = "alternative_names")
  String altNames;

  @Column(name = "microregion")
  String microregion;

  @Column(name = "mesoregion")
  String mesoregion;
  
}
