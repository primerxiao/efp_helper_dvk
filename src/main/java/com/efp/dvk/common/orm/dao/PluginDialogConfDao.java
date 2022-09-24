package com.efp.dvk.common.orm.dao;

import com.efp.dvk.common.orm.entity.PluginDialogConf;
import org.fastsql.annotation.Param;
import org.fastsql.annotation.Select;

import java.util.List;

public interface PluginDialogConfDao {

    @Select("select * from plugin_dialog_conf where conf_key=#{confId}")
    PluginDialogConf findOne(@Param("confId") String confId);

    @Select("select * from user")
    List<PluginDialogConf> selectAll();

}
