# CQRS and Event Sourcing setup in profile

### 1. Add the following maven dependency inside **profile/pom.xml**

```
<dependency>
    <groupId>org.axonframework</groupId>
    <artifactId>axon-spring-boot-starter</artifactId>
</dependency>
```

### 2. Add the following property inside application.yml

```yaml
axon:
  eventhandling:
    processors:
      profile-group:
        mode: subscribing
  axonserver:
    servers: localhost:8124
```

### 3. Create the following subpackages

- com.eazybytes.profile.command
    - aggregate
    - controller
    - event
    - interceptor
- com.eazybytes.profile.query
    - controller
    - handler
    - projection

### 4. Create the following classes under the respective packages

For the actual source code, please refer to the GitHub repo,

- com.eazybytes.profile.command
    - CreateCustomerCommand
    - DeleteCustomerCommand
    - UpdateCustomerCommand
- com.eazybytes.profile.command.event
    - CustomerCreatedEvent
    - CustomerDeletedEvent
    - CustomerUpdatedEvent
- com.eazybytes.profile.command.aggregate
    - CustomerAggregate
- com.eazybytes.profile.command.controller
    - CustomerCommandController
- com.eazybytes.profile.command.interceptor
    - CustomerCommandInterceptor
- com.eazybytes.profile.query
    - FindCustomerQuery
- com.eazybytes.profile.query.projection
    - CustomerProjection
- com.eazybytes.profile.query.handler
    - CustomersQueryHandler
- com.eazybytes.profile.query.controller
    - CustomerQueryController

### 4. Create the following method in CustomerRepository

```java
Optional<Customer> findByCustomerIdAndActiveSw(String customerId, boolean active);
```

### 4. Create the following method in CustomerMapper

```java
public static Customer mapEventToCustomer(CustomerUpdatedEvent event, Customer profile) {
    profile.setName(event.getName());
    profile.setEmail(event.getEmail());
    return profile;
}
```

### 5. Update the ICustomerService with the below abstract methods

Once the interface is updated, update the CustomerServiceImpl class as well with the code present in the repository

```java
public interface ICustomerService {

    /**
     * @param profile - Customer Object
     */
    void createCustomer(Customer profile);

    /**
     * @param mobileNumber - Input Mobile Number
     * @return Customer Details based on a given mobileNumber
     */
    CustomerDto fetchCustomer(String mobileNumber);

    /**
     * @param event - CustomerUpdatedEvent Object
     * @return boolean indicating if the update of Customer details is successful or not
     */
    boolean updateCustomer(CustomerUpdatedEvent event);

    /**
     * @param customerId - Input Customer ID
     * @return boolean indicating if the delete of Customer details is successful or not
     */
    boolean deleteCustomer(String customerId);
}
```

### 6. Delete the CustomerController class & it's package as we separated our APIs in to Commands and Queries

### 7. Add the below method inside the GlobalExceptionHandler class

```java

@ExceptionHandler(CommandExecutionException.class)
public ResponseEntity<ErrorResponseDto> handleGlobalException(CommandExecutionException exception,
        WebRequest webRequest) {
    ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
            webRequest.getDescription(false),
            HttpStatus.INTERNAL_SERVER_ERROR,
            "CommandExecutionException occurred due to: "+exception.getMessage(),
            LocalDateTime.now()
    );
    return new ResponseEntity<>(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
}
```

### 8. Inside the CustomersApplication class, make the following changes

```java
package com.eazybytes.profile;

import com.eazybytes.common.config.AxonConfig;
import com.eazybytes.profile.command.interceptor.CustomerCommandInterceptor;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventhandling.PropagatingErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@Import({AxonConfig.class})
public class CustomersApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomersApplication.class, args);
    }

    @Autowired
    public void registerCustomerCommandInterceptor(ApplicationContext context,
                                                   CommandBus commandBus) {
        commandBus.registerDispatchInterceptor(context.getBean(CustomerCommandInterceptor.class));
    }

    @Autowired
    public void configure(EventProcessingConfigurer config) {
        config.registerListenerInvocationErrorHandler("profile-group",
                conf -> PropagatingErrorHandler.instance());
    }

}
```

---