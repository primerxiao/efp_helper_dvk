package com.efp.dvk.plugins.generator.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class GeneratorConfig implements Serializable {
    private String name;
    private String type;
    private String fileName;
    private String filePath;
    private String templateContent;
}
