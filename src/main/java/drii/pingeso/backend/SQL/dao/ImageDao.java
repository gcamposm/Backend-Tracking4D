package drii.pingeso.backend.SQL.dao;

import drii.pingeso.backend.SQL.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageDao extends JpaRepository<Image, Integer> {
}
