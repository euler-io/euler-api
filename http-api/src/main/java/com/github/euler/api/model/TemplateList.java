package com.github.euler.api.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * TemplateList
 */
@Validated
public class TemplateList {
	@JsonProperty("total")
	private Integer total = null;

	@JsonProperty("templates")
	@Valid
	private List<Template> templates = null;

	public TemplateList total(Integer total) {
		this.total = total;
		return this;
	}

	/**
	 * Get total
	 * 
	 * @return total
	 **/
	@Schema
	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public TemplateList templates(List<Template> templates) {
		this.templates = templates;
		return this;
	}

	public TemplateList addTemplatesItem(Template templatesItem) {
		if (this.templates == null) {
			this.templates = new ArrayList<>();
		}
		this.templates.add(templatesItem);
		return this;
	}

	/**
	 * Get templates
	 * 
	 * @return templates
	 **/
	@Schema
	@Valid
	public List<Template> getTemplates() {
		return templates;
	}

	public void setTemplates(List<Template> templates) {
		this.templates = templates;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		TemplateList templateList = (TemplateList) o;
		return Objects.equals(this.total, templateList.total) && Objects.equals(this.templates, templateList.templates);
	}

	@Override
	public int hashCode() {
		return Objects.hash(total, templates);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class TemplateList {\n");

		sb.append("    total: ").append(toIndentedString(total)).append("\n");
		sb.append("    templates: ").append(toIndentedString(templates)).append("\n");
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
