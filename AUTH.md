# 간단한 역할 인증

Spring Security 필터 없이 JWT + AOP를 사용합니다. 기존 BCrypt 비밀번호 인코더는 유지합니다.

- 회원 로그인: `POST /api/members/login`, JSON `{"code":"회원 코드"}`
- 관리자 로그인: `POST /api/admin/login`, JSON `{"adminId":1,"password":"비밀번호"}`
- 성공 응답: `{"accessToken":"eyJ...","tokenType":"Bearer","expiresIn":2592000}`

관리자 ID로 계정을 찾고 BCrypt로 비밀번호를 비교합니다. 회원은 DB에 일치하는 코드가 있으면 로그인됩니다. JWT에는 role(ADMIN/MEMBER), iat, exp만 담으며 사용자 식별 정보는 넣지 않습니다.

관리자 요청에 `Authorization: Bearer <accessToken>`을 전달하세요. 토큰은 30일 유효하며 만료 후 다시 로그인합니다. 리프레시 토큰, 서버 로그아웃, 즉시 폐기는 없습니다.

## 관리자 기능 추가

컨트롤러 클래스 또는 public 메서드에 `@AdminOnly`를 붙이면 서명·만료·ADMIN 역할을 검사합니다. 현재 AdminApi 전체(관리자 등록 포함)에 적용했습니다. 로그인은 별도 LoginApi라 검사하지 않습니다. 애노테이션이 없는 기능은 공개입니다. Spring AOP이므로 같은 객체 내부의 직접 메서드 호출에는 적용되지 않습니다.

잘못된 로그인/토큰은 401, MEMBER 토큰으로 관리자 기능 접근은 403입니다. 최초 관리자 계정은 기존 DB에 준비되어 있어야 합니다. 등록 API 역시 ADMIN 토큰이 필요하므로 공개 회원가입으로 관리자를 만들 수 없습니다.

## 설정

기본 설정만으로 실행할 수 있습니다. JWT_SECRET이 없으면 실행할 때 무작위 서명 키를 만들므로 재시작하면 기존 토큰을 사용할 수 없습니다. 키를 유지하려면 `openssl rand -base64 32`로 만든 값을 JWT_SECRET 환경변수로 설정하세요. 여러 서버를 실행한다면 같은 키가 필요합니다.

`jwt.expires-in`은 초 단위이며 기본값은 2592000입니다.

## 테스트

`./gradlew test`: 코드 로그인, BCrypt 로그인, 입력 검증, AOP 차단/허용, 만료·서명 위조·만료 없는 토큰 거부를 검증합니다.
