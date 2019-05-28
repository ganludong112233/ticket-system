package com.workorder.ticket;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.SerializationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.sowing.common.exception.DefaultHandlerExceptionResolver;
import com.workorder.ticket.interceptor.LoginInterceptor;

@Configuration
public class WebConfiguration extends WebMvcConfigurationSupport {
	
	/*
	 * 登录校验拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(AuthortiyInterceptor()).addPathPatterns("/**")
				.excludePathPatterns("/login");
	}
	
	@Bean
	LoginInterceptor AuthortiyInterceptor() {
		return new LoginInterceptor();
	}

	/**
	 * 异常处理器
	 */
	@Override
	public void extendHandlerExceptionResolvers(
			List<HandlerExceptionResolver> exceptionResolvers) {
		DefaultHandlerExceptionResolver resolverHandler = new DefaultHandlerExceptionResolver();
		exceptionResolvers.add(0, resolverHandler);
	}
	
	@Bean
	public RedisSerializer<Object> fastJson2JsonRedisSerializer() {
		return new FastJson2JsonRedisSerializer<Object>(Object.class);
	}

	/**
	 * 参数类型转换器
	 */
	@Override
	protected void addFormatters(FormatterRegistry registry) {
		registry.addConverter(dateFormateConvert());
	}

	@Bean
	public RedisTemplate<String, String> redisTemplate(
			RedisConnectionFactory factory,
			RedisSerializer<Object> fastJson2JsonRedisSerializer) {
		StringRedisTemplate template = new StringRedisTemplate(factory);

		template.setValueSerializer(fastJson2JsonRedisSerializer);

		template.afterPropertiesSet();
		return template;
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
				if (StringUtils.isEmpty(source)) {
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

/**
 * Redis序列化
 * 
 * @author wzdong
 * @Date 2019年1月7日
 * @version 1.0
 * @param <T>
 */
class FastJson2JsonRedisSerializer<T> implements RedisSerializer<T> {

	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

	private Class<T> clazz;

	public FastJson2JsonRedisSerializer(Class<T> clazz) {
		super();
		this.clazz = clazz;
	}

	public byte[] serialize(T t) throws SerializationException {
		if (t == null) {
			return new byte[0];
		}
		return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(
				DEFAULT_CHARSET);
	}

	public T deserialize(byte[] bytes) throws SerializationException {
		if (bytes == null || bytes.length <= 0) {
			return null;
		}
		String str = new String(bytes, DEFAULT_CHARSET);

		return (T) JSON.parseObject(str, clazz);
	}

}
