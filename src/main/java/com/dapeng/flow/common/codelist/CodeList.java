package com.dapeng.flow.common.codelist;

import javax.xml.bind.annotation.XmlAttribute;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CodeList implements Serializable {
    private static final long serialVersionUID = -4166029838191656047L;
    private String id;
    private String name;
    private String comment;
    private List<Option> option = new ArrayList<>();

    @XmlAttribute
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlAttribute
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute
    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<Option> getOption() {
        return this.option;
    }

    public Option getOption(String value) {
        if (value != null) {

            for (Option wkOption : this.option) {
                if (value.equals(wkOption.getValue())) {
                    return wkOption;
                }
            }
        }

        return null;
    }

    public void setOption(List<Option> option) {
        this.option = option;
    }

    @Override
    public String toString() {
        return "CodeList{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                ", option=" + option +
                '}';
    }
}
