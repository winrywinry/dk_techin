# dk_techin 코딩 테스트
* 파일은 반드시 I/O 처리하여  `input.log`에 기록된 로그를 분석하여 통계를 `output.log`에 출력한다.
* 통계의 데이터는 정상 <200> 호출 데이터만을 사용한다.
    * 최다 호출 API KEY : 호출 횟수가 가장 많은 API KEY
    * 상위 3개의 API Service ID 와 각각의 요청 수 : 요청 수가 3위 이내인 API Service ID와 요청 횟수 출력. 요청 횟수가 동일한 경우가 발생하면 어떤 것을 출력해도 상관없음.
    * 웹브라우저별 사용 비율 : 정상요청인 경우. 요청 브라우저의 사용 비율

## Class 구조
* Main.java
    * main() : Controller 에 해당. 파일을 읽고, 쓰는 명령.
* CommonFunc.java
    * Service 에 해당.
    * 파일을 읽고, 쓰는 로직이 작성되어 있음
    * 공통 함수 위치
    * 멤버 변수
        * INPUTLOG : input.log 파일명
        * OUTPUTLOG : output.log 파일명
        * PATH : input.log 파일이 위치한 디렉토리
    * 함수
        * readAFile() : 로그 파일을 읽어 List로 반환
        * writeAFile() : 로그 파일을 분석하여 출력 파일을 작성
        * getApiKey() : URL에서 apikey를 추출.
        * getServiceID() : URL에서 Service ID를 추출.
        * getTopApiKey() : URL 에서 apikey로 group by 하여 가장 많은 apikey를 추출
        * getTopServiceID() : URL 에서 Service ID로 group by 하여 가장 많은 3건의 리스트를 추출.
        * getBrowserRate() : URL 에서 Browser로 group by 하여 List를 반환.
* LogVo.java
    * Bean 에 해당.
    * 로그를 읽어 Bean 형태로 입력함.
    * 멤버 변수
        * status : 상태
        * url : URL
        * browser : 브라우저
        * timestamp : 날짜시간