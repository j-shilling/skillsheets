package com.shilling.skillsheets.dao;

import java.io.InputStream;
import java.io.OutputStream;

import com.shilling.skillsheets.model.Notification;

public interface ModelEncoder {
	public boolean encode(Notification notification, InputStream in, OutputStream out);
}
