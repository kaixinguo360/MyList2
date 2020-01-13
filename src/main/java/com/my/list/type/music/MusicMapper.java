package com.my.list.type.music;

import com.my.list.type.ExtraMapper;
import com.my.list.type.image.Image;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MusicMapper extends ExtraMapper {
    @Select("select * from musics where music_node_id = #{id,jdbcType=BIGINT}")
    @ResultMap("BaseResultMap")
    Image selectByNodeId(Long id);
}
