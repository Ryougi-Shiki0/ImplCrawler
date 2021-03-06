package com.myproject.multifunctioncrawler.dao;

import com.myproject.multifunctioncrawler.pojo.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserDao {
    @Select("select * from myuser where id = #{id}")
    public User getById(@Param("id")long id);

    @Update("update myuser set password = #{password} where id = #{id}")
    public void update(User toBeUpdate);
}
