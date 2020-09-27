package toby.test.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping(path = "/test")
public class LolController {

	@PostMapping(path = "/lol")
	@ResponseStatus(HttpStatus.OK)
	public Mono<String> lol(@RequestBody String token) {

		log.info(token);
		return Mono.just("success");
	}
}
