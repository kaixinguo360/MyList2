package com.my.list.type.video;

import com.my.list.type.ExtraMapper;
import com.my.list.type.text.Text;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface VideoMapper extends ExtraMapper {
    @Select("select * from videos where video_node_id = #{id,jdbcType=BIGINT}")
    @ResultMap("BaseResultMap")
    Text selectByNodeId(Long id);
}
