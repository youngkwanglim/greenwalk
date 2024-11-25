# 그린워크(GreenWalk) : 플로깅 환경 보호 앱

## 프로젝트 개요
GreenWalk는 플로깅(조깅 중 쓰레기 줍기) 활동 후 쓰레기의 개수와 걸음수를 포인트로 환산하여 사용자가 원하는 기부처에 포인트를 기부할 수 있는 앱입니다. 이를 통해 환경 보호, 운동 효과, 기부의 기쁨을 동시에 누릴 수 있습니다.

## 팀 역할
- **임영광 :** 팀장 및 백엔드 개발
- **김령래 :** 백엔드 개발, 이미지 분석 및 머신러닝 모델 개발
- **마영준 :** 프론트엔드 개발
- **나희수 :** 프론트엔드 개발

## 주요 기능
- **걸음수 측정 :** 사용자의 걸음수를 자동으로 측정합니다.
- **쓰레기 갯수 카운트 :** 사용자가 업로드한 쓰레기 사진을 분석하여 쓰레기의 개수를 파악합니다.
- **포인트 적립 :** 걸음수와 쓰레기 개수를 기반으로 포인트를 적립합니다.
- **포인트 기부 :** 적립된 포인트를 사용자가 선택한 기부 단체에 기부할 수 있습니다.
- **사진 업로드 및 S3 저장 :** 사용자가 업로드한 쓰레기 사진을 AWS S3에 저장합니다.
- **포인트 및 기부 내역 조회 :** 사용자가 적립한 포인트와 기부 내역을 조회할 수 있습니다.

## 데모

|                        👯 로그인                         |                        🏠 메인 홈                         |                   🏃 플로깅 시작                    |
| :------------------------------------------------------: | :------------------------------------------------------: | :------------------------------------------------: |
| <img src="https://i.postimg.cc/YCQrN9Mj/image.gif" alt="로그인" width="80%">    | <img src="https://i.postimg.cc/CdTydGZ7/image.gif)" alt="메인 홈" width="80%">    | <img src="https://i.postimg.cc/zG5JDNYh/ezgif-com-optimize-1.gif" alt="플로깅 시작" width="80%"> |

|        📸 플로깅 완료 (사진 촬영 및 포인트 적립)         |                          💰 기부                          |                    📜 기부내역 확인                    |
| :------------------------------------------------------: | :------------------------------------------------------: | :------------------------------------------------: |
| <img src="https://i.postimg.cc/pXfGvbB3/image.gif" alt="플로깅 완료" width="80%"> | <img src="https://i.postimg.cc/sg3jczXc/image.gif)" alt="기부" width="80%">       | <img src="https://i.postimg.cc/R0FvkTwJ/image.gif" alt="기부내역 확인" width="80%"> |

</br>
</br>

## 시스템 구성 및 주요 기술 스택
<img src="https://github.com/CodeHanZoom/backend/assets/104816348/3b7f92b1-5c7f-49fc-903a-d2e1b34be56f" alt="시스템 구조" width="600">

## 기술적 문제 해결 과정
### 쓰레기 이미지 분석
- **문제 :** 초기에는 로컬 CPU를 사용하여 이미지 분석을 수행하여 학습 속도가 매우 느렸습니다.
- **해결 :** GPU를 지원하는 Google Colab을 사용하여 학습 속도를 대폭 개선했습니다. 이를 통해 학습 시간이 5번의 epochs에서 3시간 걸리던 것이 20번의 epochs에서 1시간 이하로 단축되었습니다.
- **결과 :** Precision 값이 0.67에서 0.89로 향상되어 쓰레기 탐지 정확도가 크게 개선되었습니다.

### 이미지 처리 성능 개선
- **문제 :** Python의 PIL 라이브러리를 사용하여 이미지 크기를 축소하는 과정에서 시간이 오래 걸렸습니다.
- **해결 :** Java의 Graphics2D 라이브러리를 사용하여 이미지 크기를 축소함으로써 처리 시간을 24초에서 14초로 약 50% 단축했습니다.
- **결과 :** UX가 향상되고, 서버 부하가 감소하였습니다.

## 수상
- 인천대학교 컴퓨터공학부 졸업작품에서 장려상 수상

## 발표 영상
발표 영상은 [여기](https://www.youtube.com/watch?v=BA0t6afDTkk)에서 확인할 수 있습니다.
