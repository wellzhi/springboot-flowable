package com.dapeng.flow.common.codelist;

import javax.xml.bind.annotation.XmlAttribute;
import java.io.Serializable;

/**
 * @author Administrator
 */
public class Option implements Serializable {
    private static final long serialVersionUID = 3897195064039282229L;
    private Integer index;
    private String value;
    private String name;

    @XmlAttribute
    public Integer getIndex() {
        return this.index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    @XmlAttribute
    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Option{" +
                "index=" + index +
                ", value='" + value + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
