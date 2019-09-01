package com.dapeng.flow.common.codelist;

import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlRootElement(
        name = "codelist-config"
)
public class CodeListConfig {
    private List<CodeList> codeList = new ArrayList<>();
    private Map<String, CodeList> codeListMap = new HashMap<>();

    @XmlElement(
            name = "codelist"
    )
    public List<CodeList> getCodeList() {
        return this.codeList;
    }

    public CodeList getCodeList(String id) {
        if (StringUtils.isEmpty(id)) {
            throw new IllegalArgumentException("[Assertion failed] - this argument is required; it must not be blank");
        }
        return (CodeList)this.codeListMap.get(id);
    }

    public void setCodeList(List<CodeList> codeList) {
        this.codeList = codeList;
    }

    public void build() {

        for (CodeList wkCodeList : this.codeList) {
            this.codeListMap.put(wkCodeList.getId(), wkCodeList);
        }

    }
}

