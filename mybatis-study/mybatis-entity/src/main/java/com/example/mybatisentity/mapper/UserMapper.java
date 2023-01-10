package com.example.mybatisentity.mapper;

import com.example.mybatisentity.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {

    User selectOneUser(String username);

}
