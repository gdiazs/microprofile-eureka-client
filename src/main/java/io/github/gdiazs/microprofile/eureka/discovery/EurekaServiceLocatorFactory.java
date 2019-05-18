package io.github.gdiazs.microprofile.eureka.discovery;

import java.util.Optional;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import org.eclipse.microprofile.config.Config;

import com.netflix.discovery.EurekaClient;

import io.github.gdiazs.microprofile.eureka.annotations.EurekaService;

@Dependent
public class EurekaServiceLocatorFactory {

	@Inject
	private EurekaClient eurekaClient;

	@Inject
	private Config config;

	@Produces
	@EurekaService
	public Optional<String> discoverServiceString(InjectionPoint injectionPoint) {
		final EurekaService annotation = injectionPoint.getAnnotated().getAnnotation(EurekaService.class);
		final String serviceNameConfigName = annotation.service();
		final String endpointConfigName = annotation.endpoint();

		String homePageUrl = null;

		final String serviceName = config.getValue(serviceNameConfigName, String.class);
		final String endpoint = config.getValue(endpointConfigName, String.class);
		
		if(isEurekaServiceName(serviceName)) {
			homePageUrl = eurekaClient.getNextServerFromEureka(serviceName, false).getHomePageUrl();

		}else {
			homePageUrl = serviceName + "/";
		}

		if (isValidUrl(endpoint)) {
			homePageUrl += endpoint;
		}

		return Optional.of(homePageUrl);
	}

	private boolean isEurekaServiceName(final String serviceName) {
		boolean eurekaService = !(serviceName.contains("http://") || serviceName.contains("https://"));
		return eurekaService;
	}

	private boolean isValidUrl(final String devUrl) {
		return devUrl != null && devUrl.length() > 0;
	}

}
