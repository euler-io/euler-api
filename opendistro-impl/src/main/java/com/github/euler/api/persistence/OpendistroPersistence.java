package com.github.euler.api.persistence;

import com.github.euler.opendistro.OpenDistroClient;

public abstract class OpendistroPersistence extends BaseOpendistroPersistence {

	protected final OpenDistroClient client;

	public OpendistroPersistence(OpenDistroClient client) {
		super();
		this.client = client;
	}

}
