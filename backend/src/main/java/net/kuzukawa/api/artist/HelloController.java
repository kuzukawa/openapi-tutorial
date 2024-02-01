package net.kuzukawa.api.artist;

import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@EnableAutoConfiguration
public class HelloController {
  @GetMapping("/")
  String home(){
    return "Hello world";
  }
}