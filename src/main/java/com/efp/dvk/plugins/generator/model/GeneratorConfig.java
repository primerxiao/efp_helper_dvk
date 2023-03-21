package com.efp.dvk.plugins.generator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneratorConfig implements Serializable {
    private String name;
    private String type;
    private String fileName;
    private String filePath;
    private String templateContent;
}
