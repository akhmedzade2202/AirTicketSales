package com.example.airticketsales.service;

import com.example.airticketsales.dto.request.FlightRequest;
import com.example.airticketsales.dto.response.FlightResponse;
import com.example.airticketsales.entity.Flight;
import com.example.airticketsales.exception.NotFoundException;
import com.example.airticketsales.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlightService {
    private final FlightRepository flightRepository;
    private final ModelMapper modelMapper;

    public List<FlightResponse> getAllFlights() {
        return flightRepository.findAll().stream()
                .map(flight -> modelMapper.map(flight, FlightResponse.class))
                .collect(Collectors.toList());
    }

    public FlightResponse getFlightById(Long id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Uçuş tapılmadı"));
        return modelMapper.map(flight, FlightResponse.class);
    }

    public FlightResponse createFlight(FlightRequest request) {
        Flight flight = modelMapper.map(request, Flight.class);
        flight = flightRepository.save(flight);
        return modelMapper.map(flight, FlightResponse.class);
    }

    public FlightResponse updateFlight(Long id, FlightRequest request) {
        Flight existingFlight = flightRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Uçuş tapılmadı"));

        modelMapper.map(request, existingFlight);
        existingFlight.setId(id);
        existingFlight = flightRepository.save(existingFlight);

        return modelMapper.map(existingFlight, FlightResponse.class);
    }

    public void deleteFlight(Long id) {
        if (!flightRepository.existsById(id)) {
            throw new NotFoundException("Uçuş tapılmadı");
        }
        flightRepository.deleteById(id);
    }
}