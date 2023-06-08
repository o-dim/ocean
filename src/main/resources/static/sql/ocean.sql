-- 스키마
USE ocean;


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
   USER_NO              INT             NOT NULL AUTO_INCREMENT,    		-- 회원 번호		-PK
   EMAIL                VARCHAR(100)    NOT NULL UNIQUE,				-- 회원 이메일
   PW                   VARCHAR(100)    NOT NULL,    					-- 회원 비밀번호
   PHONENO              LONGTEXT        NOT NULL,						-- 회원 전화번호
   POSTCODE             VARCHAR(20)     	NULL,							-- 회원 우편번호
   ROAD_ADDRESS         VARCHAR(100)   		NULL,							-- 회원 도로명주소
   JIBUN_ADDRESS        VARCHAR(100)   		NULL,							-- 회원 지번
   DETAIL_ADDRESS       VARCHAR(100)   		NULL,							-- 회원 상세주소
   NAME                 VARCHAR(20)    		NULL,							-- 회원 이름
   JOINED_AT            DATETIME	        NULL,							-- 회원 가입일자
   CONSTRAINT PK_USERS PRIMARY KEY(USER_NO)
);

-- 판매용 CD 
CREATE TABLE CD (
	CD_NO				INT				NOT NULL AUTO_INCREMENT,			-- CD 상품번호		-PK
    TITLE				LONGTEXT 			NULL,							-- CD 제목
    SINGER				VARCHAR(40)			NULL,							-- CD 가수
    MAIN_IMG			LONGTEXT			NULL,							-- CD 메인이미지 경로
    DETAIL_IMG			LONGTEXT			NULL,							-- CD 상세이미지 경로
    PRICE				INT					NULL,							-- CD 가격
    STOCK				INT					NULL,							-- CD 수량
    RECOMMEND_COUNT		INT					NULL,							-- 추천 갯수
    WRITED_AT			DATETIME 				NULL,							-- 등록 날짜 (최신순 보기위해)
    CONSTRAINT PK_CD PRIMARY KEY(CD_NO)
);


-- 플레이리스트 테이블
CREATE TABLE PLAYLIST (
	PLAYLIST_NO			INT				NOT NULL AUTO_INCREMENT,	-- 플레이리스트 번호 		/ PK
	USER_NO				INT				NOT NULL,					-- CD 번호				/ FK
    TITLE				VARCHAR(40)			NULL,					-- 플레이리스트 이름
    CONSTRAINT PK_PLAYLIST PRIMARY KEY(PLAYLIST_NO),
    CONSTRAINT FK_USER_NO FOREIGN KEY(USER_NO) REFERENCES USERS(USER_NO) ON DELETE CASCADE   
);

-- 스트리밍용 노래 
CREATE TABLE SONG (
	SONG_NO			INT				NOT NULL AUTO_INCREMENT,		-- 노래 번호  		/ PK
    TITLE			VARCHAR(40)			NULL,						-- 노래 제목
    SINGER			VARCHAR(40)			NULL,						-- 가수
    ALBUM			VARCHAR(40)			NULL,						-- 앨범 명
    SONG_TOTAL		INT					NULL,						-- 노래 전체갯수
    PLAYLIST_NO		INT				NOT NULL,						-- 플레이리스트 번호		/ FK
    CD_NO			INT				NOT NULL,						-- CD 상품번호		/ FK
    CONSTRAINT PK_SONG PRIMARY KEY(SONG_NO),
    CONSTRAINT FK_PLAYLIST_NO FOREIGN KEY(PLAYLIST_NO) REFERENCES PLAYLIST(PLAYLIST_NO) ON DELETE CASCADE,
    CONSTRAINT FK_CD_NO FOREIGN KEY(CD_NO) REFERENCES CD(CD_NO) ON DELETE CASCADE
);


-- 플레이리스트 바구니 테이블
CREATE TABLE PLAYLIST_DETAIL (
	PLAYLIST_DETAIL_NO	INT			NOT NULL AUTO_INCREMENT,	-- 플레이리스트 바구니 넘버	/ PK
    PLAYLIST_NO			INT			NOT NULL,					-- 플레이리스트 번호 		/ FK
    SONG_NO				INT			NOT NULL,					-- 노래번호				/ FK
    CONSTRAINT PK_PLAYLIST_DETAIL PRIMARY KEY(PLAYLIST_DETAIL_NO),
    CONSTRAINT FK_PLAYLIST_NO1 FOREIGN KEY(PLAYLIST_NO) REFERENCES PLAYLIST(PLAYLIST_NO) ON DELETE CASCADE,
	CONSTRAINT FK_SONG_NO FOREIGN KEY(SONG_NO) REFERENCES SONG(SONG_NO) ON DELETE CASCADE
);

-- 추천 테이블
CREATE TABLE RECOMMEND (
	RECOMMEND_NO		INT				NOT	NULL AUTO_INCREMENT,			-- 추천 번호		/PK
    USER_NO				INT				NOT NULL,							-- 회원 번호		/FK
    CD_NO				INT				NOT NULL,							-- CD 상품번호	/FK
    CONSTRAINT PK_RECOMMEND PRIMARY KEY(RECOMMEND_NO),
    CONSTRAINT FK_USER_NO1 FOREIGN KEY(USER_NO) REFERENCES USERS(USER_NO) ON DELETE CASCADE,
    CONSTRAINT FK_CD_NO1 FOREIGN KEY(CD_NO) REFERENCES CD(CD_NO) ON DELETE CASCADE
);

-- 해시태그 테이블
CREATE TABLE HASHTAG (
	HT_NO				INT					NOT NULL AUTO_INCREMENT,		-- 해시태그 번호 	/ PK
	NAME				VARCHAR(40)				NULL,						-- 해시태그 이름
    CONSTRAINT PK_HASHTAG PRIMARY KEY(HT_NO)
);

-- 해시태그 브릿지 (N : M 관계)
CREATE TABLE HASHTAG_CD (
	HTCD_NO				INT					NOT NULL AUTO_INCREMENT,		-- 해시태그브릿지번호 / PK
	CD_NO				INT					NOT NULL,						-- CD 상품번호	 	/ FK    
	HT_NO				INT					NOT NULL,						-- 해시태그 번호 	 / FK
    CONSTRAINT PK_HTCD PRIMARY KEY(HTCD_NO),
    CONSTRAINT FK_CD_NO2 FOREIGN KEY(CD_NO) REFERENCES CD(CD_NO) ON DELETE CASCADE,
    CONSTRAINT FK_HT_NO FOREIGN KEY(HT_NO) REFERENCES HASHTAG(HT_NO) ON DELETE CASCADE
    
);


-- 장바구니
CREATE TABLE CART (
	CART_NO				INT					NOT NULL AUTO_INCREMENT,		-- 장바구니 번호		/ PK
    MADE_AT				DATETIME					NULL,						-- 장바구니에 담긴 수량
    USER_NO				INT					NOT NULL,						-- 회원 번호			/ FK
    CONSTRAINT PK_CART PRIMARY KEY(CART_NO),
    CONSTRAINT FK_USER_NO2 FOREIGN KEY(USER_NO) REFERENCES USERS(USER_NO) ON DELETE CASCADE
);

-- 상세 장바구니
CREATE TABLE CART_DETAIL (
	CART_DETAIL_NO		INT					NOT NULL AUTO_INCREMENT,		-- 상세 장바구니번호		/PK
	COUNT				INT						NULL,						-- 수량
    CART_NO				INT					NOT NULL,						-- 장바구니 번호		/FK
    CD_NO				INT					NOT NULL,						-- CD 상품번호		/FK
    CONSTRAINT PK_CART_DETAIL PRIMARY KEY(CART_DETAIL_NO),
    CONSTRAINT FK_CART_NO FOREIGN KEY(CART_NO) REFERENCES CART(CART_NO) ON DELETE CASCADE,
    CONSTRAINT FK_CD_NO3 FOREIGN KEY(CD_NO) REFERENCES CD(CD_NO) ON DELETE CASCADE
);

-- 결제
CREATE TABLE ORDER_TBL (
	ORDER_NO			INT				NOT NULL AUTO_INCREMENT,		-- 결제 번호			/ PK
	TOTAL				INT				NOT NULL,						-- 전체 금액  
    EMAIL				VARCHAR(100)	NOT NULL,						-- 받는사람 이메일
    PHONENO				LONGTEXT			NULL,						-- 받는사람 전화번호
    POSTCODE			VARCHAR(100)		NULL,						-- 받는사람 우편번호
    ROAD_ADDRESS		VARCHAR(100)		NULL,						-- 받는사람 도로명주소
    JIBUN_ADDRESS		VARCHAR(100)		NULL,						-- 받는사람 지번
    DETAIL_ADDRESS		VARCHAR(100)		NULL,						-- 받는사람 상세주소
    NAME				VARCHAR(40)			NULL,						-- 받는사람 이름
    COUNT				INT				NOT NULL,						-- CD 구매수량
    ORDER_AT			DATETIME				NULL,						-- 주문일
    CART_NO				INT				NOT NULL,						-- 장바구니 번호		/ FK
    CD_NO				INT				NOT NULL,						-- CD상품번호			/ FK
    CONSTRAINT PK_ORDER_TBL PRIMARY KEY(ORDER_NO),
    CONSTRAINT FK_CART_NO1 FOREIGN KEY(CART_NO) REFERENCES CART(CART_NO) ON DELETE CASCADE,
    CONSTRAINT FK_CD_NO4 FOREIGN KEY(CD_NO) REFERENCES CD(CD_NO) ON DELETE CASCADE
);


-- 휴면 계정
CREATE TABLE SLEEP_USERS (
	SLEEP_USER_NO		INT					NOT NULL AUTO_INCREMENT,	-- 휴면 회원번호		/ PK
    EMAIL				VARCHAR(50)			NOT NULL UNIQUE,			-- 회원 이메일
    PW					VARCHAR(100)			NULL,					-- 회원비밀번호
    PHONENO				VARCHAR(100)		NOT NULL,					-- 회원전화번호
    POSTCODE			VARCHAR(20)				NULL,					-- 회원우편번호
    ROAD_ADDRESS		VARCHAR(100)			NULL,					-- 회원도로명주소
    JIBUN_ADDRESS		VARCHAR(100)			NULL,					-- 회원지번
    DETAIL_ADDRESS		VARCHAR(100)			NULL,					-- 회원 상세주소
    NAME				VARCHAR(30)				NULL,					-- 회원이름
    JOINED_AT			DATETIME					NULL,					-- 회원가입날짜
    SLEPT_AT			DATETIME					NULL,					-- 회원휴면날짜
    CONSTRAINT PK_SLEEP_USERS PRIMARY KEY(SLEEP_USER_NO)
);


-- 탈퇴 회원
CREATE TABLE OUT_USERS (
	OUT_USER_NO			INT					NOT NULL AUTO_INCREMENT,	-- 탈퇴 회원번호		/ PK			
    EMAIL				VARCHAR(50)			NOT NULL UNIQUE,			-- 회원이메일
    JOINED_AT			DATETIME					NULL,					-- 회원가입날짜
    OUT_AT				DATETIME					NULL,					-- 회원 탈퇴날짜
    CONSTRAINT PK_OUT_USERS PRIMARY KEY(OUT_USER_NO)
);

-- 댓글 테이블
CREATE TABLE REPLY (
	REPLY_NO			INT				NOT NULL AUTO_INCREMENT,		-- 댓글 번호			/ PK
    CONTENT				LONGTEXT			NULL,						-- 댓글 내용
    GROUP_NO			INT					NULL,						-- 댓글 그룹번호 (1번 댓글에 몇개 댓글을 달면 그룹넘버가 1이된다.)
    GROUP_ORDER			INT					NULL,						-- 댓글 오더 ( 대댓글에 필요함 )
    DEPTH				INT					NULL,						-- 원 댓글은 0 , 대댓글은 + 1
    WRITE_AT			DATETIME				NULL,						-- 작성 날짜
    CD_NO				INT				NOT NULL,						-- CD 상품번호		/ FK
    USER_NO				INT				NOT NULL,						-- 회원번호			/ FK
    CONSTRAINT PK_REPLY PRIMARY KEY(REPLY_NO),
    CONSTRAINT FK_CD_NO5 FOREIGN KEY(CD_NO) REFERENCES CD(CD_NO) ON DELETE CASCADE,
    CONSTRAINT FK_USER_NO3 FOREIGN KEY(USER_NO) REFERENCES USERS(USER_NO) ON DELETE CASCADE
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
				) VALUES (
				 'james@naver.com'
				, 'mango123!'
				, '010-1111-0000'
				, '15226'
				, '경기도 시흥시 꽃게로 353'
				, '경기도 시흥시 꽃게동 353'
				, '101호'
				, 'james'
				, NOW()
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
				) VALUES (
                  'john@gmail.com'
				, 'mango123!'
				, '010-1111-1111'
				, '17425'
				, '경기도 안산시 대부도로 123'
				, '경기도 안산시 대부도동 123'
				, '203호'
				, 'john'
				, NOW()

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
				) VALUES (
                  'wendy@kakao.com'
				, 'mango123!'
				, '010-1111-2222'
				, '15861'
				, '경기도 군포시 산본로 353'
				, '경기도 군포시 산본동 353'
				, '512호'
				, 'wendy'
				, NOW()
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
				) VALUES (
				  'winter@naver.com'
				, 'mango123!'
				, '010-1111-3333'
				, '16880'
				, '경기도 안양시 평촌로 211'
				, '경기도 안양시 평촌동 211'
				, '608호'
				, 'winter'
				, NOW()
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
				) VALUES (
				  'joy@gmail.com'
				, 'mango123!'
				, '010-1111-4444'
				, '16271'
				, '수원특례시 수원화성로 142'
				, '수원특례시 수원화성동 142'
				, '1102호'
				, 'joy'
				, NOW()
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
				) VALUES (
				 'eric@kakao.com'
				, 'mango123!'
				, '010-1111-5555'
				, '52414'
				, '경상남도 남해군 시금치로 525'
				, '경상남도 남해군 시금치동 525'
				, '304호'
				, 'eric'
				, NOW()
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
				) VALUES (
                  'harry@naver.com'
				, 'mango123!'
				, '010-1111-6666'
				, '17959'
				, '경기도 평택시 송탄로 651'
				, '경기도 평택시 송탄동 651'
				, '510호'
				, 'harry'
				, NOW()

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
				) VALUES (
				  'som@gmail.com'
				, 'mango123!'
				, '010-1111-7777'
				, '41962'
				, '대구광역시 막창로 122'
				, '대구광역시 막창동 122'
				, '409호'
				, 'som'
				, NOW()

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
				) VALUES (
				  'zico@kakao.com'
				, 'mango123!'
				, '010-1111-8888'
				, '16103'
				, '경기도 의왕시 철도박물관로 65'
				, '경기도 의왕시 철도박물관동 65'
				, '807호'
				, 'zico'
				, NOW()
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
				) VALUES (
				 'felix@naver.com'
				,'mango123!'
				,'010-1111-9999'
				,'10868'
				,'경기도 파주시 북스테이로 155'
				,'경기도 파주시 북스테이동 155'
				,'101호'
				,'felix'
				, NOW()
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
				) VALUES (
				 'merry@nate.com'
				,'mango123!'
				,'010-2222-0000'
				,'57305'
				,'전라남도 담양군 떡갈비로 85'
				,'전라남도 담양군 떡갈비동 85'
				,'102호'
				,'merry'
				, NOW()
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
				) VALUES (
				 'bella@naver.com'
				,'mango123!'
				,'010-2222-1111'
				,'54878'
				,'전라북도 전주시 비빔밥로 74'
				,'전라북도 전주시 비빔밥동 74'
				,'2103호'
				,'bella'
				, NOW()
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
				) VALUES (
				 'miny@daum.net'
				,'mango123!'
				,'010-2222-2222'
				,'16271'
				,'수원특례시 행궁로 111'
				,'수원특례시 행궁동 111'
				,'1204호'
				,'miny'
				, NOW()
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
				) VALUES (
				 'bell@kakao.com'
				,'mango123!'
				,'010-2222-3333'
				,'15856'
				,'강원도 양구군 금강산로 454'
				,'강원도 양구군 금강산동 454'
				,'506호'
				,'bell'
				, NOW()
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
				) VALUES (
				'peter@gmail.com'
				,'mango123!'
				,'010-2222-4444'
				,'21530'
				,'인천광역시 차이나타운로 744'
				,'인천광역시 차이나타운동 744'
				,'801호'
				,'peter'
				, NOW()
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
				) VALUES (
				 'pan@naver.com'
				,'mango123!'
				,'010-2222-5555'
				,'44412'
				,'울산광역시 쫀드기로 333'
				,'울산광역시 쫀드기동 333'
				,'302호'
				,'pan'
				, NOW()
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
				) VALUES (
				 'jobs@naver.com'
				,'mango123!'
				,'010-2222-6666'
				,'46978'
				,'부산광역시 해운대로 233'
				,'부산광역시 해운대동 233'
				,'604호'
				,'jobs'
				, NOW()
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
				) VALUES (
				 'nick@naver.com'
				,'mango123!'
				,'010-2222-7777'
				,'17585'
				,'경기도 안성시 탕면로 522'
				,'경기도 안성시 탕면동 522'
				,'805호'
				,'nick'
				, NOW()
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
				) VALUES (
				 'kate@nate.com'
				,'mango123!'
				,'010-2222-8888'
				,'34194'
				,'대전광역시 두루치기로 242'
				,'대전광역시 두루치기동 242'
				,'708호'
				,'kate'
				, NOW()
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
				) VALUES (
				 'kathy@daum.net'
				,'mango123!'
				,'010-2222-9999'
				,'24535'
				,'강원도 양구군 옥수수로 456'
				,'강원도 양구군 옥수수동 456'
				,'904호'
				,'kathy'
				, NOW()
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/files/1_13f06621-c7bd-4ff3-82c0-008b73e454cf_960x.jpg?v=1684379513'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/files/e8423605bdb8cf0557964f5a1cb5fd13_99cd0277-2c60-427b-aa03-5d12cc6db9be_1080x.jpg?v=1684379513'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1-1_5c6d645f-3904-43ca-b9ef-8e9bd7e0bcc6_960x.jpg?v=1681710821'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/7_94b60244-3b7f-429c-8df5-a9ca0d5c85f8_1080x.jpg?v=1681710820'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_4a466016-d60b-4517-8785-0d3180f345bf_960x.jpg?v=1677056341'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/2_2d1b6a01-35a0-4738-8d8e-188630299bb6_720x.jpg?v=1677056341'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/files/0_cda88c8b-3723-4b1f-889b-c51aca0f2ed8_960x.jpg?v=1682672246'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/files/2_aec3bf78-ccfb-49ba-821e-4e5980f5184a_1512x.jpg?v=1682672246'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_6ffe5ccb-2d9c-49a2-a58e-1998290fc400_960x.jpg?v=1660814987'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/2_7571c27b-0ad0-43b2-87a9-1abe988d8702_1512x.jpg?v=1662077372'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/15_151b72ec-a36c-4064-93a4-7ebb2c0cc16e_960x.jpg?v=1661843766'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/2_fe9cf172-655c-4ba9-99bc-e23269d4235c_720x.jpg?v=1661843773'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_42b8f051-1034-4641-849a-83f1fa43ca8d_960x.jpg?v=1634707476'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/2_b2db5189-0562-4b2e-8e99-6727ae4fd333_1512x.jpg?v=1634707475'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_0b44b3e1-a454-48c1-9e7d-587d2d62d245_960x.jpg?v=1652670802'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/2_e13c7ceb-d547-4df3-9a62-cddc26640af3_1512x.jpg?v=1664504762'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_6edc4cff-02b2-4080-af22-4a3d2d5a1b1b_960x.jpg?v=1664415407'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/2_15a6aeb4-267b-4459-81be-25783c3632e6_1512x.jpg?v=1664415407'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_1fd27125-baf8-40c8-b7d6-e244d629fa53_960x.jpg?v=1651104705'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/3_b3937e66-5632-4ea1-8302-2c549aecfc5a_1024x1024.jpg?v=1657072499'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_901e9eea-4d69-43cb-887f-6520c1ce0b5a_960x.jpg?v=1649210736'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/3_b3937e66-5632-4ea1-8302-2c549aecfc5a_1024x1024.jpg?v=1657072499'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/0_57d3c9a3-eaf3-4983-8ee4-7c96c95518f0_960x.jpg?v=1664865479'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_0ea079d1-0bad-4d8a-858f-a5d41d6c1fe5_1512x.jpg?v=1664865479'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/61WUEMO8AvL._AC_SL1000_960x.jpg?v=1651459735'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/61qQXejoOWL._AC_SL1289_1512x.jpg?v=1651459735'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/71q6eTW0AlL._AC_SL1000__1_960x.jpg?v=1651455050'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/61V5U5GtgvL._AC_SL1500_1512x.jpg?v=1651455050'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/5_b8fcfc8c-a7ce-47d4-8ad0-b861a474ec13_960x.jpg?v=1659419159'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/e8423605bdb8cf0557964f5a1cb5fd13_5901df6a-c8ac-41ec-b457-6be55a03887a_1512x.jpg?v=1659419162'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/0_cca4d598-46fe-429a-847e-6cc9cebb198f_960x.jpg?v=1647409071'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_2ab9d275-4fe1-459b-ba6c-4237405886e4_1512x.jpg?v=1647409070'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_12f96833-bb88-4faf-a8b6-ee102d938fe2_960x.jpg?v=1675042770'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/e8423605bdb8cf0557964f5a1cb5fd13_a42dffdb-1083-4512-9e2c-2c996a17561c_1512x.jpg?v=1675042770'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_98a45b7b-3ecc-472f-9a47-d3462d155cf6_960x.jpg?v=1675666137'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/2_f81429e2-f69d-4d9e-8106-b748e59731a6_1512x.jpg?v=1675666137'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/15_51a45334-bcd1-410c-a365-63c83e79cb5d_960x.jpg?v=1664853357'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/2_958b04bf-1cba-475f-9f3c-589b00b9faa7_1512x.jpg?v=1664853364'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_701486d3-98d9-4caf-948a-93870a4421d1_960x.jpg?v=1636419710'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/2_23afd3fe-ecd0-4d15-96cf-35e0d3628ee4_1512x.jpg?v=1636419710'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/0_e50e55af-badc-4117-b8ac-06ac2e96c471_960x.jpg?v=1666949018'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_bea4b010-ede8-4b87-aed9-842eee1dfa04_1512x.jpg?v=1666949018'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_e48a899a-9d99-4eb1-8f68-4b19c16d54aa_960x.png?v=1682315100'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/2_66bcfed6-882e-46c7-92e4-42009d56e8f3_1512x.png?v=1682315100'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_b247ab8a-832c-4a35-a620-d3a533b9b365_960x.jpg?v=1681715090'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/e8423605bdb8cf0557964f5a1cb5fd13_0b30d014-f2c5-4be3-bbb6-b5bee0441f82_1512x.jpg?v=1681715090'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_da62eb45-607e-4097-8cc4-1405e0cd883d_960x.jpg?v=1681705606'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/2_2c8a8db5-b368-44a2-830f-c66bcf9cef05_1512x.jpg?v=1681705606'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_431caced-fbec-43b4-afd4-4304f4720ec4_960x.jpg?v=1679549266'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_2b242452-a15d-42d4-b572-77f5688be57e_1512x.jpg?v=1680844443'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_b5005fa9-950b-4bca-9eaa-e2ada6b39f2f_960x.jpg?v=1681172594'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/2_67c791c6-468f-409b-a580-4daeef153010_1512x.jpg?v=1681172597'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_796f8900-4ad5-4a13-b6c3-7199dda4f3e5_960x.jpg?v=1678350273'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/2_7b0b0249-d452-454b-9193-9ae09d45ba9c_1512x.jpg?v=1678350273'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_a4ded31b-a2e4-4aef-927b-fd3fa980130d_960x.jpg?v=1676530863'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/2_edd99654-316e-4474-b827-cc81a17de309_1512x.jpg?v=1676530863'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_4b928287-2eb7-49d2-8b01-f9a10ee795af_960x.jpg?v=1673406624'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/2_8acf81ea-27c0-4300-a242-827fa6741ddd_1512x.jpg?v=1673406624'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/NOTSHY-ONLINECOVER-fix_002_960x.jpg?v=1649407354'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/2_6fa036ac-1b02-4dfc-8e6e-1cfeffabf7e0_1512x.jpg?v=1649407354'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_0497edb2-2c10-4808-a0ab-8bd084d3a4d4_960x.jpg?v=1673258362'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/e8423605bdb8cf0557964f5a1cb5fd13_18674736-3e65-452b-a1d7-d2f5f183eeee_1512x.jpg?v=1673258363'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_bb0996ad-c396-468b-a961-4d04bd1bd92e_960x.jpg?v=1670216160'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/e8423605bdb8cf0557964f5a1cb5fd13_99054511-882e-44ce-9ad7-c263efc54a41_1512x.jpg?v=1670216159'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/0_efb4fa12-e048-4d80-b2c0-0cab3e73305a_960x.jpg?v=1666148999'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_f8b40a83-cf48-4958-925e-2d0a546602c9_1512x.jpg?v=1666149000'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_daf7e8bb-f17b-47eb-b057-0eff1d152d32_960x.jpg?v=1664345803'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/2_464f08e8-1aa4-4112-a06c-24630de7046f_1512x.jpg?v=1664345803'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/15_01e63fc2-1f02-4e78-9eb9-60cd13834a68_960x.jpg?v=1663124122'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/4_de4a1d47-55c8-42aa-9141-ea3b036b92a3_1512x.jpg?v=1663124122'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_9163af3a-a7a6-470c-9e0d-0cacb1162c94_960x.jpg?v=1660884044'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/2_621a089e-cbe5-4854-bd35-38f929a1abbe_1512x.jpg?v=1662453619'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_2d5607ba-7d3e-4dc9-8f63-3c667c402b06_960x.jpg?v=1656665330'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/2_fe3f4da9-7e32-460d-9643-73c55f743c30_1512x.jpg?v=1656665330'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/files/1_59a3e452-99a1-4157-ae6f-92f37a909063_960x.jpg?v=1685432741'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/files/e8423605bdb8cf0557964f5a1cb5fd13_b1b28fea-f377-431c-b740-3a32d572ac23_1512x.jpg?v=1685432741'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/0_111da63e-1728-4798-a456-f5d9bf60a662_960x.jpg?v=1661507092'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_119722fa-430e-44fd-9790-832ae77044b5_1512x.jpg?v=1661507091'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_b4fffda4-5fd1-454b-b161-ab483ef4c615_960x.jpg?v=1680158329'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/2_2fcff1bf-ee23-4ea3-925c-b7c65310f459_1512x.jpg?v=1680158329'
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
         ,'김은비'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_9011a5b9-c8c0-49da-9a90-bf43b88703d9_960x.jpg?v=1665620495'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/2_e4c99f31-718d-4519-b4fe-937421582b3a_1512x.jpg?v=1665620495'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_63c26614-50ad-4574-8fca-067b0791f9bd_960x.jpg?v=1654849692'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/2_94b3cf51-5da7-41fa-8316-a07b09e963a4_1512x.jpg?v=1654849693'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/CaseVer._960x.jpg?v=1636508314'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_8978f11e-ce78-4cb4-b5c9-edd2c4a71374_1512x.png?v=1636508314'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_cd691936-83ef-448a-b021-6a210896cc07_960x.jpg?v=1668563665'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/2_7b366a97-dbe3-4537-839d-e2dfba69cf1f_1512x.jpg?v=1668563665'
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
          '제이홉(BTS)'
         ,'JACK IN THE BOX (WEVERSE ALBUMS)'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/0_d609588c-9759-4908-9934-75b03624e2b0_960x.jpg?v=1665653007'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_3567181d-ea6a-4a27-bc73-7096f479f316_1512x.jpg?v=1665653007'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/15_b0e1d0c2-82e4-460e-a640-07d7045dfd17_960x.jpg?v=1678159856'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/2_2261d3a9-569d-482e-850b-915139f42789_1512x.jpg?v=1678159863'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_0ad847ff-2c66-47ac-9478-c87e18065ac4_960x.jpg?v=1676525205'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/2_cf91775e-9ea8-463c-8fd5-e3e31ad7e1e0_1512x.jpg?v=1676525220'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/files/15_893750ca-7ad2-477a-abf2-97a2d39f1486_960x.jpg?v=1683167637'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/files/2_f72f3f6e-6f90-40cf-b6ea-a3b9ad5a143e_1512x.jpg?v=1683167641'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/15_ef297b24-32fc-41ed-b756-55e4a74b3a80_960x.jpg?v=1664179844'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/3_a9723107-83d0-402a-9dfa-a8300d36f129_1512x.jpg?v=1664179843'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_facf1691-d948-45b4-b752-570bd88b31fe_960x.jpg?v=1668995703'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/2_b62ac46d-5897-473f-a932-d2feef1f0990_1512x.jpg?v=1668995702'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/15_c6299baa-5596-4faf-b774-6bb1f8ff4969_960x.jpg?v=1674693752'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/2_ce3e13c5-e640-4faf-8472-12658204ba94_1512x.jpg?v=1674693755'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_a8c6cd28-5596-466e-9ff5-28e573ece462_960x.jpg?v=1670306440'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/3_26a3a4d4-3e61-42c9-a5ab-e4a083c1e753_1512x.jpg?v=1671158489'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_776934c3-e9ef-4164-b31d-c34a65a9bf78_960x.jpg?v=1677546065'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/e8423605bdb8cf0557964f5a1cb5fd13_6d2cfb07-2fc4-41b3-a545-7a213f88ee68_1512x.jpg?v=1677546064'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/0_49f255bc-ba92-45f2-96c3-f18b97aa9481_960x.jpg?v=1658732265'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_4d387ac6-1c2b-45de-9307-40e318504e4b_1512x.jpg?v=1658732265'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/files/01.aespa__3___TabloidVer._960x.jpg?v=1682670917'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/files/02.aespa__3___TabloidVer._960x.jpg?v=1682670918'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/0_6f504251-0ccc-4c6e-848f-335855e29bb1_960x.jpg?v=1667365885'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_fbe92e94-3793-465b-b7e4-b28c9724716e_1512x.jpg?v=1667365886'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_482835d5-053e-41f6-a8c1-3cc8ff135573_960x.jpg?v=1645688876'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_5e35aefa-562c-4106-b9f6-0aaa31f127c5_1512x.jpg?v=1676510719'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_978ea9a6-d05f-4756-a691-5a5626b596b5_960x.jpg?v=1647491315'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/2_3f0eec93-81cb-47ce-b76f-445e0dc6eef3_1512x.jpg?v=1647491315'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_1eaa9fd5-6868-4175-b3df-5e2fe36b3719_960x.jpg?v=1650422764'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/3_a6154a4b-bd49-4d93-87d0-2b529a12815e_1512x.jpg?v=1661315882'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_73f24ddf-01dd-45aa-954e-d0da7706ad27_960x.jpg?v=1677817334'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/2_fedab53a-604a-4869-837a-f4f51bf75ec8_1512x.jpg?v=1677817334'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/R_891b9ba3-ce50-4239-a979-c246e95b0c2e_960x.jpg?v=1657158561'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/3_049e581d-6554-412b-9908-a2a29083439c_1512x.jpg?v=1657158561'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/15_66e9c3d2-f9a0-4f85-a129-54c3761fb809_960x.jpg?v=1661159627'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/2_15c8b1f8-19cb-489a-9f5e-9c87411d0e51_1512x.jpg?v=1661159641'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/15_960x.jpg?v=1648096159'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/2_47c44ce9-e136-4cab-bdea-45ce9dbe087a_1512x.jpg?v=1648096166'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_59ff9c96-e9e8-4b99-a3b8-434578eafd7f_960x.jpg?v=1638349035'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/2_39395a4e-cc84-47e7-802e-1dfb034d01c7_1512x.jpg?v=1638349034'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_9cd85789-2066-4027-b494-9970095d9c2c_960x.jpg?v=1634292606'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/3_196948c8-dc88-4caa-9a75-b980c9d37e94_1512x.jpg?v=1634292607'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/01_960x.jpg?v=1634545850'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/2_d98fdeee-dcd2-4710-b543-e8c0f4247614_1512x.jpg?v=1634545849'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_60650ac6-e397-4118-903c-a1673e542c5d_960x.jpg?v=1647854267'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/2_98bd70df-1f27-489a-99d2-6fe884e29b8f_1512x.jpg?v=1647854268'
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
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/1_6ceca896-06b4-43f5-8fc9-1b490dfcd3ad_960x.jpg?v=1664933290'
         ,'https://cdn.shopify.com/s/files/1/0558/0429/7407/products/2_851e5657-1cf4-4f3f-986b-6f24ebe40c96_1512x.jpg?v=1664933290'
         ,'18600'
         ,100
				 ,0
         ,NOW()
);     

SELECT *
  FROM CD;


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
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (43, 5);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (44, 3);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (44, 4);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (45, 3);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (45, 4);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (46, 3);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (46, 4);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (47, 3);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (47, 4);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (48, 3);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (48, 4);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (49, 3);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (49, 4);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (50, 3);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (50, 4);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (51, 3);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (51, 4);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (52, 3);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (52, 4);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (53, 3);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (53, 4);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (54, 3);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (54, 4);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (55, 3);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (55, 4);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (56, 3);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (56, 4);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (57, 3);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (57, 4);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (58, 3);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (58, 4);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (59, 3);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (59, 4);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (60, 3);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (60, 4);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (61, 3);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (61, 4);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (62, 3);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (62, 4);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (63, 3);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (63, 4);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (64, 3);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (64, 4);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (65, 3);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (65, 4);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (66, 1);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (66, 2);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (66, 5);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (67, 1);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (67, 2);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (67, 5);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (68, 1);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (68, 2);
INSERT INTO HASHTAG_CD (CD_NO, HT_NO) VALUES (68, 5); 




