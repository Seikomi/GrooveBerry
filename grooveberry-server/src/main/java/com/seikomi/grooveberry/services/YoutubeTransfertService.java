package com.seikomi.grooveberry.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seikomi.grooveberry.GrooveberryServer;
import com.seikomi.janus.net.JanusServer;
import com.seikomi.janus.services.DataTranferService;

public class YoutubeTransfertService extends DataTranferService {
	static final Logger LOGGER = LoggerFactory.getLogger(YoutubeTransfertService.class);

	public YoutubeTransfertService(JanusServer server) {
		super(server);
	}

	public void youtubeDownload(String url) throws IOException {
		youtubeDownloadCall(url);
	}

	private void youtubeDownloadCall(String url) throws IOException {
		Path path = Paths.get(GrooveberryServer.USER_HOME_PATH + "/.grooveberry/library/");
		Process p = Runtime.getRuntime().exec(
				new String[] { "youtube-dl.exe", "-x", "--audio-format", "mp3", "-o", path + "/%(title)s.%(ext)s", url });

		new Thread("YoutubeDownloadThread") {
			
			@Override
			public void run() {
				try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
					String line = "";
					while ((line = reader.readLine()) != null) {
						LOGGER.info(line);
					}

				} catch (IOException e) {
					LOGGER.error("An error occurs during youtube download", e);
				}
			}
		}.start();
	}

}
