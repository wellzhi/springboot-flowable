package com.dapeng.flow.common.codelist;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.dapeng.flow.common.exception.UnexpectedException;

import javax.servlet.ServletContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeListManager {
    private final Logger log = LoggerFactory.getLogger(CodeListManager.class);
    public static final String CODELIST_KEY = "codelist";
    public static final String OPTIONS_KEY = "codelistOptions";
    private CodeListConfig codeListConfig = null;
    private static CodeListManager instance = new CodeListManager();

    public static CodeListManager getInstance() {
        return instance;
    }

    private CodeListManager() {
        this.codeListConfig = this.loadCodeListConfig();

    }

    public CodeList getCodeList(String id) {
        if (StringUtils.isEmpty(id)) {
            throw new IllegalArgumentException("[Assertion failed] - this argument is required; it must not be blank");
        }
        return this.codeListConfig.getCodeList(id);
    }

    public CodeListConfig getCodeListManager() {
        return this.codeListConfig;
    }

    public CodeListConfig reloadCodeListConfig(InputStream defineInput) {
        CodeListConfig codeListConfig = null;

        try {
            JAXBContext jc = JAXBContext.newInstance(CodeListConfig.class);
            Unmarshaller u = jc.createUnmarshaller();
            codeListConfig = (CodeListConfig)u.unmarshal(defineInput);
            codeListConfig.build();
            return codeListConfig;
        } catch (JAXBException var5) {
            throw new UnexpectedException("Occur error when load code list.", var5);
        }
    }

    private CodeListConfig loadCodeListConfig() {
        InputStream is = CodeListManager.class.getClassLoader().getResourceAsStream("codelist.xml");
        return is != null ? this.reloadCodeListConfig(is) : null;
    }

    public void loadToServletConext(ServletContext context) {
        if (context == null) {
            throw new IllegalArgumentException("ServletConext can't be null.");
        }
        this.log.info("Loading CodeList to ServletConext ...");
        if (this.codeListConfig != null) {
            Map<String, List<Option>> optionListMap = new HashMap<>();
            Map<String, Map<String, Option>> optionValueMap = new HashMap<>();

            for (CodeList wkCodeList : this.codeListConfig.getCodeList()) {
                this.log.debug("#loadCodeListToServletConext CodeList:" + wkCodeList.getId());
                optionListMap.put(wkCodeList.getId(), wkCodeList.getOption());
                Map<String, Option> optionsMap = new HashMap<>();

                for (Option option : wkCodeList.getOption()) {
                    optionsMap.put(option.getValue(), option);
                }

                optionValueMap.put(wkCodeList.getId(), optionsMap);
            }

            context.setAttribute("codelist", optionListMap);
            context.setAttribute("codelistOptions", optionValueMap);
            this.log.info("Load CodeList to ServletConext completed.");
        }
    }
}
