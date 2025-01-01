-- Set the time zone to Asia/Seoul
SET GLOBAL time_zone = 'Asia/Seoul';
SET time_zone = 'Asia/Seoul';

-- DB 생성
CREATE DATABASE IF NOT EXISTS company;
USE company;

-- TALK_LIST 테이블 생성
CREATE TABLE TALK_LIST (
    TALK_ID VARCHAR(10),
    SPEAKER VARCHAR(255),
    START_DTM DATETIME,
    UPDATE_DTM DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PLACE VARCHAR(255),
    SEAT INT,
    TALK_DESC TEXT,
    PRIMARY KEY (TALK_ID)
);

-- TALK_MEM 테이블 생성
CREATE TABLE TALK_MEM (
    TALK_ID VARCHAR(10) NOT NULL,
    MEM_ID VARCHAR(10) NOT NULL,
    STATE VARCHAR(255),
    DTM DATETIME DEFAULT CURRENT_TIMESTAMP,
    UPDATE_DTM DATETIME  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (TALK_ID, MEM_ID),
    FOREIGN KEY (TALK_ID) REFERENCES TALK_LIST(TALK_ID)
);

-- 다음 TALK_ID를 생성하기 위한 시퀀스 테이블 생성
CREATE TABLE TALK_ID_SEQ (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY
);

-- 트리거를 사용하여 TALK_ID를 자동으로 생성
DELIMITER //

CREATE TRIGGER before_insert_talk_list
BEFORE INSERT ON TALK_LIST
FOR EACH ROW
BEGIN
    DECLARE new_id INT;

    -- TALK_ID가 없는 경우에만 시퀀스를 사용하여 TALK_ID 생성
    IF NEW.TALK_ID IS NULL OR NEW.TALK_ID = '' THEN
        -- 시퀀스 테이블에서 새로운 id 값 가져오기
        INSERT INTO TALK_ID_SEQ VALUES (NULL);
        SET new_id = LAST_INSERT_ID();

        -- TALK_ID를 T00001 형식으로 설정
        SET NEW.TALK_ID = CONCAT('T', LPAD(new_id, 5, '0'));
    END IF;
END//

DELIMITER ;

-- DB 사용자 생성 & company DB에 모든 권한 부여
DROP USER IF EXISTS 'hradmin'@'%';
CREATE USER 'hradmin'@'%' IDENTIFIED BY 'hrpass';
GRANT ALL PRIVILEGES ON company.* TO 'hradmin'@'%';
FLUSH PRIVILEGES;

-- 기본 강연데이터 생성
INSERT INTO talk_list (place, seat, speaker, start_dtm, talk_desc)
VALUES ('test-place', 5, 'test-speaker', '2025-01-05 10:00:00', 'test01');

INSERT INTO talk_list (place, seat, speaker, start_dtm, talk_desc)
VALUES ('test-place', 10, 'test-speaker', '2025-01-03 10:00:00', 'test02');

INSERT INTO talk_list (place, seat, speaker, start_dtm, talk_desc)
VALUES ('test-place', 3, 'test-speaker', '2024-12-31 10:00:00', 'test03');
