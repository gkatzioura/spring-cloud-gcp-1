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

package com.google.cloud.spring.kms;

import com.google.cloud.kms.v1.CryptoKeyName;
import com.google.cloud.spring.core.GcpProjectIdProvider;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class KMSPropertyUtilsTests {

	private static final GcpProjectIdProvider DEFAULT_PROJECT_ID_PROVIDER = () -> "defaultProject";

	@Test
	public void testNonKms() {
		String cryptoKeyNameStr = "spring.cloud.datasource";
		assertThatThrownBy(() ->
				KMSPropertyUtils.getCryptoKeyName(cryptoKeyNameStr, DEFAULT_PROJECT_ID_PROVIDER))
				.isInstanceOf(KMSException.class)
				.hasMessageContaining("Cryptographic key names should start with kms://");
	}

	@Test
	public void testInvalidKMSFormat_missingValues() {
		String cryptoKeyNameStr = "kms://";

		assertThatThrownBy(() ->
				KMSPropertyUtils.getCryptoKeyName(cryptoKeyNameStr, DEFAULT_PROJECT_ID_PROVIDER))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("Unrecognized format for specifying a GCP KMS");
	}

	@Test
	public void testKMSFormat_noProject() {
		String cryptoKeyNameStr = "kms://europe-west2/key-ring-id/key-id";

		CryptoKeyName cryptoKeyName = KMSPropertyUtils.getCryptoKeyName(cryptoKeyNameStr, DEFAULT_PROJECT_ID_PROVIDER);

		assertThat(cryptoKeyName.getProject()).isEqualTo(DEFAULT_PROJECT_ID_PROVIDER.getProjectId());
		assertThat(cryptoKeyName.getLocation()).isEqualTo("europe-west2");
		assertThat(cryptoKeyName.getKeyRing()).isEqualTo("key-ring-id");
		assertThat(cryptoKeyName.getCryptoKey()).isEqualTo("key-id");
	}

	@Test
	public void testKMSFormat_lean() {
		String cryptoKeyNameStr = "kms://test-project/europe-west2/key-ring-id/key-id";

		CryptoKeyName cryptoKeyName = KMSPropertyUtils.getCryptoKeyName(cryptoKeyNameStr, DEFAULT_PROJECT_ID_PROVIDER);

		assertThat(cryptoKeyName.getProject()).isEqualTo("test-project");
		assertThat(cryptoKeyName.getLocation()).isEqualTo("europe-west2");
		assertThat(cryptoKeyName.getKeyRing()).isEqualTo("key-ring-id");
		assertThat(cryptoKeyName.getCryptoKey()).isEqualTo("key-id");
	}

	@Test
	public void testKMSFormat_verbose() {
		String cryptoKeyNameStr = "kms://projects/test-project/locations/europe-west2/keyRings/key-ring-id/cryptoKeys/key-id";

		CryptoKeyName cryptoKeyName = KMSPropertyUtils.getCryptoKeyName(cryptoKeyNameStr, DEFAULT_PROJECT_ID_PROVIDER);

		assertThat(cryptoKeyName.getProject()).isEqualTo("test-project");
		assertThat(cryptoKeyName.getLocation()).isEqualTo("europe-west2");
		assertThat(cryptoKeyName.getKeyRing()).isEqualTo("key-ring-id");
		assertThat(cryptoKeyName.getCryptoKey()).isEqualTo("key-id");
	}

}