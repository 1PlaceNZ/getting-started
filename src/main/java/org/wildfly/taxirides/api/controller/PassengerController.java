package org.wildfly.taxirides.api.controller;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.wildfly.taxirides.api.dto.input.PassengerInput;
import org.wildfly.taxirides.api.dto.output.PassengerOutput;
import org.wildfly.taxirides.domain.entity.Passenger;
import org.wildfly.taxirides.domain.service.PassengerService;

import java.util.List;
import java.util.stream.Collectors;

@Path("/passengers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PassengerController {

    @Inject
    private PassengerService passengerService;

    @GET
    public Response listPassengers() {
        List<Passenger> passengers = passengerService.listAllPassengers();
        List<PassengerOutput> passengerDTOs = passengers.stream()
                .map(passenger -> PassengerOutput.builder()
                        .id(passenger.getId())
                        .firstName(passenger.getFirstName())
                        .lastName(passenger.getLastName())
                        .age(passenger.getAge())
                        .build()
                ).collect(Collectors.toList());
        return Response.ok(passengerDTOs).build();
    }

    @POST
    @Transactional
    public Response addPassenger(@Valid PassengerInput input) {
        Passenger passenger = Passenger.builder()
                .firstName(input.getFirstName())
                .lastName(input.getLastName())
                .age(input.getAge())
                .build();

        Passenger createdPassenger = passengerService.addPassenger(passenger);

        PassengerOutput createdPassengerOutput = PassengerOutput.builder()
                .id(createdPassenger.getId())
                .firstName(createdPassenger.getFirstName())
                .lastName(createdPassenger.getLastName())
                .age(createdPassenger.getAge())
                .build();

        return Response.status(Response.Status.CREATED)
                .entity(createdPassengerOutput)
                .build();
    }
}
