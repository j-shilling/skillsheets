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
	private final String imageurl;
	
	@JsonCreator
	public TokenId(@JsonProperty("tokenid") String val,
			@JsonProperty("imageurl") String imageurl) {
		this.tokenId = val;
		this.imageurl = imageurl;
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
	
	@JsonProperty ("imageurl")
	public String getImageUrl() {
		return this.imageurl;
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
