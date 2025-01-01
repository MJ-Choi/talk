# 1. 프로젝트 개요

사내에서 강연 개최 및 신청을 위한 API

# 2. 개발환경

- jdk 17
- springboot 3.4.1
- mariadb (/container 폴더의 db 파일 실행)

## 2-1. 컨테이너
이 프로젝트 어플리케이션을 실행하고 테스트 하기 위한 컨테이너 구조
```agsl
container/
├── docker-compose.yml
├── init.sql
├── app/
│   ├── app.py 
│   └── config
└── streamlit_img/
    ├── Dockerfile
    └── requirements.txt
```
- `docker-compose.yml`: DB와 테스트를 위한 클라이언트 WEB UI
- `init.sql`: DB에서 사용할 DB, 테이블, 사용자 생성
- `app/`: 로컬 테스트를 위한 클라이언트 web ui. 이 컨테이너는 이 프로젝트를 실행한 후 정상동작한다.
- `streamlit_img/`: 로컬 테스트를 위한 클라이언트 web ui 컨테이너의 base image 생성용.

### 2-1-1. 컨테이너 생성
어플리케이션 테스트를 위한 client ui 컨테이너의 베이스 이미지 생성.
streamlit 라이브러리를 포함하는 파이썬 컨테이너 이미지를 로컬에 생성. 

> ** 한 번 생성 후, 이후에는 실행하지 않아도 된다.

```agsl
$ cd container/streamlit_img/
$ docker build -t streamlit/streamlit:latest .
# docker images 
REPOSITORY            TAG        IMAGE ID       CREATED              SIZE
streamlit/streamlit   latest     e7319ac6d6b2   About a minute ago   621MB

```
### 2-1-2. 컨테이너 실행
어플리케이션에서 사용하는 DB와 테스트를 위한 client ui 컨테이너를 실행
```agsl
$ cd container/
$ docker-compose -f talk-compose.yml up -d
```
## 2-2. 테스트 클라이언트
API 테스트를 위한 클라이언트 UI
웹 브라우저에서 아래와 같이 입력 후 실행
```agsl
http://localhost:8501
```
`Select Menu`에서 backOffice와 front 용 API를 구분하여 테스트 가능


# 3. Data
DB 테이블 데이터

## 3-1. 강연목록

- 테이블명: TALK_LIST

| 컬럼명       | 데이터타입    | 설명        |
|-----------|----------|-----------|
| TALK_ID   | string   | 강연ID (PK) |
| SPEAKER   | string   | 강연자       |
| START_DTM | datetime | 강연시간(강연일) |
| PLACE     | string   | 강연장       |
| SEAT      | number   | 신청가능인원    |
| TALK_DESC | string   | 강연설명      |

## 3-2. 강연신청자

- 테이블명: TALK_MEM

| 컬럼명         | 데이터타입    | 설명                 |
|-------------|----------|--------------------|
| TALK_ID     | string   | 강연ID (PK)          |
| MEM_ID      | string   | 강연신청자 사번 (PK)      |
| STATE       | string   | 상태(01: 등록, 02: 취소) |
| DTM         | datetime | 등록일                |
| UPDATED_DTM | datetime   | 변경일                |

# 4. API 목록

| Method | URL                      | Description                            |
|--------|--------------------------|----------------------------------------|
| GET    | /talk/mgmt               | (back office) 전체 강연 목록 조회              |
| POST   | /talk/mgmt/register      | (back office) 강연 등록                    |
| GET    | /talk/mgmt/list/{talkId} | (back office) 강연 신청자 목록(talkId = 강연ID) |
| GET    | /talk                    | (front) 강연 목록                          |
| POST   | /talk/register           | (front) 강연 신청                          |
| GET    | /talk/list/{id}          | (front) 신청내역 조회(id = 사번)               |
| POST   | /talk/cancel             | (front) 신청한 강연 취소                      |
| GET    | /talk/popular            | (front) 실시간 인기 강연 조회                   |

# 5. todo
swagger 적용
> http://localhost:8008/api/swagger-ui/index.

현재 api 테스트는 postman 또는 python으로 구현한 web 컨테이너를 통해 테스트 가능 