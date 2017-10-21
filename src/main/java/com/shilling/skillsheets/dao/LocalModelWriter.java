package com.shilling.skillsheets.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.shilling.skillsheets.model.Notification;
import com.shilling.skillsheets.model.User;

public class LocalModelWriter implements ModelWriter {

	private final Logger logger;
	private final LocalFiles files;
	private final ModelEncoder encoder;
	
	@Autowired
	private LocalModelWriter (LocalFiles files, ModelEncoder encoder) {
		this.logger = LogManager.getLogger(LocalModelWriter.class);
		this.files = files;
		this.encoder = encoder;
	}

	@Override
	public boolean write(User user, Notification notification) {

		this.logger.traceEntry("Saving " + notification);
		
		this.logger.trace("Finding an opening notifications file.");
		try (RandomAccessFile file = new RandomAccessFile(this.files.notifications(user), "rw")) {
			
			FileChannel channel = file.getChannel();
			FileLock lock = channel.lock();
			
			this.logger.trace("Copying old information to a temporary file.");
			File tempfile = new File(this.files.notifications(user).toString() + ".tmp");
			tempfile.createNewFile();
			
			FileChannel tempchannel = new FileOutputStream(tempfile).getChannel();
			FileLock templock = tempchannel.lock();
			channel.transferTo(0, channel.size(), tempchannel);
			templock.release();
			tempchannel.close();
			
			this.logger.trace("Sending streams to the encoder.");
			InputStream in = new FileInputStream (tempfile);
			OutputStream out = Channels.newOutputStream(channel);
			this.encoder.encode(notification, in, out);
			
			/* Clean up */
			lock.release();
			tempfile.delete();
			in.close();
			out.close();
			
			return true;

		} catch (IOException e) {
			this.logger.error(e.getMessage());
			return false;
		}
	}
}
