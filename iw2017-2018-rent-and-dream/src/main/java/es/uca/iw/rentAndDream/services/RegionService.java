package es.uca.iw.rentAndDream.services;

import java.util.List;

import javax.naming.NameNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uca.iw.rentAndDream.entities.Region;
import es.uca.iw.rentAndDream.repositories.RegionRepository;

@Service
public class RegionService {

	@Autowired
	private RegionRepository repo;

	public Region loadRegionByName(String name) throws NameNotFoundException {

		Region region = repo.findByName(name);
		if (region == null) {
			throw new NameNotFoundException(name);
		}
		return region;
	}

	public Region save(Region region) {
		return repo.save(region);
	}

	public List<Region> findByNameStartsWithIgnoreCase(String name) {
		return repo.findByNameStartsWithIgnoreCase(name);
	}
	
	public Region findOne(Long arg0) {
		return repo.findOne(arg0);
	}

	public void delete(Region arg0) {
		repo.delete(arg0);
	}

	public List<Region> findAll() {
		return repo.findAll();
	}
}