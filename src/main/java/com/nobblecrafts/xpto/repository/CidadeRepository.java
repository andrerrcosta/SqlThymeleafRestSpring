package com.nobblecrafts.xpto.repository;

import java.util.List;

import com.nobblecrafts.xpto.model.CidadeModel;
import com.nobblecrafts.xpto.model.CidadesPorEstadoModel;
import com.nobblecrafts.xpto.model.DistantCityModel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CidadeRepository extends PagingAndSortingRepository<CidadeModel, Long> {

  Page<CidadeModel> findAll(Pageable pageable);

  Page<CidadeModel> findByUf(String uf, Pageable pageable);

  Page<CidadeModel> findByName(String uf, Pageable pageable);

  Page<CidadeModel> findByCapital(Boolean capital, Pageable pageable);

  Page<CidadeModel> findByCapitalOrderByName(Boolean capital, Pageable pageable);

  Page<CidadeModel> findByLongitude(Double longitude, Pageable pageable);

  Page<CidadeModel> findByLatitude(Double latitude, Pageable pageable);

  Page<CidadeModel> findByNoAccents(String noAccents, Pageable pageable);

  Page<CidadeModel> findByAltNames(String altNames, Pageable pageable);

  Page<CidadeModel> findByMicroregion(String microregion, Pageable pageable);

  Page<CidadeModel> findByMesoregion(String mesoregion, Pageable pageable);

  // LIKE

  Page<CidadeModel> findByUfContainingIgnoreCase(String uf, Pageable pageable);

  Page<CidadeModel> findByNameContainingIgnoreCase(String uf, Pageable pageable);

  @Query("FROM CidadeModel c WHERE CAST(c.longitude AS string) LIKE %:longitude%")
  Page<CidadeModel> findByLongitudeContaining(@Param("longitude") String longitude, Pageable pageable);

  @Query("FROM CidadeModel c WHERE CAST(c.latitude AS string) LIKE %:latitude%")
  Page<CidadeModel> findByLatitudeContaining(@Param("latitude") String latitude, Pageable pageable);

  Page<CidadeModel> findByNoAccentsContainingIgnoreCase(String noAccents, Pageable pageable);

  Page<CidadeModel> findByAltNamesContainingIgnoreCase(String altNames, Pageable pageable);

  Page<CidadeModel> findByMicroregionContainingIgnoreCase(String microregion, Pageable pageable);

  Page<CidadeModel> findByMesoregionContainingIgnoreCase(String mesoregion, Pageable pageable);

  // Count

  Long countByUfContainingIgnoreCase(String uf);

  Long countByNameContainingIgnoreCase(String uf);

  Long countByCapital(Boolean capital);

  @Query("SELECT count(*) FROM CidadeModel c WHERE CAST(c.longitude AS string) LIKE %:longitude%")
  Long countByLongitudeContaining(@Param("longitude") String longitude);

  @Query("SELECT count(*) FROM CidadeModel c WHERE CAST(c.latitude AS string) LIKE %:latitude%")
  Long countByLatitudeContaining(@Param("latitude") String latitude);

  Long countByNoAccentsContainingIgnoreCase(String noAccents);

  Long countByAltNamesContainingIgnoreCase(String altNames);

  Long countByMicroregionContainingIgnoreCase(String microregion);

  Long countByMesoregionContainingIgnoreCase(String mesoregion);

  @Query(value = "SELECT uf as estado, count(uf) AS cidades FROM Cidade GROUP BY uf ORDER BY cidades DESC LIMIT 1", nativeQuery = true)
  CidadesPorEstadoModel getStateWithLargestNumberOfCities();

  @Query(value = "SELECT uf as estado, count(uf) AS cidades FROM Cidade GROUP BY uf ORDER BY cidades ASC LIMIT 1", nativeQuery = true)
  CidadesPorEstadoModel getStateWithLessCities();

  @Query(value = "select uf as estado, count(uf) as cidades from Cidade where uf = ?1 group by uf", nativeQuery = true)
  CidadesPorEstadoModel getCidadesPorEstado(String estado);

  @Query(value = "select c1.lat as latitude, c1.lon as longitude, c1.name as cidade, SQRT(POWER((c1.lat - c2.lat), 2) + POWER((c1.lon - c2.lon), 2)) as eq from cidade c1 inner join cidade c2 order by eq DESC LIMIT 2", nativeQuery = true)
  List<DistantCityModel> heavyMetalQueryForMostDistantCities();

  @Query(value = "select * from Cidade where ", nativeQuery = true)
  Page<CidadeModel> queryForAllColumns(String value, Pageable pageable);

  @Query(value = "select count(*) from Cidade where ", nativeQuery = true)
  Long countForAllColumns(String value);

}