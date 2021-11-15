package com.github.euler.api.model;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * TemplateDetails
 */
@Validated
public class TemplateDetails extends Template {

    @JsonProperty("tags")
    private String[] tags = new String[0];

	@JsonProperty("config")
	private Object config = null;

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

    public TemplateDetails tags(String[] tags) {
        this.tags = tags;
        return this;
    }

	public TemplateDetails config(Object config) {
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

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		TemplateDetails templateDetails = (TemplateDetails) o;
		return Objects.equals(this.config, templateDetails.config) && 
		        Objects.equals(this.tags, templateDetails.tags) && 
		        super.equals(o);
	}

	@Override
	public int hashCode() {
		return Objects.hash(tags, config, super.hashCode());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class TemplateDetails {\n");
		sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
        sb.append("    config: ").append(toIndentedString(config)).append("\n");
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
