package com.github.euler.api.model;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * TemplateParams
 */
@Validated
public class TemplateParams {

    @JsonProperty("tags")
    private String[] tags = new String[0];

    @JsonProperty("params")
    private Object params = null;

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

    public TemplateParams tags(String[] tags) {
        this.tags = tags;
        return this;
    }

    public TemplateParams params(Object params) {
        this.params = params;
        return this;
    }

    /**
     * Get params
     * 
     * @return params
     **/
    @Schema
    @NotNull
    public Object getParams() {
        return params;
    }

    public void setParams(Object params) {
        this.params = params;
    }

    public TemplateParams seed(String seed) {
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
        TemplateParams templateParams = (TemplateParams) o;
        return Objects.equals(this.tags, templateParams.tags) &&
                Objects.equals(this.params, templateParams.params) &&
                Objects.equals(this.seed, templateParams.seed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tags, params, seed);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class TemplateParams {\n");

        sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
        sb.append("    params: ").append(toIndentedString(params)).append("\n");
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
