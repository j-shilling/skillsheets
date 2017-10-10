package com.shilling.skillsheets;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenId {
	
	private final String tokenId;
	
	@JsonCreator
	public TokenId(@JsonProperty("tokenid") String val) {
		this.tokenId = val;
	}
	
	@JsonProperty ("tokenid")
	public String getTokenId() {
		return this.tokenId;
	}
	
	@Override
	public String toString() {
		return this.getTokenId();
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.getTokenId().equals(obj);
	}
	
	@Override
	public int hashCode() {
		return this.getTokenId().hashCode();
	}
}
