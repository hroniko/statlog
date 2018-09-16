package com.hroniko.stats.search.restentity;

import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PutResponse {
    private String _index;
    private String _type;
    private String _id;
    private Integer _version;
    private Map<String, Integer> _shards;
    private Boolean created;

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

    public Integer get_version() {
        return _version;
    }

    public void set_version(Integer _version) {
        this._version = _version;
    }

    public Map<String, Integer> get_shards() {
        return _shards;
    }

    public void set_shards(Map<String, Integer> _shards) {
        this._shards = _shards;
    }

    public Boolean getCreated() {
        return created;
    }

    public void setCreated(Boolean created) {
        this.created = created;
    }
}
