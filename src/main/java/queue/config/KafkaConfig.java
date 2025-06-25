package queue.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {
    private final Environment env;

    @Bean
    public NewTopic couponTopic() {
        // 파티션 수 = 예상 처리량 / 컨슈머 개수에 따라 조정
        return new NewTopic(env.getProperty("coupon.topic"), 3, (short)1);
    }
}
