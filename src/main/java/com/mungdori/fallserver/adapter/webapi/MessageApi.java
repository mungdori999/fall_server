package com.mungdori.fallserver.adapter.webapi;

import com.mungdori.fallserver.domain.message.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/message")
public class MessageApi {

        @PostMapping("")
        public void sendMessage(@RequestBody Message message) {

        }
}
