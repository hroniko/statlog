package com.hroniko.stats.search.restentity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hroniko.stats.search.restentity.getresponse.Hits;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetResponse {

    private Long took;
    private Boolean timed_out;
    private Map<String, Long> _shards;
    private Hits hits;

    public Long getTook() {
        return took;
    }

    public void setTook(Long took) {
        this.took = took;
    }

    public Boolean getTimed_out() {
        return timed_out;
    }

    public void setTimed_out(Boolean timed_out) {
        this.timed_out = timed_out;
    }

    public Map<String, Long> get_shards() {
        return _shards;
    }

    public void set_shards(Map<String, Long> _shards) {
        this._shards = _shards;
    }

    public Hits getHits() {
        return hits;
    }

    public void setHits(Hits hits) {
        this.hits = hits;
    }
}
