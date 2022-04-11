package com.es.core.dao.iosu;

import com.es.core.model.stat.Stat;
import com.es.core.model.stat.UnionStat;

import java.time.LocalDateTime;
import java.util.List;

public interface IosuDao {

    void createTableForDate(LocalDateTime date);

    List<Stat> getStat();

    List<UnionStat> getUnionStat(Long brandId, Long typeId);
}
