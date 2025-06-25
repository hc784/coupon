package queue.controller;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.RequiredArgsConstructor;
import queue.service.QueueService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class QueueSseController {

    private final QueueService queueService;
    private final Executor executor = Executors.newCachedThreadPool();

    @GetMapping(value = "/queue/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable long userId) {
        SseEmitter emitter = new SseEmitter(0L);  // timeout 무제한
        executor.execute(() -> {
            try {
                while (true) {
                    Long pos = queueService.currentPosition(userId);
                    if (pos == null) { // 큐에서 빠졌으면 완료
                        emitter.send(SseEmitter.event()
                                               .name("done")
                                               .data("발급 완료"));
                        emitter.complete();
                        break;
                    }
                    emitter.send(SseEmitter.event()
                                           .name("position")
                                           .data(pos));
                    Thread.sleep(1_000);          // 1초 간격
                }
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }
}
