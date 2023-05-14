package SQA.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import SQA.controller.MeasurementInterceptor;


@Configuration
@ComponentScan("com.cmpe275.cusr.controller")
public class WebConfiguration extends WebMvcConfigurerAdapter{
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(measurementInterceptor()).addPathPatterns("/search");
	}
	@Bean
	public MeasurementInterceptor measurementInterceptor() {
	return new MeasurementInterceptor();
	}
	


}
