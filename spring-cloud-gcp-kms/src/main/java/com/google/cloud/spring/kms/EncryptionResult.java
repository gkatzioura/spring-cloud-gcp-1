package com.google.cloud.spring.kms;

import com.google.protobuf.ByteString;

public class EncryptionResult {

	private final ByteString byteString;

	EncryptionResult(ByteString byteString) {
		this.byteString = byteString;
	}

	public byte[] toByteArray() {
		return byteString.toByteArray();
	}

	public String toStringUtf8() {
		return byteString.toStringUtf8();
	}

}
