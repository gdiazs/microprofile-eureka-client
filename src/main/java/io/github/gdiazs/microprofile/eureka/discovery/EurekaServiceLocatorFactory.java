package io.github.gdiazs.microprofile.eureka.discovery;

import java.util.Optional;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import com.netflix.discovery.EurekaClient;

import io.github.gdiazs.microprofile.eureka.annotations.EurekaService;

@Dependent
public class EurekaServiceLocatorFactory {

	@Inject
	private EurekaClient eurekaClient;

	@Produces
	@EurekaService
	public Optional<String> discoverServiceString(InjectionPoint injectionPoint) {
		final EurekaService annotation = injectionPoint.getAnnotated().getAnnotation(EurekaService.class);
		final String serviceName = annotation.service();
		final String endpoint = annotation.endpoint();

		String homePageUrl = eurekaClient.getNextServerFromEureka(serviceName, false).getHomePageUrl();
		if (endpoint.length() > 0) {
			homePageUrl += endpoint;
		}
		return Optional.of(homePageUrl);
	}

}
