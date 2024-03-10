package edu.java.persitence.common.repository;

import edu.java.persitence.common.dto.Link;
import java.util.List;

public interface LinkRepository {

    List<Link> findAll();

    Long add(Link link);

    Long remove(long linkId);

    Link findById(long linkId);

    Link findByUrl(String url);
}
