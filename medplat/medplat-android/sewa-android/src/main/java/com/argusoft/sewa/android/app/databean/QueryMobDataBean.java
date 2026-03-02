package com.argusoft.sewa.android.app.databean;

import androidx.annotation.NonNull;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by prateek on 18/5/18.
 */

public class QueryMobDataBean {

    private String code;

    private List<LinkedHashMap<String, Object>> result;

    private Map<String, Object> parameters;

    private Integer sequence;

    public QueryMobDataBean() {
    }

    public QueryMobDataBean(String code, List<LinkedHashMap<String, Object>> result, Map<String, Object> parameters, Integer sequence) {
        this.code = code;
        this.result = result;
        this.parameters = parameters;
        this.sequence = sequence;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<LinkedHashMap<String, Object>> getResult() {
        return result;
    }

    public void setResult(List<LinkedHashMap<String, Object>> result) {
        this.result = result;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    @NonNull
    @Override
    public String toString() {
        return "QueryMobDataBean{" +
                "code='" + code + '\'' +
                ", result=" + result +
                ", parameters=" + parameters +
                ", sequence=" + sequence +
                '}';
    }
}
