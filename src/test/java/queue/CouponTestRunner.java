package queue;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CouponTestRunner {

    public static void main(String[] args) throws Exception {
        String url = "http://localhost:8080/api/coupon";
        HttpClient client = HttpClient.newHttpClient();

        for (int i = 1; i <= 1000; i++) {
            long userId = i; // userId를 1부터 1000까지 다르게 설정
            String requestUrl = url + "?userId=" + userId;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(requestUrl))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("[" + userId + "] " + response.statusCode() + ": " + response.body());

            Thread.sleep(5); // 과도한 부하 방지 (필요시 제거)
        }
    }
}
