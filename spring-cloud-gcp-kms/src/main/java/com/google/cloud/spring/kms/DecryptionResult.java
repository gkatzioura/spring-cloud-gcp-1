package com.google.cloud.spring.kms;

import java.util.Base64;

import com.google.protobuf.ByteString;

public class DecryptionResult {

	private final ByteString byteString;

	DecryptionResult(ByteString byteString) {
		this.byteString = byteString;
	}


	public byte[] toByteArray() {
		return byteString.toByteArray();
	}

	public String toStringUtf8() {
		return byteString.toStringUtf8();
	}


}
