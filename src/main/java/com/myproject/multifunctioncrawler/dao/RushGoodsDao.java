package com.myproject.multifunctioncrawler.dao;

import com.myproject.multifunctioncrawler.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface RushGoodsDao {
    @Select("select * from myuser where id = #{id}")
    public User getById(@Param("id")long id);
}
