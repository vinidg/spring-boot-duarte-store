package br.com.vinicius.springboot.service;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

import br.com.vinicius.springboot.service.exceptions.FileException;

@Service
public class S3Service {

	private Logger LOGGER = LoggerFactory.getLogger(S3Service.class);

	@Autowired
	private AmazonS3 amazonS3;

	@Value("${aws.bucket}")
	private String bucketName;
//
//	public URI uploadFile(MultipartFile multipartFile, String dir) {
//		try {
//			String fileName = multipartFile.getOriginalFilename();
//			InputStream is = multipartFile.getInputStream();
//			String contentType = multipartFile.getContentType();
//
//			return uploadFile(is, dir, fileName, contentType);
//		} catch (IOException e) {
//			throw new FileException("Erro de IO: "+e.getMessage());
//		}
//
//	}

	public URI uploadFile(InputStream is, String dir, String fileName, String contentType) {
		try {
			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentType(contentType);
			LOGGER.info("Iniciando upload");
			amazonS3.putObject(bucketName, dir+"/"+fileName, is, meta);
			LOGGER.info("Terminou upload");
			return amazonS3.getUrl(bucketName, fileName).toURI();
		} catch (URISyntaxException e) {
			throw new FileException("Erro ao converter URL para URI");
		}

	}
}
