package com.shilling.skillsheets.dao;

import java.io.OutputStream;

public interface ModelEncoder {
	public String getFileExtension();
	public boolean encode(Object obj, OutputStream out);
}
