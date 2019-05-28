package com.workorder.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

@Configuration
public class WebConfiguration extends WebMvcConfigurationSupport {

	/**
	 * 参数类型转换器
	 */
	@Override
	protected void addFormatters(FormatterRegistry registry) {
		registry.addConverter(dateFormateConvert());
	}

	/**
	 * Fastjson replace Jackson
	 */
	@Override
	public void configureMessageConverters(
			List<HttpMessageConverter<?>> converters) {
		
		// 定义一个转换消息的对象
		FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();

		// 添加fastjson的配置信息 比如 ：是否要格式化返回的json数据
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		// 在转换器中添加配置信息
		fastConverter.setFastJsonConfig(fastJsonConfig);
		List<MediaType> supportedMediaTypes = new ArrayList<>();
		supportedMediaTypes.add(MediaType.APPLICATION_JSON);
		supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
		supportedMediaTypes.add(MediaType.APPLICATION_ATOM_XML);
		supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
		supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
		supportedMediaTypes.add(MediaType.APPLICATION_PDF);
		supportedMediaTypes.add(MediaType.APPLICATION_RSS_XML);
		supportedMediaTypes.add(MediaType.APPLICATION_XHTML_XML);
		supportedMediaTypes.add(MediaType.APPLICATION_XML);
		supportedMediaTypes.add(MediaType.IMAGE_GIF);
		supportedMediaTypes.add(MediaType.IMAGE_JPEG);
		supportedMediaTypes.add(MediaType.IMAGE_PNG);
		supportedMediaTypes.add(MediaType.TEXT_EVENT_STREAM);
		supportedMediaTypes.add(MediaType.TEXT_HTML);
		supportedMediaTypes.add(MediaType.TEXT_MARKDOWN);
		supportedMediaTypes.add(MediaType.TEXT_PLAIN);
		supportedMediaTypes.add(MediaType.TEXT_XML);
		fastConverter.setSupportedMediaTypes(supportedMediaTypes);
		// fastConverter.getFastJsonConfig().setSerializerFeatures(
		// SerializerFeature.DisableCircularReferenceDetect);
		// 将转换器添加到converters中
		converters.add(fastConverter);
	}

	@Bean
	public Converter<String, Date> dateFormateConvert() {
		final String dateFormat = "yyyy-MM-dd HH:mm:ss";
		final String shortDateFormat = "yyyy-MM-dd";
		return new Converter<String, Date>() {
			@Override
			public Date convert(String source) {
				if (StringUtils.isBlank(source)) {
					return null;
				}
				source = source.trim();
				try {
					if (source.contains("-")) {
						SimpleDateFormat formatter;
						if (source.contains(":")) {
							formatter = new SimpleDateFormat(dateFormat);
						} else {
							formatter = new SimpleDateFormat(shortDateFormat);
						}

						Date dtDate = formatter.parse(source);
						return dtDate;
					} else if (source.matches("^\\d+$")) {
						Long lDate = new Long(source);
						return new Date(lDate);
					}
				} catch (Exception e) {
					throw new RuntimeException(String.format(
							"parser %s to Date fail", source));
				}
				throw new RuntimeException(String.format(
						"parser %s to Date fail", source));
			}
		};
	}

}
