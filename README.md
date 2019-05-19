# microprofile-eureka-client

Using this library you can connect clients with Eureka Discovery Server using MicroProfile


## Create a MicroProfile microservice
https://start.microprofile.io/

Compile this project

```bash
mvn clean install
```

 Add to the Microprofile Project POM the next dependency:

 ```xml
  <dependency>
    <groupId>io.github.gdiazs</groupId>
    <artifactId>microprofile-eureka-client</artifactId>
    <version>0.0.1-SNAPSHOT</version>
  </dependency>
```

In the resource folder add the file  [**eureka-client.properties** ](https://github.com/Netflix/eureka/blob/master/eureka-server/src/main/resources/eureka-client.properties)

Once your eureka server it's up you should add in your JAX-RS endpoint the reference to the service and endpoint

```java
@Path("/hello")
@Singleton
public class HelloController {

	@Inject
	@EurekaService(service = "ping.serviceName", endpoint="ping.endpoint") //must be defined in microprofile-config.properties
	private Optional<String> urlPingService;

	@GET
	public String sayHello() {

		String result = "";
		if (urlPingService.isPresent()) {
			result = ClientBuilder.newClient().target(urlPingService.get()).request(MediaType.APPLICATION_JSON)
					.get(String.class);
		}

		return result;
	}
}

```

