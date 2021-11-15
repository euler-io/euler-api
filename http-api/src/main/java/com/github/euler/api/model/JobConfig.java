package com.github.euler.api.model;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * JobConfig
 */
@Validated
public class JobConfig {

    @JsonProperty("tags")
    private String[] tags = new String[0];

    @JsonProperty("config")
    private Object config = null;

    @JsonProperty("seed")
    private String seed = null;

    /**
     * Get tags
     * 
     * @return tags
     **/
    @Schema
    @NotNull
    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public JobConfig tags(String[] tags) {
        this.tags = tags;
        return this;
    }

    public JobConfig config(Object config) {
        this.config = config;
        return this;
    }

    /**
     * Get config
     * 
     * @return config
     **/
    @Schema
    @NotNull

    public Object getConfig() {
        return config;
    }

    public void setConfig(Object config) {
        this.config = config;
    }

    public JobConfig seed(String seed) {
        this.seed = seed;
        return this;
    }

    /**
     * Get seed
     * 
     * @return seed
     **/
    @Schema
    @NotNull

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JobConfig jobConfig = (JobConfig) o;
        return Objects.equals(this.tags, jobConfig.tags) &&
                Objects.equals(this.config, jobConfig.config) &&
                Objects.equals(this.seed, jobConfig.seed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tags, config, seed);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class JobConfig {\n");
        sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
        sb.append("    config: ").append(toIndentedString(config)).append("\n");
        sb.append("    seed: ").append(toIndentedString(seed)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
