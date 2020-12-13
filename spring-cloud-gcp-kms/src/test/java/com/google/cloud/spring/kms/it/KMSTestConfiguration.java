/*
 * Copyright 2017-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.cloud.spring.kms.it;

import java.io.IOException;

import com.google.api.gax.core.CredentialsProvider;
import com.google.cloud.kms.v1.KeyManagementServiceClient;
import com.google.cloud.kms.v1.KeyManagementServiceSettings;
import com.google.cloud.spring.core.Credentials;
import com.google.cloud.spring.core.DefaultCredentialsProvider;
import com.google.cloud.spring.core.DefaultGcpEnvironmentProvider;
import com.google.cloud.spring.core.DefaultGcpProjectIdProvider;
import com.google.cloud.spring.core.GcpEnvironmentProvider;
import com.google.cloud.spring.core.GcpProjectIdProvider;
import com.google.cloud.spring.kms.KMSTemplate;
import com.google.protobuf.ByteString;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * Configuration for Integration tests.
 *
 * @author Emmanouil Gkatziouras
 */
@Configuration
public class KMSTestConfiguration {

	private final GcpProjectIdProvider projectIdProvider;

	private final CredentialsProvider credentialsProvider;

	public KMSTestConfiguration(
			ConfigurableEnvironment configurableEnvironment) throws IOException {

		this.projectIdProvider = new DefaultGcpProjectIdProvider();
		this.credentialsProvider = new DefaultCredentialsProvider(Credentials::new);

		// Registers {@link ByteString} type converters to convert to String and byte[].
		configurableEnvironment.getConversionService().addConverter(
				new Converter<ByteString, String>() {
					@Override
					public String convert(ByteString source) {
						return source.toStringUtf8();
					}
				});

		configurableEnvironment.getConversionService().addConverter(
				new Converter<ByteString, byte[]>() {
					@Override
					public byte[] convert(ByteString source) {
						return source.toByteArray();
					}
				});
	}

	@Bean
	public GcpProjectIdProvider gcpProjectIdProvider() {
		return this.projectIdProvider;
	}

	@Bean
	public static GcpEnvironmentProvider gcpEnvironmentProvider() {
		return new DefaultGcpEnvironmentProvider();
	}

	@Bean
	public KeyManagementServiceClient keyManagementServiceClient() throws IOException {
		KeyManagementServiceSettings settings = KeyManagementServiceSettings.newBuilder()
				.setCredentialsProvider(this.credentialsProvider)
				.build();

		return KeyManagementServiceClient.create(settings);
	}

	@Bean
	public KMSTemplate kmsTemplate(KeyManagementServiceClient client) {
		return new KMSTemplate(client, this.projectIdProvider);
	}


}