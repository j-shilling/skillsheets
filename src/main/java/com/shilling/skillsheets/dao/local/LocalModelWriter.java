package com.shilling.skillsheets.dao.local;

import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.shilling.skillsheets.dao.ModelEncoder;
import com.shilling.skillsheets.dao.ModelWriter;
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
	public boolean write(User user, Object obj) {

		this.logger.traceEntry("Saving " + obj);
		
		this.logger.trace("Finding an opening notifications file.");
		try (RandomAccessFile file = new RandomAccessFile(this.files.getFile(user, obj.getClass()), "rw")) {
			
			FileChannel channel = file.getChannel();
			FileLock lock = channel.lock();
			OutputStream out = Channels.newOutputStream(channel);
			this.encoder.encode(obj, out);
			
			/* Clean up */
			
			if (channel.isOpen()) {
				lock.release();
				channel.close();
			}
			
			return true;

		} catch (IOException e) {
			this.logger.error(e.getMessage());
			return false;
		}
	}
}
