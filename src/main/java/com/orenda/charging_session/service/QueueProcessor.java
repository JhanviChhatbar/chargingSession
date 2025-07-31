package com.orenda.charging_session.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orenda.charging_session.model.CallbackPayload;
import com.orenda.charging_session.model.Decision;
import com.orenda.charging_session.model.SessionRequest;
import com.orenda.charging_session.repository.DecisionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.*;

@Service
public class QueueProcessor {

    private final BlockingQueue<SessionRequest> queue = new LinkedBlockingQueue<>();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final AuthorizationService authService;
    private DecisionRepository decisionRepository;

    @Autowired
    public QueueProcessor( DecisionRepository decisionRepository, AuthorizationService authService){
        this.decisionRepository = decisionRepository;
        this.authService = authService;
        System.out.println("constructor");
        startProcessing();
    }

    private void startProcessing() {
        Thread processorThread = new Thread(() -> {
            System.out.println("startProcessing");
            while (true) {
                try {
                    SessionRequest request = queue.take();

                    Future<?> future = executorService.submit(() -> {
                        processRequest(request);
                    });

                    try {
                        future.get(5, TimeUnit.SECONDS);
                    } catch (TimeoutException e) {
                        System.out.println("Processing timed out for: " + request.getDriverTokenId());

                        Decision decision = new Decision(request.getStationId(), request.getDriverTokenId(), "unknown");
                        decisionRepository.save(decision);

                        createCallBackPayload(request, "unknown");
                        future.cancel(true);
                    }

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        processorThread.setDaemon(true);
        processorThread.start();
    }

    public void enqueue(SessionRequest request) {
        System.out.println(request);
        queue.offer(request);
        System.out.println("queue size " + queue.size());
    }

    private void processRequest(SessionRequest request){
        String status = authService.authorize(request);

        Decision decision = new Decision(request.getStationId(), request.getDriverTokenId(), status);
        decisionRepository.save(decision);

        createCallBackPayload(request, status);
    }

    private void createCallBackPayload(SessionRequest request, String status){
        CallbackPayload payload = new CallbackPayload(request.getStationId(),
                request.getDriverTokenId(), status);
        sendCallback(request.getCallBackURL(), payload);
        System.out.println("Decision persisted: " + payload);

    }

    private void sendCallback(String callBackURL, CallbackPayload payload){
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(callBackURL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writeValueAsString(payload)))
                    .build();

            client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
