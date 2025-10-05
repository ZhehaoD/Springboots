package com.example.springboots.mapper;

import com.example.springboots.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO \"user\" (username, password, name, phone, email, address, avatar, role) " +
            "VALUES (#{username}, #{password}, #{name}, #{phone}, #{email}, #{address}, #{avatar}, #{role})")
    void insert(User user);

    @Update("update \"user\" set username = #{username}, password = #{password}, name= #{name}, phone =#{phone}," +
            " email =#{email}, address=#{address}, avatar=#{avatar} where id=#{id}")
    void updateUser(User user);

    @Delete("delete from \"user\" where id=#{id}")
    void deleteUser(Integer id);

    @Delete("delete from \"user\" where id in ()")
    void batchDeleteUser(List<Integer> ids);

    @Select("select * from \"user\" order by id desc")
    List<User> selectAll();

    @Select("select * from \"user\" where id =#{id} order by id desc")
    User selectByID(Integer id);

    @Select("select * from \"user\" where name =#{name} order by id desc")
    List<User> selectByName(String name);

    @Select("select * from \"user\" where name =#{name} and username=#{username} order by id desc")
    List<User> selectByMore(String username, String name);

    @Select("SELECT * FROM \"user\" " +
            "WHERE name LIKE CONCAT('%', #{name}, '%') " +
            "  AND username LIKE CONCAT('%', #{username}, '%') " +
            "ORDER BY id DESC")
    List<User> selectByMo(String username, String name);

    @Select("SELECT * FROM \"user\" " +
            "WHERE (name LIKE CONCAT('%', #{name}, '%') OR #{name} IS NULL) " +
            "  AND (username LIKE CONCAT('%', #{username}, '%') OR #{username} IS NULL) " +
            "ORDER BY id DESC " +
            "LIMIT #{pageSize} OFFSET #{skipNum}")
    List<User> selectByPage(@Param("skipNum") Integer skipNum, @Param("pageSize") Integer pageSize,@Param("username") String username, @Param("name") String name);

    @Select("SELECT count(id) FROM \"user\" " +
            "WHERE (name LIKE '%' || #{name} || '%' OR #{name} IS NULL) " +
            "  AND (username LIKE '%' || #{username} || '%' OR #{username} IS NULL)")
    int selectCountByPage(@Param("username") String username, @Param("name") String name);

    @Select("select * from \"user\" where username =#{username} order by id desc")
    User selectByUsername(String username);
}

