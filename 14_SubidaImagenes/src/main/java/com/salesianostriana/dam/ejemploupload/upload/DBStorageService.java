package com.salesianostriana.dam.ejemploupload.upload;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.stream.Stream;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.salesianostriana.dam.ejemploupload.upload.modelo.ImagenEntity;
import com.salesianostriana.dam.ejemploupload.upload.modelo.ImagenEntityRepository;

/**
 * Este servicio se encarga de recibir una imagen a través de un objeto
 * MultipartFile, la reduce de tamaño y la convierte en una cadena Base64
 * 
 * ¡OJO! Esto ni de lejos es lo más eficiente para gestionar imágenes, pero
 * nos permitirá almacenar algunas pequeñas y a una calidad medio-baja
 * dentro de nuestras tablas.
 * 
 * @author lmlopez
 *
 */
@Service
public class DBStorageService implements StorageService<ImagenEntity, Long> {
	
	
	private float compressionQuality;
	
	private int maxSize;

	private final ImagenEntityRepository repository;
	
	public DBStorageService(
			@Value("${image.compression-quality:0.5}") float cQ,
			@Value("${image.max-size:1024}") int mS,
			ImagenEntityRepository repository
			) {
		this.compressionQuality = cQ;
		this.maxSize = mS;
		this.repository = repository;
	}

	private final Logger logger = LoggerFactory.getLogger(DBStorageService.class);

	@Override
	public void init() {
		// En este caso no es necesario hacer nada
	}

	/**
	 * Este es el método que se utiliza para transformar el Mutiparte
	 * recibido en una cadena de caracteres en base64
	 * @param file
	 * @return Cadena de caracteres que se debe almacenar para acceder a la imagen
	 */
	@Override
	public String store(MultipartFile file) {
		if (file.isEmpty()) {
			logger.debug("Se ha subido un fichero vacío");
			return null;
		}

		try {
			String base64img = Base64.getEncoder().encodeToString(compressImg(file.getBytes(), file.getContentType()));
			ImagenEntity saved = repository.save(new ImagenEntity(base64img));
			return Long.toString(saved.getId());
		} catch (IOException e) {
			logger.error("Error al transformar el fichero a base64");
			logger.error(e.getMessage());
			return null;
		}
	}
	
	
	@Override
	public Stream<ImagenEntity> loadAll() {
		return repository.findAll().stream();
	}

	@Override
	public ImagenEntity load(Long id) {
		return repository.findById(id).orElse(null);
	}
	
	/**
	 * Rescata la imagen de la base de datos a partir de uso ID,
	 * y lo transforma de base64 a un Resource. 
	 * @param id
	 * @return
	 */
	@Override
	public Resource loadAsResource(Long id) {

		ImagenEntity imagen = load(id);
		if (imagen != null) {
			byte[] byteImage = Base64.getDecoder().decode(imagen.getContenido());

			Resource resource = new ByteArrayResource(byteImage);

			if (resource.exists() || resource.isReadable()) {
				return resource;
			}

		}

		return null;
	}


	@Override
	public void delete(Long id) {
		repository.deleteById(id);
		
	}

	@Override
	public void deleteAll() {
		repository.deleteAllInBatch();
	}

	

	/**
	 * Método privado que comprime y escala una imagen a la mitad
	 * 
	 * @param source
	 * @return
	 * @throws IOException
	 */
	private byte[] compressImg(byte[] source, String contentType) throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		BufferedImage preResize = ImageIO.read(new ByteArrayInputStream(source));
		BufferedImage image = null;
		if (preResize.getWidth() >= maxSize || preResize.getHeight() >= maxSize) {
			image = resize(preResize, maxSize);
		} else {
			image = preResize;
		}

		ImageWriter writer = ImageIO.getImageWritersByMIMEType(contentType).next();
		ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
		writer.setOutput(ios);

		ImageWriteParam param = writer.getDefaultWriteParam();
		if (param.canWriteCompressed()) {
			param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			param.setCompressionQuality(compressionQuality);
		}

		writer.write(null, new IIOImage(image, null, null), param);

		byte[] result = baos.toByteArray();

		baos.close();
		ios.close();
		writer.dispose();

		return result;

	}

	/**
	 * Este método sirve para escalar una imagen, preferentemente a un tamaño más
	 * pequeño que el original
	 * 
	 * @param image Imagen a escalar
	 * @param max   Tamaño máximo del lado más grande.
	 * @return
	 */
	private BufferedImage resize(BufferedImage image, int max) {
		if (image.getWidth() > max || image.getHeight() > max) {
			float aspectRatio = (float) image.getWidth() / image.getHeight();
			int width = 0, height = 0;
			if (image.getWidth() >= image.getHeight()) {
				width = max;
				height = (int) (max / aspectRatio);
			} else {
				height = max;
				width = (int) (max * aspectRatio);
			}
			Image tmp = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			BufferedImage resized = new BufferedImage(width, height, image.getType());
			Graphics2D g2d = resized.createGraphics();
			g2d.drawImage(tmp, 0, 0, null);
			g2d.dispose();
			g2d.setComposite(AlphaComposite.Src);
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2d.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			return resized;
		} else
			return image;
	}

	

}
