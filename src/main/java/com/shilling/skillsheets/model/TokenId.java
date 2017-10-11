package com.shilling.skillsheets.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A class to wrap a Google Token ID
 * 
 * @author Jake Shilling
 *
 */
public class TokenId {
	
	private final String tokenId;
	
	@JsonCreator
	public TokenId(@JsonProperty("tokenid") String val) {
		this.tokenId = val;
	}
	
	/**
	 * Get the Token ID as a string.
	 * 
	 * @return	The token id string.
	 */
	@JsonProperty ("tokenid")
	public String getTokenId() {
		return this.tokenId;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.getTokenId();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		return this.getTokenId().equals(obj);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return this.getTokenId().hashCode();
	}
}
