package configuration;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;

public class JwtRestConfiguration extends Configuration {
	
	@NotEmpty
	private String secretKey;

	@JsonProperty
	public String getSecretKey() {
		return secretKey;
	}

	@JsonProperty
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	

}
