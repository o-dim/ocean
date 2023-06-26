USE quddls6;

DROP TABLE IF EXISTS KAKAO_APPROVE_RESPONSE;
DROP TABLE IF EXISTS AMOUNT;
DROP TABLE IF EXISTS USERS_ACCESS;
DROP TABLE IF EXISTS OUT_USERS;
DROP TABLE IF EXISTS SLEEP_USERS;
DROP TABLE IF EXISTS REPLY;
DROP TABLE IF EXISTS RECOMMEND;
DROP TABLE IF EXISTS PLAYLIST_DETAIL;
DROP TABLE IF EXISTS SONG;
DROP TABLE IF EXISTS PLAYLIST;
DROP TABLE IF EXISTS HASHTAG_CD;
DROP TABLE IF EXISTS CART_DETAIL;
DROP TABLE IF EXISTS ORDER_TBL;
DROP TABLE IF EXISTS CART;
DROP TABLE IF EXISTS CD;
DROP TABLE IF EXISTS HASHTAG;
DROP TABLE IF EXISTS USERS;






-- 회원 테이블 (아이디를 대체할만한 것이나 ( 이메일로 )   
CREATE TABLE USERS (
   USER_NO              INT             NOT NULL AUTO_INCREMENT,          -- 회원 번호      -PK
   EMAIL                VARCHAR(100)    NOT NULL UNIQUE,            -- 회원 이메일
   PW                   VARCHAR(100)    NOT NULL,                   -- 회원 비밀번호
   PHONENO              LONGTEXT        NOT NULL,                  -- 회원 전화번호
   POSTCODE             VARCHAR(20)        NULL,                     -- 회원 우편번호
   ROAD_ADDRESS         VARCHAR(100)         NULL,                     -- 회원 도로명주소
   JIBUN_ADDRESS        VARCHAR(100)         NULL,                     -- 회원 지번
   DETAIL_ADDRESS       VARCHAR(100)         NULL,                     -- 회원 상세주소
   NAME                 VARCHAR(20)          NULL,                     -- 회원 이름
   JOINED_AT            DATETIME           NULL,                     -- 회원 가입일자
   AGREECODE             INT                 NOT NULL,                  -- 동의여부(0:필수, 1:위치, 2:이벤트, 3:위치+이벤트)
   PW_MODIFIED_AT       DATETIME              NULL,                      -- 비밀번호변경일
   AUTOLOGIN_EMAIL      VARCHAR(100),                                  -- 자동로그인할 때 사용하는 ID(SESSION_ID를 사용함)
   AUTOLOGIN_EXPIRED_AT DATETIME,                                       -- 자동로그인 만료일
   CONSTRAINT PK_USERS PRIMARY KEY(USER_NO)
);


-- 판매용 CD 
CREATE TABLE CD (
   CD_NO            INT            NOT NULL AUTO_INCREMENT,         -- CD 상품번호      -PK
    TITLE            LONGTEXT          NULL,                     -- CD 제목
    SINGER            VARCHAR(40)         NULL,                     -- CD 가수
    MAIN_IMG         LONGTEXT         NULL,                     -- CD 메인이미지 경로(링크)
    DETAIL_IMG         LONGTEXT         NULL,                     -- CD 상세이미지 경로(링크)
    PRICE            INT               NULL,                     -- CD 가격
    STOCK            INT               NULL,                     -- CD 수량
    RECOMMEND_COUNT      INT               NULL,                     -- 추천 갯수
    WRITED_AT         DATETIME             NULL,                  -- 등록 날짜 (최신순 보기위해)
    CONSTRAINT PK_CD PRIMARY KEY(CD_NO)
);


-- 플레이리스트 테이블
CREATE TABLE PLAYLIST (
   PLAYLIST_NO         INT            NOT NULL AUTO_INCREMENT,   -- 플레이리스트 번호       / PK
   USER_NO            INT            NOT NULL,               -- CD 번호            / FK
    TITLE            VARCHAR(40)         NULL,               -- 플레이리스트 이름
    CONSTRAINT PK_PLAYLIST PRIMARY KEY(PLAYLIST_NO),
    CONSTRAINT FK_USER_NO FOREIGN KEY(USER_NO) REFERENCES USERS(USER_NO) ON DELETE CASCADE   
);

-- 스트리밍용 노래 
CREATE TABLE SONG (
   SONG_NO         INT            NOT NULL AUTO_INCREMENT,      -- 노래 번호        / PK
    TITLE         VARCHAR(40)         NULL,                  -- 노래 제목
    SINGER         VARCHAR(40)         NULL,                  -- 가수
    ALBUM         VARCHAR(40)         NULL,                  -- 앨범 명
    SONG_TOTAL      INT               NULL,                  -- 노래 전체갯수
    PLAYLIST_NO      INT            NOT NULL,                  -- 플레이리스트 번호      / FK
    CD_NO         INT            NOT NULL,                  -- CD 상품번호      / FK
    CONSTRAINT PK_SONG PRIMARY KEY(SONG_NO),
    CONSTRAINT FK_PLAYLIST_NO FOREIGN KEY(PLAYLIST_NO) REFERENCES PLAYLIST(PLAYLIST_NO) ON DELETE CASCADE,
    CONSTRAINT FK_CD_NO FOREIGN KEY(CD_NO) REFERENCES CD(CD_NO) ON DELETE CASCADE
);


-- 플레이리스트 바구니 테이블
CREATE TABLE PLAYLIST_DETAIL (
   PLAYLIST_DETAIL_NO   INT         NOT NULL AUTO_INCREMENT,   -- 플레이리스트 바구니 넘버   / PK
    PLAYLIST_NO         INT         NOT NULL,               -- 플레이리스트 번호       / FK
    SONG_NO            INT         NOT NULL,               -- 노래번호            / FK
    CONSTRAINT PK_PLAYLIST_DETAIL PRIMARY KEY(PLAYLIST_DETAIL_NO),
    CONSTRAINT FK_PLAYLIST_NO1 FOREIGN KEY(PLAYLIST_NO) REFERENCES PLAYLIST(PLAYLIST_NO) ON DELETE CASCADE,
   CONSTRAINT FK_SONG_NO FOREIGN KEY(SONG_NO) REFERENCES SONG(SONG_NO) ON DELETE CASCADE
);

-- 추천 테이블
CREATE TABLE RECOMMEND (
   RECOMMEND_NO      INT            NOT   NULL AUTO_INCREMENT,         -- 추천 번호      /PK
    USER_NO            INT            NOT NULL,                     -- 회원 번호      /FK
    CD_NO            INT            NOT NULL,                     -- CD 상품번호   /FK
    CONSTRAINT PK_RECOMMEND PRIMARY KEY(RECOMMEND_NO),
    CONSTRAINT FK_USER_NO1 FOREIGN KEY(USER_NO) REFERENCES USERS(USER_NO) ON DELETE CASCADE,
    CONSTRAINT FK_CD_NO1 FOREIGN KEY(CD_NO) REFERENCES CD(CD_NO) ON DELETE CASCADE
);

-- 해시태그 테이블
CREATE TABLE HASHTAG (
   HT_NO            INT               NOT NULL AUTO_INCREMENT,      -- 해시태그 번호    / PK
   NAME            VARCHAR(40)            NULL,                  -- 해시태그 이름
    CONSTRAINT PK_HASHTAG PRIMARY KEY(HT_NO)
);

-- 해시태그 브릿지 (N : M 관계)
CREATE TABLE HASHTAG_CD (
   HTCD_NO            INT               NOT NULL AUTO_INCREMENT,      -- 해시태그브릿지번호 / PK
   CD_NO            INT               NOT NULL,                  -- CD 상품번호       / FK    
   HT_NO            INT               NOT NULL,                  -- 해시태그 번호     / FK
    CONSTRAINT PK_HTCD PRIMARY KEY(HTCD_NO),
    CONSTRAINT FK_CD_NO2 FOREIGN KEY(CD_NO) REFERENCES CD(CD_NO) ON DELETE CASCADE,
    CONSTRAINT FK_HT_NO FOREIGN KEY(HT_NO) REFERENCES HASHTAG(HT_NO) ON DELETE CASCADE
    
);


-- 장바구니
CREATE TABLE CART (
   CART_NO            INT               NOT NULL AUTO_INCREMENT,      -- 장바구니 번호      / PK
    MADE_AT            DATETIME               NULL,                  -- 장바구니에 담긴 수량
    USER_NO            INT               NOT NULL,                  -- 회원 번호         / FK
    CONSTRAINT PK_CART PRIMARY KEY(CART_NO),
    CONSTRAINT FK_USER_NO2 FOREIGN KEY(USER_NO) REFERENCES USERS(USER_NO) ON DELETE CASCADE
);

-- 상세 장바구니
CREATE TABLE CART_DETAIL (
   CART_DETAIL_NO      INT               NOT NULL AUTO_INCREMENT,      -- 상세 장바구니번호      /PK
   COUNT            INT                  NULL,                  -- 수량
    CART_NO            INT               NOT NULL,                  -- 장바구니 번호      /FK
    CD_NO            INT               NOT NULL,                  -- CD 상품번호         /FK
    CONSTRAINT PK_CART_DETAIL PRIMARY KEY(CART_DETAIL_NO),
    CONSTRAINT FK_CART_NO FOREIGN KEY(CART_NO) REFERENCES CART(CART_NO) ON DELETE CASCADE,
    CONSTRAINT FK_CD_NO3 FOREIGN KEY(CD_NO) REFERENCES CD(CD_NO) ON DELETE CASCADE
);

-- 결제
CREATE TABLE ORDER_TBL (
   ORDER_NO         INT            NOT NULL AUTO_INCREMENT,      -- 결제 번호         / PK
   TOTAL            INT            NOT NULL,                  -- 전체 금액  
    EMAIL            VARCHAR(100)   NOT NULL,                  -- 받는사람 이메일
    PHONENO            LONGTEXT         NULL,                  -- 받는사람 전화번호
    POSTCODE         VARCHAR(100)      NULL,                  -- 받는사람 우편번호
    ROAD_ADDRESS      VARCHAR(100)      NULL,                  -- 받는사람 도로명주소
    JIBUN_ADDRESS      VARCHAR(100)      NULL,                  -- 받는사람 지번
    DETAIL_ADDRESS      VARCHAR(100)      NULL,                  -- 받는사람 상세주소
    NAME            VARCHAR(40)         NULL,                  -- 받는사람 이름
    COUNT            INT            NOT NULL,                  -- CD 구매수량
    ORDER_AT         DATETIME         NULL,                  -- 주문일
    CART_NO            INT            NOT NULL,                  -- 장바구니 번호      / FK
    CD_NO            INT            NOT NULL,                  -- CD상품번호         / FK
    CONSTRAINT PK_ORDER_TBL PRIMARY KEY(ORDER_NO),
    CONSTRAINT FK_CART_NO1 FOREIGN KEY(CART_NO) REFERENCES CART(CART_NO) ON DELETE CASCADE,
    CONSTRAINT FK_CD_NO4 FOREIGN KEY(CD_NO) REFERENCES CD(CD_NO) ON DELETE CASCADE
);


-- 휴면 계정
CREATE TABLE SLEEP_USERS (
   SLEEP_USER_NO      INT               NOT NULL AUTO_INCREMENT,   -- 휴면 회원번호      / PK
    EMAIL            VARCHAR(50)         NOT NULL UNIQUE,         -- 회원 이메일
    PW               VARCHAR(100)         NULL,               -- 회원비밀번호
    PHONENO            VARCHAR(100)      NOT NULL,               -- 회원전화번호
    POSTCODE         VARCHAR(20)            NULL,               -- 회원우편번호
    ROAD_ADDRESS      VARCHAR(100)         NULL,               -- 회원도로명주소
    JIBUN_ADDRESS      VARCHAR(100)         NULL,               -- 회원지번
    DETAIL_ADDRESS      VARCHAR(100)         NULL,               -- 회원 상세주소
    NAME            VARCHAR(30)            NULL,               -- 회원이름
    JOINED_AT         DATETIME            NULL,               -- 회원가입날짜
    AGREECODE          INT              NOT NULL,
    PW_MODIFIED_AT     DATETIME                  NULL,                -- 비밀번호변경일
    SLEPT_AT         DATETIME            NULL,               -- 회원휴면날짜
    CONSTRAINT PK_SLEEP_USERS PRIMARY KEY(SLEEP_USER_NO)
);

-- 탈퇴 회원
CREATE TABLE OUT_USERS (
   OUT_USER_NO         INT               NOT NULL AUTO_INCREMENT,   -- 탈퇴 회원번호      / PK         
    EMAIL            VARCHAR(50)         NOT NULL UNIQUE,         -- 회원이메일
    JOINED_AT         DATETIME               NULL,               -- 회원가입날짜
    OUT_AT            DATETIME               NULL,               -- 회원 탈퇴날짜
    CONSTRAINT PK_OUT_USERS PRIMARY KEY(OUT_USER_NO)
);

-- 댓글 테이블
CREATE TABLE REPLY (
   REPLY_NO         INT            NOT NULL AUTO_INCREMENT,      -- 댓글 번호         / PK
    CONTENT            LONGTEXT         NULL,                  -- 댓글 내용
    GROUP_NO         INT               NULL,                  -- 댓글 그룹번호 (1번 댓글에 몇개 댓글을 달면 그룹넘버가 1이된다.)
    GROUP_ORDER         INT               NULL,                  -- 댓글 오더 ( 대댓글에 필요함 )
    DEPTH            INT               NULL,                  -- 원 댓글은 0 , 대댓글은 + 1
    WRITE_AT         DATETIME         NULL,                  -- 작성 날짜
    USER_NO            INT            NOT NULL,                  -- 회원번호         / FK
    IDOL_NO            INT               NULL,
    CONSTRAINT PK_REPLY PRIMARY KEY(REPLY_NO),
    CONSTRAINT FK_USER_NO3 FOREIGN KEY(USER_NO) REFERENCES USERS(USER_NO) ON DELETE CASCADE
);

-- 회원 접속 기록(회원마다 마지막 로그인 날짜 1개만 기록)
CREATE TABLE USERS_ACCESS (
    EMAIL                VARCHAR(100)    NOT NULL UNIQUE, -- 회원 이메일
    LAST_LOGIN_AT DATETIME,                                -- 마지막 로그인 날짜
   CONSTRAINT FK_USER_ACCESS FOREIGN KEY(EMAIL) REFERENCES USERS(EMAIL) ON DELETE CASCADE
);

CREATE TABLE AMOUNT (
   TOTAL      INT      NOT NULL UNIQUE,
    TAX_FREE   int      NULL,
    VAT         INT      NULL,
    POINT      INT      NULL,
    DISCOUNT   INT      NULL,
    CONSTRAINT PK_TOTAL PRIMARY KEY (TOTAL)
);


CREATE TABLE KAKAO_APPROVE_RESPONSE(
   AID               VARCHAR(100)   NOT NULL UNIQUE,
    TID               LONGTEXT,
    CID               LONGTEXT,
    SID               LONGTEXT,
    PARTNER_ORDER_ID   LONGTEXT,
    PARTNER_USER_ID      LONGTEXT,
    PAYMENT_METHOD_TYPE   LONGTEXT,
    ITEM_NAME         LONGTEXT,
   ITEM_CODE         LONGTEXT,
    QUANTITY         INT,
   CREATED_AT         DATETIME,
    APPROVED_AT         DATETIME,
    CONSTRAINT PK_AID PRIMARY KEY(AID)
);


-- USERS 데이터 삽입
INSERT INTO USERS
            (
             EMAIL
            , PW
            , PHONENO
            , POSTCODE
            , ROAD_ADDRESS
            , JIBUN_ADDRESS
            , DETAIL_ADDRESS
            , NAME
            , JOINED_AT
         , AGREECODE
         , PW_MODIFIED_AT 
            , AUTOLOGIN_EMAIL 
            , AUTOLOGIN_EXPIRED_AT
            ) VALUES (
             'admin@gmail.com'
            , SHA2('mango123!',256)
            , '010-1111-0000'
            , '15226'
            , '서울시 가산'
            , '서울시 가산'
            , '4885호'
            , '관리자'
            , NOW()
         ,0
         ,NULL
         ,NULL
         ,NULL
);

INSERT INTO USERS
            (
             EMAIL
            , PW
            , PHONENO
            , POSTCODE
            , ROAD_ADDRESS
            , JIBUN_ADDRESS
            , DETAIL_ADDRESS
            , NAME
            , JOINED_AT
               , AGREECODE
               , PW_MODIFIED_AT 
            , AUTOLOGIN_EMAIL 
            , AUTOLOGIN_EXPIRED_AT
            ) VALUES (
             'james@naver.com'
            , SHA2('mango123!',256)
            , '010-1111-0000'
            , '15226'
            , '경기도 시흥시 꽃게로 353'
            , '경기도 시흥시 꽃게동 353'
            , '101호'
            , 'james'
            , NOW()
                ,0
                ,NULL
                ,NULL
                ,NULL
);

INSERT INTO USERS
            (
             EMAIL
            , PW
            , PHONENO
            , POSTCODE
            , ROAD_ADDRESS
            , JIBUN_ADDRESS
            , DETAIL_ADDRESS
            , NAME
            , JOINED_AT
               , AGREECODE
               , PW_MODIFIED_AT 
               , AUTOLOGIN_EMAIL 
            , AUTOLOGIN_EXPIRED_AT
            ) VALUES (
                  'john@gmail.com'
            , SHA2('mango123!',256)
            , '010-1111-1111'
            , '17425'
            , '경기도 안산시 대부도로 123'
            , '경기도 안산시 대부도동 123'
            , '203호'
            , 'john'
            , '20220101'
                , 0
                ,NULL
                ,NULL
            ,NULL
);
INSERT INTO USERS
            (
             EMAIL
            , PW
            , PHONENO
            , POSTCODE
            , ROAD_ADDRESS
            , JIBUN_ADDRESS
            , DETAIL_ADDRESS
            , NAME
            , JOINED_AT
               , AGREECODE
               , PW_MODIFIED_AT 
               , AUTOLOGIN_EMAIL 
            , AUTOLOGIN_EXPIRED_AT
            ) VALUES (
                  'wendy@kakao.com'
            , SHA2('mango123!',256)
            , '010-1111-2222'
            , '15861'
            , '경기도 군포시 산본로 353'
            , '경기도 군포시 산본동 353'
            , '512호'
            , 'wendy'
            , NOW()
                , 0
                ,NULL
                ,NULL
                ,NULL
);
INSERT INTO USERS
            (
             EMAIL
            , PW
            , PHONENO
            , POSTCODE
            , ROAD_ADDRESS
            , JIBUN_ADDRESS
            , DETAIL_ADDRESS
            , NAME
            , JOINED_AT
               , AGREECODE
               , PW_MODIFIED_AT 
               , AUTOLOGIN_EMAIL 
            , AUTOLOGIN_EXPIRED_AT
            ) VALUES (
              'winter@naver.com'
            , SHA2('mango123!',256)
            , '010-1111-3333'
            , '16880'
            , '경기도 안양시 평촌로 211'
            , '경기도 안양시 평촌동 211'
            , '608호'
            , 'winter'
            , NOW()
                , 0
                ,NULL
                ,NULL
                ,NULL
);

INSERT INTO USERS
            (
             EMAIL
            , PW
            , PHONENO
            , POSTCODE
            , ROAD_ADDRESS
            , JIBUN_ADDRESS
            , DETAIL_ADDRESS
            , NAME
            , JOINED_AT
               , AGREECODE
               , PW_MODIFIED_AT 
               , AUTOLOGIN_EMAIL 
            , AUTOLOGIN_EXPIRED_AT
            ) VALUES (
              'joy@gmail.com'
            , SHA2('mango123!',256)
            , '010-1111-4444'
            , '16271'
            , '수원특례시 수원화성로 142'
            , '수원특례시 수원화성동 142'
            , '1102호'
            , 'joy'
            , NOW()
                , 0
                ,NULL
                ,NULL
                ,NULL
);
INSERT INTO USERS
            (
             EMAIL
            , PW
            , PHONENO
            , POSTCODE
            , ROAD_ADDRESS
            , JIBUN_ADDRESS
            , DETAIL_ADDRESS
            , NAME
            , JOINED_AT
               , AGREECODE
               , PW_MODIFIED_AT 
               , AUTOLOGIN_EMAIL 
            , AUTOLOGIN_EXPIRED_AT
            ) VALUES (
             'eric@kakao.com'
            , SHA2('mango123!',256)
            , '010-1111-5555'
            , '52414'
            , '경상남도 남해군 시금치로 525'
            , '경상남도 남해군 시금치동 525'
            , '304호'
            , 'eric'
            , NOW()
                , 0
                ,NULL
                ,NULL
                ,NULL
);
INSERT INTO USERS
            (
             EMAIL
            , PW
            , PHONENO
            , POSTCODE
            , ROAD_ADDRESS
            , JIBUN_ADDRESS
            , DETAIL_ADDRESS
            , NAME
            , JOINED_AT
               , AGREECODE
               , PW_MODIFIED_AT 
               , AUTOLOGIN_EMAIL 
            , AUTOLOGIN_EXPIRED_AT
            ) VALUES (
                  'harry@naver.com'
            , SHA2('mango123!',256)
            , '010-1111-6666'
            , '17959'
            , '경기도 평택시 송탄로 651'
            , '경기도 평택시 송탄동 651'
            , '510호'
            , 'harry'
            , NOW()
                , 0
                ,NULL
                ,NULL
                ,NULL

);
INSERT INTO USERS
            (
             EMAIL
            , PW
            , PHONENO
            , POSTCODE
            , ROAD_ADDRESS
            , JIBUN_ADDRESS
            , DETAIL_ADDRESS
            , NAME
            , JOINED_AT
               , AGREECODE
               , PW_MODIFIED_AT 
               , AUTOLOGIN_EMAIL 
            , AUTOLOGIN_EXPIRED_AT
            ) VALUES (
              'som@gmail.com'
            , SHA2('mango123!',256)
            , '010-1111-7777'
            , '41962'
            , '대구광역시 막창로 122'
            , '대구광역시 막창동 122'
            , '409호'
            , 'som'
            , NOW()
                , 0
                ,NULL
                ,NULL
                ,NULL

);
INSERT INTO USERS
            (
             EMAIL
            , PW
            , PHONENO
            , POSTCODE
            , ROAD_ADDRESS
            , JIBUN_ADDRESS
            , DETAIL_ADDRESS
            , NAME
            , JOINED_AT
               , AGREECODE
               , PW_MODIFIED_AT 
               , AUTOLOGIN_EMAIL 
            , AUTOLOGIN_EXPIRED_AT
            ) VALUES (
              'zico@kakao.com'
            , SHA2('mango123!',256)
            , '010-1111-8888'
            , '16103'
            , '경기도 의왕시 철도박물관로 65'
            , '경기도 의왕시 철도박물관동 65'
            , '807호'
            , 'zico'
            , NOW()
                , 0
                ,NULL
                ,NULL
                ,NULL
);

INSERT INTO USERS
            (
             EMAIL
            , PW
            , PHONENO
            , POSTCODE
            , ROAD_ADDRESS
            , JIBUN_ADDRESS
            , DETAIL_ADDRESS
            , NAME
            , JOINED_AT
               , AGREECODE
               , PW_MODIFIED_AT 
               , AUTOLOGIN_EMAIL 
            , AUTOLOGIN_EXPIRED_AT
            ) VALUES (
             'felix@naver.com'
            , SHA2('mango123!',256)
            ,'010-1111-9999'
            ,'10868'
            ,'경기도 파주시 북스테이로 155'
            ,'경기도 파주시 북스테이동 155'
            ,'101호'
            ,'felix'
            , NOW()
                , 0
                ,NULL
                ,NULL
                ,NULL
);

INSERT INTO USERS
            (
             EMAIL
            , PW
            , PHONENO
            , POSTCODE
            , ROAD_ADDRESS
            , JIBUN_ADDRESS
            , DETAIL_ADDRESS
            , NAME
            , JOINED_AT
               , AGREECODE
               , PW_MODIFIED_AT 
               , AUTOLOGIN_EMAIL 
            , AUTOLOGIN_EXPIRED_AT
            ) VALUES (
             'merry@nate.com'
            , SHA2('mango123!',256)
            ,'010-2222-0000'
            ,'57305'
            ,'전라남도 담양군 떡갈비로 85'
            ,'전라남도 담양군 떡갈비동 85'
            ,'102호'
            ,'merry'
            , NOW()
                , 0
                ,NULL
                ,NULL
                ,NULL
);
INSERT INTO USERS
            (
             EMAIL
            , PW
            , PHONENO
            , POSTCODE
            , ROAD_ADDRESS
            , JIBUN_ADDRESS
            , DETAIL_ADDRESS
            , NAME
            , JOINED_AT
               , AGREECODE
               , PW_MODIFIED_AT 
               , AUTOLOGIN_EMAIL 
            , AUTOLOGIN_EXPIRED_AT
            ) VALUES (
             'bella@naver.com'
            , SHA2('mango123!',256)
            ,'010-2222-1111'
            ,'54878'
            ,'전라북도 전주시 비빔밥로 74'
            ,'전라북도 전주시 비빔밥동 74'
            ,'2103호'
            ,'bella'
            , NOW()
                , 0
                ,NULL
                ,NULL
                ,NULL
);
INSERT INTO USERS
            (
             EMAIL
            , PW
            , PHONENO
            , POSTCODE
            , ROAD_ADDRESS
            , JIBUN_ADDRESS
            , DETAIL_ADDRESS
            , NAME
            , JOINED_AT
               , AGREECODE
               , PW_MODIFIED_AT 
               , AUTOLOGIN_EMAIL 
            , AUTOLOGIN_EXPIRED_AT
            ) VALUES (
             'miny@daum.net'
            , SHA2('mango123!',256)
            ,'010-2222-2222'
            ,'16271'
            ,'수원특례시 행궁로 111'
            ,'수원특례시 행궁동 111'
            ,'1204호'
            ,'miny'
            , NOW()
                , 0
                ,NULL
                ,NULL
                ,NULL
);
INSERT INTO USERS
            (
             EMAIL
            , PW
            , PHONENO
            , POSTCODE
            , ROAD_ADDRESS
            , JIBUN_ADDRESS
            , DETAIL_ADDRESS
            , NAME
            , JOINED_AT
               , AGREECODE
               , PW_MODIFIED_AT 
               , AUTOLOGIN_EMAIL 
            , AUTOLOGIN_EXPIRED_AT
            ) VALUES (
             'bell@kakao.com'
            , SHA2('mango123!',256)
            ,'010-2222-3333'
            ,'15856'
            ,'강원도 양구군 금강산로 454'
            ,'강원도 양구군 금강산동 454'
            ,'506호'
            ,'bell'
            , NOW()
                , 0
                ,NULL
                ,NULL
                ,NULL
);
INSERT INTO USERS
            (
             EMAIL
            , PW
            , PHONENO
            , POSTCODE
            , ROAD_ADDRESS
            , JIBUN_ADDRESS
            , DETAIL_ADDRESS
            , NAME
            , JOINED_AT
               , AGREECODE
               , PW_MODIFIED_AT 
               , AUTOLOGIN_EMAIL 
            , AUTOLOGIN_EXPIRED_AT
            ) VALUES (
            'peter@gmail.com'
            , SHA2('mango123!',256)
            ,'010-2222-4444'
            ,'21530'
            ,'인천광역시 차이나타운로 744'
            ,'인천광역시 차이나타운동 744'
            ,'801호'
            ,'peter'
            , NOW()
                , 0
                ,NULL
                ,NULL
                ,NULL
);
INSERT INTO USERS
            (
             EMAIL
            , PW
            , PHONENO
            , POSTCODE
            , ROAD_ADDRESS
            , JIBUN_ADDRESS
            , DETAIL_ADDRESS
            , NAME
            , JOINED_AT
               , AGREECODE
               , PW_MODIFIED_AT 
               , AUTOLOGIN_EMAIL 
            , AUTOLOGIN_EXPIRED_AT
            ) VALUES (
             'pan@naver.com'
            , SHA2('mango123!',256)
            ,'010-2222-5555'
            ,'44412'
            ,'울산광역시 쫀드기로 333'
            ,'울산광역시 쫀드기동 333'
            ,'302호'
            ,'pan'
            , NOW()
                , 0
                ,NULL
                ,NULL
                ,NULL
);

INSERT INTO USERS
            (
             EMAIL
            , PW
            , PHONENO
            , POSTCODE
            , ROAD_ADDRESS
            , JIBUN_ADDRESS
            , DETAIL_ADDRESS
            , NAME
            , JOINED_AT
               , AGREECODE
               , PW_MODIFIED_AT 
               , AUTOLOGIN_EMAIL 
            , AUTOLOGIN_EXPIRED_AT
            ) VALUES (
             'jobs@naver.com'
            , SHA2('mango123!',256)
            ,'010-2222-6666'
            ,'46978'
            ,'부산광역시 해운대로 233'
            ,'부산광역시 해운대동 233'
            ,'604호'
            ,'jobs'
            , NOW()
                , 0
                ,NULL
                ,NULL
                ,NULL
);
INSERT INTO USERS
            (
             EMAIL
            , PW
            , PHONENO
            , POSTCODE
            , ROAD_ADDRESS
            , JIBUN_ADDRESS
            , DETAIL_ADDRESS
            , NAME
            , JOINED_AT
               , AGREECODE
               , PW_MODIFIED_AT 
               , AUTOLOGIN_EMAIL 
            , AUTOLOGIN_EXPIRED_AT
            ) VALUES (
             'nick@naver.com'
            , SHA2('mango123!',256)
            ,'010-2222-7777'
            ,'17585'
            ,'경기도 안성시 탕면로 522'
            ,'경기도 안성시 탕면동 522'
            ,'805호'
            ,'nick'
            , NOW()
            , 0
                ,NULL
                ,NULL
                ,NULL
);
INSERT INTO USERS
            (
             EMAIL
            , PW
            , PHONENO
            , POSTCODE
            , ROAD_ADDRESS
            , JIBUN_ADDRESS
            , DETAIL_ADDRESS
            , NAME
            , JOINED_AT
               , AGREECODE
               , PW_MODIFIED_AT 
               , AUTOLOGIN_EMAIL 
            , AUTOLOGIN_EXPIRED_AT
            ) VALUES (
             'kate@nate.com'
            , SHA2('mango123!',256)
            ,'010-2222-8888'
            ,'34194'
            ,'대전광역시 두루치기로 242'
            ,'대전광역시 두루치기동 242'
            ,'708호'
            ,'kate'
            , NOW()
            , 0
                ,NULL
                ,NULL
                ,NULL
);
INSERT INTO USERS
            (
             EMAIL
            , PW
            , PHONENO
            , POSTCODE
            , ROAD_ADDRESS
            , JIBUN_ADDRESS
            , DETAIL_ADDRESS
            , NAME
            , JOINED_AT
               , AGREECODE
               , PW_MODIFIED_AT 
               , AUTOLOGIN_EMAIL 
            , AUTOLOGIN_EXPIRED_AT
            ) VALUES (
             'kathy@daum.net'
            , SHA2('mango123!',256)
            ,'010-2222-9999'
            ,'24535'
            ,'강원도 양구군 옥수수로 456'
            ,'강원도 양구군 옥수수동 456'
            ,'904호'
            ,'kathy'
            , NOW()
                , 0
                ,NULL
                ,NULL
                ,NULL
);

INSERT INTO SLEEP_USERS
         (
             EMAIL
            , PW
            , PHONENO
            , POSTCODE
            , ROAD_ADDRESS
            , JIBUN_ADDRESS
            , DETAIL_ADDRESS
            , NAME
            , JOINED_AT
            , AGREECODE
         , PW_MODIFIED_AT 
         , SLEPT_AT 
            ) VALUES (
         'john@gmail.com'
            , SHA2('mango123!',256)
            , '010-1111-1111'
            , '17425'
            , '경기도 안산시 대부도로 123'
            , '경기도 안산시 대부도동 123'
            , '203호'
            , 'john'
            ,  DATE_SUB(DATE(NOW()), INTERVAL 1 YEAR)
            ,0
         ,NULL
         ,NOW()
);




INSERT INTO HASHTAG (
         NAME
         ) VALUES (
         'new'
);

INSERT INTO HASHTAG (
         NAME
         ) VALUES (
         'pre-order'
);

INSERT INTO HASHTAG (
         NAME
         ) VALUES (
         'HOT'
);

INSERT INTO HASHTAG (
         NAME
         ) VALUES (
         'TOP100'
);

INSERT INTO HASHTAG (
         NAME
         ) VALUES (
         '선착순특전'
);

INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          '★★★★★ 5-STAR STANDARD EDITION 3RD ALBUM'
         ,'스트레이 키즈(STRAY KIDS)'
         ,'/quddls6/storage/cdImg/eaecc4f0bfcf40beabd1e2aca7d4764b.png'
       ,'/quddls6/storage/cdImg/36c1099c04e64385b3898eb0584dae45.png'
         ,15000
         ,100
       ,0
         ,NOW()
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          '10TH MINI ALBUM'
         ,'세븐틴'
         ,'/quddls6/storage/cdImg/1-1_5c6d645f-3904-43ca-b9ef-8e9bd7e0bcc6_960x.png'
         ,'/quddls6/storage/cdImg/7_94b60244-3b7f-429c-8df5-a9ca0d5c85f8_1080x.png'
         ,24000
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        ) VALUES (
        'TEDDY BEAR 4TH SINGLE ALBUM'
         ,'스테이씨(STAYC)'
         ,'/quddls6/storage/cdImg/1_4a466016-d60b-4517-8785-0d3180f345bf_960x.png'
         ,'/quddls6/storage/cdImg/2_2d1b6a01-35a0-4738-8d8e-188630299bb6_720x.png'
         ,16000
         ,100
       ,0
         ,NOW()         
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
       ) VALUES (
          'MY WORLD [ZINE VER.] 3RD MINI ALBUM'
         ,'에스파(aespa)'
         ,'/quddls6/storage/cdImg/0_cda88c8b-3723-4b1f-889b-c51aca0f2ed8_960x.png'
         ,'/quddls6/storage/cdImg/2_aec3bf78-ccfb-49ba-821e-4e5980f5184a_1512x.png'
         ,17000
         ,100
       ,0
         ,NOW()         
);

INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
       ) VALUES (
          'THE BILLAGE OF PERCEPTION: CHAPTER TWO ALBUM'
         ,'빌리(BILLLIE)'
         ,'/quddls6/storage/cdImg/1_6ffe5ccb-2d9c-49a2-a58e-1998290fc400_960x.png'
         ,'/quddls6/storage/cdImg/2_7571c27b-0ad0-43b2-87a9-1abe988d8702_1512x.png'
         ,14000
         ,100
       ,0
         ,NOW()        
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'FLASH'
         ,'로켓 펀치(ROCKET PUNCH)'
         ,'/quddls6/storage/cdImg/15_151b72ec-a36c-4064-93a4-7ebb2c0cc16e_960x_11zon.png'
         ,'/quddls6/storage/cdImg/2_fe9cf172-655c-4ba9-99bc-e23269d4235c_720x_11zon.png'
         ,19000
         ,100
       ,0
         ,NOW()         
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'SAVAGE (1ST MINI ALBUM) [HALLUCINATION QUEST VER.] ALBUM'
         ,'에스파(aespa)'
         ,'/quddls6/storage/cdImg/1_42b8f051-1034-4641-849a-83f1fa43ca8d_960x_11zon.png'
         ,'/quddls6/storage/cdImg/2_b2db5189-0562-4b2e-8e99-6727ae4fd333_1512x.png'
         ,20000
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'MONSTER [BASE NOTE VER.] (1ST MINI ALBUM) ALBUM'
         ,'아이린 & 슬기'
         ,'/quddls6/storage/cdImg/1_0b44b3e1-a454-48c1-9e7d-587d2d62d245_960x_11zon.png'
         ,'/quddls6/storage/cdImg/2_e13c7ceb-d547-4df3-9a62-cddc26640af3_1512x_11zon.png'
         ,23000
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          '28 REASONS [SPECIAL VER.] 1ST MINI ALBUM'
         ,'슬기(Red Velvet)'
         ,'/quddls6/storage/cdImg/1_6edc4cff-02b2-4080-af22-4a3d2d5a1b1b_960x.png'
         ,'/quddls6/storage/cdImg/2_15a6aeb4-267b-4459-81be-25783c3632e6_1512x.png'
         ,13000
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'SHAPE OF LOVE 11TH MINI ALBUM'
         ,'몬스타 X(MONSTA X)'
         ,'/quddls6/storage/cdImg/1_1fd27125-baf8-40c8-b7d6-e244d629fa53_960x_11zon.png'
         ,'/quddls6/storage/cdImg/2_e13c7ceb-d547-4df3-9a62-cddc26640af3_1512x_11zon.png'
         ,22000
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'LOVE DIVE 2ND SINGLE ALBUM'
         ,'아이브(IVE)'
         ,'/quddls6/storage/cdImg/1_901e9eea-4d69-43cb-887f-6520c1ce0b5a_960x_11zon.png'
         ,'/quddls6/storage/cdImg/3_b3937e66-5632-4ea1-8302-2c549aecfc5a_1024x1024_11zon.png'
         ,17500
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          '2ND MINI ALBUM ANTIFRAGILE'
         ,'르세라핌(LE SSERAFIM)'
         ,'/quddls6/storage/cdImg/0_57d3c9a3-eaf3-4983-8ee4-7c96c95518f0_960x_11zon.png'
         ,'/quddls6/storage/cdImg/1_0ea079d1-0bad-4d8a-858f-a5d41d6c1fe5_1512x_11zon.png'
         ,16900
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'NEVER GONNA DANCE AGAIN EXTENDED ALBUM'
         ,'태민(SHINEE)'
         ,'/quddls6/storage/cdImg/61WUEMO8AvL._AC_SL1000_960x_11zon.png'
         ,'/quddls6/storage/cdImg/61qQXejoOWL._AC_SL1289_1512x_11zon.png'
         ,15700
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'NEVER GONNA DANCE AGAIN : ACT 1 (VOL.3) ALBUM'
         ,'태민(SHINEE)'
         ,'/quddls6/storage/cdImg/71q6eTW0AlL._AC_SL1000__1_960x_11zon.png'
         ,'/quddls6/storage/cdImg/61V5U5GtgvL._AC_SL1500_1512x_11zon.png'
         ,14800
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'FOREVER 1 [DELUXE VER.] 7TH ALBUM'
         ,'소녀시대(SNSD GIRLS GENERATION)'
         ,'/quddls6/storage/cdImg/5_b8fcfc8c-a7ce-47d4-8ad0-b861a474ec13_960x_11zon.png'
         ,'/quddls6/storage/cdImg/e8423605bdb8cf0557964f5a1cb5fd13_5901df6a-c8ac-41ec-b457-6be55a03887a_1512x_11zon.png'
         ,19900
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'THE REVE FESTIVAL 2022 : FEEL MY RHYTHM [REVE VER.] ALBUM'
         ,'레드벨벳(Red Velvet)'
         ,'/quddls6/storage/cdImg/0_cca4d598-46fe-429a-847e-6cc9cebb198f_960x_11zon.png'
         ,'/quddls6/storage/cdImg/1_2ab9d275-4fe1-459b-ba6c-4237405886e4_1512x_11zon.png'
         ,20000
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'AY-YO [SMINI VER.] SMART ALBUM'
         ,'NCT 127'
         ,'/quddls6/storage/cdImg/1_12f96833-bb88-4faf-a8b6-ee102d938fe2_960x.png'
         ,'/quddls6/storage/cdImg/e8423605bdb8cf0557964f5a1cb5fd13_a42dffdb-1083-4512-9e2c-2c996a17561c_1512x.png'
         ,24700
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'BE AWAKE (8TH MINI ALBUM)'
         ,'더보이즈(THE BOYZ)'
         ,'/quddls6/storage/cdImg/1_98a45b7b-3ecc-472f-9a47-d3462d155cf6_960x.png'
         ,'/quddls6/storage/cdImg/2_f81429e2-f69d-4d9e-8106-b748e59731a6_1512x.png'
         ,22500
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'MAXIDENT [STANDARD EDITION]'
         ,'스트레이 키즈(STRAY KIDS)'
         ,'/quddls6/storage/cdImg/15_51a45334-bcd1-410c-a365-63c83e79cb5d_960x.png'
         ,'/quddls6/storage/cdImg/2_958b04bf-1cba-475f-9f3c-589b00b9faa7_1512x.png'
         ,19400
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'DIMENSION : DILEMMA ALBUM'
         ,'엔하이픈(ENHYPEN)'
         ,'/quddls6/storage/cdImg/1_701486d3-98d9-4caf-948a-93870a4421d1_960x.png'
         ,'/quddls6/storage/cdImg/2_23afd3fe-ecd0-4d15-96cf-35e0d3628ee4_1512x.png'
         ,24200
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'NCT #127 NEO ZONE [T VER.] CD'
         ,'NCT 127'
         ,'/quddls6/storage/cdImg/0_e50e55af-badc-4117-b8ac-06ac2e96c471_960x.png'
         ,'/quddls6/storage/cdImg/1_bea4b010-ede8-4b87-aed9-842eee1dfa04_1512x.png'
         ,27000
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'HOUSE OF TRICKY : DOORBELL RINGING [ROCK VER.]'
         ,'XIKERS'
         ,'/quddls6/storage/cdImg/1_e48a899a-9d99-4eb1-8f68-4b19c16d54aa_960x.png'
         ,'/quddls6/storage/cdImg/2_66bcfed6-882e-46c7-92e4-42009d56e8f3_1512x.png'
         ,16800
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'PYGMALION [MAIN VER.] 9TH MINI ALBUM'
         ,'원어스(ONEUS)'
         ,'/quddls6/storage/cdImg/1_b247ab8a-832c-4a35-a620-d3a533b9b365_960x.png'
         ,'/quddls6/storage/cdImg/e8423605bdb8cf0557964f5a1cb5fd13_0b30d014-f2c5-4be3-bbb6-b5bee0441f82_1512x.png'
         ,13400
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          '1ST STUDIO ALBUM UNFORGIVEN'
         ,'르세라핌(LE SSERAFIM)'
         ,'/quddls6/storage/cdImg/1_da62eb45-607e-4097-8cc4-1405e0cd883d_960x.png'
         ,'/quddls6/storage/cdImg/2_2c8a8db5-b368-44a2-830f-c66bcf9cef05_1512x.png'
         ,15000
         ,100
       ,0
         ,NOW()          
);

INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'LOVESTRUCK! 4TH MINI ALBUM'
         ,'케플러(KEP1ER)'
         ,'/quddls6/storage/cdImg/1_431caced-fbec-43b4-afd4-4304f4720ec4_960x.png'
         ,'/quddls6/storage/cdImg/1_2b242452-a15d-42d4-b572-77f5688be57e_1512x.png'
         ,13000
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'IVE IVE (VOL.1) ALBUM'
         ,'아이브(IVE)'
         ,'/quddls6/storage/cdImg/1_b5005fa9-950b-4bca-9eaa-e2ada6b39f2f_960x.png'
         ,'/quddls6/storage/cdImg/2_67c791c6-468f-409b-a580-4daeef153010_1512x.png'
         ,20000
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'ROSE BLOSSOM (1ST MINI ALBUM)'
         ,'하이키(H1-KEY)'
         ,'/quddls6/storage/cdImg/1_796f8900-4ad5-4a13-b6c3-7199dda4f3e5_960x.png'
         ,'/quddls6/storage/cdImg/2_7b0b0249-d452-454b-9193-9ae09d45ba9c_1512x.png'
         ,16000
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'INSERT COIN (3RD EP ALBUM)'
         ,'루시(LUCY)'
         ,'/quddls6/storage/cdImg/1_a4ded31b-a2e4-4aef-927b-fd3fa980130d_960x.png'
         ,'/quddls6/storage/cdImg/2_edd99654-316e-4474-b827-cc81a17de309_1512x.png'
         ,15000
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          '3RD MINI ALBUM BAMBI'
         ,'백현(EXO)'
         ,'/quddls6/storage/cdImg/1_4b928287-2eb7-49d2-8b01-f9a10ee795af_960x.png'
         ,'/quddls6/storage/cdImg/2_8acf81ea-27c0-4300-a242-827fa6741ddd_1512x.png'
         ,20000
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'NOT SHY'
         ,'잇지(ITZY)'
         ,'/quddls6/storage/cdImg/NOTSHY-ONLINECOVER-fix_002_960x.png'
         ,'/quddls6/storage/cdImg/2_6fa036ac-1b02-4dfc-8e6e-1cfeffabf7e0_1512x.png'
         ,16200
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'STAMP ON IT [SMINI VER.]'
         ,'GOT THE BEAT'
         ,'/quddls6/storage/cdImg/1_0497edb2-2c10-4808-a0ab-8bd084d3a4d4_960x.png'
         ,'/quddls6/storage/cdImg/e8423605bdb8cf0557964f5a1cb5fd13_18674736-3e65-452b-a1d7-d2f5f183eeee_1512x.png'
         ,18600
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'CHESHIRE [SPECIAL EDITION] ALBUM'
         ,'잇지(ITZY)'
         ,'/quddls6/storage/cdImg/1_bb0996ad-c396-468b-a961-4d04bd1bd92e_960x.png'
         ,'/quddls6/storage/cdImg/e8423605bdb8cf0557964f5a1cb5fd13_99054511-882e-44ce-9ad7-c263efc54a41_1512x.png'
         ,21000
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'THE ASTRONAUT'
         ,'진'
         ,'/quddls6/storage/cdImg/0_efb4fa12-e048-4d80-b2c0-0cab3e73305a_960x.png'
         ,'/quddls6/storage/cdImg/1_f8b40a83-cf48-4958-925e-2d0a546602c9_1512x.png'
         ,24000
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'APOCALYPSE : FOLLOW US [T VER.(LIMITED EDITION)] 7TH MINI ALBUM'
         ,'드림캐쳐(DREAMCATCHER)'
         ,'/quddls6/storage/cdImg/1_daf7e8bb-f17b-47eb-b057-0eff1d152d32_960x.png'
         ,'/quddls6/storage/cdImg/2_464f08e8-1aa4-4112-a06c-24630de7046f_1512x.png'
         ,15000
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'I LOVE 5TH MINI ALBUM'
         ,'여자아이들((G)I-DLE)'
         ,'/quddls6/storage/cdImg/15_01e63fc2-1f02-4e78-9eb9-60cd13834a68_960x.png'
         ,'/quddls6/storage/cdImg/4_de4a1d47-55c8-42aa-9141-ea3b036b92a3_1512x.png'
         ,17900
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          '2 BADDIES [PHOTOBOOK VER.] 4TH ALBUM'
         ,'NCT 127'
         ,'/quddls6/storage/cdImg/1_9163af3a-a7a6-470c-9e0d-0cacb1162c94_960x.png'
         ,'/quddls6/storage/cdImg/2_621a089e-cbe5-4854-bd35-38f929a1abbe_1512x.png'
         ,30000
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'GIRLS [KWANGYA VER.] 2ND MINI ALBUM'
         ,'에스파(aespa)'
         ,'/quddls6/storage/cdImg/1_2d5607ba-7d3e-4dc9-8f63-3c667c402b06_960x.png'
         ,'/quddls6/storage/cdImg/2_fe3f4da9-7e32-460d-9643-73c55f743c30_1512x.png'
         ,15200
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'SHALALA [DIGIPACK VER.] 1ST MINI ALBUM'
         ,'태용(NCT 127)'
         ,'/quddls6/storage/cdImg/1_59a3e452-99a1-4157-ae6f-92f37a909063_960x.png'
         ,'/quddls6/storage/cdImg/e8423605bdb8cf0557964f5a1cb5fd13_b1b28fea-f377-431c-b740-3a32d572ac23_1512x.png'
         ,18700
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'GASOLINE [BOOKLET VER.] 2ND ALBUM'
         ,'키(SHINEE)'
         ,'/quddls6/storage/cdImg/0_111da63e-1728-4798-a456-f5d9bf60a662_960x.png'
         ,'/quddls6/storage/cdImg/1_119722fa-430e-44fd-9790-832ae77044b5_1512x.png'
         ,23200
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          '2ND MINI ALBUM OVER THE MOON CD'
         ,'이채연'
         ,'/quddls6/storage/cdImg/1_b4fffda4-5fd1-454b-b161-ab483ef4c615_960x.png'
         ,'/quddls6/storage/cdImg/2_2fcff1bf-ee23-4ea3-925c-b7c65310f459_1512x.png'
         ,17500
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          '3RD MINI ALBUM LETHALITY CD'
         ,'권은비'
         ,'/quddls6/storage/cdImg/1_9011a5b9-c8c0-49da-9a90-bf43b88703d9_960x.png'
         ,'/quddls6/storage/cdImg/2_e4c99f31-718d-4519-b4fe-937421582b3a_1512x.png'
         ,16300
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'IM NAYEON 1ST MINI ALBUM'
         ,'나연(TWICE)'
         ,'/quddls6/storage/cdImg/1_63c26614-50ad-4574-8fca-067b0791f9bd_960x.png'
         ,'/quddls6/storage/cdImg//2_94b3cf51-5da7-41fa-8316-a07b09e963a4_1512x.png'
         ,15900
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'HELLO [CASE VER.] ALBUM'
         ,'조이(RED VELVET)'
         ,'/quddls6/storage/cdImg/CaseVer._960x.png'
         ,'/quddls6/storage/cdImg/1_8978f11e-ce78-4cb4-b5c9-edd2c4a71374_1512x.png'
         ,18000
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'INDIGO [BOOK EDITION] ALBUM'
         ,'RM(BTS)'
         ,'/quddls6/storage/cdImg/1_cd691936-83ef-448a-b021-6a210896cc07_960x.png'
         ,'/quddls6/storage/cdImg/2_7b366a97-dbe3-4537-839d-e2dfba69cf1f_1512x.png'
         ,20000
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'JACK IN THE BOX (WEVERSE ALBUMS)'
         ,'제이홉(BTS)'
         ,'/quddls6/storage/cdImg/0_d609588c-9759-4908-9934-75b03624e2b0_960x.png'
         ,'/quddls6/storage/cdImg/1_3567181d-ea6a-4a27-bc73-7096f479f316_1512x.png'
         ,21000
         ,100
       ,0
         ,NOW()          
);

INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'ROVER [SLEEVE VER.] ALBUM'
         ,'카이(EXO)'
         ,'/quddls6/storage/cdImg/15_b0e1d0c2-82e4-460e-a640-07d7045dfd17_960x.png'
         ,'/quddls6/storage/cdImg/2_2261d3a9-569d-482e-850b-915139f42789_1512x.png'
         ,16900
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          '1ST MINI ALBUM TRUTH OR LIE'
         ,'황민현(뉴이스트)'
         ,'/quddls6/storage/cdImg/1_0ad847ff-2c66-47ac-9478-c87e18065ac4_960x.png'
         ,'/quddls6/storage/cdImg/2_cf91775e-9ea8-463c-8fd5-e3e31ad7e1e0_1512x.png'
         ,17500
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'THE COLORS OF LOVE (2ND MINI ALBUM)'
         ,'방용국'
         ,'/quddls6/storage/cdImg/15_893750ca-7ad2-477a-abf2-97a2d39f1486_960x.png'
         ,'/quddls6/storage/cdImg/2_f72f3f6e-6f90-40cf-b6ea-a3b9ad5a143e_1512x.png'
         ,15200
         ,100
       ,0
         ,NOW()          
);

INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'BITTERSWEET (2ND SINGLE ALBUM)'
         ,'원호(몬스타엑스)'
         ,'/quddls6/storage/cdImg/15_ef297b24-32fc-41ed-b756-55e4a74b3a80_960x.png'
         ,'/quddls6/storage/cdImg/3_a9723107-83d0-402a-9dfa-a8300d36f129_1512x.png'
         ,14400
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          '3RD MINI ALBUM [ON AND ON]'
         ,'TEMPEST'
         ,'/quddls6/storage/cdImg/1_facf1691-d948-45b4-b752-570bd88b31fe_960x.png'
         ,'/quddls6/storage/cdImg/2_b62ac46d-5897-473f-a932-d2feef1f0990_1512x.png'
         ,12300
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'VARIOUS [PHOTOBOOK] 3RD MINI ALBUM'
         ,'비비즈(VIVIZ)'
         ,'/quddls6/storage/cdImg/15_c6299baa-5596-4faf-b774-6bb1f8ff4969_960x.png'
         ,'/quddls6/storage/cdImg/2_ce3e13c5-e640-4faf-8472-12658204ba94_1512x.png'
         ,17000
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'CHASE [COMPLETE VER.] ALBUM'
         ,'민호(SHINEE)'
         ,'/quddls6/storage/cdImg/1_a8c6cd28-5596-466e-9ff5-28e573ece462_960x.png'
         ,'/quddls6/storage/cdImg/3_26a3a4d4-3e61-42c9-a5ab-e4a083c1e753_1512x.png'
         ,16900
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'CIRCLE [QR VER.] ALBUM'
         ,'온유(SHINEE)'
         ,'/quddls6/storage/cdImg/1_776934c3-e9ef-4164-b31d-c34a65a9bf78_960x.png'
         ,'/quddls6/storage/cdImg/e8423605bdb8cf0557964f5a1cb5fd13_6d2cfb07-2fc4-41b3-a545-7a213f88ee68_1512x.png'
         ,19400
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          '1ST EP NEW JEANS [WEVERSE ALBUM VER.]'
         ,'뉴진스(NEWJEANS)'
         ,'/quddls6/storage/cdImg/0_49f255bc-ba92-45f2-96c3-f18b97aa9481_960x.png'
         ,'/quddls6/storage/cdImg/1_4d387ac6-1c2b-45de-9307-40e318504e4b_1512x.png'
         ,31000
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'MY WORLD [TABLOID VER.] 3RD MINI ALBUM'
         ,'에스파(aespa)'
         ,'/quddls6/storage/cdImg/01.aespa__3___TabloidVer._960x.png'
         ,'/quddls6/storage/cdImg/02.aespa__3___TabloidVer._960x.png'
         ,22000
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          '2ND MINI ALBUM WHEE CD'
         ,'휘인(마마무)'
         ,'/quddls6/storage/cdImg/0_6f504251-0ccc-4c6e-848f-335855e29bb1_960x.png'
         ,'/quddls6/storage/cdImg/1_fbe92e94-3793-465b-b7e4-b28c9724716e_1512x.png'
         ,18700
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'VOL.1 I NEVER DIE CD'
         ,'여자아이들((G)-IDLE)'
         ,'/quddls6/storage/cdImg/1_482835d5-053e-41f6-a8c1-3cc8ff135573_960x.png'
         ,'/quddls6/storage/cdImg/1_5e35aefa-562c-4106-b9f6-0aaa31f127c5_1512x.png'
         ,19900
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'MORE & MORE (9TH MINI) ALBUM'
         ,'트와이스(TWICE)'
         ,'/quddls6/storage/cdImg/1_978ea9a6-d05f-4756-a691-5a5626b596b5_960x.png'
         ,'/quddls6/storage/cdImg/2_3f0eec93-81cb-47ce-b76f-445e0dc6eef3_1512x.png'
         ,29000
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'RIGHT THROUGH ME (2ND MINI ALBUM)'
         ,'데이식스(DAY6)'
         ,'/quddls6/storage/cdImg/1_1eaa9fd5-6868-4175-b3df-5e2fe36b3719_960x.png'
         ,'/quddls6/storage/cdImg/3_a6154a4b-bd49-4d93-87d0-2b529a12815e_1512x.png'
         ,18500
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'READY TO BE [DIGIPACK VER.] ALBUM'
         ,'트와이스(TWICE)'
         ,'/quddls6/storage/cdImg/1_73f24ddf-01dd-45aa-954e-d0da7706ad27_960x.png'
         ,'/quddls6/storage/cdImg/2_fedab53a-604a-4869-837a-f4f51bf75ec8_1512x.png'
         ,19600
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'THE BOOK OF US : THE DEMON ALBUM (RANDOM VER.)'
         ,'데이식스(DAY6)'
         ,'/quddls6/storage/cdImg/R_891b9ba3-ce50-4239-a979-c246e95b0c2e_960x.png'
         ,'/quddls6/storage/cdImg/3_049e581d-6554-412b-9908-a2a29083439c_1512x.png'
         ,24000
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'AFTER LIKE'
         ,'아이브(IVE)'
         ,'/quddls6/storage/cdImg/15_66e9c3d2-f9a0-4f85-a129-54c3761fb809_960x.png'
         ,'/quddls6/storage/cdImg/2_15c8b1f8-19cb-489a-9f5e-9c87411d0e51_1512x.png'
         ,22000
         ,100
       ,0
         ,NOW()          
);

INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'GLITCH MODE [DIGIPACK VER.]'
         ,'NCT DREAM'
         ,'/quddls6/storage/cdImg/15_960x.png'
         ,'/quddls6/storage/cdImg/2_47c44ce9-e136-4cab-bdea-45ce9dbe087a_1512x.png'
         ,18900
         ,100
       ,0
         , NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'UNIVERSE [JEWEL CASE VER.] (VOL.3) ALBUM'
         ,'NCT'
         ,'/quddls6/storage/cdImg/1_59ff9c96-e9e8-4b99-a3b8-434578eafd7f_960x.png'
         ,'/quddls6/storage/cdImg/2_39395a4e-cc84-47e7-802e-1dfb034d01c7_1512x.png'
         ,'22700'
         ,100
       ,0
         ,NOW()         
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'FAVORITE (VOL.3 REPACKAGE) ALBUM'
         ,'NCT 127'
         ,'/quddls6/storage/cdImg/1_9cd85789-2066-4027-b494-9970095d9c2c_960x.png'
         ,'/quddls6/storage/cdImg/3_196948c8-dc88-4caa-9a75-b980c9d37e94_1512x.png'
         ,'19900'
         ,100
       ,0
         ,NOW()         
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'HOT&COLD (5TH MINI ALBUM) ALBUM'
         ,'박지훈'
         ,'/quddls6/storage/cdImg/01_960x.png'
         ,'/quddls6/storage/cdImg/2_d98fdeee-dcd2-4710-b543-e8c0f4247614_1512x.png'
         ,'15400'
         ,100
       ,0
         ,NOW()          
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          '2ND MINI ALBUM COLOR CD'
         ,'권은비'
         ,'/quddls6/storage/cdImg/1_60650ac6-e397-4118-903c-a1673e542c5d_960x.png'
         ,'/quddls6/storage/cdImg/2_98bd70df-1f27-489a-99d2-6fe884e29b8f_1512x.png'
         ,'16600'
         ,100
       ,0
         ,NOW()        
);
INSERT INTO CD (
         TITLE
       , SINGER
       , MAIN_IMG
       , DETAIL_IMG
       , PRICE
       , STOCK
       , RECOMMEND_COUNT
       , WRITED_AT
        )VALUES (
          'HUSH RUSH 1ST MINI ALBUM'
         ,'이채연'
         ,'/quddls6/storage/cdImg/1_6ceca896-06b4-43f5-8fc9-1b490dfcd3ad_960x.png'
         ,'/quddls6/storage/cdImg/2_851e5657-1cf4-4f3f-986b-6f24ebe40c96_1512x.png'
         ,'18600'
         ,100
       ,0
         ,NOW()
);   


INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (1, 1);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (1, 2);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (1, 5);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (2, 3);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (2, 4);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (3, 2);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (3, 5);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (4, 1);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (4, 2);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (4, 5);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (5, 4);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (5, 5);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (6, 1);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (6, 3);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (7, 4);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (7, 5);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (8, 3);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (8, 4);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (9, 1);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (9, 3);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (10, 1);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (10, 2);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (10, 5);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (11, 4);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (12, 1);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (12, 4);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (13, 1);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (13, 3);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (14, 1);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (14, 2);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (14, 5);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (15, 3);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (15, 4);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (16, 3);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (16, 4);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (17, 1);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (17, 3);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (18, 1);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (18, 2);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (18, 5);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (19, 1);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (19, 2);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (19, 5);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (20, 3);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (20, 4);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (21, 1);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (21, 2);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (21, 5);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (22, 1);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (22, 2);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (22, 5);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (23, 3);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (23, 4);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (24, 3);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (24, 4);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (25, 4);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (26, 1);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (26, 4);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (27, 1);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (27, 3);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (28, 3);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (29, 1);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (29, 2);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (29, 5);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (30, 3);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (30, 4);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (31, 3);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (31, 4);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (32, 3);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (32, 4);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (33, 1);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (33, 2);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (33, 5);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (34, 3);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (34, 4);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (35, 3);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (35, 4);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (36, 3);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (36, 4);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (37, 3);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (37, 4);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (38, 3);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (38, 4);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (39, 1);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (39, 2);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (39, 5);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (40, 1);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (40, 2);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (40, 5);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (41, 3);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (41, 4);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (42, 1);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (42, 2);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (42, 5);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (43, 1);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (43, 2);