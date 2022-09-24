package com.efp.dvk.common.orm.entity;

import com.efp.dvk.common.lang.annation.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PluginDialogConf {
    @Id
    private String confKey;
    private String confValue;
}
