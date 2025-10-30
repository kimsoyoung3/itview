INSERT INTO content_entity (
    id,
    release_date,
    poster,
    age,
    creator_name,
    duration,
    nation,
    title,
    channel_name,
    content_type,
    description
) VALUES (
    1,
    '2025-07-23',
    'https://an2-img.amz.wtchn.net/image/v2/YnkAD0r--6dNls6Xu5Y0BA.webp?jwt=ZXlKaGJHY2lPaUpJVXpJMU5pSjkuZXlKdmNIUnpJanBiSW1SZk5Ea3dlRGN3TUhFNE1DSmRMQ0p3SWpvaUwzWXlMM04wYjNKbEwybHRZV2RsTHpFMk5UWTBORFl3TXpBMU5qVTFJbjAuY2cyNTJBbU5nTnYyVFpvdm1xN0psT29iRF9BVkFsajJCWFFIRWc1aGY0RQ==',
    '15세',
    '김병우',
    '1시간 57분',
    '한국',
    '전지적 독자 시점',
    NULL,
    'MOVIE',
    '10년 이상 연재된 소설이 완결된 날 소설 속 세계가 현실이 되어 버리고, 유일한 독자였던 ‘김독자’가 소설의 주인공 ‘유중혁’ 그리고 동료들과 함께 멸망한 세계에서 살아남기 위한 이야기'
),
(
    2,
    '2025-05-24',
    'https://an2-img.amz.wtchn.net/image/v2/GrodRsHBz5qLVzFUV2semg.webp?jwt=ZXlKaGJHY2lPaUpJVXpJMU5pSjkuZXlKdmNIUnpJanBiSW1SZk5Ea3dlRGN3TUhFNE1DSmRMQ0p3SWpvaUwzWXlMM04wYjNKbEwybHRZV2RsTHpFd01qQTBNekF3TWpnNE5USTRNeklpZlEuV0daMVRqME1MQWVVNDh1Nnc2aG1RZ1ItVXctWm12d2JIVnRFNlhxSFlYRQ==',
    '15세',
    NULL,
    '12부작',
    '한국',
    '미지의 서울',
    'TVN',
    'SERIES',
    '얼굴 빼고 모든 게 다른 쌍둥이 자매가 인생을 맞바꾸는 거짓말로 진짜 사랑과 인생을 찾아가는 로맨틱 성장 드라마'
),
(
    3,
    '2018-12-20',
    'https://an2-img.amz.wtchn.net/image/v2/JJDixjnj0pdChmZEbiGAiQ.webp?jwt=ZXlKaGJHY2lPaUpJVXpJMU5pSjkuZXlKdmNIUnpJanBiSW1SZk5Ea3dlRGN3TUhFNE1DSmRMQ0p3SWpvaUwzWXlMM04wYjNKbEwySnZiMnN2TVRZM01qYzRNamcxTlRJek56TTBNelF4TXlKOS5YVjdBMjZMZmNSbk5WSFB4dm1ONUhFaGUtdFBLQ2tFV3ZTWUtLY2dmdGIw',
    '전체 이용가',
    '궤도',
    '300p',
    '한국',
    '궤도의 과학 허세',
    NULL,
    'BOOK',
    '저자가 오랜 시간 과학 커뮤니케이터로 활동한 내공이 집약되어 있다. 가상화폐, 다이어트, 연애와 같은 친숙한 주제를 과학적으로 분석하는 한편, 힉스 입자, 블랙홀, 양자역학과 같은 ‘하드코어’한 과학 개념들도 다루는데 모두 쉽고 흥미롭게 즐길 수 있다. 양자역학에 관한 ‘썰’만 푸는 거 같은데도, 다 읽고 나면 양자역학이 뭔지 알 수 있겠다 싶은 것이다.'
),
(
    4,
    '2014-06-13',
    'https://image-comic.pstatic.net/webtoon/626904/thumbnail/thumbnail_IMAG21_7378645728290681654.JPEG',
    '전체 이용가',
    '하가',
    '14화',
    '한국',
    '시타를 위하여',
    'NAVER',
    'WEBTOON',
    '운명이 바뀌게 되는 한 소녀와, 그녀를 구하고 싶었던 한 남자의 이야기.'
),
(
    5,
    '2025-06-25',
    '//i.namu.wiki/i/H8VbQ3LREIfSiDu8p0H76zQlq9OL7FD1bguXJTYm1WcMSPC8YL6Jyvi4B9hoaFWka_5yGVGsQIileJYLl8b-xp-OzLT7DqfdKNgS38KZHrmGx8mzZBr9ntmgjKobXsP5hVop5qpu6NoG3qMK9PjYWQ.webp',
    '전체 이용가',
    '프로미스나인',
    '17분 44초',
    '한국',
    'From Our 20`s',
    NULL,
    'RECORD',
    '어디로든 갈 수 있고 무엇이든 될 수 있는 아름다운 20대의 이야기'
);

INSERT INTO CONTENT_GENRE_ENTITY (ID, CONTENT_ID, GENRE) VALUES
(1, 1, 'FANTASY'),
(2, 2, 'ROMANCE'),
(3, 2, 'DRAMA'),
(4, 3, 'NATURAL_SCIENCE'),
(5, 4, 'DRAMA'),
(6, 5, 'KPOP');

INSERT INTO PERSON_ENTITY (ID, PROFILE, JOB, NAME) VALUES
(1, 'https://an2-img.amz.wtchn.net/image/v2/hl5Fa4zvOzHHzdbUR5FxpQ.jpg?jwt=ZXlKaGJHY2lPaUpJVXpJMU5pSjkuZXlKdmNIUnpJanBiSW1SZk1qUXdlREkwTUNKZExDSndJam9pTDNZeUwzTjBiM0psTDNCbGNuTnZiaTg0T1RVMU5UUXhOekk1TkRBME5EazJJbjAuYzh6X3YxQ29oNGl6LW1EUWJJNWthc3dheGlrZE4zU2U4RmpCMHdBbU9MMA==', '배우', '안효섭'),
(2, 'https://an2-img.amz.wtchn.net/image/v2/fz53MXeztWO6DMlBVCHpqg.jpg?jwt=ZXlKaGJHY2lPaUpJVXpJMU5pSjkuZXlKdmNIUnpJanBiSW1SZk1qUXdlREkwTUNKZExDSndJam9pTDNZeUwzTjBiM0psTDNCbGNuTnZiaTh6TVRNNE9UYzBPVGN6TmpJMU1ESTBJbjAuWmV0eG03VkdjaVpXZ2Jwa2NPSnpJczUzeklKVi1kdUtzX3pVbFV0cnpSWQ==', '배우', '박보영'),
(3, 'https://an2-img.amz.wtchn.net/image/v2/Igw5RrBGS7gkxpZkenJJcQ.jpg?jwt=ZXlKaGJHY2lPaUpJVXpJMU5pSjkuZXlKdmNIUnpJanBiSW1SZk1qUXdlREkwTUNKZExDSndJam9pTDNZeUwzTjBiM0psTDNCbGNuTnZiaTh4TmpZMU5qSTJNemMxTXpJNU56UXdOemd6SW4wLktpVU02SnZZZlRreHZWWExGcmRXNDhqMHNzVkxhcUpTWXFqZWEtdHdiLVU=', '저자', '궤도'),
(4, NULL, '웹툰 작가', '하가'),
(5, 'https://i.namu.wiki/i/IuIPUAZB715UE-GDGktRSnQ7b5v_dP4l3W-se62fIZrYnt0pZ_DxTzprPHlYCJzso2y_4fh80cTMgCKUrPf5rPOSjgCR3N0_SIqXE2zhQk2dtvy9xGAOwqgPJnw4U2yosRhKWrSecuzI5phJFKbxlw.webp', '가수', '백지헌');

INSERT INTO CREDIT_ENTITY (ID, PERSON_ID, CONTENT_ID, DEPARTMENT, ROLE, CHARACTER_NAME) VALUES
(1, 1, 1, '출연', '주연', '김독자'),
(2, 2, 2, '출연', '출연', '유미지/유미래'),
(3, 3, 3, '저자', '저자', NULL),
(4, 4, 4, '웹툰 작가', '글/그림', NULL),
(5, 5, 5, '참여', '보컬', NULL);

INSERT INTO GALLERY_ENTITY (ID, CONTENT_ID, PHOTO) VALUES
(1, 1, 'https://an2-img.amz.wtchn.net/image/v2/4Ge-3Sq9_dW8XQuDUWTrGw.jpg?jwt=ZXlKaGJHY2lPaUpJVXpJMU5pSjkuZXlKdmNIUnpJanBiSW1SZk5qUXdlRE0yTUhFNE1DSmRMQ0p3SWpvaUwzWXlMM04wYjNKbEwybHRZV2RsTHpFeU1UWTJOVEEwTkRZek5qRTBNak1pZlEuRFdxcVhxSlJaUVJvYjRtVWpRZGNWQUhMRmN0TWxGX20wclh4YVB2a1lRZw=='),
(2, 1, 'https://an2-img.amz.wtchn.net/image/v2/0uZZaAXDy5rqGhOzRK_Usg.jpg?jwt=ZXlKaGJHY2lPaUpJVXpJMU5pSjkuZXlKdmNIUnpJanBiSW1SZk5qUXdlRE0yTUhFNE1DSmRMQ0p3SWpvaUwzWXlMM04wYjNKbEwybHRZV2RsTHpJMk5UZzNPRGcyTXpBME9UVTROalVpZlEudENYUUV0YjJnTzlRczVCUmdaMVM1NjlsV0RFYXFkTFZ1Y1IzWXF0Vk54SQ=='),
(3, 2, 'https://an2-img.amz.wtchn.net/image/v2/bHSSbNyTFObF8kfiqUqePg.jpg?jwt=ZXlKaGJHY2lPaUpJVXpJMU5pSjkuZXlKdmNIUnpJanBiSW1SZk5qUXdlRE0yTUhFNE1DSmRMQ0p3SWpvaUwzWXlMM04wYjNKbEwybHRZV2RsTHpZek1qY3hNalUwTkRJeU5ETTFNVGdpZlEuQWRIeEFBRFJzSy1kZ25WUV92UWdmUFNqc1Znb0haU1cwZkoyTGZJQ1VGQQ=='),
(4, 2, 'https://an2-img.amz.wtchn.net/image/v2/qszd9M0BPeyg61SoZ11z4Q.jpg?jwt=ZXlKaGJHY2lPaUpJVXpJMU5pSjkuZXlKdmNIUnpJanBiSW1SZk5qUXdlRE0yTUhFNE1DSmRMQ0p3SWpvaUwzWXlMM04wYjNKbEwybHRZV2RsTHpFME1UQXlNakExTURBMk5qUXpOemtpZlEub3FiZmIzNmhtSjhiZDlWUEZCbWJqM0c3aERGTFRIb3VhMG01aEpabWdaQQ==');

INSERT INTO VIDEO_ENTITY (ID, CONTENT_ID, TITLE, IMAGE, URL) VALUES
(1, 1, '메인 예고편', 'https://img.youtube.com/vi/Xb96_61kMS8/0.jpg', 'https://www.youtube.com/watch?v=Xb96_61kMS8'),
(2, 1, '15초 예고편', 'https://img.youtube.com/vi/lN-1XTiy8tE/0.jpg', 'https://www.youtube.com/watch?v=lN-1XTiy8tE');

INSERT INTO EXTERNAL_SERVICE_ENTITY (ID, CONTENT_ID, TYPE, HREF) VALUES
(1, 2, 'NETFLIX', 'https://www.netflix.com/kr/title/82024804'),
(2, 2, 'TVING', 'https://www.tving.com/contents/P001770706'),
(3, 3, 'ALADIN', 'https://www.aladin.co.kr/shop/wproduct.aspx?ItemId=296753343'),
(4, 3, 'YES24', 'https://www.yes24.com/Product/Goods/110211255'),
(5, 3, 'KYOBO', 'https://product.kyobobook.co.kr/detail/S000061351110'),
(6, 4, 'NAVER', 'https://comic.naver.com/webtoon/list?titleId=626904');