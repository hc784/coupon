import http from 'k6/http';
import { sleep } from 'k6';
import { check } from 'k6';

export const options = {
  vus: 100,            // 동시에 실행할 가상 유저 수
  duration: '30s',     // 테스트 시간 (총 30초)
};

export default function () {
  // ① 랜덤 userId 생성 (1 ~ 10000)
  const userId = Math.floor(Math.random() * 10000) + 1;

  // ② 대기열 등록 요청
  const res1 = http.post(`http://localhost:8080/api/coupon?userId=${userId}`);
  check(res1, {
    '대기열 등록 성공': (r) => r.status === 200 || r.status === 400,
  });

  // ③ 순서 확인 3회 (폴링 시뮬레이션)
  for (let i = 0; i < 3; i++) {
    const res2 = http.get(`http://localhost:8080/api/queue/${userId}`);
    check(res2, {
      '순서 조회 성공': (r) => r.status === 200,
    });
    sleep(1); // 1초 간격으로 폴링
  }
}
