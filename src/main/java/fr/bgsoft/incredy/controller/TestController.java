package fr.bgsoft.incredy.controller;

import java.time.Instant;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping(value = "/api/v1/test")
public class TestController {
	@Operation(summary = "Gets the current time")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Current time", content = @Content) })
	@GetMapping("/currenttime")
	public String currentTime() {
		return Instant.now().toString();
	}

	@GetMapping("/currentuser")
	public String currentUser() {
		return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
	}

}
