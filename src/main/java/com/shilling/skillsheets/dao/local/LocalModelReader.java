package com.shilling.skillsheets.dao.local;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.shilling.skillsheets.dao.ModelDecoder;
import com.shilling.skillsheets.dao.ModelReader;
import com.shilling.skillsheets.model.User;

public class LocalModelReader implements ModelReader {
	
	private final Logger logger;
	private final LocalFiles files;
	private final ModelDecoder decoder;
	
	@Autowired
	private LocalModelReader (LocalFiles files, ModelDecoder decoder) {
		this.logger = LogManager.getLogger(LocalModelReader.class);
		this.files = files;
		this.decoder = decoder;
	}

	@Override
	public <T> Optional<T> read(User user, Class<T> klass) {
		this.logger.traceEntry("Getting " + klass.getSimpleName() + " for " + user);
		File file = this.files.getFile(user, klass);
		
		try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
			
			FileChannel channel = raf.getChannel();
			FileLock lock = channel.lock();
			InputStream in = Channels.newInputStream(channel);
			
			Optional<T> ret = this.decoder.decode(in, klass);
			
			if (channel.isOpen()) {
				lock.release();
				channel.close();
			}
			
			return ret;
			
		} catch (IOException e) {
			this.logger.error(e.getMessage());
			return Optional.empty();
		}
	}

}
