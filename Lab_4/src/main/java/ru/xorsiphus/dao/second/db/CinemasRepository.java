package ru.xorsiphus.dao.second.db;

import org.springframework.stereotype.Repository;
import ru.xorsiphus.entity.Cinemas;


@Repository("cinemasRepository")
public interface CinemasRepository extends BaseRepository<Cinemas>
{

}