# ootd-be

api, batch 모듈

JDK 17 + Spring Boot 3.0 기반

---
## common 모듈

api 모듈과 batch 모듈에서 같이 사용할지도 모를 기능들 모아놓음.


---

## api 모듈

api 스펙은 ```/api/swagger-ui``` 에서 확인

### 인증

Spring Security + JWT 적용. 자세한 내용은 WIKI 문서 참조(는 아직 작성 안함)

### 무신사 검색 api

CORS 적용여부 체크 안해봄.

그래서 일단 프록시 개념으로 api 서버에서 처리하게 함.

너무 자주 검색하면 차단 당할 위험도 있지 않을까..?

클라이언트단에서 처리 가능한지 확인 필요.(토이니까, 그냥 이대로 둘지도...)



---

## batch 모듈

아직 기능 미정.. 필요없을지도..?
