package com.hroniko.stats.search.restentity.getresponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hroniko.stats.entity.OneLog;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Hit {

    private String _index;
    private String _type;
    private String _id;
    private Double _score;
    private OneLog _source;

    public String get_index() {
        return _index;
    }

    public void set_index(String _index) {
        this._index = _index;
    }

    public String get_type() {
        return _type;
    }

    public void set_type(String _type) {
        this._type = _type;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Double get_score() {
        return _score;
    }

    public void set_score(Double _score) {
        this._score = _score;
    }

    public OneLog get_source() {
        return _source;
    }

    public void set_source(OneLog _source) {
        this._source = _source;
    }
}
