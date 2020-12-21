package com.google.cloud.spring.kms;

import java.util.Base64;

import com.google.protobuf.ByteString;

public class EncryptionResult {

	private final ByteString byteString;

	EncryptionResult(ByteString byteString) {
		this.byteString = byteString;
	}

	public byte[] toByteArray() {
		return byteString.toByteArray();
	}

	public String toBase64() {
		byte[] bytes = byteString.toByteArray();
		byte[] encoded = Base64.getEncoder().encode(bytes);
		return new String(encoded);
	}

}
