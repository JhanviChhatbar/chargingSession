package com.orenda.charging_session.controller;

import com.orenda.charging_session.model.SessionRequest;
import com.orenda.charging_session.service.QueueProcessor;
import com.orenda.charging_session.util.ValidatorUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ChargingSessionController {

    private final QueueProcessor queueProcessor;

    public ChargingSessionController(QueueProcessor queueProcessor){
        this.queueProcessor = queueProcessor;
    }

    @PostMapping("/initiate-session")
    public ResponseEntity<Map<String, String>> initiateSession(@RequestBody SessionRequest sessionRequest){
        if(!ValidatorUtil.isValidUUID(sessionRequest.getStationId()) ||
                !ValidatorUtil.isValidToken(sessionRequest.getDriverTokenId()) ||
                !ValidatorUtil.isValidURL(sessionRequest.getCallBackURL())){
            return ResponseEntity.badRequest()
                    .body(Map.of("status", "error", "message", "Invalid input"));
        }

        queueProcessor.enqueue(sessionRequest);

        return ResponseEntity.ok(Map.of(
                "status", "accepted",
                "message", "Request is being processed asynchronously. The result will be sent to the provided callback URL."
        ));
    }

}
