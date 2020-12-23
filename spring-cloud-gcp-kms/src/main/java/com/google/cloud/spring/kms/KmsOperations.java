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

import com.google.cloud.spring.core.GcpProjectIdProvider;

/**
 * Describes supported operations that one can perform on the KMS API.
 *
 * <p>For the methods you need specify the secret from GCP KMS by URI string.
 * The following secret URI syntax is supported:
 *
 * 1. Shortest form - specify location ID, key ring ID, and key ID. Project is derived through {@link GcpProjectIdProvider}.
 * kms://{location-id}/{key-ring-id}/{key-id}
 *
 * 2.  Short form - specify project ID, location ID, key ring ID, and key ID
 * kms://{project-id}/{location-id}/{key-ring-id}/{key-id}
 *
 * 3. Long form - specify project ID, location ID, key ring ID, and key ID (full resource name)
 * kms://projects/{project-id}/locations/{location-id}/keyRings/{key-ring-id}/cryptoKeys/{key-id}
 *
 * @author Emmanouil Gkatziouras
 */
public interface KmsOperations {

	/**
	 * Encrypt the {@code text} using the specified KMS URI string {@code cryptoKey}.
	 *
	 * <p>
	 * An encryption request will be issued using GCP KMS.
	 *
	 * @param cryptoKey The KMS URI string
	 * @param text UTF-8 encoded text to encrypt
	 * @return The encrypted bytes
	 */
	byte[] encryptText(String cryptoKey, String text);

	/**
	 * Encrypt the {@code bytes} using the specified KMS URI string {@code cryptoKey}.
	 *
	 * <p>
	 * An encryption request will be issued using GCP KMS.
	 *
	 * @param cryptoKey The KMS URI string
	 * @param bytes The bytes to encrypt
	 * @return The encrypted bytes
	 */
	byte[] encryptBytes(String cryptoKey, byte[] bytes);

	/**
	 * Decrypt the text using the specified KMS URI string {@code cryptoKey}
	 *
	 * <p>
	 * A decryption request will be issued using GCP KMS.
	 *
	 * @param cryptoKey The KMS URI string
	 * @param cipherText The encrypted bytes
	 * @return The decrypted bytes
	 */
	byte[] decryptBytes(String cryptoKey, byte[] cipherText);

	/**
	 * Decrypt the text using the specified KMS URI string {@code cryptoKey}
	 *
	 * <p>
	 * A decryption request will be issued using GCP KMS.
	 *
	 * @param cryptoKey The KMS URI string
	 * @param cipherText The encrypted bytes
	 * @return The decrypted bytes UTF-8 encoded
	 */
	String decryptText(String cryptoKey, byte[] cipherText);
}